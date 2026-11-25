package undecided.agiloom.requirementBacklog.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

/** 依存関係で結ばれたバックログアイテム1件分の表示情報。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BacklogItemDependencyResponse {
  private SnowflakeId backlogItemId;
  private String title;
  private BacklogItemStatus status;
  private String statusLabel;
  /** 依存先が DONE で依存が満たされているか。依存元一覧では対象アイテム自身が依存先になるため、その状態で判定する。 */
  private boolean satisfied;
}
