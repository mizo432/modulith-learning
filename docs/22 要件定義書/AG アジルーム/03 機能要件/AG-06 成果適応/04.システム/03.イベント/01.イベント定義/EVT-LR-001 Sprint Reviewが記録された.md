# EVT-LR-001 Sprint Reviewが記録された

## 概要

Sprintの成果、フィードバック、決定、未解決事項が記録されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                |
|------------------|-----------------------------------|
| イベントID       | EVT-LR-001                        |
| イベント名       | Sprint Reviewが記録された         |
| 英語名           | `SprintReviewRecorded`            |
| 集約             | Sprint Review記録                 |
| 関連ユースケース | UC-LR-001 Sprint Reviewを記録する |
| 関連情報         | INF-LR-01 Sprint Review記録       |
| 発生条件         | Sprint Reviewの結果を記録したとき |

## イベントペイロード

| プロパティ      | 型             | 必須 | 説明                        |
|-----------------|----------------|------|-----------------------------|
| `eventId`       | UUID           | ✓   | イベントを一意に識別するID  |
| `occurredAt`    | LocalDateTime  | ✓   | イベントが発生した時刻      |
| `aggregateId`   | String         | ✓   | 関連する集約ルートのID      |
| `aggregateType` | String         | ✓   | "SprintReview"              |
| `version`       | int            | ✓   | 集約のバージョン            |
| `reviewId`      | String         | ✓   | Sprint Reviewの一意な識別子 |
| `sprintId`      | String         | ✓   | 関連するスプリントのID      |
| `outcomes`      | List\<String\> | ✓   | 成果リスト                  |
| `feedback`      | List\<String\> | ✗   | フィードバックリスト        |
| `decisions`     | List\<String\> | ✗   | 決定事項リスト              |
| `openItems`     | List\<String\> | ✗   | 未解決事項リスト            |
| `recordedBy`    | String         | ✓   | 記録者のアクターID          |

## 不変条件

- 一度記録されたイベントは変更・削除しない

## 関連イベント

| イベント                                    | 関係             |
|---------------------------------------------|------------------|
| EVT-LR-002 Sprint Retrospectiveが記録された | 振り返り時に発生 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.learning.outcomeAdaptation.spi.sprintReview.event.SprintReviewRecorded`
