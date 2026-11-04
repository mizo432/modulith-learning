# EVT-EF-07 アクセス制御ポリシーが変更された

## 概要

アクセス制御ポリシーが変更されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-07                                                          |
| イベント名       | アクセス制御ポリシーが変更された                                   |
| 英語名           | `AccessControlPolicyChanged`                                       |
| 集約             | アクセス制御ポリシー (AccessControlPolicy)                         |
| 関連ユースケース | UC-EF-010 アクセス制御ポリシーを変更する                           |
| 関連情報         | INF-EF-04 アクセス制御ポリシー                                     |
| 発生条件         | 管理者がアクセス制御ポリシーを変更したとき                         |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "AccessControlPolicy"                    |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `policyId`        | String         | ✓   | アクセス制御ポリシーの一意な識別子       |
| `systemId`        | String         | ✓   | 関連する業務システムの識別子             |
| `policyName`      | String         | ✓   | ポリシー名                               |
| `changedBy`       | String         | ✓   | 変更者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-03 業務システムが登録された | 関連する業務システム登録時に発生 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formFoundation.spi.accessControlPolicy.event.AccessControlPolicyChanged`
