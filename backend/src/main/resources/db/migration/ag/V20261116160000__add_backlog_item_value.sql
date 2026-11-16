-- BacklogItem に value カラムを追加し、範囲制約を適用します。
ALTER TABLE undecided_ag.backlog_items ADD COLUMN value INTEGER;
ALTER TABLE undecided_ag.backlog_items
    ADD CONSTRAINT ck_backlog_items_value CHECK (value IS NULL OR (value >= 0 AND value <= 100));
COMMENT ON COLUMN undecided_ag.backlog_items.value IS 'Value（0〜100）';
