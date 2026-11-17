# 情報ER図

## 概要

本ファイルは、EF-04 保管・アーカイブの情報エンティティ間の関係をER図で定義します。

## ER図（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "RetentionPeriod\nINF-EF-19" as RetentionPeriod {
  * retention_id : String
  * form_type : String
  * retention_years : Integer
  * start_date : LocalDate
  * end_date : LocalDate
  * extension_condition : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "Disposal\nINF-EF-20" as Disposal {
  * disposal_id : String
  * form_id : String
  * disposal_date : LocalDateTime
  * disposed_by : String
  * disposal_reason : String
  * status : String
  --
  created_at : LocalDateTime
}

entity "ArchivePolicy\nINF-EF-21" as ArchivePolicy {
  * policy_id : String
  * policy_name : String
  * form_type : String
  * archive_method : String
  * schedule_cron : String
  * status : String
  --
  created_at : LocalDateTime
  updated_at : LocalDateTime
}

entity "ArchiveTarget\nINF-EF-22" as ArchiveTarget {
  * target_id : String
  * form_id : String
  * policy_id : String
  * archive_status : String
  * scheduled_at : LocalDateTime
  --
  created_at : LocalDateTime
}

entity "ArchiveProcess\nINF-EF-23" as ArchiveProcess {
  * process_id : String
  * target_id : String
  * process_status : String
  * started_at : LocalDateTime
  * completed_at : LocalDateTime
  * error_message : String
  --
  created_at : LocalDateTime
}

entity "FormRestore\nINF-EF-24" as FormRestore {
  * restore_id : String
  * form_id : String
  * requested_by : String
  * restore_status : String
  * requested_at : LocalDateTime
  * restored_at : LocalDateTime
  --
  created_at : LocalDateTime
}

RetentionPeriod "1" -- "0..*" Disposal : defines >
ArchivePolicy "1" -- "0..*" ArchiveTarget : applies >
ArchiveTarget "1" -- "0..*" ArchiveProcess : has >
ArchiveProcess "1" -- "0..*" FormRestore : triggers >

@enduml
```

## ER図（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "保存期間\nINF-EF-19" as RetentionPeriod {
  * 保存期間ID : String
  * 帳票種類 : String
  * 保存年数 : Integer
  * 開始日 : LocalDate
  * 終了日 : LocalDate
  * 延長条件 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "廃棄\nINF-EF-20" as Disposal {
  * 廃棄ID : String
  * 帳票ID : String
  * 廃棄日時 : LocalDateTime
  * 廃棄者 : String
  * 廃棄理由 : String
  * 状態 : String
  --
  作成日時 : LocalDateTime
}

entity "アーカイブポリシー\nINF-EF-21" as ArchivePolicy {
  * ポリシーID : String
  * ポリシー名 : String
  * 帳票種類 : String
  * アーカイブ方法 : String
  * スケジュール : String
  * 状態 : String
  --
  作成日時 : LocalDateTime
  更新日時 : LocalDateTime
}

entity "アーカイブ対象\nINF-EF-22" as ArchiveTarget {
  * 対象ID : String
  * 帳票ID : String
  * ポリシーID : String
  * アーカイブステータス : String
  * 予定日時 : LocalDateTime
  --
  作成日時 : LocalDateTime
}

entity "アーカイブ処理\nINF-EF-23" as ArchiveProcess {
  * 処理ID : String
  * 対象ID : String
  * 処理ステータス : String
  * 開始日時 : LocalDateTime
  * 完了日時 : LocalDateTime
  * エラーメッセージ : String
  --
  作成日時 : LocalDateTime
}

entity "帳票自動復元\nINF-EF-24" as FormRestore {
  * 復元ID : String
  * 帳票ID : String
  * 要求者 : String
  * 復元ステータス : String
  * 要求日時 : LocalDateTime
  * 復元日時 : LocalDateTime
  --
  作成日時 : LocalDateTime
}

RetentionPeriod "1" -- "0..*" Disposal : 定義 >
ArchivePolicy "1" -- "0..*" ArchiveTarget : 適用 >
ArchiveTarget "1" -- "0..*" ArchiveProcess : 保有 >
ArchiveProcess "1" -- "0..*" FormRestore : 誘発 >

@enduml
```

## 情報エンティティ一覧

| 情報ID | 情報名 | 主要属性 | 関連エンティティ |
|--------|--------|----------|-----------------|
| INF-EF-19 | 保存期間 | retention_id, form_type, retention_years, end_date | 廃棄 |
| INF-EF-20 | 廃棄 | disposal_id, form_id, disposal_date, disposed_by | 保存期間 |
| INF-EF-21 | アーカイブポリシー | policy_id, policy_name, archive_method, schedule_cron | アーカイブ対象 |
| INF-EF-22 | アーカイブ対象 | target_id, form_id, policy_id, archive_status | アーカイブポリシー, アーカイブ処理 |
| INF-EF-23 | アーカイブ処理 | process_id, target_id, process_status, started_at | アーカイブ対象, 帳票自動復元 |
| INF-EF-24 | 帳票自動復元 | restore_id, form_id, requested_by, restore_status | アーカイブ処理 |

## 関係一覧

| 源エンティティ | 関係 | 対象エンティティ | 多重度 | 説明 |
|---------------|------|-----------------|--------|------|
| 保存期間 | 定義 | 廃棄 | 1 : 0..* | 保存期間満了で廃棄対象となる |
| アーカイブポリシー | 適用 | アーカイブ対象 | 1 : 0..* | ポリシーに基づいてアーカイブ対象を決定 |
| アーカイブ対象 | 保有 | アーカイブ処理 | 1 : 0..* | 対象ごとにアーカイブ処理を実行 |
| アーカイブ処理 | 誘発 | 帳票自動復元 | 1 : 0..* | アーカイブ後の復元要求を処理 |
