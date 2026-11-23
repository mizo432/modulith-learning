-- 帳票テーブルにバージョン系列IDカラムを追加します。
ALTER TABLE undecided_ef.documents
    ADD COLUMN series_id BIGINT;

COMMENT ON COLUMN undecided_ef.documents.series_id IS 'バージョン系列ID（同一系列は同一ファイルのバージョン）';
