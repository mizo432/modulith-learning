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
import undecided.erp.common.entity.PptEntity;
import undecided.erp.common.entity.SnowflakeId;

/**
 * TaskHistoryクラスは、エンティティとしてタスクの変更履歴を表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`historyId`: 履歴固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`task`: この履歴が属するタスク。
 *   <li>`changeType`: 変更の種類。
 *   <li>`fieldName`: 変更されたフィールド名。
 *   <li>`oldValue`: 変更前の値。
 *   <li>`newValue`: 変更後の値。
 *   <li>`changedBy`: 変更を行ったユーザーのID。
 *   <li>`changedAt`: 変更が行われた日時。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "task_histories")
@NoArgsConstructor
public class TaskHistory extends PptEntity<TaskHistory> implements Serializable {

  /**
   * ユニークな履歴識別子を表す変数。
   *
   * <p>アプリケーションにおける各履歴を一意に識別するために使用されます。 データベース上の "history_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "history_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId historyId;

  /**
   * この履歴が属するタスクを表す変数。
   *
   * <p>タスクと履歴の関連付けを表します。 データベース上の "task_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "task_id", nullable = false)
  @Getter
  private Task task;

  /**
   * 変更の種類を表す変数。
   *
   * <p>履歴の変更種類を示します。 データベース上の "change_type" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Enumerated(EnumType.STRING)
  @Column(name = "change_type", nullable = false)
  private ChangeType changeType;

  /**
   * 変更されたフィールド名を表す変数。
   *
   * <p>変更が行われたフィールドの名前を示します。 データベース上の "field_name" カラムに対応します。
   */
  @Getter
  @Column(name = "field_name")
  private String fieldName;

  /**
   * 変更前の値を表す変数。
   *
   * <p>変更が行われる前のフィールドの値を示します。 データベース上の "old_value" カラムに対応します。
   */
  @Getter
  @Column(name = "old_value", length = 1000)
  private String oldValue;

  /**
   * 変更後の値を表す変数。
   *
   * <p>変更が行われた後のフィールドの値を示します。 データベース上の "new_value" カラムに対応します。
   */
  @Getter
  @Column(name = "new_value", length = 1000)
  private String newValue;

  /**
   * 変更を行ったユーザーのIDを表す変数。
   *
   * <p>変更を行ったユーザーのIDを示します。 データベース上の "changed_by" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "changed_by", nullable = false)
  private Long changedBy;

  /**
   * 変更が行われた日時を表す変数。
   *
   * <p>変更が行われた日時を示します。 データベース上の "changed_at" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "changed_at", nullable = false)
  private LocalDateTime changedAt;

  /**
   * 履歴IDを設定します。
   *
   * @param historyId 設定する履歴ID
   */
  public void setHistoryId(SnowflakeId historyId) {
    this.historyId = historyId;
  }

  /**
   * この履歴が属するタスクを設定します。
   *
   * @param task 設定するタスク
   */
  public void setTask(Task task) {
    this.task = task;
  }

  /**
   * 変更の種類を設定します。
   *
   * @param changeType 設定する変更種類
   */
  public void setChangeType(ChangeType changeType) {
    this.changeType = changeType;
  }

  /**
   * 変更されたフィールド名を設定します。
   *
   * @param fieldName 設定するフィールド名
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   * 変更前の値を設定します。
   *
   * @param oldValue 設定する変更前の値
   */
  public void setOldValue(String oldValue) {
    this.oldValue = oldValue;
  }

  /**
   * 変更後の値を設定します。
   *
   * @param newValue 設定する変更後の値
   */
  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

  /**
   * 変更を行ったユーザーのIDを設定します。
   *
   * @param changedBy 設定する変更者ID
   */
  public void setChangedBy(Long changedBy) {
    this.changedBy = changedBy;
  }

  /**
   * 変更が行われた日時を設定します。
   *
   * @param changedAt 設定する変更日時
   */
  public void setChangedAt(LocalDateTime changedAt) {
    this.changedAt = changedAt;
  }

  /**
   * このメソッドはTaskHistoryクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return TaskHistoryオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "TaskHistory{"
        + "historyId="
        + historyId
        + ", task="
        + (task != null ? task.toString() : "null")
        + ", changeType="
        + changeType
        + ", fieldName='"
        + fieldName
        + '\''
        + ", oldValue='"
        + oldValue
        + '\''
        + ", newValue='"
        + newValue
        + '\''
        + ", changedBy="
        + changedBy
        + ", changedAt="
        + changedAt
        + '}';
  }

  /** タスク履歴の変更種類を表す列挙型。 */
  public enum ChangeType {
    /** タスクの作成。 */
    CREATED,

    /** タスクの更新。 */
    UPDATED,

    /** タスクの削除。 */
    DELETED,

    /** タスクのステータス変更。 */
    STATUS_CHANGED,

    /** タスクの担当者変更。 */
    ASSIGNEE_CHANGED,

    /** タスクのスプリント変更。 */
    SPRINT_CHANGED,

    /** タスクの見積もり時間変更。 */
    ESTIMATE_CHANGED,

    /** タスクの残り時間変更。 */
    REMAINING_CHANGED,

    /** タスクの期限日変更。 */
    DUE_DATE_CHANGED,

    /** その他の変更。 */
    OTHER
  }
}
