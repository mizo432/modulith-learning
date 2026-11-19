package undecided.notification.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知エンティティ（NTスキーマ）。
 *
 * <p>コメントの追加・削除に対する通知を管理します。
 * 未読/既読の追跡を行い、アプリ内スナックバーで表示するために使用されます。
 */
@Entity
@Table(schema = "undecided_nt", name = "notifications")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationEntity {

  @Id
  @Column(nullable = false)
  protected SnowflakeId id;

  @Column(name = "recipient_id", nullable = false)
  protected SnowflakeId recipientId;

  @Column(name = "backlog_item_id", nullable = false)
  protected SnowflakeId backlogItemId;

  @Column(name = "comment_id")
  protected SnowflakeId commentId;

  @Column(nullable = false, length = 200)
  protected String title;

  @Column(nullable = false, length = 2000)
  protected String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "notification_type", nullable = false, length = 30)
  protected NotificationType type;

  @Column(name = "is_read", nullable = false)
  @Builder.Default
  protected Boolean isRead = false;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  @jakarta.persistence.Version
  @Column(name = "version", nullable = false)
  @Builder.Default
  protected Long version = 0L;

  /**
   * 通知を既読としてマークします。
   */
  public void markAsRead() {
    this.isRead = true;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 通知が未読かどうかを返します。
   */
  public boolean isUnread() {
    return !this.isRead;
  }
}
