# 情報ER図

要求・プロダクトバックログ管理業務領域で扱う情報の関係を示すER図です。

## 1. 目的

本書は、AG-BACKLOG 要求バックログ管理業務領域のシステムレイヤーで扱う情報エンティティ間の関係を示すER図です。

## 2. PlantUML ER図

```plantuml
@startuml
title AG-BACKLOG 要求バックログ管理 情報ER図
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "BacklogItem" as BacklogItem {
  * backlog_item_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  product_goal_id : SnowflakeID <<FK>>
  parent_item_id : SnowflakeID <<FK>>
}

entity "Feedback" as Feedback {
  * feedback_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  submitted_by : UUID <<FK Employee>>
  problem_id : SnowflakeID <<FK>>
}

entity "DecisionRecord" as DecisionRecord {
  * decision_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
  decided_by : UUID <<FK Employee>>
}

entity "UserProblem" as UserProblem {
  * problem_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  reported_by : UUID <<FK Employee>>
}

' BacklogItem self-referencing hierarchy
BacklogItem ||--o{ BacklogItem : decomposes

' Feedback relates to UserProblem
UserProblem ||--o{ Feedback : informs

' Feedback relates to BacklogItem (via project)
BacklogItem ||--o{ Feedback : receives

' DecisionRecord concerns BacklogItem
BacklogItem ||--o{ DecisionRecord : concerns

@enduml
```

## 3. 日本語版 ER図

```plantuml
@startuml
title AG-BACKLOG 要求バックログ管理 情報ER図（日本語版）
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "バックログ項目" as BacklogItem {
  * backlog_item_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  product_goal_id : SnowflakeID <<FK>>
  parent_item_id : SnowflakeID <<FK>>
}

entity "フィードバック" as Feedback {
  * feedback_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  submitted_by : UUID <<FK Employee>>
  problem_id : SnowflakeID <<FK>>
}

entity "意思決定" as DecisionRecord {
  * decision_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : SnowflakeID <<FK>>
  decided_by : UUID <<FK Employee>>
}

entity "課題" as UserProblem {
  * problem_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  reported_by : UUID <<FK Employee>>
}

' バックログ項目の自己参照階層
バックログ項目 ||--o{ バックログ項目 : 分解

' 課題からフィードバックへ
課題 ||--o{ フィードバック : 情報提供

' バックログ項目からフィードバックへ
バックログ項目 ||--o{ フィードバック : 受信

' バックログ項目から意思決定へ
バックログ項目 ||--o{ 意思決定 : 関連

@enduml
```

## 4. エンティティ一覧

| エンティティ   | 情報名 (ID)                | 説明                                   |
|----------------|----------------------------|----------------------------------------|
| BacklogItem    | バックログ項目 (INF-BL-01) | Epic、Feature、Story、欠陥などの項目   |
| Feedback       | フィードバック (INF-BL-03) | 利用者からの意見・評価                 |
| DecisionRecord | 意思決定 (INF-BL-05)       | 決定内容、根拠、決定者                 |
| UserProblem    | 課題 (INF-DIS-03)          | 業務上の困りごとや繰り返し発生する問題 |

## 5. 主要な関係

- **BacklogItem → BacklogItem**：Epic、Feature、Storyの階層関係（バックログ階層: INF-BL-06）
- **BacklogItem → Feedback**：項目に対する利用者からのフィードバック
- **BacklogItem → DecisionRecord**：項目に関する意思決定の記録
- **UserProblem → Feedback**：課題に対するフィードバック

## 6. 参照エンティティ

以下のエンティティは他の業務領域で定義され、参照として使用されます。

| エンティティ | 所属業務領域 | 説明           |
|--------------|--------------|----------------|
| Project      | 共通         | プロジェクト   |
| ProductGoal  | AG-DISCOVERY | プロダクト目標 |
| Employee     | 共通         | 従業員         |
