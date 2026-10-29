# party_role_mapping テーブルカラム定義

## 概要

パーティーとロールの対応テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | party_role_mapping |
| 説明 | パーティーとロールの対応 |
| 情報ID | INF-AS-PARTY-03 |
| 履歴管理要 | Y |
| 証跡要 | N |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | party_id | BIGINT | Y | N | パーティーID | FK: party |
| 02 | role_id | BIGINT | Y | N | ロールID | FK: party_role |
| 03 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 04 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 05 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | pk_party_role_mapping | party_id, role_id | 一意 | PK |
| 02 | idx_party_role_party | party_id | 非一意 | パーティーでの検索 |
| 03 | idx_party_role_role | role_id | 非一意 | ロールでの検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [party テーブル](./party.md)
- [party_role テーブル](./party_role.md)
