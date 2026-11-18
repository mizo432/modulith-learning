-- READY 状態を CHECK 制約に追加します。
-- 既存データへの影響はありません（既存の SUGGESTED/ACCEPTED/FROZEN はそのまま有効）。

ALTER TABLE undecided_ag.backlog_items DROP CONSTRAINT ck_backlog_items_status;
ALTER TABLE undecided_ag.backlog_items
    ADD CONSTRAINT ck_backlog_items_status CHECK (status IN ('SUGGESTED', 'ACCEPTED', 'FROZEN', 'READY'));

COMMENT ON COLUMN undecided_ag.backlog_items.status IS '状態（SUGGESTED／ACCEPTED／FROZEN／READY）';
