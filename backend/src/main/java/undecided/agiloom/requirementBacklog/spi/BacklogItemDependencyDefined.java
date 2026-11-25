package undecided.agiloom.requirementBacklog.spi;

import java.time.LocalDateTime;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムの依存関係が定義されたイベント（RDRA EVT-BL-014）。
 *
 * @param backlogItemId 依存元のバックログアイテムID
 * @param dependencyItemId 依存先のバックログアイテムID
 * @param scrumTeamId 所属するスクラムチームID
 * @param actorEmployeeId 依存関係を定義した従業員ID（POまたはSM）
 * @param occurredAt 発生日時
 */
public record BacklogItemDependencyDefined(
    SnowflakeId backlogItemId,
    SnowflakeId dependencyItemId,
    SnowflakeId scrumTeamId,
    SnowflakeId actorEmployeeId,
    LocalDateTime occurredAt) {}
