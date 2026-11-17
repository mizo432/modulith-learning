# DDDモデル候補

## 概要

本ファイルは、EF-05 運用・監査のDDD（Domain-Driven Design）モデル候補を定義します。

## サブドメイン候補

| 項番 | サブドメイン名 | 種類 | 説明 |
|------|---------------|------|------|
| 01 | 通知管理 | 支援 | 通知設定・配信の管理 |
| 02 | 監査ログ管理 | コア | システム操作の監査証跡管理 |
| 03 | 運用管理 | コア | システム運用エラーの管理 |
| 04 | 帳票分析 | 支援 | 帳票利用の統計分析 |
| 05 | アーカイブ運用 | 支援 | アーカイブ運用の監視・管理 |

## アグリゲート候補

| 項番 | アグリゲート名 | 集約ルート | 説明 |
|------|---------------|-----------|------|
| 01 | NotificationSetting | NotificationSetting | 通知設定の集約 |
| 02 | AuditLog | AuditLog | 監査ログの集約 |
| 03 | OperationManagement | OperationManagement | 運用管理の集約 |
| 04 | FormAnalytics | FormAnalytics | 帳票分析の集約 |
| 05 | ArchiveOperation | ArchiveOperation | アーカイブ運用の集約 |

## エンティティ候補

| 項番 | エンティティ名 | アグリゲート | 説明 |
|------|---------------|-------------|------|
| 01 | NotificationSetting | NotificationSetting | 通知設定 |
| 02 | AuditLog | AuditLog | 監査ログ |
| 03 | OperationManagement | OperationManagement | 運用管理 |
| 04 | FormAnalytics | FormAnalytics | 帳票分析 |
| 05 | ArchiveOperation | ArchiveOperation | アーカイブ運用 |

## 値オブジェクト候補

| 項番 | 値オブジェクト名 | 使用箇所 | 説明 |
|------|-----------------|---------|------|
| 01 | NotificationId | NotificationSetting | 通知識別子 |
| 02 | NotificationType | NotificationSetting | 通知種類 |
| 03 | TriggerEvent | NotificationSetting | トリガーイベント |
| 04 | ChannelType | NotificationSetting | チャンネル種類 |
| 05 | LogId | AuditLog | ログ識別子 |
| 06 | ActionType | AuditLog | 操作種類 |
| 07 | TargetEntity | AuditLog | 対象エンティティ |
| 08 | ActionDetail | AuditLog | 操作詳細 |
| 09 | OperationId | OperationManagement | 運用識別子 |
| 10 | ErrorType | OperationManagement | エラー種類 |
| 11 | Severity | OperationManagement | 重大度 |
| 12 | AnalyticsId | FormAnalytics | 分析識別子 |
| 13 | MetricType | FormAnalytics | メトリック種類 |
| 14 | MetricValue | FormAnalytics | メトリック値 |
| 15 | OperationStatus | ArchiveOperation | 運用ステータス |

## ドメインサービス候補

| 項番 | サービス名 | 説明 |
|------|-----------|------|
| 01 | NotificationService | 通知配信ロジック |
| 02 | AuditLogService | 監査ログ記録ロジック |
| 03 | AnalyticsService | 分析データ集計ロジック |

## ドメインイベント候補

| 項番 | イベント名 | 発生元 | 説明 |
|------|-----------|--------|------|
| 01 | NotificationSettingChanged | NotificationSetting | 通知設定が変更された |
| 02 | AuditLogRecorded | AuditLog | 監査ログが記録された |
| 03 | OperationErrorOccurred | OperationManagement | 運用エラーが発生した |
| 04 | FormAnalyticsUpdated | FormAnalytics | 帳票分析データが更新された |
