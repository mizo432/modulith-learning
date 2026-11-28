package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintCancelled;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("スプリントキャンセル時のスライドリスナーの単体テスト")
@ExtendWith(MockitoExtension.class)
class SprintCancelledSlideListenerTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(20L);
  private static final SnowflakeId CANCELLED_SPRINT_ID = SnowflakeId.of(1001L);
  private static final SnowflakeId NEXT_SPRINT_ID = SnowflakeId.of(1002L);
  private static final SprintCancelled EVENT =
      new SprintCancelled(CANCELLED_SPRINT_ID, TEAM_ID, 1, LocalDateTime.of(2026, 11, 24, 10, 0));

  @Mock private BacklogItemRepository backlogItemRepository;
  @Mock private SprintFacade sprintFacade;

  private SprintCancelledSlideListener listener;

  @BeforeEach
  void setUp() {
    listener = new SprintCancelledSlideListener(backlogItemRepository, sprintFacade, Clock.systemDefaultZone());
  }

  private BacklogItem item(long id) {
    return BacklogItem.builder()
        .id(SnowflakeId.of(id))
        .title("Item " + id)
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .status(BacklogItemStatus.ESTIMATED)
        .storyPoint(5)
        .sprintId(CANCELLED_SPRINT_ID)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private Sprint nextSprint() {
    return Sprint.builder()
        .id(NEXT_SPRINT_ID)
        .productId(SnowflakeId.of(30L))
        .sprintNumber(2)
        .title("Sprint 2")
        .status(SprintStatus.PLANNED)
        .teamId(TEAM_ID)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Test
  @DisplayName("次スプリントがあるとき、割り当て済みアイテムは次スプリントへスライドする")
  void slidesAssignedItemsToNextSprint() {
    BacklogItem item1 = item(101L);
    BacklogItem item2 = item(102L);
    when(backlogItemRepository.findBySprintId(CANCELLED_SPRINT_ID)).thenReturn(List.of(item1, item2));
    when(sprintFacade.findNextSprint(TEAM_ID, 1)).thenReturn(Optional.of(nextSprint()));

    listener.onSprintCancelled(EVENT);

    assertThat(item1.getSprintId()).isEqualTo(NEXT_SPRINT_ID);
    assertThat(item2.getSprintId()).isEqualTo(NEXT_SPRINT_ID);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<Iterable<BacklogItem>> captor = ArgumentCaptor.forClass(Iterable.class);
    verify(backlogItemRepository).saveAll(captor.capture());
    assertThat(captor.getValue()).containsExactlyInAnyOrder(item1, item2);
  }

  @Test
  @DisplayName("次スプリントがないとき、割り当て済みアイテムの割り当ては解除される")
  void unassignsItemsWhenThereIsNoNextSprint() {
    BacklogItem item1 = item(101L);
    when(backlogItemRepository.findBySprintId(CANCELLED_SPRINT_ID)).thenReturn(List.of(item1));
    when(sprintFacade.findNextSprint(TEAM_ID, 1)).thenReturn(Optional.empty());

    listener.onSprintCancelled(EVENT);

    assertThat(item1.getSprintId()).isNull();
    verify(backlogItemRepository).saveAll(any());
  }

  @Test
  @DisplayName("割り当て済みアイテムがないとき、次スプリントを引かず保存もしない")
  void doesNothingWhenNoItemsAreAssigned() {
    when(backlogItemRepository.findBySprintId(CANCELLED_SPRINT_ID)).thenReturn(List.of());

    listener.onSprintCancelled(EVENT);

    verify(sprintFacade, never()).findNextSprint(any(), org.mockito.ArgumentMatchers.anyInt());
    verify(backlogItemRepository, never()).saveAll(any());
  }
}
