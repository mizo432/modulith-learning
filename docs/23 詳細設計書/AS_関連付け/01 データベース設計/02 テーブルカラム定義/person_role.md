# person_role テーブルカラム定義

## 概要

人ロール情報テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | person_role |
| 説明 | 人ロール情報 |
| 情報ID | INF-AS-PERSON-02 |
| 履歴管理要 | Y |
| 証跡要 | Y |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | person_role_id | BIGINT | Y | Y | 人ロールID | PK, AUTO_INCREMENT |
| 02 | party_id | BIGINT | Y | N | パーティーID | FK: party |
| 03 | role_code | VARCHAR(30) | Y | N | ロールコード | |
| 04 | role_name | VARCHAR(50) | Y | N | ロール名称 | |
| 05 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 06 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 07 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |
| 08 | created_by | VARCHAR(50) | N | N | 作成者 | |
| 09 | updated_at | TIMESTAMP | Y | N | 更新日時 | DEFAULT CURRENT_TIMESTAMP |
| 10 | updated_by | VARCHAR(50) | N | N | 更新者 | |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_person_role_party | party_id | 非一意 | パーティーでの検索 |
| 02 | idx_person_role_code | role_code | 非一意 | ロールコードでの検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [party テーブル](./party.md)
