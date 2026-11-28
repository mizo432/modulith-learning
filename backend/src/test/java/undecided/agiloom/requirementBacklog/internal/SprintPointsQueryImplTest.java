package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintPointsQueryImplのテスト")
class SprintPointsQueryImplTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId SPRINT_ID = SnowflakeId.of(3001L);

  private final BacklogItemRepository backlogItemRepository = mock(BacklogItemRepository.class);
  private final SprintPointsQueryImpl query = new SprintPointsQueryImpl(backlogItemRepository);

  private BacklogItem item(BacklogItemStatus status, Integer storyPoint) {
    return BacklogItem.builder()
        .id(SnowflakeId.of(100L))
        .title("Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .status(status)
        .storyPoint(storyPoint)
        .sprintId(SPRINT_ID)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Test
  @DisplayName("割り当て済み合計は、対象スプリントの全アイテムのSP合計（未設定は0）となる")
  void sumAssignedIncludesAllStatusesAndTreatsNullAsZero() {
    when(backlogItemRepository.findBySprintId(SPRINT_ID))
        .thenReturn(
            List.of(
                item(BacklogItemStatus.ESTIMATED, 5),
                item(BacklogItemStatus.IN_PROGRESS, 8),
                item(BacklogItemStatus.SUGGESTED, null)));

    assertThat(query.sumAssignedStoryPoints(SPRINT_ID)).isEqualTo(13L);
  }

  @Test
  @DisplayName("割り当て済み合計は、アイテムが無いとき0となる")
  void sumAssignedIsZeroWhenNoItems() {
    when(backlogItemRepository.findBySprintId(SPRINT_ID)).thenReturn(List.of());

    assertThat(query.sumAssignedStoryPoints(SPRINT_ID)).isZero();
  }

  @Test
  @DisplayName("完了合計は、DONEのアイテムのSP合計のみとなる")
  void sumCompletedIncludesDoneOnly() {
    when(backlogItemRepository.findBySprintId(SPRINT_ID))
        .thenReturn(
            List.of(
                item(BacklogItemStatus.DONE, 5),
                item(BacklogItemStatus.DONE, 3),
                item(BacklogItemStatus.ESTIMATED, 8),
                item(BacklogItemStatus.IN_PROGRESS, 2)));

    assertThat(query.sumCompletedStoryPoints(SPRINT_ID)).isEqualTo(8L);
  }

  @Test
  @DisplayName("完了合計は、DONEが無いとき0となる")
  void sumCompletedIsZeroWhenNoDoneItems() {
    when(backlogItemRepository.findBySprintId(SPRINT_ID))
        .thenReturn(List.of(item(BacklogItemStatus.ESTIMATED, 8)));

    assertThat(query.sumCompletedStoryPoints(SPRINT_ID)).isZero();
  }

  @Test
  @DisplayName("完了合計は、DONEでSP未設定のアイテムを0として扱う")
  void sumCompletedTreatsNullStoryPointAsZero() {
    when(backlogItemRepository.findBySprintId(SPRINT_ID))
        .thenReturn(List.of(item(BacklogItemStatus.DONE, null)));

    assertThat(query.sumCompletedStoryPoints(SPRINT_ID)).isZero();
  }
}
