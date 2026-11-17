# organization テーブルカラム定義

## 概要

組織基本情報テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | organization |
| 説明 | 組織基本情報 |
| 情報ID | INF-AS-ORG-01 |
| 履歴管理要 | Y |
| 証跡要 | Y |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | organization_id | BIGINT | Y | Y | 組織ID | PK, AUTO_INCREMENT |
| 02 | party_id | BIGINT | Y | Y | パーティーID | FK: party, 一意制約 |
| 03 | organization_code | VARCHAR(30) | Y | Y | 組織コード | 一意制約 |
| 04 | name | VARCHAR(100) | Y | N | 組織名 | |
| 05 | organization_type_code | VARCHAR(20) | Y | N | 組織種類コード | FK: organization_type |
| 06 | parent_organization_id | BIGINT | N | N | 親組織ID | FK: organization (自己参照) |
| 07 | status | VARCHAR(20) | Y | N | 状態 | 有効/無効 |
| 08 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 09 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 10 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |
| 11 | created_by | VARCHAR(50) | N | N | 作成者 | |
| 12 | updated_at | TIMESTAMP | Y | N | 更新日時 | DEFAULT CURRENT_TIMESTAMP |
| 13 | updated_by | VARCHAR(50) | N | N | 更新者 | |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_organization_party | party_id | 一意 | パーティーでの検索 |
| 02 | idx_organization_code | organization_code | 一意 | 組織コードでの検索 |
| 03 | idx_organization_type | organization_type_code | 非一意 | 組織種類での検索 |
| 04 | idx_organization_parent | parent_organization_id | 非一意 | 親組織での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [party テーブル](./party.md)
- [organization_type テーブル](./organization_type.md)
