# ER-01 元号管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "ERA" as ERA {
  * era_id : INTEGER <<not null>>
  --
  名称 : VARCHAR
  英字略名 : VARCHAR
  適用開始日 : VARCHAR
  適用終了日 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "元号" as ERA {
  * era_id : INTEGER <<not null>>
  --
  名称 : VARCHAR
  英字略名 : VARCHAR
  適用開始日 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| ERA | 元号 | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
