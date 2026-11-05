# DDDモデル候補

## 概要

本ファイルは、EF-04 保管・アーカイブのDDD（Domain-Driven Design）モデル候補を定義します。

## サブドメイン候補

| 項番 | サブドメイン名 | 種類 | 説明 |
|------|---------------|------|------|
| 01 | 保存期間管理 | コア | 帳票種類ごとの保存期間管理 |
| 02 | 廃棄管理 | コア | 保存期間満了後の帳票廃棄管理 |
| 03 | アーカイブポリシー | コア | アーカイブ対象・方法・スケジュールの管理 |
| 04 | アーカイブ処理 | コア | アーカイブ処理の実行・監視 |
| 05 | 帳票自動復元 | 支援 | アーカイブ帳票の自動復元管理 |

## アグリゲート候補

| 項番 | アグリゲート名 | 集約ルート | 説明 |
|------|---------------|-----------|------|
| 01 | RetentionPeriod | RetentionPeriod | 保存期間の集約 |
| 02 | Disposal | Disposal | 廃棄の集約 |
| 03 | ArchivePolicy | ArchivePolicy | アーカイブポリシーの集約 |
| 04 | ArchiveTarget | ArchiveTarget | アーカイブ対象の集約 |
| 05 | ArchiveProcess | ArchiveProcess | アーカイブ処理の集約 |
| 06 | FormRestore | FormRestore | 帳票自動復元の集約 |

## エンティティ候補

| 項番 | エンティティ名 | アグリゲート | 説明 |
|------|---------------|-------------|------|
| 01 | RetentionPeriod | RetentionPeriod | 保存期間 |
| 02 | Disposal | Disposal | 廃棄 |
| 03 | ArchivePolicy | ArchivePolicy | アーカイブポリシー |
| 04 | ArchiveTarget | ArchiveTarget | アーカイブ対象 |
| 05 | ArchiveProcess | ArchiveProcess | アーカイブ処理 |
| 06 | FormRestore | FormRestore | 帳票自動復元 |

## 値オブジェクト候補

| 項番 | 値オブジェクト名 | 使用箇所 | 説明 |
|------|-----------------|---------|------|
| 01 | RetentionId | RetentionPeriod | 保存期間識別子 |
| 02 | RetentionYears | RetentionPeriod | 保存年数 |
| 03 | ExpiryDate | RetentionPeriod | 期限日 |
| 04 | DisposalId | Disposal | 廃棄識別子 |
| 05 | DisposalReason | Disposal | 廃棄理由 |
| 06 | PolicyId | ArchivePolicy | ポリシー識別子 |
| 07 | ArchiveMethod | ArchivePolicy | アーカイブ方法 |
| 08 | ScheduleCron | ArchivePolicy | スケジュール |
| 09 | TargetId | ArchiveTarget | 対象識別子 |
| 10 | ArchiveStatus | ArchiveTarget | アーカイブステータス |
| 11 | ProcessId | ArchiveProcess | 処理識別子 |
| 12 | ProcessStatus | ArchiveProcess | 処理ステータス |
| 13 | RestoreId | FormRestore | 復元識別子 |
| 14 | RestoreStatus | FormRestore | 復元ステータス |

## ドメインサービス候補

| 項番 | サービス名 | 説明 |
|------|-----------|------|
| 01 | RetentionManagementService | 保存期間管理ロジック |
| 02 | ArchiveExecutionService | アーカイブ処理実行ロジック |
| 03 | AutoRestoreService | 自動復元ロジック |

## ドメインイベント候補

| 項番 | イベント名 | 発生元 | 説明 |
|------|-----------|--------|------|
| 01 | RetentionPeriodSet | RetentionPeriod | 保存期間が設定された |
| 02 | FormDisposed | Disposal | 帳票が廃棄された |
| 03 | ArchiveProcessCompleted | ArchiveProcess | アーカイブ処理が完了した |
| 04 | FormRestoreRequested | FormRestore | 帳票復元が要求された |
