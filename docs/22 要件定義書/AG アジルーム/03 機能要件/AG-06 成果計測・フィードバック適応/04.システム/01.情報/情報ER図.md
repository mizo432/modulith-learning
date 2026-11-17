# 情報ER図

成果計測・フィードバックによる適応業務領域（AG-06）のER図をPlantUMLで定義します。

## 英語版

```plantuml
@startuml
title AG-06 Information ER Diagram

entity "Sprint Review" as SprintReview {
  *id : Long <<not null>>
  sprintId : Long
  title : String
  goal : String
  goalStatus : Enum
  completedStories : Integer
  plannedStories : Integer
  status : Enum
  participants : String
  agenda : String
  decisions : String
  unresolvedItems : String
  createdAt : DateTime
  createdBy : String
  updatedAt : DateTime
  updatedBy : String
}

entity "Feedback" as Feedback {
  *id : Long <<not null>>
  sprintReviewId : Long
  title : String
  content : String
  type : Enum
  source : String
  status : Enum
  decision : String
  reason : String
  createdAt : DateTime
  createdBy : String
  updatedAt : DateTime
  updatedBy : String
}

entity "Retrospective" as Retrospective {
  *id : Long <<not null>>
  sprintId : Long
  title : String
  wentWell : String
  toImprove : String
  learnings : String
  decisions : String
  createdAt : DateTime
  createdBy : String
  updatedAt : DateTime
  updatedBy : String
}

entity "Improvement Action" as ImprovementAction {
  *id : Long <<not null>>
  retrospectiveId : Long
  title : String
  content : String
  category : Enum
  assignee : String
  deadline : Date
  status : Enum
  effect : String
  createdAt : DateTime
  createdBy : String
  updatedAt : DateTime
  updatedBy : String
}

entity "Outcome Metric" as OutcomeMetric {
  *id : Long <<not null>>
  name : String
  definition : String
  baselineValue : Double
  targetValue : Double
  unit : String
  dataSource : String
  updateFrequency : Enum
  createdAt : DateTime
  createdBy : String
  updatedAt : DateTime
  updatedBy : String
}

entity "Metric Measurement" as MetricMeasurement {
  *id : Long <<not null>>
  metricId : Long
  value : Double
  measuredAt : DateTime
  measuredBy : String
}

entity "Risk" as Risk {
  *id : Long <<not null>>
  title : String
  content : String
  impact : String
  assignee : String
  status : Enum
  createdAt : DateTime
  createdBy : String
  updatedAt : DateTime
  updatedBy : String
}

entity "Pending Decision" as PendingDecision {
  *id : Long <<not null>>
  title : String
  content : String
  impact : String
  assignee : String
  status : Enum
  createdAt : DateTime
  createdBy : String
  updatedAt : DateTime
  updatedBy : String
}

SprintReview ||--o{ Feedback : has
Retrospective ||--o{ ImprovementAction : has
OutcomeMetric ||--o{ MetricMeasurement : has
@enduml
```

## 日本語版

```plantuml
@startuml
title AG-06 情報ER図

entity "Sprint Review" as SprintReview {
  *id : Long <<not null>>
  sprintId : Long
  title : String
  goal : String
  goalStatus : Enum
  status : Enum
  decisions : String
  createdAt : DateTime
  createdBy : String
}

entity "フィードバック" as Feedback {
  *id : Long <<not null>>
  sprintReviewId : Long
  title : String
  content : String
  type : Enum
  status : Enum
  decision : String
  createdAt : DateTime
  createdBy : String
}

entity "Retrospective" as Retrospective {
  *id : Long <<not null>>
  sprintId : Long
  title : String
  wentWell : String
  toImprove : String
  learnings : String
  createdAt : DateTime
  createdBy : String
}

entity "改善アクション" as ImprovementAction {
  *id : Long <<not null>>
  retrospectiveId : Long
  title : String
  content : String
  category : Enum
  assignee : String
  status : Enum
  effect : String
  createdAt : DateTime
  createdBy : String
}

entity "成果指標" as OutcomeMetric {
  *id : Long <<not null>>
  name : String
  definition : String
  baselineValue : Double
  targetValue : Double
  createdAt : DateTime
  createdBy : String
}

entity "指標測定値" as MetricMeasurement {
  *id : Long <<not null>>
  metricId : Long
  value : Double
  measuredAt : DateTime
}

entity "リスク" as Risk {
  *id : Long <<not null>>
  title : String
  status : Enum
  createdAt : DateTime
}

entity "判断待ち" as PendingDecision {
  *id : Long <<not null>>
  title : String
  status : Enum
  createdAt : DateTime
}

SprintReview ||--o{ Feedback : 関連
Retrospective ||--o{ ImprovementAction : 関連
OutcomeMetric ||--o{ MetricMeasurement : 関連
@enduml
```

## 情報一覧

| 番号 | 情報名 | 情報種類 |
|------|--------|----------|
| 1 | Sprint Review | リソース |
| 2 | フィードバック | リソース |
| 3 | フィードバック対応 | 対応表 |
| 4 | Retrospective | リソース |
| 5 | 改善アクション | リソース |
| 6 | リリース履歴 | リソース |
| 7 | 成果指標 | リソース |
| 8 | 指標測定値 | イベント |
| 9 | リスク | リソース |
| 10 | 判断待ち | リソース |
| 11 | ダッシュボード設定 | リソース |
| 12 | レポート出力履歴 | イベント |
| 13 | フィードバック種別 | 対照表 |
| 14 | 改善カテゴリ | 対照表 |

## 関係一覧

| 源情報 | 対象情報 | 関係 | 多重度 |
|--------|----------|------|--------|
| Sprint Review | フィードバック | 1:N | 1つのReviewに複数のフィードバック |
| Retrospective | 改善アクション | 1:N | 1つの振り返りに複数の改善アクション |
| 成果指標 | 指標測定値 | 1:N | 1つの指標に複数の測定値 |
| フィードバック | フィードバック対応 | 1:N | 1つのフィードバックに複数の対応 |
