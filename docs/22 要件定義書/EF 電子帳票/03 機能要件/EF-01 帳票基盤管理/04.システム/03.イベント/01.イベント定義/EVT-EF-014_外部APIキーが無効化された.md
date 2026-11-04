# EVT-EF-14 外部APIキーが無効化された

## 概要

外部APIキーが無効化されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-14                                                          |
| イベント名       | 外部APIキーが無効化された                                         |
| 英語名           | `ExternalApiKeyDisabled`                                           |
| 集約             | 外部APIキー (ExternalApiKey)                                       |
| 関連ユースケース | UC-EF-021 外部APIキーを無効化する                                   |
| 関連情報         | INF-EF-08 外部APIキー                                               |
| 発生条件         | 管理者が外部APIキーを無効化したとき                                 |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "ExternalApiKey"                         |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `apiKeyId`        | String         | ✓   | 外部APIキーの一意な識別子               |
| `systemId`        | String         | ✓   | 関連する業務システムの識別子             |
| `disabledBy`      | String         | ✓   | 無効化者の利用者ID                       |
| `reason`          | String         | ✗   | 無効化理由                               |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-13 外部APIキーが発行された | 発行時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formFoundation.spi.externalApiKey.event.ExternalApiKeyDisabled`
