# affiliation テーブルカラム定義

## 概要

所属対応表テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | affiliation |
| 説明 | 所属対応表 |
| 情報ID | INF-AS-PERSON-03 |
| 履歴管理要 | Y |
| 証跡要 | N |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | affiliation_id | BIGINT | Y | Y | 所属ID | PK, AUTO_INCREMENT |
| 02 | employee_id | BIGINT | Y | N | 従業員ID | FK: employee |
| 03 | organization_id | BIGINT | Y | N | 組織ID | FK: organization |
| 04 | position | VARCHAR(50) | N | N | 役職 | |
| 05 | is_primary | BOOLEAN | Y | N | 主所属フラグ | |
| 06 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 07 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 08 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_affiliation_employee | employee_id | 非一意 | 従業員での検索 |
| 02 | idx_affiliation_organization | organization_id | 非一意 | 組織での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [employee テーブル](./employee.md)
- [organization テーブル](./organization.md)
