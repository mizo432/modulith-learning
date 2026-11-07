# EF-01 帳票基盤管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "FORM_DEFINITION" as FORM_DEFINITION {
  * form_def_id : INTEGER <<not null>>
  --
  種類 : VARCHAR
  レイアウト : VARCHAR
  テンプレート : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "帳票定義" as FORM_DEFINITION {
  * form_def_id : INTEGER <<not null>>
  --
  種類 : VARCHAR
  レイアウト : VARCHAR
  テンプレート : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| FORM_DEFINITION | 帳票定義 | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
