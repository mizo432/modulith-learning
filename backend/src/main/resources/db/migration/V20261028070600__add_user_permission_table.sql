-- JBジョブ管理: user_permissionテーブル作成

CREATE TABLE user_permission (
    id BIGINT NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    permission_level VARCHAR(20) NOT NULL,
    system_permission VARCHAR(50) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX idx_user_permission_user_id ON user_permission(user_id);
CREATE INDEX idx_user_permission_role ON user_permission(permission_level);
