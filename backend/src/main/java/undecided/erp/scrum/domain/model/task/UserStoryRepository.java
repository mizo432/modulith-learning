package undecided.erp.scrum.domain.model.task;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.epic.Epic;
import undecided.erp.scrum.domain.model.product.ProductBacklog;

/**
 * ユーザーストーリーに関連するデータアクセス操作を提供するリポジトリインターフェース。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 ユーザーストーリーエンティティに対する標準的なCRUD操作と、
 * カスタムクエリメソッドを提供します。
 */
@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, SnowflakeId> {

  /**
   * 指定されたプロダクトバックログに関連するすべてのユーザーストーリーを取得します。
   *
   * @param productBacklog 検索対象のプロダクトバックログ
   * @return 指定されたプロダクトバックログに関連するユーザーストーリーのリスト
   */
  List<UserStory> findByProductBacklog(ProductBacklog productBacklog);

  /**
   * 指定されたプロダクトバックログIDに関連するすべてのユーザーストーリーを取得します。
   *
   * @param backlogId 検索対象のプロダクトバックログID
   * @return 指定されたプロダクトバックログIDに関連するユーザーストーリーのリスト
   */
  List<UserStory> findByProductBacklogBacklogId(SnowflakeId backlogId);

  /**
   * 指定されたエピックに関連するすべてのユーザーストーリーを取得します。
   *
   * @param epic 検索対象のエピック
   * @return 指定されたエピックに関連するユーザーストーリーのリスト
   */
  List<UserStory> findByEpic(Epic epic);

  /**
   * 指定されたエピックIDに関連するすべてのユーザーストーリーを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 指定されたエピックIDに関連するユーザーストーリーのリスト
   */
  List<UserStory> findByEpicEpicId(SnowflakeId epicId);

  /**
   * 指定されたタイトルを持つユーザーストーリーを検索します。
   *
   * @param title 検索対象のタイトル
   * @return 指定されたタイトルを持つユーザーストーリーのOptional
   */
  Optional<UserStory> findByTitle(String title);

  /**
   * 指定されたタイトルを含むユーザーストーリーを検索します。
   *
   * @param title 検索対象のタイトルの一部
   * @return 指定されたタイトルを含むユーザーストーリーのリスト
   */
  List<UserStory> findByTitleContaining(String title);

  /**
   * 指定されたステータスを持つユーザーストーリーを検索します。
   *
   * @param status 検索対象のステータス
   * @return 指定されたステータスを持つユーザーストーリーのリスト
   */
  List<UserStory> findByStatus(UserStory.Status status);

  /**
   * 指定された優先度を持つユーザーストーリーを検索します。
   *
   * @param priority 検索対象の優先度
   * @return 指定された優先度を持つユーザーストーリーのリスト
   */
  List<UserStory> findByPriority(UserStory.Priority priority);

  /**
   * 指定されたプロダクトバックログIDとステータスを持つユーザーストーリーを検索します。
   *
   * @param backlogId 検索対象のプロダクトバックログID
   * @param status 検索対象のステータス
   * @return 指定されたプロダクトバックログIDとステータスを持つユーザーストーリーのリスト
   */
  List<UserStory> findByProductBacklogBacklogIdAndStatus(
      SnowflakeId backlogId, UserStory.Status status);

  /**
   * 指定されたエピックIDとステータスを持つユーザーストーリーを検索します。
   *
   * @param epicId 検索対象のエピックID
   * @param status 検索対象のステータス
   * @return 指定されたエピックIDとステータスを持つユーザーストーリーのリスト
   */
  List<UserStory> findByEpicEpicIdAndStatus(SnowflakeId epicId, UserStory.Status status);

  /**
   * 複数のスプリントにまたがるタスクを持つユーザーストーリーを検索します。
   *
   * @return 複数のスプリントにまたがるタスクを持つユーザーストーリーのリスト
   */
  @Query(
      "SELECT DISTINCT us FROM UserStory us JOIN us.tasks t "
          + "GROUP BY us.storyId "
          + "HAVING COUNT(DISTINCT t.sprint.sprintId) > 1")
  List<UserStory> findUserStoriesSpanningMultipleSprints();

  /**
   * 指定されたエピックに属し、複数のスプリントにまたがるタスクを持つユーザーストーリーを検索します。
   *
   * @param epicId 検索対象のエピックID
   * @return 指定されたエピックに属し、複数のスプリントにまたがるタスクを持つユーザーストーリーのリスト
   */
  @Query(
      "SELECT DISTINCT us FROM UserStory us JOIN us.tasks t "
          + "WHERE us.epic.epicId = :epicId "
          + "GROUP BY us.storyId "
          + "HAVING COUNT(DISTINCT t.sprint.sprintId) > 1")
  List<UserStory> findUserStoriesInEpicSpanningMultipleSprints(@Param("epicId") SnowflakeId epicId);

  /**
   * 指定されたプロダクトバックログに関連するすべてのユーザーストーリーをバックログ順序でソートして取得します。
   *
   * @param productBacklog 検索対象のプロダクトバックログ
   * @return 指定されたプロダクトバックログに関連するユーザーストーリーのリスト（バックログ順序でソート）
   */
  List<UserStory> findByProductBacklogOrderByBacklogOrderAsc(ProductBacklog productBacklog);

  /**
   * 指定されたプロダクトバックログIDに関連するすべてのユーザーストーリーをバックログ順序でソートして取得します。
   *
   * @param backlogId 検索対象のプロダクトバックログID
   * @return 指定されたプロダクトバックログIDに関連するユーザーストーリーのリスト（バックログ順序でソート）
   */
  List<UserStory> findByProductBacklogBacklogIdOrderByBacklogOrderAsc(SnowflakeId backlogId);
}
