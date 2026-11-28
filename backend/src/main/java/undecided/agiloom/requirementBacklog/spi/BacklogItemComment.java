package undecided.agiloom.requirementBacklog.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムへのコメントエンティティ。
 *
 * <p>チームメンバーとステークホルダー（チーム外の従業員。Issue #570 の決定）が
 * バックログアイテムにコメントできます。
 * インスタンスは {@code @Builder} を使用して作成します。
 */
@Entity
@Table(schema = "undecided_ag", name = "backlog_item_comments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BacklogItemComment {

  /** 投稿者名の最大長。項目制約の単一の情報源（ADR-002）。 */
  public static final int AUTHOR_NAME_MAX_LENGTH = 100;

  /** コメントの一意な識別子。 */
  @Id protected SnowflakeId id;

  /** コメント対象のバックログアイテムへの参照。 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "backlog_item_id", nullable = false)
  protected BacklogItem backlogItem;

  /** 投稿者の従業員ID（チームメンバー、またはステークホルダー＝チーム外の従業員）。 */
  @Column(name = "author_employee_id")
  protected SnowflakeId authorEmployeeId;

  /** 投稿者名。 */
  @Column(name = "author_name", nullable = false, length = AUTHOR_NAME_MAX_LENGTH)
  protected String authorName;

  /** コメント本文。 */
  @Column(nullable = false, columnDefinition = "TEXT")
  protected String body;

  /** 投稿日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  protected LocalDateTime createdAt;

  /** 親コメントへの自己参照（返信がある場合）。ルートコメントの場合はnull。 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comment_id")
  @Nullable
  protected BacklogItemComment parentComment;

  /** 子返信があるかどうか（クエリサービスで計算して設定）。DBカラムではない。 */
  @jakarta.persistence.Transient
  @Builder.Default
  protected boolean hasReplies = false;

  /** コメント本文を更新します。 */
  public void updateBody(String body) {
    this.body = body;
  }

  /** 子返信有無を設定します（クエリサービス用）。 */
  public void setHasReplies(boolean hasReplies) {
    this.hasReplies = hasReplies;
  }
}
