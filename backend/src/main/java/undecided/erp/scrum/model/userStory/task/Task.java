package undecided.erp.scrum.model.userStory.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import undecided.erp.relMgmt.model.personRole.developer.Developer;
import undecided.erp.scrum.model.userStory.UserStory;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Taskクラスはソフトウェアプロジェクト内のタスクを表します。
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {

  public enum TaskStatus {
    TO_DO,
    IN_PROGRESS,
    IN_REVIEW,
    DONE
  }

  private SnowflakeId<Task> id;
  private SnowflakeId<UserStory> userStoryId;
  private String title;
  private String description;
  private TaskStatus status;
  private Double estimatedHours;
  private Developer assignedDeveloper;

  /**
   * 与えられたパラメータで新しいTaskオブジェクトを作成します。
   *
   * @param userStoryId Taskに関連付けられたUserStoryのID
   * @param title Taskのタイトル
   * @param description Taskの説明
   * @return 新しく作成されたTaskオブジェクト
   */
  public static Task create(SnowflakeId<UserStory> userStoryId, String title, String description
  ) {
    return new Task(SnowflakeId.newInstance(), userStoryId, title, description, TaskStatus.TO_DO,
        null, null);
  }

  /**
   * タスクに開発者を割り当てます。
   *
   * @param developer タスクに割り当てられる開発者
   * @return 割り当てられた開発者を持つ新しいタスクオブジェクト
   */
  public Task assignToTask(Developer developer) {
    return new Task(id, userStoryId, title, description, status,
        estimatedHours, developer);
  }

  /**
   * タスクの時間を見積もり、更新された見積もり時間を持つ新しいTaskオブジェクトを返します。
   *
   * @param estimatedHours タスクの見積もり時間
   * @return 更新された見積もり時間を持つ新しいTaskオブジェクト
   */
  public Task estimateTask(@NonNull Double estimatedHours) {
    if (estimatedHours < 0) {
      throw new IllegalArgumentException("Estimated hours cannot be negative");
    }
    return new Task(id, userStoryId, title, description, status,
        estimatedHours, assignedDeveloper);
  }

  public Task startTask() {
    if (status == TaskStatus.DONE) {
      throw new IllegalStateException("Cannot start a task that is already done");
    }
    if (status == TaskStatus.IN_PROGRESS) {
      throw new IllegalStateException("Cannot start a task that is already in progress");
    }
    if (status == TaskStatus.IN_REVIEW) {
      throw new IllegalStateException("Cannot start a task that is already in a review");
    }
    return new Task(id, userStoryId, title, description, TaskStatus.IN_PROGRESS,
        estimatedHours, assignedDeveloper);
  }

  public Task turnToInReview(Double estimatedHours) {
    if (status == TaskStatus.DONE) {
      throw new IllegalStateException("Cannot turn to inReview a task that is already done");
    }
    if (status == TaskStatus.IN_REVIEW) {
      throw new IllegalStateException("Cannot turn to inReview a task that is already in a review");
    }
    return new Task(id, userStoryId, title, description, TaskStatus.IN_REVIEW,
        estimatedHours, assignedDeveloper);
  }

  public Task finishTask() {
    if (status == TaskStatus.DONE) {
      throw new IllegalStateException("Cannot finish a task that is already done");
    }
    return new Task(id, userStoryId, title, description, TaskStatus.DONE,
        estimatedHours, assignedDeveloper);
  }

}
