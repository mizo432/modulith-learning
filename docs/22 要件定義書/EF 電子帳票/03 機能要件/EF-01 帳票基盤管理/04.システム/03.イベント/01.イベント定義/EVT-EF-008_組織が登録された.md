# EVT-EF-08 組織が登録された

## 概要

組織が新規登録されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-08                                                          |
| イベント名       | 組織が登録された                                                   |
| 英語名           | `OrganizationRegistered`                                           |
| 集約             | 組織 (Organization)                                                |
| 関連ユースケース | UC-EF-012 組織を登録する                                           |
| 関連情報         | INF-EF-05 組織                                                     |
| 発生条件         | 管理者が組織を新規登録したとき                                     |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "Organization"                           |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `orgId`           | String         | ✓   | 組織の一意な識別子                       |
| `orgName`         | String         | ✓   | 組織名                                   |
| `parentOrgId`     | String         | ✗   | 親組織の識別子                           |
| `registeredBy`    | String         | ✓   | 登録者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-09 組織が更新された       | 更新時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formFoundation.spi.organization.event.OrganizationRegistered`
