package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDate;

/**
 * 割り当て可能なスプリントの応答（Issue #525）。
 *
 * @param sprintId スプリントID（文字列）
 * @param sprintNumber スプリント番号
 * @param title スプリントのタイトル
 * @param status スプリントの状態（PLANNED / ACTIVE）
 * @param startDate 開始日
 * @param endDate 終了日
 * @param assigned このアイテムが現在割り当てられているスプリントかどうか
 */
record AssignableSprintResponse(
    String sprintId,
    Integer sprintNumber,
    String title,
    String status,
    LocalDate startDate,
    LocalDate endDate,
    boolean assigned) {}
