# AG-06 成果計測・フィードバック適応 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "METRIC" as METRIC {
  * metric_id : INTEGER <<not null>>
  --
  名称 : VARCHAR
  種類 : VARCHAR
  値 : VARCHAR
  期間 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "FEEDBACK" as FEEDBACK {
  * feedback_id : INTEGER <<not null>>
  --
  内容 : VARCHAR
  種類 : VARCHAR
  優先度 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

METRIC ||--o{ FEEDBACK
@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "指標" as METRIC {
  * metric_id : INTEGER <<not null>>
  --
  名称 : VARCHAR
  種類 : VARCHAR
  値 : VARCHAR
}

entity "フィードバック" as FEEDBACK {
  * feedback_id : INTEGER <<not null>>
  --
  内容 : VARCHAR
  種類 : VARCHAR
  優先度 : VARCHAR
}

METRIC ||--o{ FEEDBACK
@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| METRIC | 指標 | R |
| FEEDBACK | フィードバック | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| METRIC | FEEDBACK | 1対多 |
