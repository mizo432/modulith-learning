# NT-01 通知管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "NOTIFICATION" as NOTIFICATION {
  * notification_id : INTEGER <<not null>>
  --
  チャネル : VARCHAR
  宛先 : VARCHAR
  件名 : VARCHAR
  本文 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

entity "NOTIFICATION_CHANNEL" as NOTIFICATION_CHANNEL {
  * channel_id : INTEGER <<not null>>
  --
  タイプ : VARCHAR
  設定 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

NOTIFICATION ||--o{ NOTIFICATION_CHANNEL
@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "通知" as NOTIFICATION {
  * notification_id : INTEGER <<not null>>
  --
  チャネル : VARCHAR
  宛先 : VARCHAR
  件名 : VARCHAR
}

entity "通知チャネル" as NOTIFICATION_CHANNEL {
  * channel_id : INTEGER <<not null>>
  --
  タイプ : VARCHAR
  設定 : VARCHAR
  状態 : VARCHAR
}

NOTIFICATION ||--o{ NOTIFICATION_CHANNEL
@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| NOTIFICATION | 通知 | E |
| NOTIFICATION_CHANNEL | 通知チャネル | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| NOTIFICATION | NOTIFICATION_CHANNEL | 1対多 |
