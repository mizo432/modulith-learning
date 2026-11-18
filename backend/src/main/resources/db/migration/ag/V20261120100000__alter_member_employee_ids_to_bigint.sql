-- メンバー従業員IDリストカラムを TEXT[] から BIGINT[] へ変更します。
-- SnowflakeIdConverterがLong型に変換するため、BIGINT[]が正しい型です。

-- scrum_teams テーブル
ALTER TABLE undecided_ag.scrum_teams
    ALTER COLUMN member_employee_ids TYPE BIGINT[] USING (
        ARRAY(SELECT UNNEST(member_employee_ids)::BIGINT)
    ),
    -- ALTER TYPE 時に DEFAULT を明示的に保持する
    ALTER COLUMN member_employee_ids SET DEFAULT '{}';

-- scrum_team_subsets テーブル
ALTER TABLE undecided_ag.scrum_team_subsets
    ALTER COLUMN member_employee_ids TYPE BIGINT[] USING (
        ARRAY(SELECT UNNEST(member_employee_ids)::BIGINT)
    ),
    -- ALTER TYPE 時に DEFAULT を明示的に保持する
    ALTER COLUMN member_employee_ids SET DEFAULT '{}';
