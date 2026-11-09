package undecided.supporting.notification.spi;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 通知の値オブジェクトです。
 * <p>
 * SPIとして外部に公開される不変の通知データモデルです。
 * JPAエンティティの詳細を外部に漏洩させず、モジュール間連携に使用します。
 * </p>
 *
 * @see NotificationService
 * @see NotificationRequest
 */
@Getter
public class NotificationDTO {
    @NonNull
    private final SnowflakeId id;
    
    @NonNull
    private final UUID recipientId;
    
    private final UUID sourceId;
    
    @NonNull
    private final String sourceDomain;
    
    @NonNull
    private final String title;
    
    @NonNull
    private final String content;
    
    @NonNull
    private final NotificationType type;
    
    private final boolean isRead;
    
    @NonNull
    private final LocalDateTime createdAt;
    
    @NonNull
    private final LocalDateTime updatedAt;

    @Builder
    public NotificationDTO(
        @NonNull SnowflakeId id,
        @NonNull UUID recipientId,
        UUID sourceId,
        @NonNull String sourceDomain,
        @NonNull String title,
        @NonNull String content,
        @NonNull NotificationType type,
        boolean isRead,
        @NonNull LocalDateTime createdAt,
        @NonNull LocalDateTime updatedAt
    ) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(recipientId, "recipientId must not be null");
        Objects.requireNonNull(sourceDomain, "sourceDomain must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(content, "content must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        this.id = id;
        this.recipientId = recipientId;
        this.sourceId = sourceId;
        this.sourceDomain = sourceDomain;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
