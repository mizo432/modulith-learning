-- 大量レコード処理バッチサンプル用テーブルを作成します。
-- このマイグレーションは、大量データ処理パターン（チャンク処理、エラー処理、フォールトトレランス）のデモ用です。

CREATE TABLE undecided_ef.large_records
(
    id           BIGINT       NOT NULL,
    record_code  VARCHAR(50)  NOT NULL,
    record_name  VARCHAR(200) NOT NULL,
    category     VARCHAR(50),
    sub_category VARCHAR(50),
    amount       BIGINT,
    quantity     INTEGER,
    status       VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE INDEX idx_large_records_category ON undecided_ef.large_records (category);
CREATE INDEX idx_large_records_status ON undecided_ef.large_records (status);

COMMENT ON TABLE undecided_ef.large_records IS '大量レコード処理バッチサンプル用テーブル';
COMMENT ON COLUMN undecided_ef.large_records.id IS '一意の識別子';
COMMENT ON COLUMN undecided_ef.large_records.record_code IS 'レコードコード';
COMMENT ON COLUMN undecided_ef.large_records.record_name IS 'レコード名';
COMMENT ON COLUMN undecided_ef.large_records.category IS 'カテゴリ';
COMMENT ON COLUMN undecided_ef.large_records.sub_category IS 'サブカテゴリ';
COMMENT ON COLUMN undecided_ef.large_records.amount IS '金額';
COMMENT ON COLUMN undecided_ef.large_records.quantity IS '数量';
COMMENT ON COLUMN undecided_ef.large_records.status IS 'ステータス（ACTIVE/INACTIVE）';
COMMENT ON COLUMN undecided_ef.large_records.created_at IS '作成日時';
