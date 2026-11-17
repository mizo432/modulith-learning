# store テーブルカラム定義

## 概要

店舗基本情報テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | store |
| 説明 | 店舗基本情報 |
| 情報ID | INF-AS-ORG-05 |
| 履歴管理要 | Y |
| 証跡要 | Y |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | store_id | BIGINT | Y | Y | 店舗ID | PK, AUTO_INCREMENT |
| 02 | organization_id | BIGINT | Y | Y | 組織ID | FK: organization, 一意制約 |
| 03 | company_id | BIGINT | Y | N | 会社ID | FK: company |
| 04 | store_code | VARCHAR(30) | Y | Y | 店舗コード | 一意制約 |
| 05 | name | VARCHAR(100) | Y | N | 店舗名 | |
| 06 | address | VARCHAR(200) | N | N | 住所 | |
| 07 | phone | VARCHAR(20) | N | N | 電話番号 | |
| 08 | manager_name | VARCHAR(50) | N | N | 店長名 | |
| 09 | status | VARCHAR(20) | Y | N | 状態 | 有効/無効 |
| 10 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 11 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 12 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |
| 13 | created_by | VARCHAR(50) | N | N | 作成者 | |
| 14 | updated_at | TIMESTAMP | Y | N | 更新日時 | DEFAULT CURRENT_TIMESTAMP |
| 15 | updated_by | VARCHAR(50) | N | N | 更新者 | |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_store_organization | organization_id | 一意 | 組織での検索 |
| 02 | idx_store_company | company_id | 非一意 | 会社での検索 |
| 03 | idx_store_code | store_code | 一意 | 店舗コードでの検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [organization テーブル](./organization.md)
- [company テーブル](./company.md)
