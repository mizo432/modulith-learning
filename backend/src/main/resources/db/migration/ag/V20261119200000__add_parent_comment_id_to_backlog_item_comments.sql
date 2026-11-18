-- バックログアイテムコメントに親コメント参照（parent_comment_id）を追加
-- Issue #510: コメントはスレッド形式で返信できる

ALTER TABLE undecided_ag.backlog_item_comments
    ADD COLUMN parent_comment_id BIGINT;

ALTER TABLE undecided_ag.backlog_item_comments
    ADD CONSTRAINT fk_backlog_item_comments_parent
        FOREIGN KEY (parent_comment_id)
        REFERENCES undecided_ag.backlog_item_comments(id);

CREATE INDEX idx_backlog_item_comments_parent
    ON undecided_ag.backlog_item_comments(parent_comment_id);
