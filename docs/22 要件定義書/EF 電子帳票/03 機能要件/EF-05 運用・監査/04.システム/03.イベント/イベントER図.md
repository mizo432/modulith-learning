# イベントER図

## 概要

本ファイルは、EF-05 運用・監査のイベントエンティティ間の関係をER図で定義します。

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

entity "NotificationSettingChanged\nEVT-EF-40" as NSC {
  * event_id : String
  * notification_id : String
  * notification_type : String
  --
  occurred_at : LocalDateTime
}

entity "AuditLogRecorded\nEVT-EF-41" as ALR {
  * event_id : String
  * log_id : String
  * user_id : String
  * action_type : String
  --
  occurred_at : LocalDateTime
}

entity "OperationErrorOccurred\nEVT-EF-42" as OEO {
  * event_id : String
  * operation_id : String
  * error_type : String
  * error_message : String
  --
  occurred_at : LocalDateTime
}

entity "OperationErrorResolved\nEVT-EF-43" as OER {
  * event_id : String
  * operation_id : String
  * resolved_by : String
  --
  occurred_at : LocalDateTime
}

entity "FormAnalyticsUpdated\nEVT-EF-44" as FAU {
  * event_id : String
  * analytics_id : String
  * metric_type : String
  * metric_value : Integer
  --
  occurred_at : LocalDateTime
}

entity "ArchiveOperationWarning\nEVT-EF-45" as AOW {
  * event_id : String
  * op_id : String
  * warning_message : String
  --
  occurred_at : LocalDateTime
}

entity "ArchiveOperationError\nEVT-EF-46" as AOE {
  * event_id : String
  * op_id : String
  * error_message : String
  --
  occurred_at : LocalDateTime
}

DomainEvent <|-- NSC
DomainEvent <|-- ALR
DomainEvent <|-- OEO
DomainEvent <|-- OER
DomainEvent <|-- FAU
DomainEvent <|-- AOW
DomainEvent <|-- AOE

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

entity "通知設定が変更された\nEVT-EF-40" as NSC {
  * イベントID : String
  * 通知ID : String
  * 通知種類 : String
  --
  発生日時 : LocalDateTime
}

entity "監査ログが記録された\nEVT-EF-41" as ALR {
  * イベントID : String
  * ログID : String
  * 利用者ID : String
  * 操作種類 : String
  --
  発生日時 : LocalDateTime
}

entity "運用エラーが発生した\nEVT-EF-42" as OEO {
  * イベントID : String
  * 運用ID : String
  * エラー種類 : String
  * エラーメッセージ : String
  --
  発生日時 : LocalDateTime
}

entity "帳票分析データが更新された\nEVT-EF-44" as FAU {
  * イベントID : String
  * 分析ID : String
  * メトリック種類 : String
  * メトリック値 : Integer
  --
  発生日時 : LocalDateTime
}

DomainEvent <|-- NSC
DomainEvent <|-- ALR
DomainEvent <|-- OEO
DomainEvent <|-- FAU

@enduml
```

## イベントエンティティ一覧

| イベントID | イベント名 | 集約 | 主要属性 |
|------------|------------|------|----------|
| EVT-EF-40 | 通知設定が変更された | NotificationSetting | notification_id, notification_type |
| EVT-EF-41 | 監査ログが記録された | AuditLog | log_id, user_id, action_type |
| EVT-EF-42 | 運用エラーが発生した | OperationManagement | operation_id, error_type, error_message |
| EVT-EF-43 | 運用エラーが解決された | OperationManagement | operation_id, resolved_by |
| EVT-EF-44 | 帳票分析データが更新された | FormAnalytics | analytics_id, metric_type, metric_value |
| EVT-EF-45 | アーカイブ運用に警告が発生した | ArchiveOperation | op_id, warning_message |
| EVT-EF-46 | アーカイブ運用にエラーが発生した | ArchiveOperation | op_id, error_message |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 説明 |
|---------------|------|-----------------|------|
| DomainEvent | 継承 | 通知設定が変更された | ベースイベントから継承 |
| DomainEvent | 継承 | 監査ログが記録された | ベースイベントから継承 |
| DomainEvent | 継承 | 運用エラーが発生した | ベースイベントから継承 |
| DomainEvent | 継承 | 帳票分析データが更新された | ベースイベントから継承 |
