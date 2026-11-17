# イベントER図

## 概要

本ファイルは、EF-04 保管・アーカイブのイベントエンティティ間の関係をER図で定義します。

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

entity "RetentionPeriodSet\nEVT-EF-31" as RPS {
  * event_id : String
  * retention_id : String
  * form_type : String
  * retention_years : Integer
  --
  occurred_at : LocalDateTime
}

entity "FormDisposed\nEVT-EF-33" as FD {
  * event_id : String
  * disposal_id : String
  * form_id : String
  * disposed_by : String
  --
  occurred_at : LocalDateTime
}

entity "ArchivePolicyChanged\nEVT-EF-34" as APC {
  * event_id : String
  * policy_id : String
  * policy_name : String
  --
  occurred_at : LocalDateTime
}

entity "ArchiveTargetConfirmed\nEVT-EF-35" as ATC {
  * event_id : String
  * target_id : String
  * form_id : String
  * policy_id : String
  --
  occurred_at : LocalDateTime
}

entity "ArchiveProcessCompleted\nEVT-EF-36" as APCmpl {
  * event_id : String
  * process_id : String
  * target_id : String
  --
  occurred_at : LocalDateTime
}

entity "FormRestoreRequested\nEVT-EF-38" as FRR {
  * event_id : String
  * restore_id : String
  * form_id : String
  * requested_by : String
  --
  occurred_at : LocalDateTime
}

entity "FormRestoreCompleted\nEVT-EF-39" as FRCmpl {
  * event_id : String
  * restore_id : String
  * form_id : String
  * restored_at : LocalDateTime
  --
  occurred_at : LocalDateTime
}

DomainEvent <|-- RPS
DomainEvent <|-- FD
DomainEvent <|-- APC
DomainEvent <|-- ATC
DomainEvent <|-- APCmpl
DomainEvent <|-- FRR
DomainEvent <|-- FRCmpl

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

entity "保存期間が設定された\nEVT-EF-31" as RPS {
  * イベントID : String
  * 保存期間ID : String
  * 帳票種類 : String
  * 保存年数 : Integer
  --
  発生日時 : LocalDateTime
}

entity "帳票が廃棄された\nEVT-EF-33" as FD {
  * イベントID : String
  * 廃棄ID : String
  * 帳票ID : String
  * 廃棄者 : String
  --
  発生日時 : LocalDateTime
}

entity "アーカイブ処理が完了した\nEVT-EF-36" as APCmpl {
  * イベントID : String
  * 処理ID : String
  * 対象ID : String
  --
  発生日時 : LocalDateTime
}

entity "帳票復元が要求された\nEVT-EF-38" as FRR {
  * イベントID : String
  * 復元ID : String
  * 帳票ID : String
  * 要求者 : String
  --
  発生日時 : LocalDateTime
}

DomainEvent <|-- RPS
DomainEvent <|-- FD
DomainEvent <|-- APCmpl
DomainEvent <|-- FRR

@enduml
```

## イベントエンティティ一覧

| イベントID | イベント名 | 集約 | 主要属性 |
|------------|------------|------|----------|
| EVT-EF-31 | 保存期間が設定された | RetentionPeriod | retention_id, form_type, retention_years |
| EVT-EF-32 | 保存期限が近づいた | RetentionPeriod | retention_id, form_id, expiry_date |
| EVT-EF-33 | 帳票が廃棄された | Disposal | disposal_id, form_id, disposed_by |
| EVT-EF-34 | アーカイブポリシーが変更された | ArchivePolicy | policy_id, policy_name |
| EVT-EF-35 | アーカイブ対象が確定した | ArchiveTarget | target_id, form_id, policy_id |
| EVT-EF-36 | アーカイブ処理が完了した | ArchiveProcess | process_id, target_id |
| EVT-EF-37 | アーカイブ処理が失敗した | ArchiveProcess | process_id, target_id, error_message |
| EVT-EF-38 | 帳票復元が要求された | FormRestore | restore_id, form_id, requested_by |
| EVT-EF-39 | 帳票復元が完了した | FormRestore | restore_id, form_id, restored_at |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| DomainEvent | 継承 | 保存期間が設定された | ベースイベントから継承 |
| DomainEvent | 継承 | 帳票が廃棄された | ベースイベントから継承 |
| DomainEvent | 継承 | アーカイブ処理が完了した | ベースイベントから継承 |
| DomainEvent | 継承 | 帳票復元が要求された | ベースイベントから継承 |
