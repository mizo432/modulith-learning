# EVT-EF-03 業務システムが登録された

## 概要

業務システムが新規登録されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-03                                                          |
| イベント名       | 業務システムが登録された                                           |
| 英語名           | `BusinessSystemRegistered`                                         |
| 集約             | 業務システム (BusinessSystem)                                      |
| 関連ユースケース | UC-EF-004 業務システムを登録する                                   |
| 関連情報         | INF-EF-02 業務システム                                             |
| 発生条件         | 管理者が業務システムを新規登録したとき                             |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "BusinessSystem"                         |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `systemId`        | String         | ✓   | 業務システムの一意な識別子               |
| `systemName`      | String         | ✓   | 業務システム名                           |
| `systemType`      | String         | ✓   | システム種類                             |
| `registeredBy`    | String         | ✓   | 登録者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-04 業務システムが更新された | 更新時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formFoundation.spi.businessSystem.event.BusinessSystemRegistered`
