package undecided.agiloom.sprintExecution.internal.query;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.SprintPointsQuery;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.domain.BusinessDayCounter;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スプリントの容量を計算するクエリサービス。
 *
 * <p>ベロシティはチームの完了スプリント実績（完了SP合計 ÷ 営業日数合計）から算出する（#654・#656）。
 * データ不足時のフォールバックは設けない（#655）。
 */
@Service
@Transactional(readOnly = true)
public class SprintCapacityQueryService {

  private final SprintRepository sprintRepository;
  private final SprintPointsQuery sprintPointsQuery;
  private final BusinessDayCounter businessDayCounter;

  public SprintCapacityQueryService(
      SprintRepository sprintRepository,
      SprintPointsQuery sprintPointsQuery,
      BusinessDayCounter businessDayCounter) {
    this.sprintRepository = sprintRepository;
    this.sprintPointsQuery = sprintPointsQuery;
    this.businessDayCounter = businessDayCounter;
  }

  /**
   * 指定したスプリントの容量を返す。
   *
   * @throws IllegalArgumentException スプリントが存在しない場合
   */
  public SprintCapacity getCapacity(@NonNull SnowflakeId sprintId) {
    Sprint target =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + sprintId));
    int targetBusinessDays =
        businessDayCounter.countBusinessDays(target.getStartDate(), target.getEndDate());

    long completedPoints = 0L;
    long completedDays = 0L;
    for (Sprint sprint : sprintRepository.findByTeamId(target.getTeamId())) {
      if (sprint.isSprintZero() || sprint.getStatus() != SprintStatus.COMPLETED) {
        continue;
      }
      completedPoints += sprintPointsQuery.sumCompletedStoryPoints(sprint.getId());
      completedDays +=
          businessDayCounter.countBusinessDays(sprint.getStartDate(), sprint.getEndDate());
    }
    double velocity = completedDays == 0 ? 0.0 : (double) completedPoints / (double) completedDays;
    long assignablePoints = Math.round(velocity * targetBusinessDays);
    long assignedPoints = sprintPointsQuery.sumAssignedStoryPoints(sprintId);
    return new SprintCapacity(
        sprintId,
        targetBusinessDays,
        velocity,
        assignablePoints,
        assignedPoints,
        assignedPoints > assignablePoints);
  }
}
