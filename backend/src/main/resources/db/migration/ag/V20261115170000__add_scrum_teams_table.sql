-- スクラムチームテーブルを作成します。
CREATE TABLE undecided_ag.scrum_teams
(
    id               BIGINT        NOT NULL PRIMARY KEY,
    product_id       UUID          NOT NULL,
    team_name        VARCHAR(100)  NOT NULL,
    business_domain  VARCHAR(100)  NOT NULL,
    description      TEXT,
    po_employee_id   UUID,
    status           VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE'
        CHECK (status IN ('ACTIVE', 'ARCHIVED')),
    created_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at       TIMESTAMP
);

CREATE INDEX idx_scrum_teams_product_id ON undecided_ag.scrum_teams (product_id);
CREATE INDEX idx_scrum_teams_status ON undecided_ag.scrum_teams (status);
CREATE INDEX idx_scrum_teams_po_employee_id ON undecided_ag.scrum_teams (po_employee_id);

COMMENT ON TABLE undecided_ag.scrum_teams IS 'スクラムチーム基本情報テーブル';
COMMENT ON COLUMN undecided_ag.scrum_teams.id IS 'スクラムチームの一意な識別子';
COMMENT ON COLUMN undecided_ag.scrum_teams.product_id IS '関連するProductのID';
COMMENT ON COLUMN undecided_ag.scrum_teams.team_name IS 'チーム名';
COMMENT ON COLUMN undecided_ag.scrum_teams.business_domain IS '事業領域';
COMMENT ON COLUMN undecided_ag.scrum_teams.description IS 'チームの説明';
COMMENT ON COLUMN undecided_ag.scrum_teams.po_employee_id IS 'PO（Product Owner）の従業員ID';
COMMENT ON COLUMN undecided_ag.scrum_teams.status IS 'ステータス（ACTIVE/ARCHIVED）';
COMMENT ON COLUMN undecided_ag.scrum_teams.created_at IS '作成日時';
COMMENT ON COLUMN undecided_ag.scrum_teams.updated_at IS '更新日時';
COMMENT ON COLUMN undecided_ag.scrum_teams.deleted_at IS '論理削除日時（null=未削除）';
