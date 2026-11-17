# party_role テーブルカラム定義

## 概要

パーティーロール定義テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | party_role |
| 説明 | パーティーロール定義 |
| 情報ID | INF-AS-PARTY-ROLE-01 |
| 履歴管理要 | Y |
| 証跡要 | Y |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | role_id | BIGINT | Y | Y | ロールID | PK, AUTO_INCREMENT |
| 02 | role_code | VARCHAR(30) | Y | Y | ロールコード | 一意制約 |
| 03 | role_name | VARCHAR(50) | Y | N | ロール名称 | |
| 04 | role_type_code | VARCHAR(20) | Y | N | ロール種類コード | FK: role_type |
| 05 | description | VARCHAR(200) | N | N | 説明 | |
| 06 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 07 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 08 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |
| 09 | created_by | VARCHAR(50) | N | N | 作成者 | |
| 10 | updated_at | TIMESTAMP | Y | N | 更新日時 | DEFAULT CURRENT_TIMESTAMP |
| 11 | updated_by | VARCHAR(50) | N | N | 更新者 | |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_role_code | role_code | 一意 | ロールコードでの検索 |
| 02 | idx_role_type | role_type_code | 非一意 | ロール種類での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [role_type テーブル](./role_type.md)
