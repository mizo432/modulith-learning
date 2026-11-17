-- 既存のスクラムチームにスプリント0を自動的に追加します。
-- 既にスプリント0を持つチームはスキップされます。

INSERT INTO sprints (id, product_id, sprint_number, title, goal, status, team_id, team_name, is_sprint_zero, created_at, updated_at)
SELECT
    -- SnowflakeId用の一意なIDを生成します（既存データと重複しない様に大きな値を使用します）
    (-1 * team.id) AS id,
    team.product_id,
    0 AS sprint_number,
    'Sprint 0' AS title,
    'チームの初期準備期間' AS goal,
    'PLANNED' AS status,
    team.id::uuid AS team_id,
    team.name AS team_name,
    TRUE AS is_sprint_zero,
    CURRENT_TIMESTAMP AS created_at,
    CURRENT_TIMESTAMP AS updated_at
FROM undecided_ag.scrum_teams team
WHERE NOT EXISTS (
    SELECT 1
    FROM sprints s
    WHERE s.team_id = team.id::uuid
    AND s.is_sprint_zero = TRUE
);
