# DO-01 データベース定義 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "DATABASE" as DATABASE {
  * database_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  説明 : VARCHAR
  作成日 : VARCHAR
}

entity "SCHEMA" as SCHEMA {
  * schema_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  データベース : VARCHAR
  作成日 : VARCHAR
}

entity "TABLE" as TABLE {
  * table_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  スキーマ : VARCHAR
  作成日 : VARCHAR
}

DATABASE ||--o{ SCHEMA
SCHEMA ||--o{ TABLE
@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "データベース" as DATABASE {
  * database_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  説明 : VARCHAR
  作成日 : VARCHAR
}

entity "スキーマ" as SCHEMA {
  * schema_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  データベース : VARCHAR
  作成日 : VARCHAR
}

entity "テーブル" as TABLE {
  * table_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  スキーマ : VARCHAR
  作成日 : VARCHAR
}

DATABASE ||--o{ SCHEMA
SCHEMA ||--o{ TABLE
@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| DATABASE | データベース | R |
| SCHEMA | スキーマ | R |
| TABLE | テーブル | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| DATABASE | SCHEMA | 1対多 |
| SCHEMA | TABLE | 1対多 |
