package undecided.notification.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;
import undecided.supporting.snowflake.SnowflakeId;

/** コメントイベントで連携された従業員のメールアドレス。 */
@Entity
@Table(schema = "undecided_nt", name = "employee_email_subsets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class NotificationEmployeeEmailSubset {

  @Id
  @Column(nullable = false)
  private SnowflakeId id;

  @Column(nullable = false, length = 200)
  private String email;

  @Column(name = "occurred_at", nullable = false)
  private LocalDateTime occurredAt;

  @Column(name = "comment_id", nullable = false)
  private SnowflakeId commentId;

  @Version
  @Column(nullable = false)
  private Long version;

  static NotificationEmployeeEmailSubset from(BacklogItemCommentAdded event) {
    NotificationEmployeeEmailSubset subset = new NotificationEmployeeEmailSubset();
    subset.id = event.authorEmployeeId();
    subset.email = event.authorEmail() != null ? event.authorEmail() : "";
    subset.occurredAt = event.occurredAt();
    subset.commentId = event.commentId();
    return subset;
  }

  void apply(BacklogItemCommentAdded event) {
    int comparison = event.occurredAt().compareTo(occurredAt);
    if (comparison > 0 || (comparison == 0 && event.commentId().compareTo(commentId) > 0)) {
      email = event.authorEmail() != null ? event.authorEmail() : "";
      occurredAt = event.occurredAt();
      commentId = event.commentId();
    }
  }
}