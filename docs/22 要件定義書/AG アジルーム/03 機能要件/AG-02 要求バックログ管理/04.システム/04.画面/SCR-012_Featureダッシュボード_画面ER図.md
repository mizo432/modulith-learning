# 画面ER図

SCR-012 Featureダッシュボード画面が参照・更新する情報（エンティティ）間の関係をPlantUMLのER図で視覚化します。

## PlantUML（英語版）

```plantuml
@startuml
skinparam linetype ortho

entity "backlogItem\n(INF-BL-01)" as BacklogItem {
  * id : Long <<not null>>
  name : String
  type : String
  priority : String
  status : String
  assigneeId : Long
  description : String
  createdAt : DateTime
  updatedAt : DateTime
}

entity "backlogHierarchy\n(INF-BL-06)" as BacklogHierarchy {
  * parentId : Long <<not null>>
  * childId : Long <<not null>>
  orderIndex : Integer
}

entity "itemRelation\n(INF-BL-07)" as ItemRelation {
  * sourceId : Long <<not null>>
  * targetId : Long <<not null>>
  relationType : String
}

entity "featureDashboardSummary\n(INF-BL-22)" as FeatureDashboardSummary {
  * featureId : Long <<not null>>
  storyTotalCount : Integer
  storyCompletedCount : Integer
  storyInProgressCount : Integer
  storyPendingCount : Integer
  valueRealizationStatus : String
  valueRealizationScore : Double
  releaseReadinessStatus : String
  releaseReadinessScore : Double
  dependencyCount : Integer
  unresolvedDependencyCount : Integer
  updatedAt : DateTime
}

entity "featureQualityInfo\n(INF-BL-23)" as FeatureQualityInfo {
  * featureId : Long <<not null>>
  testCaseCount : Integer
  passedCaseCount : Integer
  failedCaseCount : Integer
  pendingCaseCount : Integer
  defectCount : Integer
  criticalDefectCount : Integer
  highDefectCount : Integer
  mediumDefectCount : Integer
  lowDefectCount : Integer
  qualityScore : Double
  updatedAt : DateTime
}

entity "featureReleaseInfo\n(INF-BL-24)" as FeatureReleaseInfo {
  * featureId : Long <<not null>>
  plannedReleaseDate : Date
  actualReleaseDate : Date
  releaseName : String
  releaseReadiness : Boolean
  readinessBlockers : String
  includedStoryCount : Integer
  updatedAt : DateTime
}

BacklogItem ||--o{ BacklogHierarchy : "has"
BacklogItem }o--o{ ItemRelation : "related to"
BacklogItem ||--|| FeatureDashboardSummary : "has"
BacklogItem ||--|| FeatureQualityInfo : "has"
BacklogItem ||--|| FeatureReleaseInfo : "has"
@enduml
```

## PlantUML（日本語版）

```plantuml
@startuml
skinparam linetype ortho

entity "バックログ項目\n(INF-BL-01)" as BacklogItem {
  * ID : Long <<not null>>
  名前 : String
  種別 : String
  優先度 : String
  状態 : String
  担当者ID : Long
  説明 : String
  作成日時 : DateTime
  更新日時 : DateTime
}

entity "バックログ階層\n(INF-BL-06)" as BacklogHierarchy {
  * 親ID : Long <<not null>>
  * 子ID : Long <<not null>>
  順序 : Integer
}

entity "項目関連付け\n(INF-BL-07)" as ItemRelation {
  * 元ID : Long <<not null>>
  * 先ID : Long <<not null>>
  関連種別 : String
}

entity "Featureダッシュボード集計\n(INF-BL-22)" as FeatureDashboardSummary {
  * FeatureID : Long <<not null>>
  Story総数 : Integer
  Story完了数 : Integer
  Story進行中数 : Integer
  Story未着手数 : Integer
  価値実現状況 : String
  価値実現スコア : Double
  提供準備状況 : String
  提供準備スコア : Double
  依存関係数 : Integer
  未解決依存数 : Integer
  更新日時 : DateTime
}

entity "Feature品質情報\n(INF-BL-23)" as FeatureQualityInfo {
  * FeatureID : Long <<not null>>
  テストケース総数 : Integer
  合格数 : Integer
  不合格数 : Integer
  未実行数 : Integer
  欠陥総数 : Integer
  重大欠陥数 : Integer
  高欠陥数 : Integer
  中欠陥数 : Integer
  低欠陥数 : Integer
  品質スコア : Double
  更新日時 : DateTime
}

entity "Featureリリース情報\n(INF-BL-24)" as FeatureReleaseInfo {
  * FeatureID : Long <<not null>>
  予定リリース日 : Date
  実際リリース日 : Date
  リリース名 : String
  提供準備完了 : Boolean
  準備阻害要因 : String
  含まれるStory数 : Integer
  更新日時 : DateTime
}

BacklogItem ||--o{ BacklogHierarchy : "持つ"
BacklogItem }o--o{ ItemRelation : "関連する"
BacklogItem ||--|| FeatureDashboardSummary : "持つ"
BacklogItem ||--|| FeatureQualityInfo : "持つ"
BacklogItem ||--|| FeatureReleaseInfo : "持つ"
@enduml
```

## 情報一覧

| 情報ID | 情報名 | 操作種類 | 説明 |
|--------|--------|----------|------|
| INF-BL-01 | バックログ項目 | 参照 | Feature、Storyなどのバックログ項目 |
| INF-BL-06 | バックログ階層 | 参照 | FeatureとStoryの階層関係 |
| INF-BL-07 | 項目関連付け | 参照 | Feature間の依存関係 |
| INF-BL-22 | Featureダッシュボード集計 | 参照 | Feature単位の集計情報 |
| INF-BL-23 | Feature品質情報 | 参照 | Featureのテスト結果、欠陥数、品質指標 |
| INF-BL-24 | Featureリリース情報 | 参照 | Featureのリリース予定、準備状況 |

## 関係一覧

| 元情報 | 先情報 | 関係 | 説明 |
|--------|--------|------|------|
| INF-BL-01 | INF-BL-06 | 1対多 | 1つのバックログ項目は複数の階層関係を持つ |
| INF-BL-01 | INF-BL-07 | 多対多 | バックログ項目は複数の関連付けを持つ |
| INF-BL-01 | INF-BL-22 | 1対1 | 1つのFeatureは1つの集計情報を持つ |
| INF-BL-01 | INF-BL-23 | 1対1 | 1つのFeatureは1つの品質情報を持つ |
| INF-BL-01 | INF-BL-24 | 1対1 | 1つのFeatureは1つのリリース情報を持つ |
