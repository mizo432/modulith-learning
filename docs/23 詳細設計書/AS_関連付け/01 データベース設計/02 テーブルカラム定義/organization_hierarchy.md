# organization_hierarchy テーブルカラム定義

## 概要

組織階層テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | organization_hierarchy |
| 説明 | 組織階層 |
| 情報ID | INF-AS-ORG-03 |
| 履歴管理要 | Y |
| 証跡要 | N |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | hierarchy_id | BIGINT | Y | Y | 階層ID | PK, AUTO_INCREMENT |
| 02 | parent_organization_id | BIGINT | Y | N | 親組織ID | FK: organization |
| 03 | child_organization_id | BIGINT | Y | N | 子組織ID | FK: organization |
| 04 | level | INT | Y | N | 階層レベル | |
| 05 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 06 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 07 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_hierarchy_parent | parent_organization_id | 非一意 | 親組織での検索 |
| 02 | idx_hierarchy_child | child_organization_id | 非一意 | 子組織での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [organization テーブル](./organization.md)
