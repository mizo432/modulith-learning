-- メンバー従業員IDリストカラムを TEXT[] から BIGINT[] へ変更します。
-- SnowflakeIdConverterがLong型に変換するため、BIGINT[]が正しい型です。

-- scrum_teams テーブル
ALTER TABLE undecided_ag.scrum_teams
    ALTER COLUMN member_employee_ids DROP DEFAULT,
    ALTER COLUMN member_employee_ids TYPE BIGINT[] USING (member_employee_ids::BIGINT[]),
    ALTER COLUMN member_employee_ids SET DEFAULT '{}'::bigint[];

-- scrum_team_subsets テーブル
ALTER TABLE undecided_ag.scrum_team_subsets
    ALTER COLUMN member_employee_ids DROP DEFAULT,
    ALTER COLUMN member_employee_ids TYPE BIGINT[] USING (member_employee_ids::BIGINT[]),
    ALTER COLUMN member_employee_ids SET DEFAULT '{}'::bigint[];
