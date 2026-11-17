# 情報ER図

プロダクトディスカバリー業務領域で扱う情報の関係を示すER図です。

## 1. 目的

本書は、AG-01 プロダクトディスカバリー業務領域のシステムレイヤーで扱う情報エンティティ間の関係を示すER図です。

## 2. PlantUML ER図

```plantuml
@startuml
title AG-01 プロダクトディスカバリー 情報ER図
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "ProductGoal" as ProductGoal {
  * product_goal_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
}

entity "SuccessMetric" as SuccessMetric {
  * metric_id : SnowflakeID <<PK>>
  --
  product_goal_id : SnowflakeID <<FK>>
}

entity "UserProblem" as UserProblem {
  * problem_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  reported_by : UUID <<FK Employee>>
}

entity "Feedback" as Feedback {
  * feedback_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  submitted_by : UUID <<FK Employee>>
  problem_id : SnowflakeID <<FK>>
}

' ProductGoal defines SuccessMetrics
ProductGoal ||--o{ SuccessMetric : measures

' UserProblem informs Feedback
UserProblem ||--o{ Feedback : informs

@enduml
```

## 3. 日本語版 ER図

```plantuml
@startuml
title AG-01 プロダクトディスカバリー 情報ER図（日本語版）
hide circle
skinparam linetype ortho
skinparam shadowing false
skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam packageStyle rectangle

entity "プロダクト目標" as ProductGoal {
  * product_goal_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
}

entity "成功指標" as SuccessMetric {
  * metric_id : SnowflakeID <<PK>>
  --
  product_goal_id : SnowflakeID <<FK>>
}

entity "課題" as UserProblem {
  * problem_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  reported_by : UUID <<FK Employee>>
}

entity "フィードバック" as Feedback {
  * feedback_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  submitted_by : UUID <<FK Employee>>
  problem_id : SnowflakeID <<FK>>
}

' プロダクト目標から成功指標へ
プロダクト目標 ||--o{ 成功指標 : 測定

' 課題からフィードバックへ
課題 ||--o{ フィードバック : 情報提供

@enduml
```

## 4. エンティティ一覧

| エンティティ  | 情報名 (ID)               | 説明                                   |
|---------------|---------------------------|----------------------------------------|
| ProductGoal   | Product Goal (INF-DIS-02) | プロダクト目標、期間、状態             |
| SuccessMetric | 成功指標 (INF-DIS-09)     | 指標の定義、基準値、目標値             |
| UserProblem   | 課題 (INF-DIS-03)         | 業務上の困りごとや繰り返し発生する問題 |
| Feedback      | 要望 (INF-DIS-04)         | 顧客や利用者から受けた意見             |

## 5. 主要な関係

- **ProductGoal → SuccessMetric**：目標に対する成功指標の定義（Product Goalと成功指標: INF-DIS-12）
- **UserProblem → Feedback**：課題に対するフィードバック

## 6. 参照エンティティ

以下のエンティティは他の業務領域で定義され、参照として使用されます。

| エンティティ | 所属業務領域 | 説明         |
|--------------|--------------|--------------|
| Project      | 共通         | プロジェクト |
| Employee     | 共通         | 従業員       |
