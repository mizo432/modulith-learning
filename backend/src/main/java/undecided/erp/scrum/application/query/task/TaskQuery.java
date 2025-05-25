package undecided.erp.scrum.application.query.task;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.sprint.Sprint;
import undecided.erp.scrum.domain.model.sprint.SprintRepository;
import undecided.erp.scrum.domain.model.task.Task;
import undecided.erp.scrum.domain.model.task.Task.Status;
import undecided.erp.scrum.domain.model.task.TaskAttachment;
import undecided.erp.scrum.domain.model.task.TaskAttachmentRepository;
import undecided.erp.scrum.domain.model.task.TaskComment;
import undecided.erp.scrum.domain.model.task.TaskCommentRepository;
import undecided.erp.scrum.domain.model.task.TaskHistory;
import undecided.erp.scrum.domain.model.task.TaskHistoryRepository;
import undecided.erp.scrum.domain.model.task.TaskRepository;
import undecided.erp.scrum.domain.model.task.UserStory;
import undecided.erp.scrum.domain.model.task.UserStoryRepository;

/**
 * タスクに関連するクエリ操作を提供するサービスクラス。
 *
 * <p>このクラスはタスクの検索や取得などの操作を提供します。 また、タスクコメント、履歴、添付ファイルの取得も行います。
 */
@Service
@RequiredArgsConstructor
public class TaskQuery {

  private final TaskRepository taskRepository;
  private final UserStoryRepository userStoryRepository;
  private final SprintRepository sprintRepository;
  private final TaskCommentRepository taskCommentRepository;
  private final TaskHistoryRepository taskHistoryRepository;
  private final TaskAttachmentRepository taskAttachmentRepository;

  /**
   * 指定されたタスクIDに対応するタスクを取得します。
   *
   * @param taskId タスクID
   * @return タスクのOptional
   */
  @Transactional(readOnly = true)
  public Optional<Task> findById(SnowflakeId taskId) {
    return taskRepository.findById(taskId);
  }

  /**
   * 指定されたタイトルを持つタスクを検索します。
   *
   * @param title 検索対象のタイトル
   * @return タスクのOptional
   */
  @Transactional(readOnly = true)
  public Optional<Task> findByTitle(String title) {
    return taskRepository.findByTitle(title);
  }

  /**
   * 指定されたタイトルを含むタスクを検索します。
   *
   * @param title 検索対象のタイトルの一部
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findByTitleContaining(String title) {
    return taskRepository.findByTitleContaining(title);
  }

  /**
   * 指定されたユーザーストーリーに関連するすべてのタスクを取得します。
   *
   * @param storyId ユーザーストーリーID
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findByUserStoryId(SnowflakeId storyId) {
    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("User story not found: " + storyId));
    return taskRepository.findByUserStory(userStory);
  }

  /**
   * 指定されたスプリントに関連するすべてのタスクを取得します。
   *
   * @param sprintId スプリントID
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findBySprintId(SnowflakeId sprintId) {
    Sprint sprint =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + sprintId));
    return taskRepository.findBySprint(sprint);
  }

  /**
   * 指定されたステータスを持つタスクを検索します。
   *
   * @param status 検索対象のステータス
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findByStatus(Status status) {
    return taskRepository.findByStatus(status);
  }

  /**
   * 指定された担当者IDを持つタスクを検索します。
   *
   * @param assignee 検索対象の担当者ID
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findByAssignee(Long assignee) {
    return taskRepository.findByAssignee(assignee);
  }

  /**
   * 指定されたユーザーストーリーIDとステータスを持つタスクを検索します。
   *
   * @param storyId 検索対象のユーザーストーリーID
   * @param status 検索対象のステータス
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findByUserStoryIdAndStatus(SnowflakeId storyId, Status status) {
    return taskRepository.findByUserStoryStoryIdAndStatus(storyId, status);
  }

  /**
   * 指定されたスプリントIDとステータスを持つタスクを検索します。
   *
   * @param sprintId 検索対象のスプリントID
   * @param status 検索対象のステータス
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findBySprintIdAndStatus(SnowflakeId sprintId, Status status) {
    return taskRepository.findBySprintSprintIdAndStatus(sprintId, status);
  }

  /**
   * 指定されたユーザーストーリーIDと担当者IDを持つタスクを検索します。
   *
   * @param storyId 検索対象のユーザーストーリーID
   * @param assignee 検索対象の担当者ID
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findByUserStoryIdAndAssignee(SnowflakeId storyId, Long assignee) {
    return taskRepository.findByUserStoryStoryIdAndAssignee(storyId, assignee);
  }

  /**
   * 指定されたスプリントIDと担当者IDを持つタスクを検索します。
   *
   * @param sprintId 検索対象のスプリントID
   * @param assignee 検索対象の担当者ID
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findBySprintIdAndAssignee(SnowflakeId sprintId, Long assignee) {
    return taskRepository.findBySprintSprintIdAndAssignee(sprintId, assignee);
  }

  /**
   * 指定されたステータスと担当者IDを持つタスクを検索します。
   *
   * @param status 検索対象のステータス
   * @param assignee 検索対象の担当者ID
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findByStatusAndAssignee(Status status, Long assignee) {
    return taskRepository.findByStatusAndAssignee(status, assignee);
  }

  /**
   * すべてのタスクを取得します。
   *
   * @return タスクのリスト
   */
  @Transactional(readOnly = true)
  public List<Task> findAll() {
    return taskRepository.findAll();
  }

  /**
   * 指定されたタスクに関連するすべてのコメントを取得します。
   *
   * @param taskId タスクID
   * @return コメントのリスト
   */
  @Transactional(readOnly = true)
  public List<TaskComment> findCommentsByTaskId(SnowflakeId taskId) {
    return taskCommentRepository.findByTaskTaskIdOrderByCreatedAtDesc(taskId);
  }

  /**
   * 指定されたタスクに関連するすべての履歴を取得します。
   *
   * @param taskId タスクID
   * @return 履歴のリスト
   */
  @Transactional(readOnly = true)
  public List<TaskHistory> findHistoriesByTaskId(SnowflakeId taskId) {
    return taskHistoryRepository.findByTaskTaskIdOrderByChangedAtDesc(taskId);
  }

  /**
   * 指定されたタスクに関連するすべての添付ファイルを取得します。
   *
   * @param taskId タスクID
   * @return 添付ファイルのリスト
   */
  @Transactional(readOnly = true)
  public List<TaskAttachment> findAttachmentsByTaskId(SnowflakeId taskId) {
    return taskAttachmentRepository.findByTaskTaskIdOrderByUploadedAtDesc(taskId);
  }

  /**
   * 指定されたコメントIDに対応するコメントを取得します。
   *
   * @param commentId コメントID
   * @return コメントのOptional
   */
  @Transactional(readOnly = true)
  public Optional<TaskComment> findCommentById(SnowflakeId commentId) {
    return taskCommentRepository.findById(commentId);
  }

  /**
   * 指定された添付ファイルIDに対応する添付ファイルを取得します。
   *
   * @param attachmentId 添付ファイルID
   * @return 添付ファイルのOptional
   */
  @Transactional(readOnly = true)
  public Optional<TaskAttachment> findAttachmentById(SnowflakeId attachmentId) {
    return taskAttachmentRepository.findById(attachmentId);
  }
}
