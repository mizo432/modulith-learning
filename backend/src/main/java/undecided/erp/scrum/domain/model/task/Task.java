package undecided.erp.scrum.domain.model.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.sprint.Sprint;
import undecided.erp.scrum.domain.model.team.Member;

/**
 * Taskクラスは、エンティティとしてスクラム開発におけるタスクを表現します。
 * <p>
 * このクラスは以下の特性を持ちます:
 * <ul>
 *   <li>`taskId`: タスク固有の識別子を表す。SnowflakeIdを使用。</li>
 *   <li>`sprint`: 関連するスプリント。</li>
 *   <li>`assignee`: タスクの担当者。</li>
 *   <li>`title`: タスクのタイトル。</li>
 *   <li>`description`: タスクの詳細説明。</li>
 *   <li>`estimatedHours`: 見積もり時間。</li>
 *   <li>`remainingHours`: 残時間。</li>
 *   <li>`status`: ステータス（未着手、進行中、完了など）。</li>
 *   <li>`priority`: 優先度（高、中、低など）。</li>
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "tasks")
@NoArgsConstructor
public class Task extends PptEntity<Task> implements Serializable {

  /**
   * ユニークなタスク識別子を表す変数。
   * <p>
   * アプリケーションにおける各タスクを一意に識別するために使用されます。 データベース上の "task_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "task_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId taskId;

  /**
   * このタスクが関連するスプリントを表す変数。
   * <p>
   * タスクが属するスプリントへの参照です。 データベース上の "sprint_id" カラムに対応します。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "sprint_id")
  private Sprint sprint;

  /**
   * このタスクの担当者を表す変数。
   * <p>
   * タスクを担当するチームメンバーへの参照です。 データベース上の "assignee_id" カラムに対応します。
   */
  @Getter
  @ManyToOne
  @JoinColumn(name = "assignee_id")
  private Member assignee;

  /**
   * タスクのタイトルを表す変数。
   * <p>
   * タスクの識別に使用される短い説明です。 データベース上の "title" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "title", nullable = false, length = 200)
  private String title;

  /**
   * タスクの詳細説明を表す変数。
   * <p>
   * タスクの詳細な説明や要件を提供します。 データベース上の "description" カラムに対応します。
   */
  @Getter
  @Column(name = "description", length = 2000)
  private String description;

  /**
   * 見積もり時間を表す変数。
   * <p>
   * タスクの完了に必要な見積もり時間です。 データベース上の "estimated_hours" カラムに対応します。
   */
  @Getter
  @Column(name = "estimated_hours")
  private Float estimatedHours;

  /**
   * 残時間を表す変数。
   * <p>
   * タスクの完了までに残っている時間です。 データベース上の "remaining_hours" カラムに対応します。
   */
  @Getter
  @Column(name = "remaining_hours")
  private Float remainingHours;

  /**
   * ステータスを表す変数。
   * <p>
   * タスクの現在の状態（未着手、進行中、完了など）を示します。 データベース上の "status" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "status", nullable = false, length = 20)
  private String status;

  /**
   * 優先度を表す変数。
   * <p>
   * タスクの優先度（高、中、低など）を示します。 データベース上の "priority" カラムに対応します。
   */
  @Getter
  @Column(name = "priority", length = 10)
  private String priority;

  /**
   * このメソッドはTaskクラスの文字列表現を生成します。
   * <p>
   * 各フィールドの値を連結して構成された文字列を返します。
   *
   * @return Taskオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "Task{" +
        "taskId=" + taskId +
        ", sprint=" + sprint +
        ", assignee=" + assignee +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", estimatedHours=" + estimatedHours +
        ", remainingHours=" + remainingHours +
        ", status='" + status + '\'' +
        ", priority='" + priority + '\'' +
        '}';
  }
}
