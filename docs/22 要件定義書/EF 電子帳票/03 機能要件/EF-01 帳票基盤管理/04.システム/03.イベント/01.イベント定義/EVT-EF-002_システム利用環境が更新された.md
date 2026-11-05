# EVT-EF-02 システム利用環境が更新された

## 概要

システム利用環境の設定が更新されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-02                                                          |
| イベント名       | システム利用環境が更新された                                       |
| 英語名           | `SystemEnvironmentUpdated`                                         |
| 集約             | システム利用環境 (SystemEnvironment)                               |
| 関連ユースケース | UC-EF-002 システム利用環境を更新する                               |
| 関連情報         | INF-EF-01 システム利用環境                                         |
| 発生条件         | 管理者がシステム利用環境の設定を更新したとき                       |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "SystemEnvironment"                      |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `environmentId`   | String         | ✓   | システム利用環境の一意な識別子           |
| `systemName`      | String         | ✗   | システム名                               |
| `environmentType` | String         | ✗   | 環境区分                                 |
| `updatedBy`       | String         | ✓   | 更新者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-01 システム利用環境が登録された | 登録時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formFoundation.spi.systemEnvironment.event.SystemEnvironmentUpdated`
