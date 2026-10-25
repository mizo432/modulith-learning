# EVT-SP-001 スプリントが作成された

## 概要

スプリント期間、状態、チームが定義されて登録されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                 |
|------------------|----------------------------------------------------|
| イベントID       | EVT-SP-001                                         |
| イベント名       | スプリントが作成された                             |
| 英語名           | `SprintCreated`                                    |
| 集約             | スプリント                                         |
| 関連ユースケース | UC-SP-001 スプリントを作成する                     |
| 関連情報         | INF-SP-01 スプリント                               |
| 発生条件         | スプリント期間、状態、チームを定義して登録したとき |

## イベントペイロード

| プロパティ      | 型            | 必須 | 説明                       |
|-----------------|---------------|------|----------------------------|
| `eventId`       | UUID          | ✓   | イベントを一意に識別するID |
| `occurredAt`    | LocalDateTime | ✓   | イベントが発生した時刻     |
| `aggregateId`   | String        | ✓   | 関連する集約ルートのID     |
| `aggregateType` | String        | ✓   | "Sprint"                   |
| `version`       | int           | ✓   | 集約のバージョン           |
| `sprintId`      | String        | ✓   | スプリントの一意な識別子   |
| `sprintName`    | String        | ✓   | スプリント名               |
| `startDate`     | LocalDate     | ✓   | 開始日                     |
| `endDate`       | LocalDate     | ✓   | 終了日                     |
| `status`        | String        | ✓   | 初期状態（計画済み）       |
| `teamId`        | String        | ✓   | 関連するチームのID         |
| `createdBy`     | String        | ✓   | 作成者のアクターID         |

## 不変条件

- 一度記録されたイベントは変更・削除しない

## 関連イベント

| イベント                              | 関係                   |
|---------------------------------------|------------------------|
| EVT-SP-002 スプリントが更新された     | 更新時に発生           |
| EVT-SP-003 Sprint Goalが設定された    | ゴール設定時に発生     |
| EVT-SP-005 Sprint Backlogが作成された | バックログ作成時に発生 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.sprint.sprintExecution.spi.sprint.event.SprintCreated`
