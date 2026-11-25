-- バックログアイテム間の依存関係テーブルを作成します。
CREATE TABLE undecided_ag.backlog_item_dependencies (
    id BIGINT PRIMARY KEY,
    backlog_item_id BIGINT NOT NULL,
    dependency_item_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    version_col BIGINT,
    CONSTRAINT uq_backlog_item_dependencies_pair UNIQUE (backlog_item_id, dependency_item_id)
);

COMMENT ON TABLE undecided_ag.backlog_item_dependencies IS 'バックログアイテム間の依存関係';
COMMENT ON COLUMN undecided_ag.backlog_item_dependencies.backlog_item_id IS '依存元のバックログアイテムID';
COMMENT ON COLUMN undecided_ag.backlog_item_dependencies.dependency_item_id IS '依存先のバックログアイテムID';
COMMENT ON COLUMN undecided_ag.backlog_item_dependencies.created_by IS '依存関係を定義した従業員ID';
