# EVT-EF-05 ストレージ接続が設定された

## 概要

ストレージ接続が設定されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-05                                                          |
| イベント名       | ストレージ接続が設定された                                         |
| 英語名           | `StorageConnectionConfigured`                                      |
| 集約             | ストレージ接続 (StorageConnection)                                 |
| 関連ユースケース | UC-EF-007 ストレージ接続を設定する                                 |
| 関連情報         | INF-EF-03 ストレージ接続                                           |
| 発生条件         | 管理者がストレージ接続を設定したとき                               |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "StorageConnection"                      |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `storageId`       | String         | ✓   | ストレージ接続の一意な識別子             |
| `storageName`     | String         | ✓   | ストレージ名                             |
| `storageType`     | String         | ✓   | ストレージ種類                           |
| `configuredBy`    | String         | ✓   | 設定者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-06 ストレージ接続が更新された | 更新時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formFoundation.spi.storageConnection.event.StorageConnectionConfigured`
