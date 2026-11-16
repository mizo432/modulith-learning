package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
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

  private static final UUID PO_ID = UUID.randomUUID();
  private static final UUID SM_ID = UUID.randomUUID();
  private static final UUID MEMBER_ID = UUID.randomUUID();
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);

  @Mock private BacklogItemRepository backlogItemRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  @Mock private ApplicationEventPublisher eventPublisher;

  private ChangeBacklogItemStatusCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new ChangeBacklogItemStatusCommandService(
            backlogItemRepository, scrumTeamSubsetRepository, eventPublisher);
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
        .poEmployeeId(PO_ID)
        .smEmployeeId(SM_ID)
        .build();
  }

  private ChangeBacklogItemStatusCommand command(BacklogItemStatus target, UUID actor) {
    return ChangeBacklogItemStatusCommand.builder()
        .backlogItemId(ITEM_ID)
        .targetStatus(target)
        .actorEmployeeId(actor)
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("POはSUGGESTEDをACCEPTEDに変更でき、遷移イベントが発行されること")
    void shouldAcceptByPoAndPublishEvent() {
      // Arrange
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act
      service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID));

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
      assertThat(event.actorEmployeeId()).isEqualTo(PO_ID);
    }

    @Test
    @DisplayName("SMはSUGGESTEDをREJECTEDに変更できること")
    void shouldRejectBySm() {
      // Arrange
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act
      service.execute(command(BacklogItemStatus.REJECTED, SM_ID));

      // Assert
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.REJECTED);
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
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ACCEPTED, MEMBER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("ACCEPTED済みのアイテムをPOが変更しようとすると状態遷移例外になること")
    void shouldThrowOnInvalidTransition() {
      // Arrange
      BacklogItem item = suggestedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.REJECTED, PO_ID)))
          .isInstanceOf(IllegalStateException.class);
      verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("アイテムが存在しない場合、例外がスローされること")
    void shouldThrowWhenItemNotFound() {
      // Arrange
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID)))
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
      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID)))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }
}
