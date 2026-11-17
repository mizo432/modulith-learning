# AR-01 承認ワークフロー ER図

## 英語版

@startuml
left to right direction
skinparam linetype ortho

entity "APPROVAL_REQUEST" as APPROVAL_REQUEST {
  * approval_request_id : INTEGER <<not null>>
  --
  タイトル : VARCHAR
  内容 : VARCHAR
  申請者 : VARCHAR
  承認者 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
  更新日 : VARCHAR
}

entity "APPROVAL_FLOW" as APPROVAL_FLOW {
  * approval_flow_id : INTEGER <<not null>>
  --
  ステップ : VARCHAR
  担当者 : VARCHAR
  状態 : VARCHAR
  作成日 : VARCHAR
}

APPROVAL_REQUEST ||--o{ APPROVAL_FLOW
@enduml


## 日本語版

@startuml
left to right direction
skinparam linetype ortho

entity "承認リクエスト" as APPROVAL_REQUEST {
  * approval_request_id : INTEGER <<not null>>
  --
  タイトル : VARCHAR
  内容 : VARCHAR
  申請者 : VARCHAR
}

entity "承認フロー" as APPROVAL_FLOW {
  * approval_flow_id : INTEGER <<not null>>
  --
  ステップ : VARCHAR
  担当者 : VARCHAR
  状態 : VARCHAR
}

APPROVAL_REQUEST ||--o{ APPROVAL_FLOW
@enduml

## エンティティ一覧

| エンティティ名 | 日本語名 | 分類 |
|---------------|---------|------|
| APPROVAL_REQUEST | 承認リクエスト | E |
| APPROVAL_FLOW | 承認フロー | R |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 |
|---------------|---------------|------|
| APPROVAL_REQUEST | APPROVAL_FLOW | 1対多 |
