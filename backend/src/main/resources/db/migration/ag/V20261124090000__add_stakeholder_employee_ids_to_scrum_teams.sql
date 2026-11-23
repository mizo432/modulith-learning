-- スクラムチームテーブルにステークホルダー従業員IDリストカラムを追加します。
ALTER TABLE undecided_ag.scrum_teams
    ADD COLUMN stakeholder_employee_ids BIGINT[] DEFAULT '{}';

COMMENT ON COLUMN undecided_ag.scrum_teams.stakeholder_employee_ids IS 'ステークホルダーの従業員IDリスト（組織ユニットに所属しない従業員）';
