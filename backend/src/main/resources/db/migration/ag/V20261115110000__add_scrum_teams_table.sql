-- スクラムチームテーブルを作成します。
CREATE TABLE undecided_ag.scrum_teams
(
    id               BIGINT       NOT NULL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    business_domain  VARCHAR(100) NOT NULL,
    po_employee_id   UUID,
    po_name          VARCHAR(100),
    product_id       BIGINT       NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_scrum_teams_products FOREIGN KEY (product_id) REFERENCES undecided_ag.products (id)
);

CREATE INDEX idx_scrum_teams_product_id ON undecided_ag.scrum_teams (product_id);

COMMENT ON TABLE undecided_ag.scrum_teams IS 'スクラムチーム基本情報テーブル';
COMMENT ON COLUMN undecided_ag.scrum_teams.id IS 'スクラムチームの一意な識別子';
COMMENT ON COLUMN undecided_ag.scrum_teams.name IS 'チーム名';
COMMENT ON COLUMN undecided_ag.scrum_teams.business_domain IS '事業領域';
COMMENT ON COLUMN undecided_ag.scrum_teams.po_employee_id IS 'PO（Product Owner）の従業員ID';
COMMENT ON COLUMN undecided_ag.scrum_teams.po_name IS 'POの名前（非正規化フィールド）';
COMMENT ON COLUMN undecided_ag.scrum_teams.product_id IS '関連するProductへの参照';
COMMENT ON COLUMN undecided_ag.scrum_teams.created_at IS 'スクラムチームが作成された日時';
COMMENT ON COLUMN undecided_ag.scrum_teams.updated_at IS 'スクラムチームが最後に更新された日時';
