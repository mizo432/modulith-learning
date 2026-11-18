ALTER TABLE undecided_ag.backlog_items
    ADD COLUMN version INTEGER DEFAULT 0;

COMMENT ON COLUMN undecided_ag.backlog_items.version IS '楽観ロック用のバージョン番号';
