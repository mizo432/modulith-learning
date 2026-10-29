# party テーブルカラム定義

## 概要

パーティー基本情報テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | party |
| 説明 | パーティー基本情報 |
| 情報ID | INF-AS-PARTY-01 |
| 履歴管理要 | Y |
| 証跡要 | Y |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | party_id | BIGINT | Y | Y | パーティーID | PK, AUTO_INCREMENT |
| 02 | party_type_code | VARCHAR(20) | Y | N | パーティー種類コード | FK: party_type |
| 03 | name | VARCHAR(100) | Y | N | 名称 | |
| 04 | code | VARCHAR(50) | N | Y | コード | 一意制約 |
| 05 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 06 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 07 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |
| 08 | created_by | VARCHAR(50) | N | N | 作成者 | |
| 09 | updated_at | TIMESTAMP | Y | N | 更新日時 | DEFAULT CURRENT_TIMESTAMP |
| 10 | updated_by | VARCHAR(50) | N | N | 更新者 | |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_party_type_code | party_type_code | 非一意 | 種類コードでの検索 |
| 02 | idx_party_code | code | 一意 | コードでの検索 |
| 03 | idx_party_valid_from | valid_from | 非一意 | 有効開始日での検索 |
| 04 | idx_party_valid_to | valid_to | 非一意 | 有効終了日での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [party_type テーブル](./party_type.md)
