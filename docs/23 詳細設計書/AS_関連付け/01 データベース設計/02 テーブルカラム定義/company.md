# company テーブルカラム定義

## 概要

会社基本情報テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | company |
| 説明 | 会社基本情報 |
| 情報ID | INF-AS-ORG-04 |
| 履歴管理要 | Y |
| 証跡要 | Y |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | company_id | BIGINT | Y | Y | 会社ID | PK, AUTO_INCREMENT |
| 02 | organization_id | BIGINT | Y | Y | 組織ID | FK: organization, 一意制約 |
| 03 | company_code | VARCHAR(30) | Y | Y | 会社コード | 一意制約 |
| 04 | name | VARCHAR(100) | Y | N | 会社名 | |
| 05 | address | VARCHAR(200) | N | N | 住所 | |
| 06 | phone | VARCHAR(20) | N | N | 電話番号 | |
| 07 | representative | VARCHAR(50) | N | N | 代表者名 | |
| 08 | status | VARCHAR(20) | Y | N | 状態 | 有効/無効 |
| 09 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 10 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 11 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |
| 12 | created_by | VARCHAR(50) | N | N | 作成者 | |
| 13 | updated_at | TIMESTAMP | Y | N | 更新日時 | DEFAULT CURRENT_TIMESTAMP |
| 14 | updated_by | VARCHAR(50) | N | N | 更新者 | |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_company_organization | organization_id | 一意 | 組織での検索 |
| 02 | idx_company_code | company_code | 一意 | 会社コードでの検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [organization テーブル](./organization.md)
