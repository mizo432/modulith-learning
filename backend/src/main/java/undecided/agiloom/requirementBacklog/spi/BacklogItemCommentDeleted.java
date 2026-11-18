package undecided.agiloom.requirementBacklog.spi;

import java.time.LocalDateTime;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムのコメントが削除されたことを示すドメインイベント。
 *
 * <p>通知モジュール（NT）がこのイベントを受信し、コメント参加者（操作者除く）へ通知を作成する。
 *
 * @param backlogItemId バックログアイテムID
 * @param commentId 削除されたコメントID
 * @param actorEmployeeId コメントを削除した従業員ID（操作者）
 * @param occurredAt 発生日時
 */
public record BacklogItemCommentDeleted(
    SnowflakeId backlogItemId,
    SnowflakeId commentId,
    SnowflakeId actorEmployeeId,
    LocalDateTime occurredAt) {}
