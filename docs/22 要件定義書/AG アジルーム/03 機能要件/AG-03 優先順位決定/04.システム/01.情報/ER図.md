# ER図

優先順位決定業務領域で扱う情報（エンティティ）間の関係を示すER図です。

## 1. 目的

本書は、AG-03 優先順位決定業務領域のシステムレイヤーで扱う情報（エンティティ）間の関係を示すER図（エンティティ関係図）です。

## 2. PlantUML ER図（英語版）

```plantuml
@startuml
title [Priority Decision] ER Diagram

skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

entity "EvaluationCriteria" as EvaluationCriteria {
  * id : STRING <<not null>>
  --
  itemName : STRING
  weight : DOUBLE
  definedBy : STRING
  definedAt : TIMESTAMP
  updatedAt : TIMESTAMP
}

entity "PriorityScore" as PriorityScore {
  * id : STRING <<not null>>
  --
  requirementId : STRING
  valueScore : INTEGER
  kanoCategory : STRING
  riskLevel : STRING
  costEstimate : INTEGER
  priority : INTEGER
  calculatedAt : TIMESTAMP
  decidedAt : TIMESTAMP
}

entity "KanoClassification" as KanoClassification {
  * id : STRING <<not null>>
  --
  requirementId : STRING
  kanoCategory : STRING
  evaluatedBy : STRING
  evaluatedAt : TIMESTAMP
}

entity "RiskAssessment" as RiskAssessment {
  * id : STRING <<not null>>
  --
  requirementId : STRING
  riskDescription : STRING
  probability : DOUBLE
  impact : DOUBLE
  riskLevel : STRING
  identifiedBy : STRING
  identifiedAt : TIMESTAMP
}

entity "CostEstimate" as CostEstimate {
  * id : STRING <<not null>>
  --
  requirementId : STRING
  implementationCost : INTEGER
  maintenanceCost : INTEGER
  estimatedBy : STRING
  estimatedAt : TIMESTAMP
}

entity "InvestmentDecision" as InvestmentDecision {
  * id : STRING <<not null>>
  --
  requirementId : STRING
  decisionType : STRING
  reason : STRING
  decidedBy : STRING
  decidedAt : TIMESTAMP
}

entity "Dependency" as Dependency {
  * id : STRING <<not null>>
  --
  requirementId : STRING
  dependencyId : STRING
  dependencyType : STRING
  definedBy : STRING
  definedAt : TIMESTAMP
}

entity "Countermeasure" as Countermeasure {
  * id : STRING <<not null>>
  --
  riskId : STRING
  description : STRING
  assignee : STRING
  definedAt : TIMESTAMP
  completedAt : TIMESTAMP
}

entity "ChangeHistory" as ChangeHistory {
  * id : STRING <<not null>>
  --
  scoreId : STRING
  beforePriority : INTEGER
  afterPriority : INTEGER
  reason : STRING
  changedBy : STRING
  changedAt : TIMESTAMP
}

entity "KanoCategory" as KanoCategory {
  * code : STRING <<not null>>
  --
  name : STRING
  description : STRING
}

entity "RiskLevel" as RiskLevel {
  * code : STRING <<not null>>
  --
  name : STRING
  description : STRING
}

entity "PriorityInvestmentMapping" as PriorityInvestmentMapping {
  * id : STRING <<not null>>
  --
  scoreId : STRING
  decisionId : STRING
  mappedAt : TIMESTAMP
}

EvaluationCriteria ||--o{ PriorityScore : used for
KanoClassification ||--o{ PriorityScore : included in
RiskAssessment ||--o{ PriorityScore : included in
CostEstimate ||--o{ PriorityScore : included in
PriorityScore ||--o{ InvestmentDecision : leads to
PriorityScore ||--o{ ChangeHistory : has
RiskAssessment ||--o{ Countermeasure : has
KanoCategory ||--o{ KanoClassification : referenced by
RiskLevel ||--o{ RiskAssessment : referenced by
PriorityScore ||--o{ PriorityInvestmentMapping : mapped to
InvestmentDecision ||--o{ PriorityInvestmentMapping : mapped to
@enduml
```

## 3. 日本語版 ER図

