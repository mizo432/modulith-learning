# TA-01 税率管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "TAX_RATE" as TAX_RATE {
  * tax_rate_id : INTEGER <<not null>>
  --
  名称 : VARCHAR
  種類 : VARCHAR
  税率 : VARCHAR
  適用期間 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "税率" as TAX_RATE {
  * tax_rate_id : INTEGER <<not null>>
  --
  名称 : VARCHAR
  種類 : VARCHAR
  税率 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| TAX_RATE | 税率 | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
