-- sprintExecution 領域の UUID カラムを BIGINT へ変更します。
-- sprints テーブルの product_id と team_id、
-- sprint_settings テーブルの product_id を SnowflakeId へ移行します。
-- 開発データは再投入前提のため、型変換は単純化します（USING NULL）。

-- sprints テーブル
ALTER TABLE sprints
    ALTER COLUMN product_id TYPE BIGINT USING NULL;

ALTER TABLE sprints
    ALTER COLUMN team_id TYPE BIGINT USING NULL;

-- sprint_settings テーブル
ALTER TABLE undecided_ag.sprint_settings
    ALTER COLUMN product_id TYPE BIGINT USING NULL;
