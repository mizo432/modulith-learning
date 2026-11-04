# EVT-EF-20 帳票生成設定が変更された

## 概要

帳票生成設定が変更されたことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-20                                                          |
| イベント名       | 帳票生成設定が変更された                                           |
| 英語名           | `FormGenerationSettingChanged`                                     |
| 集約             | 帳票生成設定 (FormGenerationSetting)                               |
| 関連ユースケース | UC-EF-030 帳票生成設定を変更する                                   |
| 関連情報         | INF-EF-12 帳票生成設定                                             |
| 発生条件         | 管理者が帳票生成設定を変更したとき                                 |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "FormGenerationSetting"                  |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `settingId`       | String         | ✓   | 帳票生成設定の一意な識別子               |
| `formDefId`       | String         | ✓   | 関連する帳票定義の識別子                 |
| `outputFormat`    | String         | ✓   | 出力形式                                 |
| `changedBy`       | String         | ✓   | 変更者の利用者ID                         |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-16 帳票定義が登録された   | 関連する帳票定義登録時に発生 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.formManagement.spi.formGenerationSetting.event.FormGenerationSettingChanged`
