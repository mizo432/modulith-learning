-- JBジョブ管理: audit_logテーブル作成

CREATE TABLE audit_log (
    id BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id VARCHAR(50) NOT NULL,
    result VARCHAR(20) NOT NULL,
    message VARCHAR(500),
    user_id VARCHAR(50) NOT NULL,
    performed_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    PRIMARY KEY (id)
);

CREATE INDEX idx_audit_log_action_type ON audit_log(action_type);
CREATE INDEX idx_audit_log_user_id ON audit_log(user_id);
CREATE INDEX idx_audit_log_performed_at ON audit_log(performed_at);
