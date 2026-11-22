package undecided.agiloom.requirementBacklog.spi;

import java.time.LocalDateTime;
import java.util.List;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムの分割イベント。
 *
 * @param backlogItemId 分割元のバックログアイテムID
 * @param scrumTeamId 所属するスクラムチームID
 * @param childItemIds 作成された子アイテムのID
 * @param actorEmployeeId 分割した従業員ID
 * @param occurredAt 発生日時
 */
public record BacklogItemSplit(
    SnowflakeId backlogItemId,
    SnowflakeId scrumTeamId,
    List<SnowflakeId> childItemIds,
    SnowflakeId actorEmployeeId,
    LocalDateTime occurredAt) {}