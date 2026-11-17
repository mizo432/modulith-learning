# FI-01 金融機関管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "FINANCIAL_INSTITUTION" as FINANCIAL_INSTITUTION {
  * fi_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  コード : VARCHAR
  支店 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "金融機関" as FINANCIAL_INSTITUTION {
  * fi_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  コード : VARCHAR
  支店 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| FINANCIAL_INSTITUTION | 金融機関 | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
