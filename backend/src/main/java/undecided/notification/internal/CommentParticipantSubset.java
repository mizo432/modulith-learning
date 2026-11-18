package undecided.notification.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * コメント参加者のサブセット（NTスキーマ）。
 *
 * <p>requirementBacklog領域のコメント参加者情報を、イベント連携により保持する。
 * 各バックログアイテムに対して、重複排除された参加者IDを保持する。
 */
@Entity
@Table(schema = "undecided_nt", name = "comment_participants")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentParticipantSubset {

  @Id
  @Column(nullable = false)
  protected SnowflakeId id;

  @Column(name = "backlog_item_id", nullable = false)
  protected SnowflakeId backlogItemId;

  @Column(name = "employee_id", nullable = false)
  protected SnowflakeId employeeId;

  @Column(name = "employee_name", nullable = false, length = 100)
  protected String employeeName;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();
}
