# EF-03 帳票利用 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "FORM_DELIVERY" as FORM_DELIVERY {
  * form_delivery_id : INTEGER <<not null>>
  --
  宛先 : VARCHAR
  日時 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "帳票配信" as FORM_DELIVERY {
  * form_delivery_id : INTEGER <<not null>>
  --
  宛先 : VARCHAR
  日時 : VARCHAR
  状態 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| FORM_DELIVERY | 帳票配信 | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
