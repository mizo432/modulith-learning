# EVT-EF-40 通知設定が変更された

## 概要

通知設定が変更されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-40                                                          |
| イベント名       | 通知設定が変更された                                               |
| 英語名           | `NotificationSettingChanged`                                       |
| 集約             | 通知設定 (NotificationSetting)                                     |
| 関連ユースケース | UC-EF-066 通知設定を変更する                                       |
| 関連情報         | INF-EF-25 通知設定                                                 |
| 発生条件         | 利用者が通知設定を変更したとき                                     |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "NotificationSetting"                    |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `notificationId`  | String         | ✓   | 通知設定の一意な識別子                   |
| `userId`          | String         | ✓   | 通知対象利用者の識別子                   |
| `notificationType`| String         | ✓   | 通知種類                                 |
| `changedBy`       | String         | ✓   | 変更者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-41 監査ログが記録された   | 記録時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.operationAudit.spi.notificationSetting.event.NotificationSettingChanged`
