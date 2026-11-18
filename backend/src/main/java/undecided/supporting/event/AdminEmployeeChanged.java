package undecided.supporting.event;

import undecided.supporting.snowflake.SnowflakeId;

/**
 * 従業員に対するADMINロールの付与・剥離を他事業領域へ伝えるイベント。
 *
 * <p>requirementBacklog などの他領域がこのイベントを受信して、
 * 自領域内にADMIN保持者のサブセット（射影）を保持・更新する。
 *
 * @param employeeId 従業員ID
 * @param isAdmin ADMINロールが付与された場合は true、剥離された場合は false
 */
public record AdminEmployeeChanged(SnowflakeId employeeId, boolean isAdmin) {}
