# EVT-EF-16 帳票定義が登録された

## 概要

帳票定義が新規登録されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-16                                                          |
| イベント名       | 帳票定義が登録された                                               |
| 英語名           | `FormDefinitionRegistered`                                         |
| 集約             | 帳票定義 (FormDefinition)                                          |
| 関連ユースケース | UC-EF-024 帳票定義を登録する                                       |
| 関連情報         | INF-EF-10 帳票定義                                                 |
| 発生条件         | 管理者が帳票定義を新規登録したとき                                 |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "FormDefinition"                         |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `formDefId`       | String         | ✓   | 帳票定義の一意な識別子                   |
| `formType`        | String         | ✓   | 帳票種類                                 |
| `formName`        | String         | ✓   | 帳票名                                   |
| `systemId`        | String         | ✓   | 関連する業務システムの識別子             |
| `registeredBy`    | String         | ✓   | 登録者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-17 帳票定義が更新された   | 更新時に発生                 |
| EVT-EF-18 帳票定義が公開された   | 公開時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formManagement.spi.formDefinition.event.FormDefinitionRegistered`
