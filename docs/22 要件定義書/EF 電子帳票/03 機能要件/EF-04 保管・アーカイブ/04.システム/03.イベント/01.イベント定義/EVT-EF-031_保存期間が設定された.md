# EVT-EF-31 保存期間が設定された

## 概要

帳票の保存期間が設定されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-31                                                          |
| イベント名       | 保存期間が設定された                                               |
| 英語名           | `RetentionPeriodSet`                                               |
| 集約             | 保存期間 (RetentionPeriod)                                         |
| 関連ユースケース | UC-EF-050 保存期間を設定する                                       |
| 関連情報         | INF-EF-19 保存期間                                                 |
| 発生条件         | 管理者が帳票の保存期間を設定したとき                               |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "RetentionPeriod"                        |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `retentionId`     | String         | ✓   | 保存期間の一意な識別子                   |
| `formDefId`       | String         | ✓   | 関連する帳票定義の識別子                 |
| `retentionPeriod` | int            | ✓   | 保存期間（月数）                         |
| `setBy`           | String         | ✓   | 設定者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-32 保存期限が近づいた     | 期限接近時に発生             |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.storageArchive.spi.retentionPeriod.event.RetentionPeriodSet`
