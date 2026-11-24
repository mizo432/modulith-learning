package undecided.agiloom.sprintExecution.spi;

import java.time.LocalDateTime;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スプリントのキャンセルイベント（Issue #525、決定 #653）。
 *
 * <p>ステータスが CANCELLED へ変更されたときと、スプリントが論理削除されたときに発行します。
 * 割り当て済みバックログアイテムの次スプリントへのスライドは、このイベントを受けて
 * バックログ側が行います。
 *
 * @param sprintId キャンセルされたスプリントID
 * @param scrumTeamId 所属するスクラムチームID
 * @param sprintNumber スプリント番号（次スプリントの特定に使う）
 * @param occurredAt 発生日時
 */
public record SprintCancelled(
    SnowflakeId sprintId, SnowflakeId scrumTeamId, int sprintNumber, LocalDateTime occurredAt) {}
