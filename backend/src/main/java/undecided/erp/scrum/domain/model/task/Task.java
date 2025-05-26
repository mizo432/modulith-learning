package undecided.erp.scrum.domain.model.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.sprint.Sprint;

/**
 * Taskクラスは、エンティティとしてスクラム開発におけるタスクを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`taskId`: タスク固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`userStory`: このタスクが属するユーザーストーリー。
 *   <li>`sprint`: このタスクが割り当てられているスプリント。
 *   <li>`title`: タスクのタイトル。
 *   <li>`description`: タスクの詳細説明。
 *   <li>`assignee`: タスクの担当者。
 *   <li>`estimatedHours`: 見積もり時間。
 *   <li>`remainingHours`: 残り時間。
 *   <li>`status`: 状態。
 *   <li>`dueDate`: 期限日。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@NoArgsConstructor
@Setter
public class Task extends PptEntity<Task> implements Serializable {

  /**
   * ユニークなタスク識別子を表す変数。
   *
   * <p>アプリケーションにおける各タスクを一意に識別するために使用されます。 データベース上の "task_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "task_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId taskId;

  /**
   * このタスクが属するユーザーストーリーを表す変数。
   *
   * <p>ユーザーストーリーとタスクの関連付けを表します。 データベース上の "story_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "story_id")
  @Getter
  private UserStory userStory;

  /**
   * このタスクが割り当てられているスプリントを表す変数。
   *
   * <p>スプリントとタスクの関連付けを表します。 データベース上の "sprint_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "sprint_id")
  @Getter
  private Sprint sprint;

  /**
   * タスクのタイトルを表す変数。
   *
   * <p>タスクの識別に使用されるタイトルです。 データベース上の "title" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "title", nullable = false, length = 200)
  private String title;

  /**
   * タスクの詳細説明を表す変数。
   *
   * <p>タスクの詳細な説明を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * タスクの担当者を表す変数。
   *
   * <p>タスクを担当するチームメンバーのIDを示します。 データベース上の "assignee" カラムに対応します。
   */
  @Getter
  @Column(name = "assignee")
  private Long assignee;

  /**
   * 見積もり時間を表す変数。
   *
   * <p>タスクの完了に必要な見積もり時間を示します。 データベース上の "estimated_hours" カラムに対応します。
   */
  @Getter
  @Column(name = "estimated_hours")
  private Float estimatedHours;

  /**
   * 残り時間を表す変数。
   *
   * <p>タスクの完了までの残り時間を示します。 データベース上の "remaining_hours" カラムに対応します。
   */
  @Getter
  @Column(name = "remaining_hours")
  private Float remainingHours;

  /**
   * 状態を表す変数。
   *
   * <p>タスクの現在の状態を示します。 データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private Status status;

  /**
   * 期限日を表す変数。
   *
   * <p>タスクの完了期限を示します。 データベース上の "due_date" カラムに対応します。
   */
  @Getter
  @Column(name = "due_date")
  private LocalDateTime dueDate;

  /**
   * このメソッドはTaskクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Taskオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Task{"
        + "taskId="
        + taskId
        + ", userStory="
        + (userStory != null ? userStory.toString() : "null")
        + ", sprint="
        + (sprint != null ? sprint.toString() : "null")
        + ", title='"
        + title
        + '\''
        + ", description='"
        + description
        + '\''
        + ", assignee="
        + assignee
        + ", estimatedHours="
        + estimatedHours
        + ", remainingHours="
        + remainingHours
        + ", status="
        + status
        + ", dueDate="
        + dueDate
        + '}';
  }

  /** タスクの状態を表す列挙型。 */
  public enum Status {
    /** 未着手の状態。 */
    TODO,

    /** 進行中の状態。 */
    IN_PROGRESS,

    /** レビュー中の状態。 */
    REVIEW,

    /** 完了した状態。 */
    DONE
  }
}
