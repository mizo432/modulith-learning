# NT-01 通知管理 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "Notification" as NOTIFICATION {
  * id : LONG <<not null>>
  --
  recipient_id : UUID
  source_id : UUID
  source_domain : STRING
  title : STRING
  content : STRING
  type : ENUM
  is_read : BOOLEAN
  created_at : TIMESTAMP
  updated_at : TIMESTAMP
  deleted_at : TIMESTAMP
}

@enduml

## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "通知" as NOTIFICATION {
  * 通知ID : LONG <<not null>>
  --
  受信者ID : UUID
  送信元ID : UUID
  送信元事業領域 : STRING
  タイトル : STRING
  本文 : STRING
  通知タイプ : ENUM
  既読フラグ : BOOLEAN
  作成日時 : TIMESTAMP
  更新日時 : TIMESTAMP
  削除日時 : TIMESTAMP
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| Notification | 通知 | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| 従業員 | 通知 | 1対多 |
