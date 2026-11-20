package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import undecided.agiloom.requirementBacklog.spi.FeatureBacklogItem;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャーとバックログアイテムの関連リポジトリ。
 */
public interface FeatureBacklogItemRepository extends JpaRepository<FeatureBacklogItem, FeatureBacklogItem.FeatureBacklogItemId> {

  /**
   * フィーチャーIDで関連一覧を取得します。
   *
   * @param featureId フィーチャーID
   * @return 関連一覧
   */
  @Query("SELECT fbi FROM FeatureBacklogItem fbi WHERE fbi.featureId = :featureId")
  List<FeatureBacklogItem> findByFeatureId(@Param("featureId") SnowflakeId featureId);

  /**
   * バックログアイテムIDで関連を取得します。
   *
   * @param backlogItemId バックログアイテムID
   * @return 関連（存在する場合）
   */
  @Query("SELECT fbi FROM FeatureBacklogItem fbi WHERE fbi.backlogItemId = :backlogItemId")
  Optional<FeatureBacklogItem> findByBacklogItemId(@Param("backlogItemId") SnowflakeId backlogItemId);

  /**
   * フィーチャーIDで関連を削除します。
   *
   * @param featureId フィーチャーID
   */
  void deleteByFeatureId(SnowflakeId featureId);
}
