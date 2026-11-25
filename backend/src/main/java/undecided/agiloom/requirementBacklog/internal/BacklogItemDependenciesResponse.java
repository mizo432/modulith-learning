package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムの依存関係（依存先・依存元と充足状態）の取得結果。 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class BacklogItemDependenciesResponse {
  private SnowflakeId backlogItemId;
  /** このアイテムが依存するアイテム（依存先）。 */
  private List<BacklogItemDependencyResponse> dependencies;
  /** このアイテムに依存するアイテム（依存元）。 */
  private List<BacklogItemDependencyResponse> dependents;
  /** 依存先がすべて DONE で依存が満たされているか（依存が無い場合も true）。 */
  private boolean dependenciesSatisfied;
  /** DONE でない依存先の件数。 */
  private int unmetDependencyCount;
}