```plantuml
@startuml
title 優先順位決定 ER図（日本語版）

skinparam entity {
  BackgroundColor #F8FAFC
  BorderColor #475569
  FontColor #0F172A
}
skinparam arrowColor #475569
skinparam shadowing false

entity "評価基準" as EvaluationCriteria {
  * 評価基準ID : STRING <<not null>>
  --
  評価項目名 : STRING
  重み付け : DOUBLE
  定義者 : STRING
  定義日時 : TIMESTAMP
  更新日時 : TIMESTAMP
}

entity "優先順位スコア" as PriorityScore {
  * スコアID : STRING <<not null>>
  --
  要求項目ID : STRING
  価値スコア : INTEGER
  Kanoカテゴリ : STRING
  リスクレベル : STRING
  コスト見積り : INTEGER
  優先順位 : INTEGER
  計算日時 : TIMESTAMP
  決定日時 : TIMESTAMP
}

entity "Kano分類結果" as KanoClassification {
  * Kano結果ID : STRING <<not null>>
  --
  要求項目ID : STRING
  Kanoカテゴリ : STRING
  評価者 : STRING
  評価日時 : TIMESTAMP
}

entity "リスク評価結果" as RiskAssessment {
  * リスクID : STRING <<not null>>
  --
  要求項目ID : STRING
  リスク内容 : STRING
  発生確率 : DOUBLE
  影響度 : DOUBLE
  リスクレベル : STRING
  特定者 : STRING
  特定日時 : TIMESTAMP
}

entity "コスト見積り" as CostEstimate {
  * コストID : STRING <<not null>>
  --
  要求項目ID : STRING
  実装コスト : INTEGER
  維持コスト : INTEGER
  見積もり者 : STRING
  見積もり日時 : TIMESTAMP
}

entity "投資判断" as InvestmentDecision {
  * 投資判断ID : STRING <<not null>>
  --
  要求項目ID : STRING
  投資判断種別 : STRING
  判断理由 : STRING
  判断者 : STRING
  判断日時 : TIMESTAMP
}

entity "依存関係" as Dependency {
  * 依存関係ID : STRING <<not null>>
  --
  要求項目ID : STRING
  依存先ID : STRING
  依存関係種別 : STRING
  定義者 : STRING
  定義日時 : TIMESTAMP
}

entity "対応策" as Countermeasure {
  * 対応策ID : STRING <<not null>>
  --
  リスクID : STRING
  対応策内容 : STRING
  担当者 : STRING
  定義日時 : TIMESTAMP
  完了日時 : TIMESTAMP
}

entity "優先順位変更履歴" as ChangeHistory {
  * 変更履歴ID : STRING <<not null>>
  --
  スコアID : STRING
  変更前優先順位 : INTEGER
  変更後優先順位 : INTEGER
  変更理由 : STRING
  変更者 : STRING
  変更日時 : TIMESTAMP
}

entity "Kanoカテゴリ" as KanoCategory {
  * カテゴリコード : STRING <<not null>>
  --
  カテゴリ名 : STRING
  説明 : STRING
}

entity "リスクレベル" as RiskLevel {
  * レベルコード : STRING <<not null>>
  --
  レベル名 : STRING
  説明 : STRING
}

entity "優先順位と投資判断" as PriorityInvestmentMapping {
  * 関連ID : STRING <<not null>>
  --
  スコアID : STRING
  投資判断ID : STRING
  関連日時 : TIMESTAMP
}

評価基準 ||--o{ 優先順位スコア : 使用
Kano分類結果 ||--o{ 優先順位スコア : 含まれる
リスク評価結果 ||--o{ 優先順位スコア : 含まれる
コスト見積り ||--o{ 優先順位スコア : 含まれる
優先順位スコア ||--o{ 投資判断 : 導く
優先順位スコア ||--o{ 優先順位変更履歴 : 保有
リスク評価結果 ||--o{ 対応策 : 保有
Kanoカテゴリ ||--o{ Kano分類結果 : 参照
リスクレベル ||--o{ リスク評価結果 : 参照
優先順位スコア ||--o{ 優先順位と投資判断 : 関連
投資判断 ||--o{ 優先順位と投資判断 : 関連
@enduml
```

## 4. 情報（エンティティ）一覧

