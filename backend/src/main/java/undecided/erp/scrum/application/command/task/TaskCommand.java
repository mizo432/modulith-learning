package undecided.erp.scrum.application.command.task;

import java.time.LocalDateTime;
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
import undecided.erp.scrum.domain.model.task.TaskHistory.ChangeType;
import undecided.erp.scrum.domain.model.task.TaskHistoryRepository;
import undecided.erp.scrum.domain.model.task.TaskRepository;
import undecided.erp.scrum.domain.model.task.UserStory;
import undecided.erp.scrum.domain.model.task.UserStoryRepository;

/**
 * タスクに関連するコマンド操作を提供するサービスクラス。
 *
 * <p>このクラスはタスクの作成、更新、削除、ステータス変更、担当者変更などの操作を提供します。 また、タスクコメント、履歴、添付ファイルの管理も行います。
 */
@Service
@RequiredArgsConstructor
public class TaskCommand {

  private final TaskRepository taskRepository;
  private final UserStoryRepository userStoryRepository;
  private final SprintRepository sprintRepository;
  private final TaskCommentRepository taskCommentRepository;
  private final TaskHistoryRepository taskHistoryRepository;
  private final TaskAttachmentRepository taskAttachmentRepository;

  /**
   * 新しいタスクを作成します。
   *
   * @param storyId ユーザーストーリーID
   * @param sprintId スプリントID（オプション）
   * @param title タイトル
   * @param description 説明
   * @param assignee 担当者ID（オプション）
   * @param estimatedHours 見積もり時間（オプション）
   * @param dueDate 期限日（オプション）
   * @param userId 操作を行うユーザーID
   * @return 作成されたタスク
   */
  @Transactional
  public Task createTask(
      SnowflakeId storyId,
      SnowflakeId sprintId,
      String title,
      String description,
      Long assignee,
      Float estimatedHours,
      LocalDateTime dueDate,
      Long userId) {

    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("User story not found: " + storyId));

