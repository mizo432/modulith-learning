package undecided.agiloom.requirementBacklog.spi;

import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムのスプリント割り当て変更イベント（Issue #525）。
 *
 * <p>割り当て・割り当て先の変更・割り当て解除のたびに発行します。状態は変わりません
 * （割り当ては状態遷移ではない。Issue #717 の決定）。ロングタームイベントパターンにより
 * 履歴はこのイベントで担保します（Issue #572 の決定）。
 *
 * @param backlogItemId バックログアイテムID
 * @param scrumTeamId 所属するスクラムチームID
 * @param previousSprintId 変更前のスプリントID（未割り当てだった場合は null）
 * @param sprintId 変更後のスプリントID（解除の場合は null）
 * @param actorEmployeeId 操作した従業員ID
 * @param occurredAt 発生日時
 */
public record BacklogItemSprintAssignmentChanged(
    SnowflakeId backlogItemId,
    SnowflakeId scrumTeamId,
    @Nullable SnowflakeId previousSprintId,
    @Nullable SnowflakeId sprintId,
    SnowflakeId actorEmployeeId,
    LocalDateTime occurredAt) {}
