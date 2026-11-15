-- スクラムチームテーブルにSM（Scrum Master）関連カラムを追加します。
ALTER TABLE undecided_ag.scrum_teams
    ADD COLUMN sm_employee_id UUID,
    ADD COLUMN sm_name VARCHAR(100);

COMMENT ON COLUMN undecided_ag.scrum_teams.sm_employee_id IS 'SM（Scrum Master）の従業員ID';
COMMENT ON COLUMN undecided_ag.scrum_teams.sm_name IS 'SMの名前（非正規化フィールド）';
