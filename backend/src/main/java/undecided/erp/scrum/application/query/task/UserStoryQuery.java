package undecided.erp.scrum.application.query.task;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.epic.Epic;
import undecided.erp.scrum.domain.model.epic.EpicRepository;
import undecided.erp.scrum.domain.model.product.ProductBacklog;
import undecided.erp.scrum.domain.model.product.ProductBacklogRepository;
import undecided.erp.scrum.domain.model.task.UserStory;
import undecided.erp.scrum.domain.model.task.UserStory.Priority;
import undecided.erp.scrum.domain.model.task.UserStory.Status;
import undecided.erp.scrum.domain.model.task.UserStoryRepository;

/**
 * ユーザーストーリーに関連するクエリ操作を提供するサービスクラス。
 *
 * <p>このクラスはユーザーストーリーの検索や取得などの操作を提供します。
 */
@Service
@RequiredArgsConstructor
public class UserStoryQuery {

  private final UserStoryRepository userStoryRepository;
  private final ProductBacklogRepository productBacklogRepository;
  private final EpicRepository epicRepository;

  /**
   * 指定されたユーザーストーリーIDに対応するユーザーストーリーを取得します。
   *
   * @param storyId ユーザーストーリーID
   * @return ユーザーストーリーのOptional
   */
  @Transactional(readOnly = true)
  public Optional<UserStory> findById(SnowflakeId storyId) {
    return userStoryRepository.findById(storyId);
  }

  /**
   * 指定されたタイトルを持つユーザーストーリーを検索します。
   *
   * @param title 検索対象のタイトル
   * @return ユーザーストーリーのOptional
   */
  @Transactional(readOnly = true)
  public Optional<UserStory> findByTitle(String title) {
    return userStoryRepository.findByTitle(title);
  }

  /**
   * 指定されたタイトルを含むユーザーストーリーを検索します。
   *
   * @param title 検索対象のタイトルの一部
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByTitleContaining(String title) {
    return userStoryRepository.findByTitleContaining(title);
  }

  /**
   * 指定されたプロダクトバックログに関連するすべてのユーザーストーリーを取得します。
   *
   * @param backlogId プロダクトバックログID
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByProductBacklogId(SnowflakeId backlogId) {
    ProductBacklog productBacklog =
        productBacklogRepository
            .findById(backlogId)
            .orElseThrow(
                () -> new IllegalArgumentException("Product backlog not found: " + backlogId));
    return userStoryRepository.findByProductBacklog(productBacklog);
  }

  /**
   * 指定されたプロダクトバックログに関連するすべてのユーザーストーリーをバックログ順序でソートして取得します。
   *
   * @param backlogId プロダクトバックログID
   * @return ユーザーストーリーのリスト（バックログ順序でソート）
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByProductBacklogIdOrderByBacklogOrderAsc(SnowflakeId backlogId) {
    ProductBacklog productBacklog =
        productBacklogRepository
            .findById(backlogId)
            .orElseThrow(
                () -> new IllegalArgumentException("Product backlog not found: " + backlogId));
    return userStoryRepository.findByProductBacklogOrderByBacklogOrderAsc(productBacklog);
  }

  /**
   * 指定されたエピックに関連するすべてのユーザーストーリーを取得します。
   *
   * @param epicId エピックID
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByEpicId(SnowflakeId epicId) {
    Epic epic =
        epicRepository
            .findById(epicId)
            .orElseThrow(() -> new IllegalArgumentException("Epic not found: " + epicId));
    return userStoryRepository.findByEpic(epic);
  }

  /**
   * 指定されたステータスを持つユーザーストーリーを検索します。
   *
   * @param status 検索対象のステータス
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByStatus(Status status) {
    return userStoryRepository.findByStatus(status);
  }

  /**
   * 指定された優先度を持つユーザーストーリーを検索します。
   *
   * @param priority 検索対象の優先度
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByPriority(Priority priority) {
    return userStoryRepository.findByPriority(priority);
  }

  /**
   * 指定されたプロダクトバックログIDとステータスを持つユーザーストーリーを検索します。
   *
   * @param backlogId 検索対象のプロダクトバックログID
   * @param status 検索対象のステータス
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByProductBacklogIdAndStatus(SnowflakeId backlogId, Status status) {
    return userStoryRepository.findByProductBacklogBacklogIdAndStatus(backlogId, status);
  }

  /**
   * 指定されたエピックIDとステータスを持つユーザーストーリーを検索します。
   *
   * @param epicId 検索対象のエピックID
   * @param status 検索対象のステータス
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findByEpicIdAndStatus(SnowflakeId epicId, Status status) {
    return userStoryRepository.findByEpicEpicIdAndStatus(epicId, status);
  }

  /**
   * すべてのユーザーストーリーを取得します。
   *
   * @return ユーザーストーリーのリスト
   */
  @Transactional(readOnly = true)
  public List<UserStory> findAll() {
    return userStoryRepository.findAll();
  }
}
