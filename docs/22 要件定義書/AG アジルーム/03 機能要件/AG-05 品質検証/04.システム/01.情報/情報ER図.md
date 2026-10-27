# 情報ER図

プロダクト品質・受入・性能検証業務領域で扱う情報の関係を示すER図です。

## 1. 目的

本書は、AG-05 品質検証業務領域のシステムレイヤーで扱う情報エンティティ間の関係を示すER図です。

## 2. PlantUML ER図

```plantuml
@startuml
title AG-05 品質検証 情報ER図
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "DefinitionOfDone" as DefinitionOfDone {
  * definition_id : SnowflakeID <<PK>>
  --
  team_id : UUID <<FK>>
}

entity "TestCase" as TestCase {
  * test_case_id : SnowflakeID <<PK>>
  --
  backlog_item_id : SnowflakeID <<FK>>
}

entity "TestRun" as TestRun {
  * test_run_id : SnowflakeID <<PK>>
  --
  test_case_id : SnowflakeID <<FK>>
  sprint_id : SnowflakeID <<FK>>
  executed_by : UUID <<FK Employee>>
}

entity "Defect" as Defect {
  * defect_id : SnowflakeID <<PK>>
  --
  backlog_item_id : SnowflakeID <<FK>>
  test_run_id : SnowflakeID <<FK>>
}

entity "PerformanceTestRun" as PerformanceTestRun {
  * performance_run_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
  sprint_id : SnowflakeID <<FK>>
}

entity "Risk" as Risk {
  * risk_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
  owner_id : UUID <<FK Employee>>
}

' Team defines DefinitionOfDone
Team ||--o{ DefinitionOfDone : defines

' BacklogItem is verified by TestCases
BacklogItem ||--o{ TestCase : verified by

' TestCase is executed as TestRuns
TestCase ||--o{ TestRun : executed as

' TestRun may reveal Defects
TestRun ||--o{ Defect : may reveal

' Defect relates to BacklogItem
BacklogItem ||--o{ Defect : relates to

' Project measures PerformanceTestRuns
Project ||--o{ PerformanceTestRun : measures

' BacklogItem verifies PerformanceTestRun
BacklogItem ||--o{ PerformanceTestRun : verifies

' Project tracks Risks
Project ||--o{ Risk : tracks

' BacklogItem may have Risks
BacklogItem ||--o{ Risk : may have

@enduml
```

## 3. 日本語版 ER図

```plantuml
@startuml
title AG-05 品質検証 情報ER図（日本語版）
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "完成基準" as DefinitionOfDone {
  * definition_id : SnowflakeID <<PK>>
  --
  team_id : UUID <<FK>>
}

entity "テストケース" as TestCase {
  * test_case_id : SnowflakeID <<PK>>
  --
  backlog_item_id : SnowflakeID <<FK>>
}

entity "テスト実行" as TestRun {
  * test_run_id : SnowflakeID <<PK>>
  --
  test_case_id : SnowflakeID <<FK>>
  sprint_id : SnowflakeID <<FK>>
  executed_by : UUID <<FK Employee>>
}

entity "欠陥" as Defect {
  * defect_id : SnowflakeID <<PK>>
  --
  backlog_item_id : SnowflakeID <<FK>>
  test_run_id : SnowflakeID <<FK>>
}

entity "性能テスト実行" as PerformanceTestRun {
  * performance_run_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
  sprint_id : SnowflakeID <<FK>>
}

entity "リスク" as Risk {
  * risk_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
  owner_id : UUID <<FK Employee>>
}

' チームから完成基準へ
チーム ||--o{ 完成基準 : 定義

' バックログ項目からテストケースへ
バックログ項目 ||--o{ テストケース : 検証

' テストケースからテスト実行へ
テストケース ||--o{ テスト実行 : 実行

' テスト実行から欠陥へ
テスト実行 ||--o{ 欠陥 : 発見

' プロジェクトから性能テスト実行へ
プロジェクト ||--o{ 性能テスト実行 : 測定

' プロジェクトからリスクへ
プロジェクト ||--o{ リスク : 管理

@enduml
```

## 4. エンティティ一覧

| エンティティ       | 情報名 (ID)                          | 説明                                   |
|--------------------|--------------------------------------|----------------------------------------|
| DefinitionOfDone   | Definition of Done (INF-QL-01)       | チームの完成基準                       |
| TestCase           | テストケース (INF-QL-02)             | 要求や受入条件に関連づけたテストケース |
| TestRun            | テスト実行結果 (INF-QL-03)           | 手動・自動テストの実行結果             |
| Defect             | 欠陥 (INF-QL-04)                     | 再現手順、影響度、状態                 |
| PerformanceTestRun | 性能テスト結果 (INF-QL-06)           | 性能テストの実行結果                   |
| Risk               | セキュリティ・品質リスク (INF-QL-07) | 品質・セキュリティ上のリスクと対応     |

## 5. 主要な関係

- **Team → DefinitionOfDone**：チームが合意した完成基準（版管理）
- **BacklogItem → TestCase**：バックログ項目に対するテストケース（テストケースと項目: INF-QL-08）
- **TestCase → TestRun**：テストケースの実行結果
- **TestRun → Defect**：テスト実行で見つかった欠陥（欠陥と項目: INF-QL-09）
- **BacklogItem → Defect**：項目に関連する欠陥
- **Project → PerformanceTestRun**：プロジェクトの性能テスト
- **Project → Risk**：プロジェクトのリスク管理

## 6. 参照エンティティ

以下のエンティティは他の業務領域で定義され、参照として使用されます。

| エンティティ | 所属業務領域 | 説明           |
|--------------|--------------|----------------|
| Team         | 共通         | 開発チーム     |
| BacklogItem  | AG-02        | バックログ項目 |
| Sprint       | AG-04        | スプリント     |
| Employee     | 共通         | 従業員         |
| Project      | 共通         | プロジェクト   |
