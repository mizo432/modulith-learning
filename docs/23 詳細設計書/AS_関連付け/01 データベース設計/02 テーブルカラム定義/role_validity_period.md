# role_validity_period テーブルカラム定義

## 概要

ロール有効期間テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | role_validity_period |
| 説明 | ロール有効期間 |
| 情報ID | INF-AS-PARTY-ROLE-03 |
| 履歴管理要 | Y |
| 証跡要 | N |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | validity_id | BIGINT | Y | Y | 有効期間ID | PK, AUTO_INCREMENT |
| 02 | party_id | BIGINT | Y | N | パーティーID | FK: party |
| 03 | role_id | BIGINT | Y | N | ロールID | FK: party_role |
| 04 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 05 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 06 | status | VARCHAR(20) | Y | N | 状態 | 有効/無効 |
| 07 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_validity_party | party_id | 非一意 | パーティーでの検索 |
| 02 | idx_validity_role | role_id | 非一意 | ロールでの検索 |
| 03 | idx_validity_period | valid_from, valid_to | 非一意 | 有効期間での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [party テーブル](./party.md)
- [party_role テーブル](./party_role.md)
