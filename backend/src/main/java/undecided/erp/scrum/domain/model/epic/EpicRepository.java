package undecided.erp.scrum.domain.model.epic;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.product.Product;

/**
 * エピックに関連するデータアクセス操作を提供するリポジトリインターフェース。
 * <p>
 * このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 エピックエンティティに対する標準的なCRUD操作と、 カスタムクエリメソッドを提供します。
 */
@Repository
public interface EpicRepository extends JpaRepository<Epic, SnowflakeId> {

  /**
   * 指定されたプロダクトに関連するすべてのエピックを取得します。
   *
   * @param product 検索対象のプロダクト
   * @return 指定されたプロダクトに関連するエピックのリスト
   */
  List<Epic> findByProduct(Product product);

  /**
   * 指定されたプロダクトIDに関連するすべてのエピックを取得します。
   *
   * @param productId 検索対象のプロダクトID
   * @return 指定されたプロダクトIDに関連するエピックのリスト
   */
  List<Epic> findByProductProductId(SnowflakeId productId);

  /**
   * 指定されたタイトルを持つエピックを検索します。
   *
   * @param title 検索対象のタイトル
   * @return 指定されたタイトルを持つエピックのリスト
   */
  List<Epic> findByTitleContaining(String title);

  /**
   * 指定されたステータスを持つエピックを検索します。
   *
   * @param status 検索対象のステータス
   * @return 指定されたステータスを持つエピックのリスト
   */
  List<Epic> findByStatus(Epic.Status status);

  /**
   * 指定されたプロダクトIDとステータスを持つエピックを検索します。
   *
   * @param productId 検索対象のプロダクトID
   * @param status 検索対象のステータス
   * @return 指定されたプロダクトIDとステータスを持つエピックのリスト
   */
  List<Epic> findByProductProductIdAndStatus(SnowflakeId productId, Epic.Status status);

  /**
   * 指定されたエピックIDに関連するユーザーストーリーの数を取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するユーザーストーリーの数
   */
  @Query("SELECT COUNT(us) FROM UserStory us WHERE us.epic.epicId = :epicId")
  long countUserStoriesByEpicId(@Param("epicId") SnowflakeId epicId);

  /**
   * 複数のスプリントにまたがるエピックを検索します。
   * <p>
   * このクエリは、2つ以上の異なるスプリントに関連付けられたタスクを持つ ユーザーストーリーが含まれるエピックを検索します。
   *
   * @return 複数のスプリントにまたがるエピックのリスト
   */
  @Query("SELECT DISTINCT e FROM Epic e JOIN e.userStories us JOIN us.tasks t " +
      "GROUP BY e.epicId, us.storyId " +
      "HAVING COUNT(DISTINCT t.sprint.sprintId) > 1")
  List<Epic> findEpicsSpanningMultipleSprints();

  /**
   * 指定されたエピックIDに関連するすべてのスプリントIDを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するスプリントIDのリスト
   */
  @Query("SELECT DISTINCT t.sprint.sprintId FROM Epic e JOIN e.userStories us JOIN us.tasks t " +
      "WHERE e.epicId = :epicId AND t.sprint IS NOT NULL")
  List<SnowflakeId> findSprintIdsByEpicId(@Param("epicId") SnowflakeId epicId);

  /**
   * 指定されたエピックIDとタイトルを持つエピックを検索します。
   *
   * @param epicId 検索対象のエピックID
   * @param title 検索対象のタイトル
   * @return 指定されたエピックIDとタイトルを持つエピックのOptional
   */
  Optional<Epic> findByEpicIdAndTitle(SnowflakeId epicId, String title);
}
