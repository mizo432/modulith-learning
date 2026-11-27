package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatusChanged;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ChangeBacklogItemStatusCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class ChangeBacklogItemStatusCommandServiceTest {

  private static final long PO_ID_VALUE = 100L;
  private static final long SM_ID_VALUE = 200L;
  private static final long MEMBER_ID_VALUE = 300L;
  private static final long ITEM_ID_VALUE = 100L;
  private static final long TEAM_ID_VALUE = 10L;

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(TEAM_ID_VALUE);

  @Mock private BacklogItemRepository backlogItemRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  @Mock private ApplicationEventPublisher eventPublisher;

  private ChangeBacklogItemStatusCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new ChangeBacklogItemStatusCommandService(
            backlogItemRepository, scrumTeamSubsetRepository, eventPublisher, Clock.systemDefaultZone());
  }

  private BacklogItem suggestedItem() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .build();
  }

  private ScrumTeamSubset team() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .poEmployeeId(SnowflakeId.of(PO_ID_VALUE))
        .smEmployeeId(SnowflakeId.of(SM_ID_VALUE))
        .build();
  }

  private ChangeBacklogItemStatusCommand command(BacklogItemStatus target, long actorIdValue) {
    return ChangeBacklogItemStatusCommand.builder()
        .backlogItemId(ITEM_ID)
        .targetStatus(target)
        .actorEmployeeId(SnowflakeId.of(actorIdValue))
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("POはSUGGESTEDをACCEPTEDに変更でき、遷移イベントが発行されること（value設定済み）")
    void shouldAcceptByPoAndPublishEvent() {
      // Arrange
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .build();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act
      service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID_VALUE));

      // Assert
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
      verify(backlogItemRepository).save(item);
      ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BacklogItemStatusChanged event = (BacklogItemStatusChanged) eventCaptor.getValue();
      assertThat(event.backlogItemId()).isEqualTo(ITEM_ID);
      assertThat(event.scrumTeamId()).isEqualTo(TEAM_ID);
      assertThat(event.fromStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      assertThat(event.toStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
      assertThat(event.actorEmployeeId()).isEqualTo(SnowflakeId.of(PO_ID_VALUE));
    }

    @Test
    @DisplayName("SMはSUGGESTEDをFROZENに変更できること")
    void shouldFreezeBySm() {
      // Arrange
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act
      service.execute(command(BacklogItemStatus.FROZEN, SM_ID_VALUE));

      // Assert
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.FROZEN);
      verify(backlogItemRepository).save(item);
      verify(eventPublisher).publishEvent(any(BacklogItemStatusChanged.class));
    }

    @Test
    @DisplayName("一般メンバーが状態変更を試みると権限例外になり、保存もイベント発行もされないこと")
    void shouldDenyMember() {
      // Arrange
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ACCEPTED, MEMBER_ID_VALUE)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("ACCEPTED済みのアイテムをPOが変更しようとすると状態遷移例外になること")
    void shouldThrowOnInvalidTransition() {
      // Arrange
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .build();
      item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.FROZEN, PO_ID_VALUE)))
          .isInstanceOf(IllegalStateException.class);
      verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("アイテムが存在しない場合、例外がスローされること")
    void shouldThrowWhenItemNotFound() {
      // Arrange
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID_VALUE)))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("BacklogItem not found");
    }

    @Test
    @DisplayName("チームのサブセットが存在しない場合、例外がスローされること")
    void shouldThrowWhenTeamNotFound() {
      // Arrange
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID_VALUE)))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }

    @Test
    @DisplayName("value=nullのアイテムをACCEPTEDに変更しようとするとIllegalArgumentExceptionがスローされること")
    void shouldThrowOnAcceptWithValueNull() {
      // Arrange
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID_VALUE)))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("SUGGESTEDからFROZENへの凍結はPOが行えること")
    void shouldFreezeByPo() {
      // Arrange
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act
      service.execute(command(BacklogItemStatus.FROZEN, PO_ID_VALUE));

      // Assert
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.FROZEN);
      verify(backlogItemRepository).save(item);
      verify(eventPublisher).publishEvent(any(BacklogItemStatusChanged.class));
    }


    @Test
    @DisplayName("FROZENからSUGGESTEDへの復帰はPOのみ可能でSMは不可なこと")
    void shouldUnfreezeByPoOnly() {
      // Arrange
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .build();
      item.changeStatus(BacklogItemStatus.FROZEN, LocalDateTime.now());
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act & Assert: POは許可される
      service.execute(command(BacklogItemStatus.SUGGESTED, PO_ID_VALUE));
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository).save(item);
      verify(eventPublisher).publishEvent(any(BacklogItemStatusChanged.class));
    }

    @Test
    @DisplayName("FROZENからSUGGESTEDへの復帰はSMが行えないこと")
    void shouldDenyUnfreezeBySm() {
      // Arrange
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .build();
      item.changeStatus(BacklogItemStatus.FROZEN, LocalDateTime.now());
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act & Assert: SMは拒否される
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.SUGGESTED, SM_ID_VALUE)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.FROZEN);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("FROZENからSUGGESTEDへの復帰は一般メンバーが行えないこと")
    void shouldDenyUnfreezeByMember() {
      // Arrange
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .build();
      item.changeStatus(BacklogItemStatus.FROZEN, LocalDateTime.now());
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act & Assert: 一般メンバーは拒否される
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.SUGGESTED, MEMBER_ID_VALUE)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.FROZEN);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }
}
