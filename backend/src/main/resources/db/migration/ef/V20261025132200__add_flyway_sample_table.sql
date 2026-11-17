-- FlywayとJPAの連携サンプル用テーブルを作成します。
-- このマイグレーションは、FlywayによるDDL管理とJPAエンティティの連携パターンを示します。

CREATE TABLE undecided_ef.sample_flyway_entities
(
    id          BIGINT       NOT NULL,
    item_name   VARCHAR(100) NOT NULL,
    quantity    INTEGER      NOT NULL DEFAULT 0,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX idx_sample_flyway_entities_status ON undecided_ef.sample_flyway_entities (status);

COMMENT ON TABLE undecided_ef.sample_flyway_entities IS 'FlywayとJPAの連携サンプル用テーブル';
COMMENT ON COLUMN undecided_ef.sample_flyway_entities.id IS 'エンティティの一意の識別子';
COMMENT ON COLUMN undecided_ef.sample_flyway_entities.item_name IS 'アイテム名';
COMMENT ON COLUMN undecided_ef.sample_flyway_entities.quantity IS '数量（デフォルト値0）';
COMMENT ON COLUMN undecided_ef.sample_flyway_entities.status IS 'ステータス（ACTIVE/INACTIVE）';
COMMENT ON COLUMN undecided_ef.sample_flyway_entities.created_at IS '作成日時（デフォルト値CURRENT_TIMESTAMP）';
