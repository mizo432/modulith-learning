# CA-01 カレンダー管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "CALENDAR" as CALENDAR {
  * calendar_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  タイプ : VARCHAR
  有効期間 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "HOLIDAY" as HOLIDAY {
  * holiday_id : INTEGER <<not null>>
  --
  日付 : VARCHAR
  名前 : VARCHAR
  作成日 : VARCHAR
}

CALENDAR ||--o{ HOLIDAY
@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "カレンダー" as CALENDAR {
  * calendar_id : INTEGER <<not null>>
  --
  名前 : VARCHAR
  タイプ : VARCHAR
  有効期間 : VARCHAR
}

entity "祝日" as HOLIDAY {
  * holiday_id : INTEGER <<not null>>
  --
  日付 : VARCHAR
  名前 : VARCHAR
  作成日 : VARCHAR
}

CALENDAR ||--o{ HOLIDAY
@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| CALENDAR | カレンダー | R |
| HOLIDAY | 祝日 | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| CALENDAR | HOLIDAY | 1対多 |
