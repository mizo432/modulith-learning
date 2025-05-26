package undecided.erp.scrum.domain.model.task;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.sprint.Sprint;

/**
 * タスクに関連するデータアクセス操作を提供するリポジトリインターフェース。
 *
 * <p>このインターフェースはSpring Data JPAのJpaRepositoryを拡張し、 タスクエンティティに対する標準的なCRUD操作と、カスタムクエリメソッドを提供します。
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, SnowflakeId> {

  /**
   * 指定されたユーザーストーリーに関連するすべてのタスクを取得します。
   *
   * @param userStory 検索対象のユーザーストーリー
   * @return 指定されたユーザーストーリーに関連するタスクのリスト
   */
  List<Task> findByUserStory(UserStory userStory);

  /**
   * 指定されたユーザーストーリーIDに関連するすべてのタスクを取得します。
   *
   * @param storyId 検索対象のユーザーストーリーID
   * @return 指定されたユーザーストーリーIDに関連するタスクのリスト
   */
  List<Task> findByUserStoryStoryId(SnowflakeId storyId);

  /**
   * 指定されたスプリントに関連するすべてのタスクを取得します。
   *
   * @param sprint 検索対象のスプリント
   * @return 指定されたスプリントに関連するタスクのリスト
   */
  List<Task> findBySprint(Sprint sprint);

  /**
   * 指定されたスプリントIDに関連するすべてのタスクを取得します。
   *
   * @param sprintId 検索対象のスプリントID
   * @return 指定されたスプリントIDに関連するタスクのリスト
   */
  List<Task> findBySprintSprintId(SnowflakeId sprintId);

  /**
   * 指定されたタイトルを持つタスクを検索します。
   *
   * @param title 検索対象のタイトル
   * @return 指定されたタイトルを持つタスクのOptional
   */
  Optional<Task> findByTitle(String title);

  /**
   * 指定されたタイトルを含むタスクを検索します。
   *
   * @param title 検索対象のタイトルの一部
   * @return 指定されたタイトルを含むタスクのリスト
   */
  List<Task> findByTitleContaining(String title);

  /**
   * 指定されたステータスを持つタスクを検索します。
   *
   * @param status 検索対象のステータス
   * @return 指定されたステータスを持つタスクのリスト
   */
  List<Task> findByStatus(Task.Status status);

  /**
   * 指定された担当者IDを持つタスクを検索します。
   *
   * @param assignee 検索対象の担当者ID
   * @return 指定された担当者IDを持つタスクのリスト
   */
  List<Task> findByAssignee(Long assignee);

  /**
   * 指定されたユーザーストーリーIDとステータスを持つタスクを検索します。
   *
   * @param storyId 検索対象のユーザーストーリーID
   * @param status 検索対象のステータス
   * @return 指定されたユーザーストーリーIDとステータスを持つタスクのリスト
   */
  List<Task> findByUserStoryStoryIdAndStatus(SnowflakeId storyId, Task.Status status);

  /**
   * 指定されたスプリントIDとステータスを持つタスクを検索します。
   *
   * @param sprintId 検索対象のスプリントID
   * @param status 検索対象のステータス
   * @return 指定されたスプリントIDとステータスを持つタスクのリスト
   */
  List<Task> findBySprintSprintIdAndStatus(SnowflakeId sprintId, Task.Status status);

  /**
   * 指定されたユーザーストーリーIDと担当者IDを持つタスクを検索します。
   *
   * @param storyId 検索対象のユーザーストーリーID
   * @param assignee 検索対象の担当者ID
   * @return 指定されたユーザーストーリーIDと担当者IDを持つタスクのリスト
   */
  List<Task> findByUserStoryStoryIdAndAssignee(SnowflakeId storyId, Long assignee);

  /**
   * 指定されたスプリントIDと担当者IDを持つタスクを検索します。
   *
   * @param sprintId 検索対象のスプリントID
   * @param assignee 検索対象の担当者ID
   * @return 指定されたスプリントIDと担当者IDを持つタスクのリスト
   */
  List<Task> findBySprintSprintIdAndAssignee(SnowflakeId sprintId, Long assignee);

  /**
   * 指定されたステータスと担当者IDを持つタスクを検索します。
   *
   * @param status 検索対象のステータス
   * @param assignee 検索対象の担当者ID
   * @return 指定されたステータスと担当者IDを持つタスクのリスト
   */
  List<Task> findByStatusAndAssignee(Task.Status status, Long assignee);
}
