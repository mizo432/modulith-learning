-- 従業員IDカラムを UUID から BIGINT へ変更します。
-- productDiscovery 領域の Product.managerId と ScrumTeam.poEmployeeId/smEmployeeId、
-- および requirementBacklog 領域の ScrumTeamSubset.poEmployeeId/smEmployeeId を SnowflakeId へ移行します。
-- 開発データは再投入前提のため、型変換は単純化します（USING NULL）。

-- products テーブル
ALTER TABLE undecided_ag.products
    ALTER COLUMN manager_id TYPE BIGINT USING NULL;

-- scrum_teams テーブル
ALTER TABLE undecided_ag.scrum_teams
    ALTER COLUMN po_employee_id TYPE BIGINT USING NULL;

ALTER TABLE undecided_ag.scrum_teams
    ALTER COLUMN sm_employee_id TYPE BIGINT USING NULL;

-- scrum_team_subsets テーブル
ALTER TABLE undecided_ag.scrum_team_subsets
    ALTER COLUMN po_employee_id TYPE BIGINT USING NULL;

ALTER TABLE undecided_ag.scrum_team_subsets
    ALTER COLUMN sm_employee_id TYPE BIGINT USING NULL;
