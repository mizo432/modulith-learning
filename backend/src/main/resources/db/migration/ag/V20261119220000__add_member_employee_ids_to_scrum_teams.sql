-- スクラムチームテーブルにメンバー従業員IDリストカラムを追加します。
ALTER TABLE undecided_ag.scrum_teams
    ADD COLUMN member_employee_ids TEXT[] DEFAULT '{}';

COMMENT ON COLUMN undecided_ag.scrum_teams.member_employee_ids IS 'チームメンバーの従業員IDリスト（PO/SMを含む）';
