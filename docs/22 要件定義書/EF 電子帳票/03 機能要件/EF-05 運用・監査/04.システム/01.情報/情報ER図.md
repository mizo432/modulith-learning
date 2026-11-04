# 情報ER図

## 概要

本ファイルは、EF-05 運用・監査の情報エンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "NotificationSetting\nINF-EF-25" as NotificationSetting {
  * notification_id : String
  * notification_type : String
  * trigger_event : String
  * recipient_id : String
  * channel_type : String
  * status : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "AuditLog\nINF-EF-26" as AuditLog {
  * log_id : String
  * user_id : String
  * action_type : String
  * target_entity : String
  * target_id : String
  * action_detail : String
  * performed_at : LocalDateTime
  --
  created_at : LocalDateTime
}

entity "OperationManagement\nINF-EF-27" as OperationManagement {
  * operation_id : String
  * error_type : String
  * error_message : String
  * severity : String
  * status : String
  * occurred_at : LocalDateTime
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "FormAnalytics\nINF-EF-28" as FormAnalytics {
  * analytics_id : String
  * form_type : String
  * metric_type : String
  * metric_value : Integer
  * period_start : LocalDate
  * period_end : LocalDate
  --
  created_at : LocalDateTime
}

entity "ArchiveOperation\nINF-EF-29" as ArchiveOperation {
  * op_id : String
  * process_id : String
  * operation_status : String
  * error_count : Integer
  * last_error : String
  * monitored_at : LocalDateTime
  --
  created_at : LocalDateTime
}

NotificationSetting "1" -- "0..*" AuditLog : triggers >
OperationManagement "1" -- "0..*" ArchiveOperation : monitors >
FormAnalytics "1" -- "0..*" AuditLog : references >

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "通知設定\nINF-EF-25" as NotificationSetting {
  * 通知ID : String
  * 通知種類 : String
  * トリガーイベント : String
  * 宛先ID : String
  * チャンネル種類 : String
  * 状態 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "監査ログ\nINF-EF-26" as AuditLog {
  * ログID : String
  * 利用者ID : String
  * 操作種類 : String
  * 対象エンティティ : String
  * 対象ID : String
  * 操作詳細 : String
  * 実行日時 : LocalDateTime
  --
  作成日時 : LocalDateTime
}

entity "運用管理\nINF-EF-27" as OperationManagement {
  * 運用ID : String
  * エラー種類 : String
  * エラーメッセージ : String
  * 重大度 : String
  * 状態 : String
  * 発生日時 : LocalDateTime
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "帳票分析\nINF-EF-28" as FormAnalytics {
  * 分析ID : String
  * 帳票種類 : String
  * メトリック種類 : String
  * メトリック値 : Integer
  * 期間開始 : LocalDate
  * 期間終了 : LocalDate
  --
  作成日時 : LocalDateTime
}

entity "アーカイブ運用\nINF-EF-29" as ArchiveOperation {
  * 運用ID : String
  * 処理ID : String
  * 運用ステータス : String
  * エラー数 : Integer
  * 最終エラー : String
  * 監視日時 : LocalDateTime
  --
  作成日時 : LocalDateTime
}

NotificationSetting "1" -- "0..*" AuditLog : 誘発 >
OperationManagement "1" -- "0..*" ArchiveOperation : 監視 >
FormAnalytics "1" -- "0..*" AuditLog : 参照 >

@enduml
```

## 情報エンティティ一覧

| 情報ID | 情報名 | 主要属性 | 関連エンティティ |
|--------|--------|----------|-----------------|
| INF-EF-25 | 通知設定 | notification_id, notification_type, trigger_event, recipient_id | 監査ログ |
| INF-EF-26 | 監査ログ | log_id, user_id, action_type, target_entity, performed_at | 通知設定, 帳票分析 |
| INF-EF-27 | 運用管理 | operation_id, error_type, error_message, severity | アーカイブ運用 |
| INF-EF-28 | 帳票分析 | analytics_id, form_type, metric_type, metric_value | 監査ログ |
| INF-EF-29 | アーカイブ運用 | op_id, process_id, operation_status, error_count | 運用管理 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 多重度 | 説明 |
|---------------|------|-----------------|--------|------|
| 通知設定 | 誘発 | 監査ログ | 1 : 0..* | 通知トリガーで監査ログが記録 |
| 運用管理 | 監視 | アーカイブ運用 | 1 : 0..* | 運用管理がアーカイブ運用を監視 |
| 帳票分析 | 参照 | 監査ログ | 1 : 0..* | 分析に監査ログデータを参照 |
