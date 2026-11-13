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
 * <p>ユーザーへの通知を管理します。
 */
@Entity
@Table(name = "notifications")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected SnowflakeId id;

    @Column(name = "recipient_id", nullable = false)
    protected UUID recipientId;

    @Column(name = "product_id", nullable = false)
    protected UUID productId;

    @Column(nullable = false, length = 200)
    protected String title;

    @Column(nullable = false, length = 2000)
    protected String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
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
