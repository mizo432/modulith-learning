package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;

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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemEstimated;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatusChanged;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ChangeBacklogItemStatusCommandServiceのESTIMATED状態遷移テスト")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ChangeBacklogItemStatusCommandServiceEstimatedTest {

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
    org.mockito.MockitoAnnotations.openMocks(this);
    service = new ChangeBacklogItemStatusCommandService(
        backlogItemRepository, scrumTeamSubsetRepository, eventPublisher, Clock.systemDefaultZone());
  }

  private BacklogItem readyItemWithStoryPoint() {
    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .storyPoint(8)
        .build();
    item.changeStatus(BacklogItemStatus.ACCEPTED);
    item.changeStatus(BacklogItemStatus.READY);
    return item;
  }

  private BacklogItem estimatedItem() {
    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .storyPoint(13)
        .build();
    item.changeStatus(BacklogItemStatus.ACCEPTED);
    item.changeStatus(BacklogItemStatus.READY);
    item.changeStatus(BacklogItemStatus.ESTIMATED);
    return item;
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
  @DisplayName("READY→ESTIMATED遷移の権限テスト")
  class ReadyToEstimatedPermissionTest {

    @Test
    @DisplayName("POがREADYをESTIMATEDに変更したとき、状態がESTIMATEDになりイベントが発行されること")
    void shouldPoChangeReadyToEstimated() {
      BacklogItem item = readyItemWithStoryPoint();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.ESTIMATED, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("SMがREADYをESTIMATEDに変更したとき、状態がESTIMATEDになりイベントが発行されること")
    void shouldSmChangeReadyToEstimated() {
      BacklogItem item = readyItemWithStoryPoint();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.ESTIMATED, SM_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("チームメンバー（PO/SM以外）がREADYをESTIMATEDに変更したとき、状態がESTIMATEDになりイベントが発行されること")
    void shouldTeamMemberChangeReadyToEstimated() {
      BacklogItem item = readyItemWithStoryPoint();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.ESTIMATED, MEMBER_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("チーム外の従業員がREADYをESTIMATEDに変更しようとすると、BacklogItemPermissionDeniedExceptionがスローされること")
    void shouldDenyOutsiderChangeReadyToEstimated() {
      BacklogItem item = readyItemWithStoryPoint();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ESTIMATED, OUTSIDER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }

  @Nested
  @DisplayName("ESTIMATED→READY戻しの権限テスト")
  class EstimatedToReadyPermissionTest {

    @Test
    @DisplayName("POがESTIMATEDをREADYに戻したとき、状態がREADYになりStoryPointが保持されること")
    void shouldPoChangeEstimatedToReady() {
      BacklogItem item = estimatedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      assertThat(item.getStoryPoint()).isEqualTo(13);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("チーム外の従業員がESTIMATEDをREADYに戻そうとすると、BacklogItemPermissionDeniedExceptionがスローされること")
    void shouldDenyOutsiderChangeEstimatedToReady() {
      BacklogItem item = estimatedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.READY, OUTSIDER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }

  @Nested
  @DisplayName("イベント発行テスト")
  class EventPublishingTest {

    @Test
    @DisplayName("READY→ESTIMATED遷移時にBacklogItemStatusChangedイベントが発行されること")
    void shouldPublishStatusChangedOnReadyToEstimated() {
      BacklogItem item = readyItemWithStoryPoint();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.ESTIMATED, PO_ID));

      ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
      verify(eventPublisher, org.mockito.Mockito.times(2)).publishEvent(eventCaptor.capture());
      List<Object> events = eventCaptor.getAllValues();
      BacklogItemStatusChanged event = (BacklogItemStatusChanged) events.get(0);
      assertThat(event.fromStatus()).isEqualTo(BacklogItemStatus.READY);
      assertThat(event.toStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
    }

    @Test
    @DisplayName("ESTIMATED→READY遷移時にBacklogItemStatusChangedイベントが発行されること")
    void shouldPublishStatusChangedOnEstimatedToReady() {
      BacklogItem item = estimatedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, PO_ID));

      ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BacklogItemStatusChanged event = (BacklogItemStatusChanged) eventCaptor.getValue();
      assertThat(event.fromStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      assertThat(event.toStatus()).isEqualTo(BacklogItemStatus.READY);
    }

    @Test
    @DisplayName("権限がない場合、保存もイベント発行もされないこと")
    void shouldNotSaveOrPublishWhenPermissionDenied() {
      BacklogItem item = readyItemWithStoryPoint();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(BacklogItemStatus.ESTIMATED, OUTSIDER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }

  @Nested
  @DisplayName("BacklogItemEstimatedイベント発行テスト")
  class BacklogItemEstimatedEventTest {

    @Test
    @DisplayName("READY→ESTIMATED遷移時にBacklogItemEstimatedイベントが発行されること")
    void shouldPublishEstimatedEventOnReadyToEstimated() {
      BacklogItem item = readyItemWithStoryPoint();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.ESTIMATED, PO_ID));

      ArgumentCaptor<BacklogItemEstimated> eventCaptor = ArgumentCaptor.forClass(BacklogItemEstimated.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BacklogItemEstimated event = eventCaptor.getValue();
      assertThat(event.backlogItemId()).isEqualTo(ITEM_ID);
      assertThat(event.scrumTeamId()).isEqualTo(TEAM_ID);
      assertThat(event.storyPoint()).isEqualTo(8);
      assertThat(event.actorEmployeeId()).isEqualTo(PO_ID);
    }

    @Test
    @DisplayName("ESTIMATED→READY遷移時はBacklogItemEstimatedイベントが発行されないこと")
    void shouldNotPublishEstimatedEventOnEstimatedToReady() {
      BacklogItem item = estimatedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, PO_ID));

      verify(eventPublisher, never()).publishEvent(any(BacklogItemEstimated.class));
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
    @DisplayName("既存のACCEPTED→READY遷移が引き続き機能すること")
    void shouldAcceptedToReadyStillWork() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .build();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.READY, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("既存のREADY→SUGGESTED遷移が引き続き機能すること")
    void shouldReadyToSuggestedStillWork() {
      BacklogItem item = BacklogItem.builder()
          .id(ITEM_ID)
          .title("Test Item")
          .creatorName("Creator")
          .scrumTeamId(TEAM_ID)
          .value(50)
          .build();
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(BacklogItemStatus.SUGGESTED, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("既存のFROZEN→SUGGESTED遷移が引き続き機能すること")
    void shouldFrozenToSuggestedStillWork() {
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
    }
  }
}
