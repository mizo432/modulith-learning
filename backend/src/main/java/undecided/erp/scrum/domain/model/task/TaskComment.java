package undecided.erp.scrum.domain.model.task;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
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
 * TaskCommentクラスは、エンティティとしてタスクに対するコメントを表現します。
 *
 * <p>このクラスは以下の特性を持ちます:
 *
 * <ul>
 *   <li>`commentId`: コメント固有の識別子を表す。SnowflakeIdを使用。
 *   <li>`task`: このコメントが属するタスク。
 *   <li>`authorId`: コメントの作成者ID。
 *   <li>`content`: コメントの内容。
 *   <li>`createdAt`: コメントの作成日時。
 *   <li>`updatedAt`: コメントの更新日時。
 * </ul>
 */
@AllArgsConstructor
@Entity
@Table(name = "task_comments")
@NoArgsConstructor
public class TaskComment extends PptEntity<TaskComment> implements Serializable {

  /**
   * ユニークなコメント識別子を表す変数。
   *
   * <p>アプリケーションにおける各コメントを一意に識別するために使用されます。 データベース上の "comment_id" カラムに対応し、null 値は許可されていません。
   */
  @Id
  @Column(name = "comment_id", columnDefinition = "BIGINT", nullable = false)
  @Convert(converter = SnowflakeId.SnowflakeIdConverter.class)
  private SnowflakeId commentId;

  /**
   * このコメントが属するタスクを表す変数。
   *
   * <p>タスクとコメントの関連付けを表します。 データベース上の "task_id" カラムに対応します。
   */
  @ManyToOne
  @JoinColumn(name = "task_id", nullable = false)
  @Getter
  private Task task;

  /**
   * コメントの作成者IDを表す変数。
   *
   * <p>コメントを作成したユーザーのIDを示します。 データベース上の "author_id" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "author_id", nullable = false)
  private Long authorId;

  /**
   * コメントの内容を表す変数。
   *
   * <p>コメントの実際のテキスト内容を示します。 データベース上の "content" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "content", nullable = false, length = 2000)
  private String content;

  /**
   * コメントの作成日時を表す変数。
   *
   * <p>コメントが作成された日時を示します。 データベース上の "created_at" カラムに対応し、null 値は許可されていません。
   */
  @Getter
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  /**
   * コメントの更新日時を表す変数。
   *
   * <p>コメントが最後に更新された日時を示します。 データベース上の "updated_at" カラムに対応します。
   */
  @Getter
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /**
   * コメントIDを設定します。
   *
   * @param commentId 設定するコメントID
   */
  public void setCommentId(SnowflakeId commentId) {
    this.commentId = commentId;
  }

  /**
   * このコメントが属するタスクを設定します。
   *
   * @param task 設定するタスク
   */
  public void setTask(Task task) {
    this.task = task;
  }

  /**
   * コメントの作成者IDを設定します。
   *
   * @param authorId 設定する作成者ID
   */
  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  /**
   * コメントの内容を設定します。
   *
   * @param content 設定するコメント内容
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * コメントの作成日時を設定します。
   *
   * @param createdAt 設定する作成日時
   */
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  /**
   * コメントの更新日時を設定します。
   *
   * @param updatedAt 設定する更新日時
   */
  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   * このメソッドはTaskCommentクラスの文字列表現を生成します。
   *
   * <p>各フィールドの値を連結して構成された文字列を返します。
   *
   * @return TaskCommentオブジェクトのフィールド情報を含む文字列表現
   */
  @Override
  public String toString() {
    return "TaskComment{"
        + "commentId="
        + commentId
        + ", task="
        + (task != null ? task.toString() : "null")
        + ", authorId="
        + authorId
        + ", content='"
        + content
        + '\''
        + ", createdAt="
        + createdAt
        + ", updatedAt="
        + updatedAt
        + '}';
  }
}
