package undecided.agiloom.requirementBacklog.spi;

import java.time.LocalDateTime;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムの見積もり完了イベント。
 *
 * <p>見積もり時（READY → ESTIMATED 遷移時）および再評価（ESTIMATED 状態での値変更時）に発行されます。
 * 通知の実装は通知モジュール側で行うため、本Issueではイベント発行のみを行います（設計決定#2）。
 *
 * @param backlogItemId バックログアイテムID
 * @param scrumTeamId 所属するスクラムチームID
 * @param storyPoint 設定されたストーリーポイント値
 * @param actorEmployeeId 見積もりを行った従業員ID
 * @param occurredAt 発生日時
 */
public record BacklogItemEstimated(
    SnowflakeId backlogItemId,
    SnowflakeId scrumTeamId,
    Integer storyPoint,
    SnowflakeId actorEmployeeId,
    LocalDateTime occurredAt) {}
