# EF-05 運用・監査 ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "AUDIT_LOG" as AUDIT_LOG {
  * audit_log_id : INTEGER <<not null>>
  --
  操作者 : VARCHAR
  日時 : VARCHAR
  操作内容 : VARCHAR
  対象 : VARCHAR
  作成日 : VARCHAR
}

@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "監査ログ" as AUDIT_LOG {
  * audit_log_id : INTEGER <<not null>>
  --
  操作者 : VARCHAR
  日時 : VARCHAR
  操作内容 : VARCHAR
}

@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| AUDIT_LOG | 監査ログ | E |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
