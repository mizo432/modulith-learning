-- バックログアイテムの従業員IDカラムを UUID から BIGINT へ変更します。
-- backlog_items.creator_employee_id と backlog_item_comments.author_employee_id を
-- SnowflakeId へ移行します。開発データは再投入前提のため、型変換は単純化します（USING NULL）。

-- backlog_items テーブル
ALTER TABLE undecided_ag.backlog_items
    ALTER COLUMN creator_employee_id TYPE BIGINT USING NULL;

-- backlog_item_comments テーブル
ALTER TABLE undecided_ag.backlog_item_comments
    ALTER COLUMN author_employee_id TYPE BIGINT USING NULL;
