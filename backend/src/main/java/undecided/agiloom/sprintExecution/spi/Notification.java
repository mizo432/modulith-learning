package undecided.agiloom.sprintExecution.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知エンティティ。
 *
 * <p>ユーザーへの通知を管理します。Sprintの進捗状況やタスクの割り当てなど、
 * チームメンバーへの重要な情報を通知するために使用されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられ、{@code create()}ファクトリメソッドを使用して
 * インスタンスを作成します。{@code @Builder}は既存のコードとの互換性のために維持されています。
 */
@Entity
@Table(name = "notifications")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {

  /** 通知の一意な識別子。 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected SnowflakeId id;

  /** 通知を受信するユーザーのID。 */
  @Column(name = "recipient_id", nullable = false)
  protected UUID recipientId;

  /** 通知に関連する製品のID。 */
  @Column(name = "product_id", nullable = false)
  protected UUID productId;

  /** 通知のタイトル。 */
  @Column(nullable = false, length = 200)
  protected String title;

  /** 通知の詳細な内容。 */
  @Column(nullable = false, length = 2000)
  protected String content;

  /** 通知の種類（TASK_ASSIGNED, SPRINT_STARTED, DEADLINE_APPROACHINGなど）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  protected NotificationType type;

  /** 既読フラグ。trueの場合は通知が読み取られたことを示します。 */
  @Column(name = "is_read", nullable = false)
  @Builder.Default
  protected Boolean isRead = false;

  /** 通知が作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** 通知が最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** 通知が論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

    /**
     * 通知を新規作成します。
     *
     * @param id ID
     * @param recipientId 受信者ID
     * @param productId 製品ID
     * @param title タイトル
     * @param content 内容
     * @param type 通知種別
     * @param isRead 既読フラグ
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param deletedAt 削除日時
     * @return 新規作成されたNotificationインスタンス
     */
    public static Notification create(
            @NonNull SnowflakeId id,
            @NonNull UUID recipientId,
            @NonNull UUID productId,
            @NonNull String title,
            @NonNull String content,
            @NonNull NotificationType type,
            Boolean isRead,
            @NonNull LocalDateTime createdAt,
            @NonNull LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        return Notification.builder()
                .id(id)
                .recipientId(recipientId)
                .productId(productId)
                .title(title)
                .content(content)
                .type(type)
                .isRead(isRead)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deletedAt(deletedAt)
                .build();
    }

    public void markAsRead() {
        this.isRead = true;
        this.updatedAt = LocalDateTime.now();
    }
}
