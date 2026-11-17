# EVT-EF-01 システム利用環境が登録された

## 概要

システム利用環境が新規登録されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-01                                                          |
| イベント名       | システム利用環境が登録された                                       |
| 英語名           | `SystemEnvironmentRegistered`                                      |
| 集約             | システム利用環境 (SystemEnvironment)                               |
| 関連ユースケース | UC-EF-001 システム利用環境を登録する                               |
| 関連情報         | INF-EF-01 システム利用環境                                         |
| 発生条件         | 管理者がシステム利用環境を新規登録したとき                         |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "SystemEnvironment"                      |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `environmentId`   | String         | ✓   | システム利用環境の一意な識別子           |
| `tenantId`        | String         | ✓   | テナントの一意な識別子                   |
| `systemName`      | String         | ✓   | システム名                               |
| `environmentType` | String         | ✓   | 環境区分                                 |
| `registeredBy`    | String         | ✓   | 登録者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-02 システム利用環境が更新された | 更新時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formFoundation.spi.systemEnvironment.event.SystemEnvironmentRegistered`
