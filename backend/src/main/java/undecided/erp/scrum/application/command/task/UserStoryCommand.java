package undecided.erp.scrum.application.command.task;

import java.util.List;
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
 * ユーザーストーリーに関連するコマンド操作を提供するサービスクラス。
 *
 * <p>このクラスはユーザーストーリーの作成、更新、削除、ステータス変更、分割などの操作を提供します。
 */
@Service
@RequiredArgsConstructor
public class UserStoryCommand {

  private final UserStoryRepository userStoryRepository;
  private final ProductBacklogRepository productBacklogRepository;
  private final EpicRepository epicRepository;

  /**
   * 新しいユーザーストーリーを作成します。
   *
   * @param backlogId プロダクトバックログID
   * @param epicId エピックID（オプション）
   * @param title タイトル
   * @param description 説明
   * @param acceptanceCriteria 受け入れ基準
   * @param priority 優先度
   * @param storyPoints ストーリーポイント
   * @return 作成されたユーザーストーリー
   */
  @Transactional
  public UserStory createUserStory(
      SnowflakeId backlogId,
      SnowflakeId epicId,
      String title,
      String description,
      String acceptanceCriteria,
      Priority priority,
      Integer storyPoints) {
    ProductBacklog productBacklog =
        productBacklogRepository
            .findById(backlogId)
            .orElseThrow(
                () -> new IllegalArgumentException("Product backlog not found: " + backlogId));

    Epic epic = null;
    if (epicId != null) {
      epic =
          epicRepository
              .findById(epicId)
              .orElseThrow(() -> new IllegalArgumentException("Epic not found: " + epicId));
    }

    // バックログ内の最大順序を取得
    Integer maxOrder =
        userStoryRepository.findByProductBacklogOrderByBacklogOrderAsc(productBacklog).stream()
            .map(UserStory::getBacklogOrder)
            .filter(order -> order != null)
            .max(Integer::compareTo)
            .orElse(0);

    UserStory userStory = new UserStory();
    userStory.setStoryId(SnowflakeId.newInstance());
    userStory.setProductBacklog(productBacklog);
    userStory.setEpic(epic);
    userStory.setTitle(title);
    userStory.setDescription(description);
    userStory.setAcceptanceCriteria(acceptanceCriteria);
    userStory.setPriority(priority);
    userStory.setStoryPoints(storyPoints);
    userStory.setStatus(Status.TODO);
    userStory.setBacklogOrder(maxOrder + 1); // 最大順序+1を設定

    return userStoryRepository.save(userStory);
  }

  /**
   * 既存のユーザーストーリーを更新します。
   *
   * @param storyId ユーザーストーリーID
   * @param epicId エピックID（オプション）
   * @param title タイトル
   * @param description 説明
   * @param acceptanceCriteria 受け入れ基準
   * @param priority 優先度
   * @param storyPoints ストーリーポイント
   * @return 更新されたユーザーストーリー
   */
  @Transactional
  public UserStory updateUserStory(
      SnowflakeId storyId,
      SnowflakeId epicId,
      String title,
      String description,
      String acceptanceCriteria,
      Priority priority,
      Integer storyPoints) {
    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("User story not found: " + storyId));

    Epic epic = null;
    if (epicId != null) {
      epic =
          epicRepository
              .findById(epicId)
              .orElseThrow(() -> new IllegalArgumentException("Epic not found: " + epicId));
    }

    userStory.setEpic(epic);
    userStory.setTitle(title);
    userStory.setDescription(description);
    userStory.setAcceptanceCriteria(acceptanceCriteria);
    userStory.setPriority(priority);
    userStory.setStoryPoints(storyPoints);

    return userStoryRepository.save(userStory);
  }

  /**
   * 既存のユーザーストーリーを削除します。
   *
   * @param storyId ユーザーストーリーID
   */
  @Transactional
  public void deleteUserStory(SnowflakeId storyId) {
    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("User story not found: " + storyId));

    userStoryRepository.delete(userStory);
  }

  /**
   * ユーザーストーリーのステータスを変更します。
   *
   * @param storyId ユーザーストーリーID
   * @param status 新しいステータス
   * @return 更新されたユーザーストーリー
   */
  @Transactional
  public UserStory changeStatus(SnowflakeId storyId, Status status) {
    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("User story not found: " + storyId));

    userStory.setStatus(status);

    return userStoryRepository.save(userStory);
  }

  /**
   * ユーザーストーリーを分割します。
   *
   * @param storyId 分割元のユーザーストーリーID
   * @param newTitle 新しいユーザーストーリーのタイトル
   * @param newDescription 新しいユーザーストーリーの説明
   * @param newStoryPoints 新しいユーザーストーリーのストーリーポイント
   * @return 分割された新しいユーザーストーリー
   */
  @Transactional
  public UserStory splitUserStory(
      SnowflakeId storyId, String newTitle, String newDescription, Integer newStoryPoints) {
    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("User story not found: " + storyId));

    UserStory newStory = userStory.split(newTitle, newDescription, newStoryPoints);
    newStory.setStoryId(SnowflakeId.newInstance());

    // バックログ内の最大順序を取得
    Integer maxOrder =
        userStoryRepository
            .findByProductBacklogOrderByBacklogOrderAsc(userStory.getProductBacklog())
            .stream()
            .map(UserStory::getBacklogOrder)
            .filter(order -> order != null)
            .max(Integer::compareTo)
            .orElse(0);

    newStory.setBacklogOrder(maxOrder + 1); // 最大順序+1を設定

    userStoryRepository.save(userStory); // 元のストーリーを保存
    return userStoryRepository.save(newStory); // 新しいストーリーを保存
  }

  /**
   * ユーザーストーリーの順序を変更します。
   *
   * @param storyId ユーザーストーリーID
   * @param newOrder 新しい順序
   * @return 更新されたユーザーストーリー
   */
  @Transactional
  public UserStory changeOrder(SnowflakeId storyId, Integer newOrder) {
    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("User story not found: " + storyId));

    Integer oldOrder = userStory.getBacklogOrder();
    if (oldOrder == null || oldOrder.equals(newOrder)) {
      return userStory; // 順序が同じ場合は何もしない
    }

    // 同じバックログ内のストーリーを取得
    List<UserStory> stories =
        userStoryRepository.findByProductBacklogOrderByBacklogOrderAsc(
            userStory.getProductBacklog());

    // 順序を更新
    for (UserStory story : stories) {
      Integer order = story.getBacklogOrder();
      if (order == null) {
        continue;
      }

      if (oldOrder < newOrder) { // 下に移動する場合
        if (order > oldOrder && order <= newOrder) {
          story.setBacklogOrder(order - 1);
          userStoryRepository.save(story);
        }
      } else { // 上に移動する場合
        if (order >= newOrder && order < oldOrder) {
          story.setBacklogOrder(order + 1);
          userStoryRepository.save(story);
        }
      }
    }

    userStory.setBacklogOrder(newOrder);
    return userStoryRepository.save(userStory);
  }
}
