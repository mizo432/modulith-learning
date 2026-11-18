-- REJECTED を FROZEN に置換します。
-- 既存の CHECK 制約を再定義し、既存データを移行します。

-- 既存の CHECK 制約を削除
ALTER TABLE undecided_ag.backlog_items DROP CONSTRAINT ck_backlog_items_status;

-- 既存の REJECTED 状態を FROZEN に置換
UPDATE undecided_ag.backlog_items SET status = 'FROZEN' WHERE status = 'REJECTED';

-- 新規 CHECK 制約を追加（SUGGESTED, ACCEPTED, FROZEN のみ許可）
ALTER TABLE undecided_ag.backlog_items
    ADD CONSTRAINT ck_backlog_items_status CHECK (status IN ('SUGGESTED', 'ACCEPTED', 'FROZEN'));

-- COMMENT を更新
COMMENT ON COLUMN undecided_ag.backlog_items.status IS '状態（SUGGESTED／ACCEPTED／FROZEN）';
