CREATE SCHEMA IF NOT EXISTS undecided_nt;

CREATE TABLE undecided_nt.notifications (
    id BIGINT NOT NULL,
    recipient_id BIGINT NOT NULL,
    backlog_item_id BIGINT NOT NULL,
    comment_id BIGINT,
    title VARCHAR(200) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    notification_type VARCHAR(30) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);
