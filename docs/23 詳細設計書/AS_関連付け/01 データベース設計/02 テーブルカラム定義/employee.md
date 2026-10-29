# employee テーブルカラム定義

## 概要

従業員基本情報テーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | employee |
| 説明 | 従業員基本情報 |
| 情報ID | INF-AS-PERSON-01 |
| 履歴管理要 | Y |
| 証跡要 | Y |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | employee_id | BIGINT | Y | Y | 従業員ID | PK, AUTO_INCREMENT |
| 02 | party_id | BIGINT | Y | Y | パーティーID | FK: party, 一意制約 |
| 03 | employee_code | VARCHAR(30) | Y | Y | 従業員コード | 一意制約 |
| 04 | first_name | VARCHAR(50) | Y | N | 名 | |
| 05 | last_name | VARCHAR(50) | Y | N | 姓 | |
| 06 | first_name_kana | VARCHAR(50) | N | N | 名(カナ) | |
| 07 | last_name_kana | VARCHAR(50) | N | N | 姓(カナ) | |
| 08 | email | VARCHAR(100) | N | Y | メールアドレス | 一意制約 |
| 09 | phone | VARCHAR(20) | N | N | 電話番号 | |
| 10 | hire_date | DATE | Y | N | 入社日 | |
| 11 | resignation_date | DATE | N | N | 退社日 | |
| 12 | status | VARCHAR(20) | Y | N | 状態 | 有効/無効 |
| 13 | valid_from | TIMESTAMP | Y | N | 有効開始日 | |
| 14 | valid_to | TIMESTAMP | N | N | 有効終了日 | |
| 15 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |
| 16 | created_by | VARCHAR(50) | N | N | 作成者 | |
| 17 | updated_at | TIMESTAMP | Y | N | 更新日時 | DEFAULT CURRENT_TIMESTAMP |
| 18 | updated_by | VARCHAR(50) | N | N | 更新者 | |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_employee_party | party_id | 一意 | パーティーでの検索 |
| 02 | idx_employee_code | employee_code | 一意 | 従業員コードでの検索 |
| 03 | idx_employee_email | email | 一意 | メールアドレスでの検索 |
| 04 | idx_employee_status | status | 非一意 | 状態での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [party テーブル](./party.md)
