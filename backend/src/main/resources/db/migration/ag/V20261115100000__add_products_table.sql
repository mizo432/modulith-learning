-- undecided_agスキーマを作成します。
CREATE SCHEMA IF NOT EXISTS undecided_ag;

-- プロダクト基本情報テーブルを作成します。
CREATE TABLE undecided_ag.products
(
    id          BIGINT       NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    manager_id  UUID,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE'
        CHECK (status IN ('ACTIVE', 'ARCHIVED')),
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  TIMESTAMP
);

CREATE INDEX idx_products_status ON undecided_ag.products (status);

COMMENT ON TABLE undecided_ag.products IS 'プロダクト基本情報テーブル';
COMMENT ON COLUMN undecided_ag.products.id IS 'プロダクトの一意な識別子';
COMMENT ON COLUMN undecided_ag.products.name IS 'プロダクトの名前';
COMMENT ON COLUMN undecided_ag.products.description IS 'プロダクトの説明（Markdown形式）';
COMMENT ON COLUMN undecided_ag.products.manager_id IS 'プロダクトマネージャーのID';
COMMENT ON COLUMN undecided_ag.products.status IS 'ステータス（ACTIVE/ARCHIVED）';
COMMENT ON COLUMN undecided_ag.products.created_at IS '作成日時';
COMMENT ON COLUMN undecided_ag.products.updated_at IS '更新日時';
COMMENT ON COLUMN undecided_ag.products.deleted_at IS '論理削除日時（null=未削除）';
