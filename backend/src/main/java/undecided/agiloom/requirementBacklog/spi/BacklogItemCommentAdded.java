package undecided.agiloom.requirementBacklog.spi;

import java.time.LocalDateTime;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムにコメントが追加されたことを示すドメインイベント。
 *
 * <p>通知モジュール（NT）がこのイベントを受信し、コメント参加者（操作者除く）へ通知を作成する。
 *
 * @param backlogItemId バックログアイテムID
 * @param commentId 追加されたコメントID
 * @param authorEmployeeId コメントを投稿した従業員ID（操作者）
 * @param authorName コメントを投稿した従業員名
 * @param occurredAt 発生日時
 */
public record BacklogItemCommentAdded(
    SnowflakeId backlogItemId,
    SnowflakeId commentId,
    SnowflakeId authorEmployeeId,
    String authorName,
    LocalDateTime occurredAt) {}
