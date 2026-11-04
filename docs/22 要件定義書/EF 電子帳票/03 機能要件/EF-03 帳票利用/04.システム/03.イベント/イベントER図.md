# イベントER図

## 概要

本ファイルは、EF-03 帳票利用のイベントエンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "DomainEvent\nBase" as DomainEvent {
  * event_id : String
  * event_type : String
  * occurred_at : LocalDateTime
  * aggregate_id : String
  * aggregate_type : String
  --
  payload : String
}

entity "FormDelivered\nEVT-EF-24" as FD {
  * event_id : String
  * delivery_id : String
  * form_id : String
  * recipient_id : String
  --
  occurred_at : LocalDateTime
}

entity "FormPreviewed\nEVT-EF-26" as FP {
  * event_id : String
  * preview_id : String
  * form_id : String
  * user_id : String
  --
  occurred_at : LocalDateTime
}

entity "FormSearched\nEVT-EF-27" as FS {
  * event_id : String
  * search_id : String
  * user_id : String
  * result_count : Integer
  --
  occurred_at : LocalDateTime
}

entity "ApprovalRequested\nEVT-EF-28" as AR {
  * event_id : String
  * approval_id : String
  * form_id : String
  * approver_id : String
  --
  occurred_at : LocalDateTime
}

entity "ApprovalCompleted\nEVT-EF-29" as AC {
  * event_id : String
  * approval_id : String
  * form_id : String
  * approved_by : String
  --
  occurred_at : LocalDateTime
}

DomainEvent <|-- FD
DomainEvent <|-- FP
DomainEvent <|-- FS
DomainEvent <|-- AR
DomainEvent <|-- AC

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "ドメインイベント\nベース" as DomainEvent {
  * イベントID : String
  * イベント種類 : String
  * 発生日時 : LocalDateTime
  * 集約ID : String
  * 集約種類 : String
  --
  ペイロード : String
}

entity "帳票が配信された\nEVT-EF-24" as FD {
  * イベントID : String
  * 配信ID : String
  * 帳票ID : String
  * 宛先ID : String
  --
  発生日時 : LocalDateTime
}

entity "承認が要求された\nEVT-EF-28" as AR {
  * イベントID : String
  * 承認ID : String
  * 帳票ID : String
  * 承認者ID : String
  --
  発生日時 : LocalDateTime
}

DomainEvent <|-- FD
DomainEvent <|-- AR

note right of DomainEvent
  他のイベントも同様に継承
end note

@enduml
```

## イベントエンティティ一覧

| イベントID | イベント名 | 集約 | 主要属性 |
|------------|------------|------|----------|
| EVT-EF-24 | 帳票が配信された | FormDelivery | delivery_id, form_id, recipient_id |
| EVT-EF-25 | 帳票配信が失敗した | FormDelivery | delivery_id, form_id, error_message |
| EVT-EF-26 | 帳票がプレビューされた | FormPreview | preview_id, form_id, user_id |
| EVT-EF-27 | 帳票が検索された | FormSearch | search_id, user_id, result_count |
| EVT-EF-28 | 承認が要求された | Approval | approval_id, form_id, approver_id |
| EVT-EF-29 | 承認が完了した | Approval | approval_id, form_id, approved_by |
| EVT-EF-30 | 承認が却下された | Approval | approval_id, form_id, rejected_by |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| DomainEvent | 継承 | 帳票が配信された | ベースイベントから継承 |
| DomainEvent | 継承 | 承認が要求された | ベースイベントから継承 |
