package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
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
@DisplayName("EstimateBacklogItemCommandServiceのテスト")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EstimateBacklogItemCommandServiceTest {

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

  private EstimateBacklogItemCommandService service;

  @BeforeEach
  void setUp() {
    org.mockito.MockitoAnnotations.openMocks(this);
    service = new EstimateBacklogItemCommandService(
        backlogItemRepository, scrumTeamSubsetRepository, eventPublisher, Clock.systemDefaultZone());
  }

  private BacklogItem readyItem() {
    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
    item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());
    return item;
  }

  private BacklogItem suggestedItem() {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private BacklogItem estimatedItem() {
    BacklogItem item = BacklogItem.builder()
        .id(ITEM_ID)
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .value(50)
        .storyPoint(5)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    item.changeStatus(BacklogItemStatus.ACCEPTED, LocalDateTime.now());
    item.changeStatus(BacklogItemStatus.READY, LocalDateTime.now());
    item.changeStatus(BacklogItemStatus.ESTIMATED, LocalDateTime.now());
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
        .updatedAt(LocalDateTime.now()).build();
  }

  private EstimateBacklogItemCommand command(Integer storyPoint, SnowflakeId actorId) {
    return EstimateBacklogItemCommand.builder()
        .backlogItemId(ITEM_ID)
        .storyPoint(storyPoint)
        .actorEmployeeId(actorId)
        .build();
  }

  @Nested
  @DisplayName("見積もり値設定のテスト")
  class EstimateTest {

    @Test
    @DisplayName("READY状態のBacklogItemにStoryPointを設定したとき、値が保存されること")
    void shouldSetStoryPointOnReadyItem() {
      BacklogItem item = readyItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(8, PO_ID));

      assertThat(item.getStoryPoint()).isEqualTo(8);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("ESTIMATED状態のBacklogItemのStoryPointを変更したとき、新しい値が保存されること")
    void shouldUpdateStoryPointOnEstimatedItem() {
      BacklogItem item = estimatedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(21, PO_ID));

      assertThat(item.getStoryPoint()).isEqualTo(21);
      verify(backlogItemRepository).save(item);
    }

    @Test
    @DisplayName("無効なStoryPoint値を設定しようとすると、IllegalArgumentExceptionがスローされること")
    void shouldRejectInvalidStoryPoint() {
      BacklogItem item = readyItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(4, PO_ID)))
          .isInstanceOf(IllegalArgumentException.class);
      verify(backlogItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("存在しないBacklogItemIdでStoryPointを設定しようとすると、IllegalArgumentExceptionがスローされること")
    void shouldRejectNonExistentBacklogItemId() {
      SnowflakeId nonExistentId = SnowflakeId.of(9999L);
      when(backlogItemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.execute(
          EstimateBacklogItemCommand.builder()
              .backlogItemId(nonExistentId)
              .storyPoint(8)
              .actorEmployeeId(PO_ID)
              .build()))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("BacklogItemEstimatedイベント発行テスト")
  class EstimatedEventTest {

    @Test
    @DisplayName("見積もり時にBacklogItemEstimatedイベントが発行されること")
    void shouldPublishEstimatedEventOnEstimate() {
      BacklogItem item = readyItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(8, PO_ID));

      ArgumentCaptor<BacklogItemEstimated> eventCaptor = ArgumentCaptor.forClass(BacklogItemEstimated.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BacklogItemEstimated event = eventCaptor.getValue();
      assertThat(event.backlogItemId()).isEqualTo(ITEM_ID);
      assertThat(event.scrumTeamId()).isEqualTo(TEAM_ID);
      assertThat(event.storyPoint()).isEqualTo(8);
      assertThat(event.actorEmployeeId()).isEqualTo(PO_ID);
    }

    @Test
    @DisplayName("再評価時にBacklogItemEstimatedイベントが発行されること")
    void shouldPublishEstimatedEventOnReEstimate() {
      BacklogItem item = estimatedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(21, PO_ID));

      ArgumentCaptor<BacklogItemEstimated> eventCaptor = ArgumentCaptor.forClass(BacklogItemEstimated.class);
      verify(eventPublisher).publishEvent(eventCaptor.capture());
      BacklogItemEstimated event = eventCaptor.getValue();
      assertThat(event.storyPoint()).isEqualTo(21);
    }
  }

  @Nested
  @DisplayName("状態遷移テスト")
  class StatusTransitionTest {

    @Test
    @DisplayName("READY状態のアイテムに見積もりを設定したとき、状態がESTIMATEDになりBacklogItemStatusChangedイベントも発行されること")
    void shouldTransitionReadyToEstimated() {
      BacklogItem item = readyItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      service.execute(command(8, PO_ID));

      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      ArgumentCaptor<BacklogItemStatusChanged> statusCaptor =
          ArgumentCaptor.forClass(BacklogItemStatusChanged.class);
      ArgumentCaptor<BacklogItemEstimated> estimatedCaptor =
          ArgumentCaptor.forClass(BacklogItemEstimated.class);
      verify(eventPublisher).publishEvent(statusCaptor.capture());
      verify(eventPublisher).publishEvent(estimatedCaptor.capture());
      BacklogItemStatusChanged statusEvent = statusCaptor.getValue();
      assertThat(statusEvent.backlogItemId()).isEqualTo(ITEM_ID);
      assertThat(statusEvent.fromStatus()).isEqualTo(BacklogItemStatus.READY);
      assertThat(statusEvent.toStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      assertThat(statusEvent.actorEmployeeId()).isEqualTo(PO_ID);
    }

    @Test
    @DisplayName("SUGGESTED状態のアイテムに見積もりを設定しようとしたとき、IllegalStateExceptionがスローされ、保存もイベント発行もされないこと")
    void shouldRejectEstimateOnSuggestedItem() {
      BacklogItem item = suggestedItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(8, PO_ID)))
          .isInstanceOf(IllegalStateException.class);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }

  @Nested
  @DisplayName("権限テスト")
  class PermissionTest {

    @Test
    @DisplayName("チーム外の従業員が見積もり値を設定しようとすると、BacklogItemPermissionDeniedExceptionがスローされること")
    void shouldDenyOutsiderEstimate() {
      BacklogItem item = readyItem();
      when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));

      assertThatThrownBy(() -> service.execute(command(8, OUTSIDER_ID)))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(backlogItemRepository, never()).save(any());
      verify(eventPublisher, never()).publishEvent(any());
    }
  }
}
