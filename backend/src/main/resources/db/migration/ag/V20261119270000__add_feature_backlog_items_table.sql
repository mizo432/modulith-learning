-- フィーチャーとバックログアイテムの関連テーブルを作成します。
-- 複合主キー（feature_id, backlog_item_id）で一意性を保証します。
-- 1つのバックログアイテムは最大1つのフィーチャーにのみ割り当てられます。
CREATE TABLE undecided_ag.feature_backlog_items
(
    feature_id      BIGINT      NOT NULL,
    backlog_item_id BIGINT      NOT NULL,
    created_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (feature_id, backlog_item_id),
    CONSTRAINT fk_feature_backlog_items_features FOREIGN KEY (feature_id) REFERENCES undecided_ag.features (id),
    CONSTRAINT fk_feature_backlog_items_backlog_items FOREIGN KEY (backlog_item_id) REFERENCES undecided_ag.backlog_items (id),
    CONSTRAINT uq_feature_backlog_items_backlog_item UNIQUE (backlog_item_id)
);

CREATE INDEX idx_feature_backlog_items_feature_id ON undecided_ag.feature_backlog_items (feature_id);

COMMENT ON TABLE undecided_ag.feature_backlog_items IS 'フィーチャーとバックログアイテムの関連';
COMMENT ON COLUMN undecided_ag.feature_backlog_items.feature_id IS 'フィーチャーID（複合主キー）';
COMMENT ON COLUMN undecided_ag.feature_backlog_items.backlog_item_id IS 'バックログアイテムID（複合主キー）';
COMMENT ON COLUMN undecided_ag.feature_backlog_items.created_at IS '紐付け日時';
