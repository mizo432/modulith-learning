-- ストーリーポイント列を追加し、ESTIMATED / IN_PROGRESS / DONE 状態を CHECK 制約に追加します。
-- 既存データへの影響はありません（story_point は NULL 許容）。

-- story_point 列を追加
ALTER TABLE undecided_ag.backlog_items ADD COLUMN story_point INTEGER;

-- story_point の CHECK 制約（フィボナッチ数列のみ許容）
ALTER TABLE undecided_ag.backlog_items
    ADD CONSTRAINT ck_backlog_items_story_point CHECK (
        story_point IN (0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89)
    );

-- status CHECK 制約に ESTIMATED / IN_PROGRESS / DONE を追加
ALTER TABLE undecided_ag.backlog_items DROP CONSTRAINT ck_backlog_items_status;
ALTER TABLE undecided_ag.backlog_items
    ADD CONSTRAINT ck_backlog_items_status CHECK (
        status IN ('SUGGESTED', 'ACCEPTED', 'FROZEN', 'READY', 'ESTIMATED', 'IN_PROGRESS', 'DONE')
    );

COMMENT ON COLUMN undecided_ag.backlog_items.story_point IS 'ストーリーポイント（見積もり値。フィボナッチ数列 0〜89 のみ許容）';
COMMENT ON COLUMN undecided_ag.backlog_items.status IS '状態（SUGGESTED／ACCEPTED／FROZEN／READY／ESTIMATED／IN_PROGRESS／DONE）';
