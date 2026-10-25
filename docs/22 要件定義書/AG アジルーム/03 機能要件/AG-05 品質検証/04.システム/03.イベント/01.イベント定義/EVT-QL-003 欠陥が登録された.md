# EVT-QL-003 欠陥が登録された

## 概要

再現手順、影響度、状態、関連項目が記録された欠陥を登録したことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                           |
|------------------|--------------------------------------------------------------|
| イベントID       | EVT-QL-003                                                   |
| イベント名       | 欠陥が登録された                                             |
| 英語名           | `DefectRegistered`                                           |
| 集約             | 欠陥                                                         |
| 関連ユースケース | UC-QL-004 欠陥を登録する                                     |
| 関連情報         | INF-QL-04 欠陥                                               |
| 発生条件         | 再現手順、影響度、状態、関連項目を記録して欠陥を登録したとき |

## イベントペイロード

| プロパティ             | 型            | 必須 | 説明                             |
|------------------------|---------------|------|----------------------------------|
| `eventId`              | UUID          | ✓   | イベントを一意に識別するID       |
| `occurredAt`           | LocalDateTime | ✓   | イベントが発生した時刻           |
| `aggregateId`          | String        | ✓   | 関連する集約ルートのID           |
| `aggregateType`        | String        | ✓   | "Defect"                         |
| `version`              | int           | ✓   | 集約のバージョン                 |
| `defectId`             | String        | ✓   | 欠陥の一意な識別子               |
| `title`                | String        | ✓   | 欠陥のタイトル                   |
| `reproductionSteps`    | String        | ✓   | 再現手順                         |
| `severity`             | String        | ✓   | 影響度（致命、重大、通常、軽微） |
| `status`               | String        | ✓   | 初期状態（新規）                 |
| `relatedBacklogItemId` | String        | ✗   | 関連するバックログ項目のID       |
| `registeredBy`         | String        | ✓   | 登録者のアクターID               |

## 不変条件

- 一度記録されたイベントは変更・削除しない

## 関連イベント

| イベント                    | 関係         |
|-----------------------------|--------------|
| EVT-QL-004 欠陥が更新された | 更新時に発生 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.quality.qualityVerification.spi.defect.event.DefectRegistered`
