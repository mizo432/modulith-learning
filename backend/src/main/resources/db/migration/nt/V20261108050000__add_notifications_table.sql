-- NY通知: notificationsテーブル作成

CREATE TABLE notifications (
    id BIGINT NOT NULL,
    recipient_id UUID NOT NULL,
    source_id UUID,
    source_domain VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    type VARCHAR(30) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX idx_notifications_recipient ON notifications(recipient_id);
CREATE INDEX idx_notifications_recipient_isread ON notifications(recipient_id, is_read);
CREATE INDEX idx_notifications_recipient_deleted ON notifications(recipient_id, deleted_at);
CREATE INDEX idx_notifications_source ON notifications(source_id);
CREATE INDEX idx_notifications_source_domain ON notifications(source_domain);