| 情報ID   | 情報名             | 英語名                  | 説明                                                     | 情報種類 |
|----------|--------------------|-------------------------|----------------------------------------------------------|----------|
| INF-PR-01 | 評価基準           | EvaluationCriteria      | 優先順位付けの評価基準（価値、Kano、リスク、コスト）     | リソース |
| INF-PR-02 | 優先順位スコア     | PriorityScore           | 評価結果に基づく優先順位スコア                           | リソース |
| INF-PR-03 | Kano分類結果       | KanoClassification      | Kano評価の結果                                           | リソース |
| INF-PR-04 | リスク評価結果     | RiskAssessment          | リスクの特定・評価結果                                   | リソース |
| INF-PR-05 | コスト見積り       | CostEstimate            | 実装コスト・維持コストの見積もり                         | リソース |
| INF-PR-06 | 投資判断           | InvestmentDecision      | 優先順位に基づく投資判断とその理由                       | リソース |
| INF-PR-07 | 依存関係           | Dependency              | 要求間の依存関係                                         | リソース |
| INF-PR-08 | 対応策             | Countermeasure          | リスクへの対応策                                         | リソース |
| INF-PR-09 | 優先順位変更履歴   | ChangeHistory           | 優先順位変更の履歴・理由                                 | リソース |
| INF-PR-10 | Kanoカテゴリ       | KanoCategory            | 当たり前品質、一元品質、魅力的品質、無関心品質、逆品質   | 対照表   |
| INF-PR-11 | リスクレベル       | RiskLevel               | 高、中、低                                               | 対照表   |
| INF-PR-12 | 優先順位と投資判断 | PriorityInvestmentMapping | 優先順位と投資判断の関連                               | 対応表   |

## 5. 関係一覧

| 関係ID | 元情報ID | 元情報名       | 先情報ID | 先情報名         | 関係の種類 | 説明                                     |
|--------|----------|----------------|----------|------------------|------------|------------------------------------------|
| REL-01 | INF-PR-01 | 評価基準       | INF-PR-02 | 優先順位スコア   | 1対多      | 評価基準は複数の優先順位スコアに使用される |
| REL-02 | INF-PR-03 | Kano分類結果   | INF-PR-02 | 優先順位スコア   | 1対1       | Kano分類結果は優先順位スコアに含まれる   |
| REL-03 | INF-PR-04 | リスク評価結果 | INF-PR-02 | 優先順位スコア   | 1対1       | リスク評価結果は優先順位スコアに含まれる |
| REL-04 | INF-PR-05 | コスト見積り   | INF-PR-02 | 優先順位スコア   | 1対1       | コスト見積りは優先順位スコアに含まれる   |
| REL-05 | INF-PR-02 | 優先順位スコア | INF-PR-06 | 投資判断         | 1対多      | 優先順位スコアは投資判断を導く           |
| REL-06 | INF-PR-02 | 優先順位スコア | INF-PR-09 | 優先順位変更履歴 | 1対多      | 優先順位スコアの変更履歴が記録される     |
| REL-07 | INF-PR-04 | リスク評価結果 | INF-PR-08 | 対応策           | 1対多      | リスク評価結果には複数の対応策が定義される |
| REL-08 | INF-PR-10 | Kanoカテゴリ   | INF-PR-03 | Kano分類結果     | 1対多      | Kanoカテゴリは複数のKano分類結果で参照される |
| REL-09 | INF-PR-11 | リスクレベル   | INF-PR-04 | リスク評価結果   | 1対多      | リスクレベルは複数のリスク評価結果で参照される |
| REL-10 | INF-PR-02 | 優先順位スコア | INF-PR-12 | 優先順位と投資判断 | 1対多    | 優先順位スコアは投資判断と関連する       |
| REL-11 | INF-PR-06 | 投資判断       | INF-PR-12 | 優先順位と投資判断 | 1対多    | 投資判断は優先順位と関連する             |

## 6. 関係の種類

関係の種類は以下の記号で表現します：

| 記号 | 意味 | 説明 |
|------|------|------|
| `||--o{` | 1対多 (1:N) | 左側の1つが右側の0個以上に関連 |
| `||--||` | 1対1 (1:1) | 左側の1つが右側の1つに関連 |
| `}o--o{` | 多対多 (N:M) | 左側の0個以上が右側の0個以上に関連 |
| `||--o|` | 1対1以上 (1:1+) | 左側の1つが右側の1個以上に関連 |

## 7. 参照情報

- 情報一覧: `04.システム/01.情報/情報一覧.md`
- 情報属性一覧: `04.システム/03.情報属性/`
- UC複合図: `03.システム境界/UC複合図.md`
