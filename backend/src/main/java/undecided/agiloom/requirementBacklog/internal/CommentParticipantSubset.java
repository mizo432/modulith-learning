package undecided.agiloom.requirementBacklog.internal;

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
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * コメント参加者のサブセット（AGスキーマ）。
 *
 * <p>バックログアイテムへのコメント参加者情報を保持し、
 * 通知モジュール（NT）へイベント連携で同期される。
 * モジュール内部でのみ使用するため、パッケージプライベートとします。
 */
@Entity
@Table(schema = "undecided_ag", name = "comment_participants")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class CommentParticipantSubset {

  @Id
  @Column(nullable = false)
  protected SnowflakeId id;

  @Column(name = "backlog_item_id", nullable = false)
  protected SnowflakeId backlogItemId;

  @Column(name = "employee_id", nullable = false)
  protected SnowflakeId employeeId;

  @Column(name = "employee_name", nullable = false, length = 100)
  protected String employeeName;

  @Column(name = "employee_email", nullable = false, length = 200)
  protected String employeeEmail;

  @Column(name = "created_at", nullable = false, updatable = false)
  protected LocalDateTime createdAt;

  /** イベントから新しいサブセットを作成します。 */
  static CommentParticipantSubset of(
      @NonNull SnowflakeId id,
      @NonNull SnowflakeId backlogItemId,
      @NonNull SnowflakeId employeeId,
      @NonNull String employeeName,
      @NonNull String employeeEmail, @NonNull LocalDateTime now) {
    return CommentParticipantSubset.builder()
        .id(id)
        .backlogItemId(backlogItemId)
        .employeeId(employeeId)
        .employeeName(employeeName)
        .employeeEmail(employeeEmail)
        .createdAt(now)
        .build();
  }
}
