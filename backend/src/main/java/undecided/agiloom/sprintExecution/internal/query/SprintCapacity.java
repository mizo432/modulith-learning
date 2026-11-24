package undecided.agiloom.sprintExecution.internal.query;

import undecided.supporting.snowflake.SnowflakeId;

/**
 * スプリントの容量（割り当て可能ポイントと割り当て済みポイント）。
 *
 * @param sprintId 対象スプリントのID
 * @param businessDays 対象スプリントの営業日数
 * @param velocityPerBusinessDay チームのベロシティ（SP/営業日）。完了スプリントが無い場合は0
 * @param assignablePoints 割り当て可能ポイント（ベロシティ × 営業日数、四捨五入）
 * @param assignedPoints 現在の割り当て済みポイント合計
 * @param overAllocated 割り当て済みが割り当て可能を超えているかどうか
 */
public record SprintCapacity(
    SnowflakeId sprintId,
    int businessDays,
    double velocityPerBusinessDay,
    long assignablePoints,
    long assignedPoints,
    boolean overAllocated) {}
