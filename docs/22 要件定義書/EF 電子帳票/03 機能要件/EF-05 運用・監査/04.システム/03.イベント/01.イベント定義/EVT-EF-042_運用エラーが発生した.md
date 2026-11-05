# EVT-EF-42 運用エラーが発生した

## 概要

運用エラーが発生したことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-42                                                          |
| イベント名       | 運用エラーが発生した                                               |
| 英語名           | `OperationErrorOccurred`                                           |
| 集約             | 運用管理 (OperationManagement)                                     |
| 関連ユースケース | UC-EF-070 運用エラーを監視する                                     |
| 関連情報         | INF-EF-27 運用管理                                                 |
| 発生条件         | システム運用中にエラーが発生したとき                               |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "OperationManagement"                    |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `operationId`     | String         | ✓   | 運用管理の一意な識別子                   |
| `operationType`   | String         | ✓   | 運用種類                                 |
| `errorMessage`    | String         | ✓   | エラーメッセージ                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-43 運用エラーが解決された | 解決時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.operationAudit.spi.operationManagement.event.OperationErrorOccurred`
