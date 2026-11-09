package undecided.supporting.notification.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知エンティティです。
 * <p>
 * 各事業領域から共通して利用可能なシステム内通知を表します。
 * 通知は受信者（recipientId）に対して送信され、タイトルと本文を持ちます。
 * </p>
 *
 * @see NotificationType
 */
@Entity
@Table(name = "notifications")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private SnowflakeId id;

    /**
     * 通知の受信者ID（従業員ID）
     */
    @Column(name = "recipient_id", nullable = false)
    private UUID recipientId;

    /**
     * 通知の送信元となる事業領域のID（例: プロダクトID、ジョブIDなど）
     */
    @Column(name = "source_id")
    private UUID sourceId;

    /**
     * 通知の送信元となる事業領域名（例: "agiloom", "jobmanagement" など）
     */
    @Column(name = "source_domain", nullable = false, length = 50)
    private String sourceDomain;

    /**
     * 通知のタイトル
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 通知の本文
     */
    @Column(nullable = false, length = 2000)
    private String content;

    /**
     * 通知の種類
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    /**
     * 既読フラグ
     */
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    /**
     * 作成日時
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 削除日時（論理削除用）
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 通知を既読としてマークします。
     */
    public void markAsRead() {
        this.isRead = true;
    }
}
