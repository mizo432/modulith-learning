# EX-01 サンプル ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "SAMPLE" as SAMPLE {
  * sample_id : INTEGER <<not null>>
  --
  タイプ : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "サンプル" as SAMPLE {
  * sample_id : INTEGER <<not null>>
  --
  タイプ : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| SAMPLE | サンプル | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
