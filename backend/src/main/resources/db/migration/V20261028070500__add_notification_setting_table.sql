-- JBジョブ管理: notification_settingテーブル作成

CREATE TABLE notification_setting (
    id BIGINT NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    target_id VARCHAR(50) NOT NULL,
    channel_type VARCHAR(20) NOT NULL,
    channel_config TEXT,
    notify_on_success BOOLEAN NOT NULL,
    notify_on_failure BOOLEAN NOT NULL,
    notify_on_unknown BOOLEAN NOT NULL,
    subject_template VARCHAR(200),
    body_template TEXT,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_notification_setting_target ON notification_setting(target_type, target_id);
CREATE INDEX idx_notification_setting_enabled ON notification_setting(enabled);
