package undecided.agiloom.requirementBacklog.internal;

import undecided.agiloom.requirementBacklog.spi.BacklogItemDependency;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテム間の依存関係のリポジトリ。 */
interface BacklogItemDependencyRepository extends CrudRepository<BacklogItemDependency, SnowflakeId> {

  /** 依存元のアイテムに紐づく依存関係の一覧を取得する。 */
  List<BacklogItemDependency> findByBacklogItemId(SnowflakeId backlogItemId);

  /** 依存先のアイテムに紐づく依存関係の一覧を取得する。 */
  List<BacklogItemDependency> findByDependencyItemId(SnowflakeId dependencyItemId);

  /** 依存元と依存先の組み合わせで依存関係を取得する。 */
  Optional<BacklogItemDependency> findByBacklogItemIdAndDependencyItemId(
      SnowflakeId backlogItemId, SnowflakeId dependencyItemId);
}
