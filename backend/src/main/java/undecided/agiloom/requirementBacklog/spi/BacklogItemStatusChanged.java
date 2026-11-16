package undecided.agiloom.requirementBacklog.spi;

import java.time.LocalDateTime;
import java.util.UUID;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムの状態遷移イベント。
 *
 * <p>ロングタームイベントパターンにより、状態遷移の履歴はこのイベントで担保します。
 * Spring Modulith のイベント発行ログ（JDBC）に永続化されるため、
 * 専用の監査ログは持ちません（Issue #572 の決定）。
 * 他事業領域が状態を必要とする場合は、このイベントを受けて自領域のサブセットを保持します。
 *
 * @param backlogItemId バックログアイテムID
 * @param scrumTeamId 所属するスクラムチームID
 * @param fromStatus 遷移前の状態
 * @param toStatus 遷移後の状態
 * @param actorEmployeeId 状態を変更した従業員ID（SMまたはPO）
 * @param occurredAt 発生日時
 */
public record BacklogItemStatusChanged(
    SnowflakeId backlogItemId,
    SnowflakeId scrumTeamId,
    BacklogItemStatus fromStatus,
    BacklogItemStatus toStatus,
    UUID actorEmployeeId,
    LocalDateTime occurredAt) {}
