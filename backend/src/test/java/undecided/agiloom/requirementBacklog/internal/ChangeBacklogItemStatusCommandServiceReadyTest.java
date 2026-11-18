package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
@DisplayName("ChangeBacklogItemStatusCommandServiceのREADY状態遷移テスト")
@ExtendWith(MockitoExtension.class)
class ChangeBacklogItemStatusCommandServiceReadyTest {

  private static final long PO_ID_VALUE = 100L;
  private static final long SM_ID_VALUE = 200L;
  private static final long MEMBER_ID_VALUE = 300L;
  private static final long OUTSIDER_ID_VALUE = 999L;
  private static final long ITEM_ID_VALUE = 100L;
  private static final long TEAM_ID_VALUE = 10L;

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(ITEM_ID_VALUE);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(TEAM_ID_VALUE);
  private static final SnowflakeId PO_ID = SnowflakeId.of(PO_ID_VALUE);
  private static final SnowflakeId SM_ID = SnowflakeId.of(SM_ID_VALUE);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(MEMBER_ID_VALUE);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(OUTSIDER_ID_VALUE);

  @Mock private BacklogItemRepository backlogItemRepository;
  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  @Mock private ApplicationEventPublisher eventPublisher;

  private ChangeBacklogItemStatusCommandService service;

  @BeforeEach
  void setUp() {
    service = new ChangeBacklogItemStatusCommandService(
        backlogItemRepository, scrumTeamSubsetRepository, eventPublisher);
  }

  private BacklogItem acceptedItem() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .build();
  }

  private ScrumTeamSubset teamWithMembers() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .poEmployeeId(PO_ID)
        .poName("PO Name")
        .smEmployeeId(SM_ID)
        .smName("SM Name")
        .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
        .build();
  }

  private ChangeBacklogItemStatusCommand command(BacklogItemStatus target, SnowflakeId actorId) {
    return ChangeBacklogItemStatusCommand.builder()
        .backlogItemId(ITEM_ID)
        .targetStatus(target)
        .actorEmployeeId(actorId)
        .build();
  }

  @Nested
  @DisplayName("ACCEPTED→READY遷移の権限テスト")
  class AcceptedToReadyPermissionTest {

    @Test
    @DisplayName("POがACCEPTEDをREADYに変更できること")
    void shouldPoChangeAcceptedToReady() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("SMがACCEPTEDをREADYに変更できること")
    void shouldSmChangeAcceptedToReady() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, SM_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("チームメンバー（PO/SM以外）がACCEPTEDをREADYに変更できること")
    void shouldTeamMemberChangeAcceptedToReady() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, MEMBER_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("チーム外の従業員がACCEPTEDをREADYに変更しようとすると権限例外になること")
    void shouldDenyOutsiderChangeAcceptedToReady() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.READY, OUTSIDER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }

  @Nested
  @DisplayName("READY→SUGGESTED戻しの権限テスト")
  class ReadyToSuggestedPermissionTest {

    @Test
    @DisplayName("POがREADYをSUGGESTEDに戻せること")
    void shouldPoChangeReadyToSuggested() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.SUGGESTED, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("SMがREADYをSUGGESTEDに戻せること")
    void shouldSmChangeReadyToSuggested() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.SUGGESTED, SM_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("チームメンバー（PO/SM以外）がREADYをSUGGESTEDに戻せること")
    void shouldTeamMemberChangeReadyToSuggested() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.SUGGESTED, MEMBER_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("チーム外の従業員がREADYをSUGGESTEDに戻そうとすると権限例外になること")
    void shouldDenyOutsiderChangeReadyToSuggested() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.SUGGESTED, OUTSIDER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }

  @Nested
  @DisplayName("イベント発行テスト")
  class EventPublishingTest {

    @Test
    @DisplayName("ACCEPTED→READY遷移時にBacklogItemStatusChangedイベントが発行されること")
    void shouldPublishEventOnAcceptedToReady() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, PO_ID));

      ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BacklogItemStatusChanged event = (BacklogItemStatusChanged) eventCaptor.getValue();
      assertThat(event.fromStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
      assertThat(event.toStatus()).isEqualTo(BacklogItemStatus.READY);
    }

    @Test
    @DisplayName("READY→SUGGESTED遷移時にBacklogItemStatusChangedイベントが発行されること")
    void shouldPublishEventOnReadyToSuggested() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.SUGGESTED, PO_ID));

      ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BacklogItemStatusChanged event = (BacklogItemStatusChanged) eventCaptor.getValue();
      assertThat(event.fromStatus()).isEqualTo(BacklogItemStatus.READY);
      assertThat(event.toStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
    }

    @Test
    @DisplayName("権限がない場合、保存もイベント発行もされないこと（ACCEPTED→READY試行時）")
    void shouldNotSaveOrPublishWhenPermissionDenied() {
      BacklogItem item = acceptedItem();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.READY, OUTSIDER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }

  @Nested
  @DisplayName("既存遷移の回帰テスト")
  class RegressionTest {

    @Test
    @DisplayName("既存のSUGGESTED→ACCEPTED遷移が引き続き機能すること")
    void shouldSuggestedToAcceptedStillWork() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .build();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.ACCEPTED, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
      verify(backlogItemRepository).save(item);
      verify(eventPublisher).publishEvent(any(BacklogItemStatusChanged.class));
    }

    @Test
    @DisplayName("既存のFROZEN→SUGGESTED（POのみ）が引き続き機能すること")
    void shouldFrozenToSuggestedByPoStillWork() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .build();
      item.changeStatus(BacklogItemStatus.FROZEN);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.SUGGESTED, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository).save(item);
      verify(eventPublisher).publishEvent(any(BacklogItemStatusChanged.class));
    }
  }
}
