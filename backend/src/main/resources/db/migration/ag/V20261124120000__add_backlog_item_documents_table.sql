-- バックログアイテムと帳票の関連付けテーブルを作成します。
CREATE TABLE undecided_ag.backlog_item_documents (
    id BIGINT PRIMARY KEY,
    backlog_item_id BIGINT NOT NULL,
    document_id BIGINT NOT NULL,
    series_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    version_col BIGINT,
    CONSTRAINT uq_backlog_item_documents_item_series UNIQUE (backlog_item_id, series_id)
);

COMMENT ON TABLE undecided_ag.backlog_item_documents IS 'バックログアイテムと帳票の関連付け';
COMMENT ON COLUMN undecided_ag.backlog_item_documents.document_id IS '帳票ID（系列の最新バージョン）';
COMMENT ON COLUMN undecided_ag.backlog_item_documents.series_id IS 'バージョン系列ID';
