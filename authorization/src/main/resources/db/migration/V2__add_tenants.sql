-- テナントテーブルを作成
CREATE TABLE tenants
(
    id          BIGSERIAL PRIMARY KEY,
    tenant_code VARCHAR(255) NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    enabled     BOOLEAN      NOT NULL DEFAULT true,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP
);

-- デフォルトテナントを挿入
INSERT INTO tenants (tenant_code, name, enabled, created_at)
VALUES ('default', 'Default Tenant', true, CURRENT_TIMESTAMP);

-- 既存ユーザーのtenant_idをデフォルトテナントに更新
UPDATE users
SET tenant_id = (SELECT id FROM tenants WHERE tenant_code = 'default')
WHERE tenant_id IS NULL;

-- tenant_idにNOT NULL制約を適用
ALTER TABLE users
    ALTER COLUMN tenant_id SET NOT NULL;

-- 外部キー制約を適用
ALTER TABLE users
    ADD CONSTRAINT fk_users_tenant
        FOREIGN KEY (tenant_id) REFERENCES tenants (id);
