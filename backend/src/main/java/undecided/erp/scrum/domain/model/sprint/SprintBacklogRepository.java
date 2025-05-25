package undecided.erp.scrum.domain.model.sprint;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;

/**
 * スプリントバックログに関連するデータアクセス操作を提供するリポジトリインターフェース。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 スプリントバックログエンティティに対する標準的なCRUD操作と、
 * カスタムクエリメソッドを提供します。
 */
@Repository
public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, SnowflakeId> {

  /**
   * 指定されたスプリントIDに関連するすべてのスプリントバックログアイテムを取得します。
   *
   * @param sprintId 検索対象のスプリントID
   * @return 指定されたスプリントIDに関連するスプリントバックログアイテムのリスト
   */
  List<SprintBacklog> findBySprintSprintId(SnowflakeId sprintId);

  /**
   * 指定されたユーザーストーリーIDに関連するすべてのスプリントバックログアイテムを取得します。
   *
   * @param storyId 検索対象のユーザーストーリーID
   * @return 指定されたユーザーストーリーIDに関連するスプリントバックログアイテムのリスト
   */
  List<SprintBacklog> findByUserStoryStoryId(SnowflakeId storyId);

  /**
   * 指定されたスプリントIDと名前を持つスプリントバックログアイテムを検索します。
   *
   * @param sprintId 検索対象のスプリントID
   * @param name 検索対象の名前
   * @return 指定されたスプリントIDと名前を持つスプリントバックログアイテムのリスト
   */
  List<SprintBacklog> findBySprintSprintIdAndName(SnowflakeId sprintId, String name);

  /**
   * 指定されたスプリントIDに関連する、残り工数が0より大きいスプリントバックログアイテムを取得します。
   *
   * @param sprintId 検索対象のスプリントID
   * @return 指定されたスプリントIDに関連する、残り工数が0より大きいスプリントバックログアイテムのリスト
   */
  List<SprintBacklog> findBySprintSprintIdAndRemainingEffortGreaterThan(
      SnowflakeId sprintId, Integer remainingEffort);
}
