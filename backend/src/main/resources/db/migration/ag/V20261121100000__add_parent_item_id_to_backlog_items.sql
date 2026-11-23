-- バックログアイテムに分割元の親アイテム参照を追加します。
ALTER TABLE undecided_ag.backlog_items
    ADD COLUMN parent_item_id BIGINT NULL;

ALTER TABLE undecided_ag.backlog_items
    ADD CONSTRAINT fk_backlog_items_parent
        FOREIGN KEY (parent_item_id)
        REFERENCES undecided_ag.backlog_items (id);

CREATE INDEX idx_backlog_items_parent_item_id
    ON undecided_ag.backlog_items (parent_item_id);