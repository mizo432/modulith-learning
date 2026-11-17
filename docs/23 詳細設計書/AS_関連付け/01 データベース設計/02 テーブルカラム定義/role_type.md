# role_type テーブルカラム定義

## 概要

ロール種類マスターテーブルのカラム定義を定義します。

## テーブル情報

| 項目 | 値 |
|------|-----|
| テーブル名 | role_type |
| 説明 | ロール種類マスター |
| 情報ID | INF-AS-PARTY-ROLE-02 |
| 履歴管理要 | N |
| 証跡要 | N |

## カラム定義

| 番号 | カラム名 | 型 | NotNull | Unique | 説明 | 制約 |
|------|---------|-----|---------|--------|------|------|
| 01 | role_type_code | VARCHAR(20) | Y | Y | ロール種類コード | PK |
| 02 | name | VARCHAR(50) | Y | N | 種類名称 | |
| 03 | description | VARCHAR(200) | N | N | 説明 | |
| 04 | created_at | TIMESTAMP | Y | N | 作成日時 | DEFAULT CURRENT_TIMESTAMP |

## インデックス

| 番号 | インデックス名 | カラム | 種類 | 説明 |
|------|--------------|--------|------|------|
| 01 | idx_role_type_name | name | 非一意 | 種類名称での検索 |

## 参照

- [テーブル一覧](../01%20table一覧.md)
- [party_role テーブル](./party_role.md)
