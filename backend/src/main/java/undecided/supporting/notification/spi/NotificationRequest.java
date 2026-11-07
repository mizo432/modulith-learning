package undecided.supporting.notification.spi;

import java.util.UUID;

/**
 * 通知送信の要求を表す値オブジェクトです。
 * <p>
 * 各事業領域から通知を送信する際に、このビルダーを使用して
 * 通知の内容を構築します。
 * </p>
 *
 * @see NotificationService
 */
public final class NotificationRequest {

    private final UUID recipientId;
    private final UUID sourceId;
    private final String sourceDomain;
    private final String title;
    private final String content;
    private final NotificationType type;

    private NotificationRequest(Builder builder) {
        this.recipientId = builder.recipientId;
        this.sourceId = builder.sourceId;
        this.sourceDomain = builder.sourceDomain;
        this.title = builder.title;
        this.content = builder.content;
        this.type = builder.type;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public UUID getSourceId() {
        return sourceId;
    }

    public String getSourceDomain() {
        return sourceDomain;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public NotificationType getType() {
        return type;
    }

    /**
     * 通知リクエストのビルダーを返します。
     *
     * @param sourceDomain 送信元の事業領域名（例: "agiloom", "jobmanagement"）
     * @return ビルダーインスタンス
     */
    public static Builder builder(String sourceDomain) {
        return new Builder(sourceDomain);
    }

    /**
     * 通知リクエストを構築するビルダーです。
     */
    public static class Builder {
        private final String sourceDomain;
        private UUID recipientId;
        private UUID sourceId;
        private String title;
        private String content;
        private NotificationType type;

        public Builder(String sourceDomain) {
            this.sourceDomain = sourceDomain;
        }

        public Builder recipientId(UUID recipientId) {
            this.recipientId = recipientId;
            return this;
        }

        public Builder sourceId(UUID sourceId) {
            this.sourceId = sourceId;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder type(NotificationType type) {
            this.type = type;
            return this;
        }

        /**
         * 通知リクエストを構築します。
         *
         * @return 構築された通知リクエスト
         * @throws IllegalStateException 必須フィールドが設定されていない場合
         */
        public NotificationRequest build() {
            if (recipientId == null) {
                throw new IllegalStateException("recipientId is required");
            }
            if (sourceDomain == null || sourceDomain.isEmpty()) {
                throw new IllegalStateException("sourceDomain is required");
            }
            if (title == null || title.isEmpty()) {
                throw new IllegalStateException("title is required");
            }
            if (content == null) {
                throw new IllegalStateException("content is required");
            }
            if (type == null) {
                throw new IllegalStateException("type is required");
            }
            return new NotificationRequest(this);
        }
    }
}
