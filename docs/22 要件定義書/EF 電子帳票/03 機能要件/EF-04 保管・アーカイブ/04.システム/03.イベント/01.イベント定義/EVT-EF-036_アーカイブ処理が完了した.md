# EVT-EF-36 アーカイブ処理が完了した

## 概要

アーカイブ処理が完了したことを表すドメインイベントです。

## イベント定義

| 項目             | 値                                                                 |
|------------------|--------------------------------------------------------------------|
| イベントID       | EVT-EF-36                                                          |
| イベント名       | アーカイブ処理が完了した                                           |
| 英語名           | `ArchiveProcessCompleted`                                          |
| 集約             | アーカイブ処理 (ArchiveProcess)                                    |
| 関連ユースケース | UC-EF-059 アーカイブ処理を実行する                                 |
| 関連情報         | INF-EF-23 アーカイブ処理                                           |
| 発生条件         | アーカイブ処理が完了したとき                                       |

## イベントペイロード

| プロパティ        | 型             | 必須 | 説明                                     |
|-------------------|----------------|------|------------------------------------------|
| `eventId`         | UUID           | ✓   | イベントを一意に識別するID               |
| `occurredAt`      | LocalDateTime  | ✓   | イベントが発生した時刻                   |
| `aggregateId`     | String         | ✓   | 関連する集約ルートのID                   |
| `aggregateType`   | String         | ✓   | "ArchiveProcess"                         |
| `version`         | int            | ✓   | 集約のバージョン                         |
| `archiveProcessId`| String         | ✓   | アーカイブ処理の一意な識別子             |
| `archivePolicyId` | String         | ✓   | 適用するアーカイブポリシーの識別子       |
| `targetCount`     | int            | ✓   | 対象件数                                 |
| `processedCount`  | int            | ✓   | 処理済み件数                             |

## 不変条件

- 一度記録されたイベントは変更・削除しない
- 訂正が必要な場合は新しいイベントとして発行する

## 関連イベント

| イベント                        | 関係                         |
|---------------------------------|------------------------------|
| EVT-EF-35 アーカイブ対象が確定した | 対象確定時に発生             |
| EVT-EF-37 アーカイブ処理が失敗した | 失敗時に発生                 |

## 実装上の注意

- Java `record` 型で不変性を保証する
- `DomainEvent` インターフェースを実装する
- パッケージ配置: `undecided.electronicForm.storageArchive.spi.archiveProcess.event.ArchiveProcessCompleted`
