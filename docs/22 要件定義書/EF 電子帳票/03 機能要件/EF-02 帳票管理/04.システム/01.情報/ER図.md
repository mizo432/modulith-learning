# EF-02 帳票管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "FORM" as FORM {
  * form_id : INTEGER <<not null>>
  --
  種類 : VARCHAR
  バージョン : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "帳票" as FORM {
  * form_id : INTEGER <<not null>>
  --
  種類 : VARCHAR
  バージョン : VARCHAR
  状態 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| FORM | 帳票 | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
