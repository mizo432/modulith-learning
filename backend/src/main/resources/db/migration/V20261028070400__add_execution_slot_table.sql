-- JBジョブ管理: execution_slotテーブル作成

CREATE TABLE execution_slot (
    id BIGINT NOT NULL,
    max_slots INTEGER NOT NULL,
    used_slots INTEGER NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
);
