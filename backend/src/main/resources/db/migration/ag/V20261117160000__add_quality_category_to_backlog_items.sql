-- バックログアイテムに品質カテゴリカラムを追加
ALTER TABLE undecided_ag.backlog_items
    ADD COLUMN quality_category VARCHAR(20);

-- 既存行のvalueから品質カテゴリを一括計算（valueがNULLの行はquality_categoryもNULLのまま）
UPDATE undecided_ag.backlog_items
SET quality_category = CASE
    WHEN value >= 76 THEN 'ATTRACTIVE'
    WHEN value >= 51 THEN 'MUST_BE'
    WHEN value >= 26 THEN 'ONE_DIMENSIONAL'
    WHEN value >= 0 THEN 'INDIFFERENT'
    ELSE NULL
END
WHERE value IS NOT NULL;
