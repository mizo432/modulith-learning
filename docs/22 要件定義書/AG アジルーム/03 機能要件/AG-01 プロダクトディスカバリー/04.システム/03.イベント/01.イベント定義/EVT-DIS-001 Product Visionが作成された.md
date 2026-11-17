# EVT-DIS-001 Product Visionが作成された

## 概要

プロダクトの対象者、課題、目指す将来像が定義されて登録されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                           |
|------------------|--------------------------------------------------------------|
| イベントID       | EVT-DIS-001                                                  |
| イベント名       | Product Visionが作成された                                   |
| 英語名           | `ProductVisionCreated`                                       |
| 集約             | Product Vision                                               |
| 関連ユースケース | UC-DIS-001 Product Visionを作成する                          |
| 関連情報         | INF-DIS-01 Product Vision                                    |
| 発生条件         | プロダクトマネージャーがProduct Visionを定義して登録したとき |

## イベントペイロード

| プロパティ      | 型            | 必須 | 説明                         |
|-----------------|---------------|------|------------------------------|
| `eventId`       | UUID          | ✓   | イベントを一意に識別するID   |
| `occurredAt`    | LocalDateTime | ✓   | イベントが発生した時刻       |
| `aggregateId`   | String        | ✓   | 関連する集約ルートのID       |
| `aggregateType` | String        | ✓   | "ProductVision"              |
| `version`       | int           | ✓   | 集約のバージョン             |
| `visionId`      | String        | ✓   | Product Visionの一意な識別子 |
| `targetUsers`   | String        | ✓   | 対象者                       |
| `problem`       | String        | ✓   | 課題                         |
| `futureImage`   | String        | ✓   | 目指す将来像                 |
| `createdBy`     | String        | ✓   | 作成者のアクターID           |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                               | 関係         |
|----------------------------------------|--------------|
| EVT-DIS-002 Product Visionが更新された | 更新時に発生 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置:
  `undecided.discovery.productDiscovery.spi.productVision.event.ProductVisionCreated`
