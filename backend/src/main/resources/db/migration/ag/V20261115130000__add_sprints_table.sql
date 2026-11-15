-- スプリントテーブルを作成します。
CREATE TABLE sprints
(
    id               BIGINT       NOT NULL PRIMARY KEY,
    product_id       UUID         NOT NULL,
    sprint_number    INTEGER      NOT NULL,
    title            VARCHAR(100) NOT NULL,
    goal             VARCHAR(1000),
    start_date       DATE,
    end_date         DATE,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PLANNED',
    team_id          UUID         NOT NULL,
    team_name        VARCHAR(100),
    velocity         INTEGER,
    notes            VARCHAR(2000),
    is_sprint_zero   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP,
    CONSTRAINT uk_sprint_product_number UNIQUE (product_id, sprint_number)
);

CREATE INDEX idx_sprints_product_id ON sprints (product_id);
CREATE INDEX idx_sprints_team_id ON sprints (team_id);
CREATE INDEX idx_sprints_status ON sprints (status);
CREATE INDEX idx_sprints_team_sprint_zero ON sprints (team_id, is_sprint_zero);

COMMENT ON TABLE sprints IS 'スプリント基本情報テーブル';
COMMENT ON COLUMN sprints.id IS 'スプリントの一意な識別子';
COMMENT ON COLUMN sprints.product_id IS '関連する製品のID';
COMMENT ON COLUMN sprints.sprint_number IS 'スプリント番号。製品ごとに連番で管理されます';
COMMENT ON COLUMN sprints.title IS 'スプリントのタイトル';
COMMENT ON COLUMN sprints.goal IS 'スプリントの目標';
COMMENT ON COLUMN sprints.start_date IS 'スプリントの開始日。スプリント0の場合はnullです';
COMMENT ON COLUMN sprints.end_date IS 'スプリントの終了日。スプリント0の場合はnullです';
COMMENT ON COLUMN sprints.status IS 'スプリントの状態（PLANNED, ACTIVE, COMPLETED, CANCELLED）';
COMMENT ON COLUMN sprints.team_id IS 'スプリントを担当するチームのID';
COMMENT ON COLUMN sprints.team_name IS 'スプリントを担当するチームの名前';
COMMENT ON COLUMN sprints.velocity IS 'チームのベロシティ';
COMMENT ON COLUMN sprints.notes IS 'スプリントに関する補足メモ';
COMMENT ON COLUMN sprints.is_sprint_zero IS 'スプリント0かどうかを示すフラグ';
COMMENT ON COLUMN sprints.created_at IS 'スプリントが作成された日時';
COMMENT ON COLUMN sprints.updated_at IS 'スプリントが最後に更新された日時';
COMMENT ON COLUMN sprints.deleted_at IS 'スプリントが論理削除された日時。nullの場合は未削除です';
