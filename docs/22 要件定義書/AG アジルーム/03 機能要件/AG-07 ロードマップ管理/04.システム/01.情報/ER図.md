# ER図

ロードマップ管理業務領域（AG-07）のエンティティ関係図をPlantUMLで定義します。

## 英語版

```plantuml
@startuml
skinparam linetype ortho

entity Roadmap {
  *id : Long <<not null>>
  name : String
  code : String
  type : Enum
  createdAt : DateTime
  updatedAt : DateTime
}

entity RoadmapFeature {
  *id : Long <<not null>>
  roadmapId : Long
  featureId : Long
  visible : Boolean
}

entity WorkPeriod {
  *id : Long <<not null>>
  roadmapFeatureId : Long
  startDate : Date
  endDate : Date
}

entity RoadmapEvent {
  *id : Long <<not null>>
  name : String
  date : Date
  shape : Enum
  scope : Enum
  featureId : Long
  createdAt : DateTime
}

entity Feature {
  *id : Long <<not null>>
  name : String
  active : Boolean
}

entity Sprint {
  *id : Long <<not null>>
  name : String
  startDate : Date
  endDate : Date
}

entity Release {
  *id : Long <<not null>>
  name : String
  startDate : Date
  endDate : Date
}

entity Story {
  *id : Long <<not null>>
  featureId : Long
  sprintId : Long
  storyPoints : Integer
  completed : Boolean
}

Roadmap ||--o{ RoadmapFeature : contains
RoadmapFeature ||--o{ WorkPeriod : has
Feature }o--o{ RoadmapFeature : mapped
Feature ||--o{ Story : has
Sprint }o--o{ Story : includes
RoadmapEvent }o--o| Feature : relates to
@enduml
```

## 日本語版

```plantuml
@startuml
skinparam linetype ortho

entity ロードマップ {
  *ID : Long <<not null>>
  名前 : String
  コード : String
  種類 : Enum
  作成日時 : DateTime
  更新日時 : DateTime
}

entity ロードマップ機能 {
  *ID : Long <<not null>>
  ロードマップID : Long
  機能ID : Long
  表示フラグ : Boolean
}

entity 作業期間 {
  *ID : Long <<not null>>
  ロードマップ機能ID : Long
  開始日 : Date
  終了日 : Date
}

entity ロードマップイベント {
  *ID : Long <<not null>>
  名前 : String
  日程 : Date
  形状 : Enum
  範囲 : Enum
  機能ID : Long
  作成日時 : DateTime
}

entity 機能 {
  *ID : Long <<not null>>
  名前 : String
  アクティブフラグ : Boolean
}

entity スプリント {
  *ID : Long <<not null>>
  名前 : String
  開始日 : Date
  終了日 : Date
}

entity リリース {
  *ID : Long <<not null>>
  名前 : String
  開始日 : Date
  終了日 : Date
}

entity ストーリー {
  *ID : Long <<not null>>
  機能ID : Long
  スプリントID : Long
  ストーリーポイント : Integer
  完了フラグ : Boolean
}

ロードマップ ||--o{ ロードマップ機能 : 包含
ロードマップ機能 ||--o{ 作業期間 : 保有
機能 }o--o{ ロードマップ機能 : 対応
機能 ||--o{ ストーリー : 包含
スプリント }o--o{ ストーリー : 包含
ロードマップイベント }o--o| 機能 : 関連
@enduml
```

## 情報一覧

| 情報ID | 情報名 | 主キー | 主要属性 |
|--------|--------|--------|----------|
| INF-RM-01 | ロードマップ | id | name, code, type, createdAt, updatedAt |
| INF-RM-03 | ロードマップ機能 | id | roadmapId, featureId, visible |
| INF-RM-04 | 作業期間 | id | roadmapFeatureId, startDate, endDate |
| INF-RM-05 | ロードマップイベント | id | name, date, shape, scope, featureId |
| INF-RM-07 | 機能 | id | name, active |
| INF-RM-08 | スプリント | id | name, startDate, endDate |
| INF-RM-09 | リリース | id | name, startDate, endDate |
| INF-RM-10 | ストーリー | id | featureId, sprintId, storyPoints, completed |

## 関係一覧

| 親エンティティ | 子エンティティ | 関係 | 種類 |
|----------------|----------------|------|------|
| ロードマップ | ロードマップ機能 | 1対多 | 包含 |
| ロードマップ機能 | 作業期間 | 1対多 | 保有 |
| 機能 | ロードマップ機能 | 多対多 | 対応 |
| 機能 | ストーリー | 1対多 | 包含 |
| スプリント | ストーリー | 1対多 | 包含 |
| ロードマップイベント | 機能 | 多対1 | 関連 |
