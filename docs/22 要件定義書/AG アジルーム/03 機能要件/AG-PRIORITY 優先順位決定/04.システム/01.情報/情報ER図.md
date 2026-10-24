# 情報ER図

価値・要求の優先順位決定業務領域で扱う情報の関係を示すER図です。

## 1. 目的

本書は、AG-PRIORITY 優先順位決定業務領域のシステムレイヤーで扱う情報エンティティ間の関係を示すER図です。

## 2. PlantUML ER図

```plantuml
@startuml
title AG-PRIORITY 優先順位決定 情報ER図
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
  product_goal_id : UUID <<FK>>
  parent_item_id : UUID <<FK>>
}

entity "KanoSurvey" as KanoSurvey {
  * kano_survey_id : SnowflakeID <<PK>>
  --
  backlog_item_id : UUID <<FK>>
  created_by : UUID <<FK Employee>>
}

entity "KanoResponse" as KanoResponse {
  * kano_response_id : SnowflakeID <<PK>>
  --
  kano_survey_id : UUID <<FK>>
  respondent_id : UUID <<FK Employee>>
}

entity "DecisionRecord" as DecisionRecord {
  * decision_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : UUID <<FK>>
  decided_by : UUID <<FK Employee>>
}

' BacklogItem is evaluated by KanoSurvey
BacklogItem ||--o{ KanoSurvey : evaluates

' Employee creates KanoSurvey
KanoSurvey }o--|| Employee : creates

' KanoSurvey collects KanoResponses
KanoSurvey ||--o{ KanoResponse : collects

' Employee responds to KanoSurvey
KanoResponse }o--o| Employee : responds

' DecisionRecord concerns BacklogItem
BacklogItem ||--o{ DecisionRecord : concerns

' Employee decides DecisionRecord
DecisionRecord }o--|| Employee : decides

@enduml
```

## 3. 日本語版 ER図

```plantuml
@startuml
title AG-PRIORITY 優先順位決定 情報ER図（日本語版）
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
  product_goal_id : UUID <<FK>>
  parent_item_id : UUID <<FK>>
}

entity "Kano調査" as KanoSurvey {
  * kano_survey_id : SnowflakeID <<PK>>
  --
  backlog_item_id : UUID <<FK>>
  created_by : UUID <<FK Employee>>
}

entity "Kano回答" as KanoResponse {
  * kano_response_id : SnowflakeID <<PK>>
  --
  kano_survey_id : UUID <<FK>>
  respondent_id : UUID <<FK Employee>>
}

entity "意思決定" as DecisionRecord {
  * decision_id : SnowflakeID <<PK>>
  --
  project_id : UUID <<FK>>
  backlog_item_id : UUID <<FK>>
  decided_by : UUID <<FK Employee>>
}

' バックログ項目からKano調査へ
バックログ項目 ||--o{ Kano調査 : 評価

' Kano調査からKano回答へ
Kano調査 ||--o{ Kano回答 : 収集

' バックログ項目から意思決定へ
バックログ項目 ||--o{ 意思決定 : 関連

@enduml
```

## 4. エンティティ一覧

| エンティティ   | 情報名 (ID)                | 説明                                 |
|----------------|----------------------------|--------------------------------------|
| BacklogItem    | バックログ項目 (INF-BL-01) | Epic、Feature、Story、欠陥などの項目 |
| KanoSurvey     | Kano質問票 (INF-PR-02)     | 充足質問・不充足質問のペア           |
| KanoResponse   | Kano回答 (INF-PR-03)       | 各質問に対する回答                   |
| DecisionRecord | 優先判断理由 (INF-PR-05)   | 優先順位決定の根拠・決定者・影響範囲 |

## 5. 主要な関係

- **BacklogItem → KanoSurvey**：バックログ項目に対するKano調査
- **KanoSurvey → KanoResponse**：調査に対する回答（Kano分類結果: INF-PR-04）
- **BacklogItem → DecisionRecord**：項目の優先順位に関する意思決定
- **Employee → KanoSurvey**：調査の作成者
- **Employee → KanoResponse**：回答者（匿名回答も可）

## 6. 参照エンティティ

以下のエンティティは他の業務領域で定義され、参照として使用されます。

| エンティティ | 所属業務領域 | 説明           |
|--------------|--------------|----------------|
| Project      | 共通         | プロジェクト   |
| ProductGoal  | AG-DISCOVERY | プロダクト目標 |
| Employee     | 共通         | 従業員         |
