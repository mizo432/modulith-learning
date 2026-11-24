package undecided.agiloom.sprintExecution.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.requirementBacklog.spi.SprintPointsQuery;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.domain.BusinessDayCounter;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintCapacityQueryServiceのテスト")
class SprintCapacityQueryServiceTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId TARGET_ID = SnowflakeId.of(3001L);
  private static final SnowflakeId DONE_1_ID = SnowflakeId.of(3101L);
  private static final SnowflakeId DONE_2_ID = SnowflakeId.of(3102L);
  private static final SnowflakeId ACTIVE_ID = SnowflakeId.of(3103L);

  private final SprintRepository sprintRepository = mock(SprintRepository.class);
  private final SprintPointsQuery sprintPointsQuery = mock(SprintPointsQuery.class);
  private final BusinessDayCounter businessDayCounter = mock(BusinessDayCounter.class);
  private final SprintCapacityQueryService service =
      new SprintCapacityQueryService(sprintRepository, sprintPointsQuery, businessDayCounter);

  private Sprint sprint(SnowflakeId id, int number, SprintStatus status) {
    return sprint(id, number, status, LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 13));
  }

  private Sprint sprint(
      SnowflakeId id, int number, SprintStatus status, LocalDate startDate, LocalDate endDate) {
    return Sprint.builder()
        .id(id)
        .sprintNumber(number)
        .teamId(TEAM_ID)
        .status(status)
        .title("Sprint " + number)
        .startDate(startDate)
        .endDate(endDate)
        .build();
  }

  private void stubTarget(Sprint target, List<Sprint> teamSprints) {
    when(sprintRepository.findById(TARGET_ID)).thenReturn(Optional.of(target));
    when(sprintRepository.findByTeamId(TEAM_ID)).thenReturn(teamSprints);
    when(businessDayCounter.countBusinessDays(any(), any())).thenReturn(10);
  }

  @Test
  @DisplayName("完了スプリントが無いとき、ベロシティ0で割り当て可能0となる")
  void noCompletedSprintsMeansZeroVelocity() {
    Sprint target = sprint(TARGET_ID, 5, SprintStatus.PLANNED);
    stubTarget(target, List.of(target));
    when(sprintPointsQuery.sumAssignedStoryPoints(TARGET_ID)).thenReturn(13L);

    SprintCapacity capacity = service.getCapacity(TARGET_ID);

    assertThat(capacity.velocityPerBusinessDay()).isZero();
    assertThat(capacity.assignablePoints()).isZero();
    assertThat(capacity.assignedPoints()).isEqualTo(13L);
    assertThat(capacity.businessDays()).isEqualTo(10);
    assertThat(capacity.overAllocated()).isTrue();
  }

  @Test
  @DisplayName("完了スプリント1件の実績から、ベロシティと割り当て可能ポイントを計算する")
  void velocityFromSingleCompletedSprint() {
    Sprint target = sprint(TARGET_ID, 5, SprintStatus.PLANNED);
    Sprint done = sprint(DONE_1_ID, 4, SprintStatus.COMPLETED);
    stubTarget(target, List.of(done, target));
    when(sprintPointsQuery.sumCompletedStoryPoints(DONE_1_ID)).thenReturn(50L);
    when(sprintPointsQuery.sumAssignedStoryPoints(TARGET_ID)).thenReturn(50L);

    SprintCapacity capacity = service.getCapacity(TARGET_ID);

    assertThat(capacity.velocityPerBusinessDay()).isEqualTo(5.0);
    assertThat(capacity.assignablePoints()).isEqualTo(50L);
    assertThat(capacity.overAllocated()).isFalse();
  }

  @Test
  @DisplayName("複数の完了スプリントは営業日数で加重平均する")
  void velocityIsWeightedAverageAcrossCompletedSprints() {
    Sprint target = sprint(TARGET_ID, 6, SprintStatus.PLANNED);
    Sprint done1 = sprint(DONE_1_ID, 4, SprintStatus.COMPLETED);
    Sprint done2 =
        sprint(
            DONE_2_ID,
            5,
            SprintStatus.COMPLETED,
            LocalDate.of(2026, 10, 19),
            LocalDate.of(2026, 10, 23));
    stubTarget(target, List.of(done1, done2, target));
    when(businessDayCounter.countBusinessDays(eq(done2.getStartDate()), eq(done2.getEndDate())))
        .thenReturn(5);
    when(sprintPointsQuery.sumCompletedStoryPoints(DONE_1_ID)).thenReturn(50L);
    when(sprintPointsQuery.sumCompletedStoryPoints(DONE_2_ID)).thenReturn(10L);
    when(sprintPointsQuery.sumAssignedStoryPoints(TARGET_ID)).thenReturn(0L);

    SprintCapacity capacity = service.getCapacity(TARGET_ID);

    assertThat(capacity.velocityPerBusinessDay()).isEqualTo(4.0);
    assertThat(capacity.assignablePoints()).isEqualTo(40L);
  }

  @Test
  @DisplayName("完了以外のスプリントはベロシティ計算に含めない")
  void nonCompletedSprintsAreExcludedFromVelocity() {
    Sprint target = sprint(TARGET_ID, 5, SprintStatus.PLANNED);
    Sprint active = sprint(ACTIVE_ID, 4, SprintStatus.ACTIVE);
    stubTarget(target, List.of(active, target));
    when(sprintPointsQuery.sumAssignedStoryPoints(TARGET_ID)).thenReturn(0L);

    SprintCapacity capacity = service.getCapacity(TARGET_ID);

    assertThat(capacity.velocityPerBusinessDay()).isZero();
    assertThat(capacity.assignablePoints()).isZero();
  }

  @Test
  @DisplayName("割り当て済みが割り当て可能を超えるとき、overAllocatedがtrueとなる")
  void overAllocatedWhenAssignedExceedsAssignable() {
    Sprint target = sprint(TARGET_ID, 5, SprintStatus.PLANNED);
    Sprint done = sprint(DONE_1_ID, 4, SprintStatus.COMPLETED);
    stubTarget(target, List.of(done, target));
    when(sprintPointsQuery.sumCompletedStoryPoints(DONE_1_ID)).thenReturn(50L);
    when(sprintPointsQuery.sumAssignedStoryPoints(TARGET_ID)).thenReturn(55L);

    SprintCapacity capacity = service.getCapacity(TARGET_ID);

    assertThat(capacity.assignablePoints()).isEqualTo(50L);
    assertThat(capacity.overAllocated()).isTrue();
  }

  @Test
  @DisplayName("対象スプリントが存在しないときは例外となる")
  void missingTargetThrows() {
    when(sprintRepository.findById(TARGET_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.getCapacity(TARGET_ID))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