    Sprint sprint = null;
    if (sprintId != null) {
      sprint =
          sprintRepository
              .findById(sprintId)
              .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + sprintId));
    }

    Task task = new Task();
    task.setTaskId(SnowflakeId.newInstance());
    task.setUserStory(userStory);
    task.setSprint(sprint);
    task.setTitle(title);
    task.setDescription(description);
    task.setAssignee(assignee);
    task.setEstimatedHours(estimatedHours);
    task.setRemainingHours(estimatedHours); // 初期値として見積もり時間を設定
    task.setStatus(Status.TODO);
    task.setDueDate(dueDate);

    Task savedTask = taskRepository.save(task);

    // 履歴を記録
    TaskHistory history = new TaskHistory();
    history.setHistoryId(SnowflakeId.newInstance());
    history.setTask(savedTask);
    history.setChangeType(ChangeType.CREATED);
    history.setChangedBy(userId);
    history.setChangedAt(LocalDateTime.now());
    taskHistoryRepository.save(history);

    return savedTask;
  }

  /**
   * 既存のタスクを更新します。
   *
   * @param taskId タスクID
   * @param title タイトル
   * @param description 説明
   * @param estimatedHours 見積もり時間（オプション）
   * @param remainingHours 残り時間（オプション）
   * @param dueDate 期限日（オプション）
   * @param userId 操作を行うユーザーID
   * @return 更新されたタスク
   */
  @Transactional
  public Task updateTask(
      SnowflakeId taskId,
      String title,
      String description,
      Float estimatedHours,
      Float remainingHours,
      LocalDateTime dueDate,
      Long userId) {

    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    // 変更前の値を保存
    String oldTitle = task.getTitle();
    String oldDescription = task.getDescription();
    Float oldEstimatedHours = task.getEstimatedHours();
    Float oldRemainingHours = task.getRemainingHours();
    LocalDateTime oldDueDate = task.getDueDate();

    // 値を更新
    task.setTitle(title);
    task.setDescription(description);
    task.setEstimatedHours(estimatedHours);
    task.setRemainingHours(remainingHours);
    task.setDueDate(dueDate);

    Task savedTask = taskRepository.save(task);

    // 履歴を記録
    if (!oldTitle.equals(title)) {
      recordHistory(savedTask, ChangeType.UPDATED, "title", oldTitle, title, userId);
    }

    if ((oldDescription == null && description != null)
        || (oldDescription != null && !oldDescription.equals(description))) {
      recordHistory(
          savedTask, ChangeType.UPDATED, "description", oldDescription, description, userId);
    }

    if ((oldEstimatedHours == null && estimatedHours != null)
        || (oldEstimatedHours != null && !oldEstimatedHours.equals(estimatedHours))) {
      recordHistory(
          savedTask,
          ChangeType.ESTIMATE_CHANGED,
          "estimatedHours",
          oldEstimatedHours != null ? oldEstimatedHours.toString() : null,
          estimatedHours != null ? estimatedHours.toString() : null,
          userId);
    }

    if ((oldRemainingHours == null && remainingHours != null)
        || (oldRemainingHours != null && !oldRemainingHours.equals(remainingHours))) {
      recordHistory(
          savedTask,
          ChangeType.REMAINING_CHANGED,
          "remainingHours",
          oldRemainingHours != null ? oldRemainingHours.toString() : null,
          remainingHours != null ? remainingHours.toString() : null,
          userId);
    }

    if ((oldDueDate == null && dueDate != null)
        || (oldDueDate != null && !oldDueDate.equals(dueDate))) {
      recordHistory(
          savedTask,
          ChangeType.DUE_DATE_CHANGED,
          "dueDate",
          oldDueDate != null ? oldDueDate.toString() : null,
          dueDate != null ? dueDate.toString() : null,
          userId);
    }

    return savedTask;
  }

  /**
   * 既存のタスクを削除します。
   *
   * @param taskId タスクID
   * @param userId 操作を行うユーザーID
   */
  @Transactional
  public void deleteTask(SnowflakeId taskId, Long userId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    // 履歴を記録
    TaskHistory history = new TaskHistory();
    history.setHistoryId(SnowflakeId.newInstance());
    history.setTask(task);
    history.setChangeType(ChangeType.DELETED);
    history.setChangedBy(userId);
    history.setChangedAt(LocalDateTime.now());
    taskHistoryRepository.save(history);

    taskRepository.delete(task);
  }

  /**
   * タスクのステータスを変更します。
   *
   * @param taskId タスクID
   * @param status 新しいステータス
   * @param userId 操作を行うユーザーID
   * @return 更新されたタスク
   */
  @Transactional
  public Task changeStatus(SnowflakeId taskId, Status status, Long userId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    Status oldStatus = task.getStatus();
    task.setStatus(status);

    Task savedTask = taskRepository.save(task);

    // 履歴を記録
    recordHistory(
        savedTask, ChangeType.STATUS_CHANGED, "status", oldStatus.name(), status.name(), userId);

    return savedTask;
  }

  /**
   * タスクの担当者を変更します。
   *
   * @param taskId タスクID
   * @param assignee 新しい担当者ID
   * @param userId 操作を行うユーザーID
   * @return 更新されたタスク
   */
  @Transactional
  public Task assignTask(SnowflakeId taskId, Long assignee, Long userId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    Long oldAssignee = task.getAssignee();
    task.setAssignee(assignee);

    Task savedTask = taskRepository.save(task);

    // 履歴を記録
    recordHistory(
        savedTask,
        ChangeType.ASSIGNEE_CHANGED,
        "assignee",
        oldAssignee != null ? oldAssignee.toString() : null,
        assignee != null ? assignee.toString() : null,
        userId);

    return savedTask;
  }

  /**
   * タスクのスプリントを変更します。
   *
   * @param taskId タスクID
   * @param sprintId 新しいスプリントID
   * @param userId 操作を行うユーザーID
   * @return 更新されたタスク
   */
  @Transactional
  public Task changeTaskSprint(SnowflakeId taskId, SnowflakeId sprintId, Long userId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    Sprint oldSprint = task.getSprint();
    Sprint newSprint = null;

    if (sprintId != null) {
      newSprint =
          sprintRepository
              .findById(sprintId)
              .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + sprintId));
    }

    task.setSprint(newSprint);

    Task savedTask = taskRepository.save(task);

    // 履歴を記録
    recordHistory(
        savedTask,
        ChangeType.SPRINT_CHANGED,
        "sprint",
        oldSprint != null ? oldSprint.getSprintId().toString() : null,
        newSprint != null ? newSprint.getSprintId().toString() : null,
        userId);

    return savedTask;
  }

  /**
   * タスクにコメントを追加します。
   *
   * @param taskId タスクID
   * @param content コメント内容
   * @param authorId コメント作成者ID
   * @return 作成されたコメント
   */
  @Transactional
  public TaskComment addComment(SnowflakeId taskId, String content, Long authorId) {
    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    TaskComment comment = new TaskComment();
    comment.setCommentId(SnowflakeId.newInstance());
    comment.setTask(task);
    comment.setAuthorId(authorId);
    comment.setContent(content);
    comment.setCreatedAt(LocalDateTime.now());

    return taskCommentRepository.save(comment);
  }

  /**
   * タスクのコメントを更新します。
   *
   * @param commentId コメントID
   * @param content 新しいコメント内容
   * @param authorId コメント更新者ID
   * @return 更新されたコメント
   */
  @Transactional
  public TaskComment updateComment(SnowflakeId commentId, String content, Long authorId) {
    TaskComment comment =
        taskCommentRepository
            .findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

    // コメント作成者のみが更新可能
    if (!comment.getAuthorId().equals(authorId)) {
      throw new IllegalArgumentException("Only the author can update the comment");
    }

    comment.setContent(content);
    comment.setUpdatedAt(LocalDateTime.now());

    return taskCommentRepository.save(comment);
  }

  /**
   * タスクのコメントを削除します。
   *
   * @param commentId コメントID
   * @param authorId コメント削除者ID
   */
  @Transactional
  public void deleteComment(SnowflakeId commentId, Long authorId) {
    TaskComment comment =
        taskCommentRepository
            .findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found: " + commentId));

    // コメント作成者のみが削除可能
    if (!comment.getAuthorId().equals(authorId)) {
      throw new IllegalArgumentException("Only the author can delete the comment");
    }

    taskCommentRepository.delete(comment);
  }

  /**
   * タスクにファイルを添付します。
   *
   * @param taskId タスクID
   * @param fileName ファイル名
   * @param fileType ファイルタイプ
   * @param fileSize ファイルサイズ
   * @param filePath ファイルパス
   * @param description ファイルの説明
   * @param uploadedBy アップロードユーザーID
   * @return 作成された添付ファイル
   */
  @Transactional
  public TaskAttachment addAttachment(
      SnowflakeId taskId,
      String fileName,
      String fileType,
      Long fileSize,
      String filePath,
      String description,
      Long uploadedBy) {

    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

    TaskAttachment attachment = new TaskAttachment();
    attachment.setAttachmentId(SnowflakeId.newInstance());
    attachment.setTask(task);
    attachment.setFileName(fileName);
    attachment.setFileType(fileType);
    attachment.setFileSize(fileSize);
    attachment.setFilePath(filePath);
    attachment.setDescription(description);
    attachment.setUploadedBy(uploadedBy);
    attachment.setUploadedAt(LocalDateTime.now());

    return taskAttachmentRepository.save(attachment);
  }

  /**
   * タスクの添付ファイルを削除します。
   *
   * @param attachmentId 添付ファイルID
   * @param userId 操作を行うユーザーID
   */
  @Transactional
  public void deleteAttachment(SnowflakeId attachmentId, Long userId) {
    TaskAttachment attachment =
        taskAttachmentRepository
            .findById(attachmentId)
            .orElseThrow(
                () -> new IllegalArgumentException("Attachment not found: " + attachmentId));

    // アップロードユーザーのみが削除可能
    if (!attachment.getUploadedBy().equals(userId)) {
      throw new IllegalArgumentException("Only the uploader can delete the attachment");
    }

    taskAttachmentRepository.delete(attachment);
  }

  /**
   * タスクの履歴を記録します。
   *
   * @param task タスク
   * @param changeType 変更タイプ
   * @param fieldName フィールド名
   * @param oldValue 古い値
   * @param newValue 新しい値
   * @param userId ユーザーID
   * @return 作成された履歴
   */
  private TaskHistory recordHistory(
      Task task,
      ChangeType changeType,
      String fieldName,
      String oldValue,
      String newValue,
      Long userId) {
    TaskHistory history = new TaskHistory();
    history.setHistoryId(SnowflakeId.newInstance());
    history.setTask(task);
    history.setChangeType(changeType);
    history.setFieldName(fieldName);
    history.setOldValue(oldValue);
    history.setNewValue(newValue);
    history.setChangedBy(userId);
    history.setChangedAt(LocalDateTime.now());
    return taskHistoryRepository.save(history);
  }
}
