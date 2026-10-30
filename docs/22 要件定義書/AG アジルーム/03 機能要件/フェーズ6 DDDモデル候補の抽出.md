# フェーズ6 DDDモデル候補の抽出

AG-01〜AG-06の全6業務領域のRDRAモデルからDDD（Domain-Driven Design）モデル候補を抽出します。

## 確認概要

| 項目 | 内容 |
|------|------|
| 確認日 | 2026-10-30 |
| 確認結果 | **合格** |
| チェッカー | Junie |

## 1. DDDモデル候補の抽出方法

RDRAモデルの以下の要素を基にDDDモデル候補を抽出します：

1. **情報一覧**: エンティティ候補、Value Object候補
2. **ER図**: エンティティ間の関係性
3. **ユースケース一覧**: ドメインサービス候補
4. **ステート一覧**: Aggregate Root候補（状態遷移を持つエンティティ）
5. **アクター一覧**: アプリケーションサービス境界
6. **外部システム一覧**: インフラストラクチャ層の連携ポイント

## 2. バウンデッドコンテキスト候補

### 2.1 AG-01 プロダクトディスカバリー

| バウンデッドコンテキスト | 説明 | 含まれるエンティティ |
|------------------------|------|-------------------|
| **Product Vision Context** | プロダクトの方向性定義 | ProductVision, ProductGoal, SuccessMetric |
| **Problem Discovery Context** | 課題・要望の発見・整理 | UserProblem, Feedback |
| **Hypothesis Testing Context** | 仮説検証 | Hypothesis, ExperimentPlan |
| **User Understanding Context** | ユーザー理解 | Stakeholder, Persona, UserJourney |
| **Kano Evaluation Context** | Kano評価 | KanoEvaluationResult, KanoCategory |

### 2.2 AG-02 要求バックログ管理

| バウンデッドコンテキスト | 説明 | 含まれるエンティティ |
|------------------------|------|-------------------|
| **Backlog Management Context** | バックログ項目の管理 | BacklogItem, AcceptanceCriterion |
| **Feedback Management Context** | フィードバック管理 | Feedback |
| **Discussion Context** | 議論・意思決定 | Comment, DecisionRecord |
| **Traceability Context** | 関連付け・トレーサビリティ | BacklogHierarchy, ItemRelation, ItemTestRelation |

### 2.3 AG-03 優先順位決定

| バウンデッドコンテキスト | 説明 | 含まれるエンティティ |
|------------------------|------|-------------------|
| **Evaluation Criteria Context** | 評価基準の定義・管理 | EvaluationCriteria, PriorityScore |
| **Kano Classification Context** | Kano分類の評価 | KanoClassification, KanoCategory |
| **Risk Assessment Context** | リスク評価・対応策 | RiskAssessment, Countermeasure |
| **Investment Decision Context** | 投資判断・依存関係 | InvestmentDecision, Dependency |

### 2.4 AG-04 スプリント実行

| バウンデッドコンテキスト | 説明 | 含まれるエンティティ |
|------------------------|------|-------------------|
| **Sprint Management Context** | スプリントの計画・実行 | Sprint, SprintGoal |
| **Sprint Backlog Context** | Sprint Backlogの管理 | SprintBacklogItem |
| **Impediment Context** | 障害の管理 | Impediment |

### 2.5 AG-05 品質検証

| バウンデッドコンテキスト | 説明 | 含まれるエンティティ |
|------------------------|------|-------------------|
| **Definition of Done Context** | 完成基準の定義 | DefinitionOfDone |
| **Test Management Context** | テストケース・実行結果 | TestCase, TestExecutionResult |
| **Defect Management Context** | 欠陥の管理 | Defect |
| **Performance Testing Context** | 性能テストの管理 | PerformanceRequirement, PerformanceTestResult |
| **Security Risk Context** | セキュリティリスク管理 | SecurityQualityRisk |

### 2.6 AG-06 成果適応

| バウンデッドコンテキスト | 説明 | 含まれるエンティティ |
|------------------------|------|-------------------|
| **Sprint Review Context** | Sprint Review管理 | SprintReview, ReviewFeedback |
| **Retrospective Context** | 振り返り管理 | Retrospective, ImprovementAction |
| **Monitoring Context** | 状況監視 | Dashboard, ProjectStatus, Risk |
| **User Review Context** | 利用者レビュー | UserReviewResult |
| **Outcome Measurement Context** | 成果測定 | OutcomeMetricData, Report |

## 3. エンティティ分類

### 3.1 Aggregate Root候補

状態遷移を持ち、一貫性境界の中心となるエンティティ：

| エンティティ | 業務領域 | 状態遷移 | 理由 |
|-------------|---------|---------|------|
| **ProductGoal** | AG-01 | 草案→有効→達成済み/見送り | 状態遷移を持つ主要エンティティ |
| **UserProblem** | AG-01 | 新規→整理中→対応済み/却下 | 課題のライフサイクル管理 |
| **Hypothesis** | AG-01 | 検証予定→検証中→検証済み | 仮説検証の進行管理 |
| **BacklogItem** | AG-02 | 新規→明確化中→リファイン済み→実装済み/却下 | バックログの中心エンティティ |
| **Feedback** | AG-02 | 新規→分類済み→対応済み | フィードバックの対応追跡 |
| **ImprovementAction** | AG-06 | 未着手→実行中→完了→効果確認中→効果確認済み | 改善アクションの進行管理 |
| **Risk** | AG-06 | 新規→対応中→解消/受容 | リスク管理の中心 |
| **UserReview** | AG-06 | 実施予定→実施中→完了 | レビューの進行管理 |
| **EvaluationCriteria** | AG-03 | 未定義→定義済み | 評価基準の管理 |
| **PriorityScore** | AG-03 | 未評価→評価中→評価済み→決定済み/再検討中 | 優先順位の進行管理 |
| **RiskAssessment** | AG-03 | 未特定→特定済み→対応中→解決済み | リスク評価の進行管理 |
| **Sprint** | AG-04 | 計画済み→実行中→完了/キャンセル | スプリントのライフサイクル |
| **SprintBacklogItem** | AG-04 | 未着手→作業中→レビュー中→完了 | 作業の進行管理 |
| **Impediment** | AG-04 | 新規→対応中→解消済み | 障害の解消管理 |
| **DefinitionOfDone** | AG-05 | 定義済み | 完成基準の管理 |
| **TestCase** | AG-05 | 準備中→有効→無効 | テストケースの管理 |
| **TestExecutionResult** | AG-05 | 実行予定→実行中→合格/不合格 | テスト実行の進行管理 |
| **Defect** | AG-05 | 新規→確認済み→対応中→修正済み→クローズド/却下 | 欠陥のライフサイクル |
| **SecurityQualityRisk** | AG-05 | 新規→対応中→解消/受容 | セキュリティリスク管理 |

### 3.2 Entity候補

Aggregate Rootに属するエンティティ：

| エンティティ | 所属Aggregate | 業務領域 |
|-------------|--------------|---------|
| SuccessMetric | ProductGoal | AG-01 |
| Feedback | UserProblem | AG-01 |
| AcceptanceCriterion | BacklogItem | AG-02 |
| Comment | BacklogItem | AG-02 |
| DecisionRecord | BacklogItem | AG-02 |
| ReviewFeedback | SprintReview | AG-06 |
| ImprovementAction | Retrospective | AG-06 |
| PriorityScore | EvaluationCriteria | AG-03 |
| KanoClassification | EvaluationCriteria | AG-03 |
| Countermeasure | RiskAssessment | AG-03 |
| Dependency | InvestmentDecision | AG-03 |
| SprintGoal | Sprint | AG-04 |
| SprintBacklogItem | Sprint | AG-04 |
| TestExecutionResult | TestCase | AG-05 |
| PerformanceTestResult | PerformanceRequirement | AG-05 |

### 3.3 Value Object候補

識別子を持たず、属性で同一性を判断するオブジェクト：

| Value Object | 所属Aggregate | 業務領域 |
|-------------|--------------|---------|
| KanoCategory | KanoEvaluationResult | AG-01 |
| BacklogItemType | BacklogItem | AG-02 |
| Priority | BacklogItem | AG-02 |
| BacklogItemStatus | BacklogItem | AG-02 |
| ImprovementActionStatus | ImprovementAction | AG-06 |
| RiskStatus | Risk | AG-06 |
| KanoCategory | KanoClassification | AG-03 |
| RiskLevel | RiskAssessment | AG-03 |
| EvaluationStatus | PriorityScore | AG-03 |
| SprintStatus | Sprint | AG-04 |
| WorkStatus | SprintBacklogItem | AG-04 |
| ImpedimentStatus | Impediment | AG-04 |
| TestCaseStatus | TestCase | AG-05 |
| DefectStatus | Defect | AG-05 |
| Severity | Defect | AG-05 |
| TestType | TestCase | AG-05 |

## 4. ドメインサービス候補

ユースケースから抽出したドメインロジック：

| ドメインサービス | 業務領域 | 説明 |
|-----------------|---------|------|
| **KanoEvaluationService** | AG-01 | Kano質問票に基づく分類ロジック |
| **BacklogRefinementService** | AG-02 | バックログ項目のリファインメント処理 |
| **StoryMappingService** | AG-02 | ユーザー活動とストーリーの配置 |
| **PrioritySettingService** | AG-02 | 優先順位の決定ロジック |
| **FeedbackClassificationService** | AG-02 | フィードバックの分類処理 |
| **SprintReviewService** | AG-06 | Sprint Reviewの開始〜終了フロー |
| **ImprovementTrackingService** | AG-06 | 改善アクションの効果追跡 |
| **OutcomeMeasurementService** | AG-06 | 成果指標の測定・分析 |
| **PriorityCalculationService** | AG-03 | 評価結果に基づく優先順位スコアの計算 |
| **KanoClassificationService** | AG-03 | Kano質問票に基づく分類ロジック |
| **RiskAssessmentService** | AG-03 | リスクの特定・評価・対応策の定義 |
| **InvestmentDecisionService** | AG-03 | 優先順位に基づく投資判断の記録 |
| **DependencyManagementService** | AG-03 | 要求間の依存関係の管理 |
| **SprintPlanningService** | AG-04 | スプリントの計画・開始・完了フロー |
| **SprintBacklogService** | AG-04 | Sprint Backlogの作成・更新・依存関係管理 |
| **ImpedimentService** | AG-04 | 障害の登録・解消管理 |
| **IncrementVerificationService** | AG-04 | インクリメントの完成確認 |
| **DefinitionOfDoneService** | AG-05 | 完成基準の定義・更新 |
| **TestCaseService** | AG-05 | テストケースの作成・更新・実行管理 |
| **DefectService** | AG-05 | 欠陥の登録・対応・検証フロー |
| **PerformanceTestService** | AG-05 | 性能テストの実行・結果分析 |
| **SecurityRiskService** | AG-05 | セキュリティリスクの管理 |

## 5. アプリケーションサービス候補

アクターの操作をドメインロジックに翻訳するサービス：

| アプリケーションサービス | 対応アクター | 業務領域 |
|-------------------------|-------------|---------|
| **ProductVisionAppService** | ACT-02 | AG-01 |
| **ProblemDiscoveryAppService** | ACT-01, ACT-02 | AG-01 |
| **HypothesisTestingAppService** | ACT-02 | AG-01 |
| **UserUnderstandingAppService** | ACT-02 | AG-01 |
| **BacklogManagementAppService** | ACT-02, ACT-04 | AG-02 |
| **FeedbackManagementAppService** | ACT-01, ACT-02 | AG-02 |
| **DiscussionAppService** | ACT-01, ACT-02, ACT-04 | AG-02 |
| **SprintReviewAppService** | ACT-01, ACT-02, ACT-03, ACT-04 | AG-06 |
| **RetrospectiveAppService** | ACT-03, ACT-04 | AG-06 |
| **MonitoringAppService** | ACT-01~ACT-05 | AG-06 |
| **EvaluationCriteriaAppService** | ACT-02 | AG-03 |
| **PriorityCalculationAppService** | ACT-02, ACT-04 | AG-03 |
| **KanoClassificationAppService** | ACT-02 | AG-03 |
| **RiskAssessmentAppService** | ACT-02, ACT-04 | AG-03 |
| **InvestmentDecisionAppService** | ACT-01, ACT-02 | AG-03 |
| **SprintPlanningAppService** | ACT-02, ACT-03, ACT-04 | AG-04 |
| **SprintBacklogAppService** | ACT-02, ACT-04 | AG-04 |
| **ImpedimentAppService** | ACT-03, ACT-04 | AG-04 |
| **DefinitionOfDoneAppService** | ACT-02, ACT-03, ACT-04 | AG-05 |
| **TestCaseAppService** | ACT-02, ACT-04 | AG-05 |
| **DefectAppService** | ACT-01, ACT-02, ACT-04 | AG-05 |
| **PerformanceTestAppService** | ACT-04 | AG-05 |
| **SecurityRiskAppService** | ACT-06 | AG-05 |

## 6. リポジトリ候補

永続化対象となるAggregate Root：

| リポジトリ | 業務領域 |
|-----------|---------|
| **ProductGoalRepository** | AG-01 |
| **UserProblemRepository** | AG-01 |
| **HypothesisRepository** | AG-01 |
| **StakeholderRepository** | AG-01 |
| **PersonaRepository** | AG-01 |
| **BacklogItemRepository** | AG-02 |
| **FeedbackRepository** | AG-02 |
| **CommentRepository** | AG-02 |
| **DecisionRecordRepository** | AG-02 |
| **SprintReviewRepository** | AG-06 |
| **RetrospectiveRepository** | AG-06 |
| **ImprovementActionRepository** | AG-06 |
| **RiskRepository** | AG-06 |
| **UserReviewRepository** | AG-06 |
| **EvaluationCriteriaRepository** | AG-03 |
| **PriorityScoreRepository** | AG-03 |
| **KanoClassificationRepository** | AG-03 |
| **RiskAssessmentRepository** | AG-03 |
| **InvestmentDecisionRepository** | AG-03 |
| **DependencyRepository** | AG-03 |
| **SprintRepository** | AG-04 |
| **SprintGoalRepository** | AG-04 |
| **SprintBacklogItemRepository** | AG-04 |
| **ImpedimentRepository** | AG-04 |
| **DefinitionOfDoneRepository** | AG-05 |
| **TestCaseRepository** | AG-05 |
| **TestExecutionResultRepository** | AG-05 |
| **DefectRepository** | AG-05 |
| **PerformanceRequirementRepository** | AG-05 |
| **PerformanceTestResultRepository** | AG-05 |
| **SecurityQualityRiskRepository** | AG-05 |

## 7. ドメインイベント候補

ステート遷移や重要な状態変化を通知するイベント：

| ドメインイベント | 業務領域 | トリガー |
|-----------------|---------|---------|
| **ProductGoalCreated** | AG-01 | Product Goalが作成された |
| **ProductGoalAchieved** | AG-01 | Product Goalが達成された |
| **HypothesisVerified** | AG-01 | 仮説の検証が完了した |
| **BacklogItemCreated** | AG-02 | バックログ項目が作成された |
| **BacklogItemRefined** | AG-02 | バックログ項目がリファインされた |
| **FeedbackReceived** | AG-02 | フィードバックが登録された |
| **DecisionRecorded** | AG-02 | 意思決定が記録された |
| **SprintReviewCompleted** | AG-06 | Sprint Reviewが完了した |
| **ImprovementActionCompleted** | AG-06 | 改善アクションが完了した |
| **RiskResolved** | AG-06 | リスクが解消された |
| **EvaluationCriteriaDefined** | AG-03 | 評価基準が定義された |
| **PriorityCalculated** | AG-03 | 優先順位スコアが計算された |
| **PriorityDecided** | AG-03 | 優先順位が決定された |
| **KanoClassified** | AG-03 | Kano分類が行われた |
| **RiskIdentified** | AG-03 | リスクが特定された |
| **InvestmentDecisionRecorded** | AG-03 | 投資判断が記録された |
| **SprintCreated** | AG-04 | スプリントが作成された |
| **SprintStarted** | AG-04 | スプリントが開始された |
| **SprintCompleted** | AG-04 | スプリントが完了した |
| **SprintBacklogCreated** | AG-04 | Sprint Backlogが作成された |
| **ImpedimentResolved** | AG-04 | 障害が解消された |
| **IncrementVerified** | AG-04 | インクリメントが完成確認された |
| **DefinitionOfDoneDefined** | AG-05 | Definition of Doneが定義された |
| **TestCaseCreated** | AG-05 | テストケースが作成された |
| **TestExecuted** | AG-05 | テストが実行された |
| **DefectRegistered** | AG-05 | 欠陥が登録された |
| **DefectClosed** | AG-05 | 欠陥がクローズされた |
| **PerformanceTestExecuted** | AG-05 | 性能テストが実行された |
| **SecurityRiskRegistered** | AG-05 | セキュリティリスクが登録された |

## 8. インフラストラクチャ層連携ポイント

| 外部システム | 業務領域 | 連携内容 |
|-------------|---------|---------|
| ES-07 監視・ログ基盤 | AG-06 | 稼働状況・エラー・ログの取得 |
| CI/CDパイプライン | AG-04 | ビルド・デプロイ状況の取得 |
| ソース管理システム | AG-04 | コミット・ブランチ・PRの関連付け |
| Gatling | AG-05 | 性能テストレポートの取り込み |

## 9. 横断的な統合考量

### 9.1 共通エンティティ

| エンティティ | 出現業務領域 | 統合方針 |
|-------------|-------------|---------|
| **UserProblem（課題）** | AG-01, AG-02 | 共通のAggregate Rootとして統合検討 |
| **Feedback（フィードバック）** | AG-01, AG-02 | 共通のEntityとして統合検討 |
| **ProductGoal** | AG-01, AG-02（参照） | AG-01で定義、AG-02から参照 |
| **BacklogItem** | AG-02, AG-03（参照）, AG-04（参照） | AG-02で定義、AG-03/AG-04から参照 |
| **KanoCategory** | AG-01, AG-03 | 共通のValue Objectとして統合検討 |
| **Dependency** | AG-03, AG-04 | 共通のEntityとして統合検討 |
| **Defect** | AG-02, AG-05 | AG-05で定義、AG-02から参照 |

### 9.2 コンテキストマップ

```
┌─────────────────────────────────────────────────────────┐
│              Product Vision Context (AG-01)              │
│  ProductVision, ProductGoal, SuccessMetric              │
└──────────────────────┬──────────────────────────────────┘
                       │ Provides Context
┌──────────────────────▼──────────────────────────────────┐
│           Problem Discovery Context (AG-01)              │
│  UserProblem, Feedback                                  │
└──────────────────────┬──────────────────────────────────┘
                       │ Follows
┌──────────────────────▼──────────────────────────────────┐
│          Backlog Management Context (AG-02)              │
│  BacklogItem, AcceptanceCriterion, Comment               │
└────────┬───────────────────┬────────────────────────────┘
         │ Customer-Supplier │ Provides Context
┌────────▼───────────────────▼────────────────────────────┐
│         Priority Decision Context (AG-03)                │
│  EvaluationCriteria, PriorityScore, RiskAssessment       │
└──────────────────────┬──────────────────────────────────┘
                       │ Customer-Supplier
┌──────────────────────▼──────────────────────────────────┐
│         Sprint Execution Context (AG-04)                 │
│  Sprint, SprintGoal, SprintBacklogItem, Impediment       │
└────────┬───────────────────┬────────────────────────────┘
         │ Customer-Supplier │ Shared Kernel
┌────────▼───────────────────▼────────────────────────────┐
│        Quality Verification Context (AG-05)              │
│  DefinitionOfDone, TestCase, Defect, PerformanceTest     │
└──────────────────────┬──────────────────────────────────┘
                       │ Customer-Supplier
┌──────────────────────▼──────────────────────────────────┐
│         Sprint Review Context (AG-06)                    │
│  SprintReview, ReviewFeedback                           │
└──────────────────────┬──────────────────────────────────┘
                       │ Shared Kernel
┌──────────────────────▼──────────────────────────────────┐
│        Retrospective Context (AG-06)                     │
│  Retrospective, ImprovementAction                       │
└──────────────────────┬──────────────────────────────────┘
                       │ Shared Kernel
┌──────────────────────▼──────────────────────────────────┐
│        Monitoring Context (AG-06)                        │
│  Dashboard, ProjectStatus, Risk                         │
└─────────────────────────────────────────────────────────┘
```

## 10. 総合評価

| 評価項目 | 結果 |
|----------|------|
| バウンデッドコンテキスト抽出 | **合格** |
| エンティティ分類 | **合格** |
| ドメインサービス抽出 | **合格** |
| アプリケーションサービス抽出 | **合格** |
| リポジトリ候補抽出 | **合格** |
| ドメインイベント候補抽出 | **合格** |
| コンテキストマップ作成 | **合格** |

### 改善提案

1. **共通エンティティの統合検討**
   - UserProblem（課題）とFeedback（フィードバック）はAG-01とAG-02で重複定義
   - 共通のShared Kernelとして統合することを検討
   - BacklogItemはAG-02で定義し、AG-03/AG-04から参照するパターンを適用
   - KanoCategoryはAG-01とAG-03で共通のValue Objectとして統合

2. **バウンデッドコンテキストの境界明確化**
   - 各バウンデッドコンテキストのAPI境界を明確に定義
   - コンテキスト間のデータ連携方法を決定
   - AG-03（優先順位決定）とAG-04（スプリント実行）間のDependencyの統合方針を明確化

3. **ドメインイベントの標準化**
   - 横断的なドメインイベントのフォーマットを統一
   - イベントソーシングの導入を検討
   - AG-04のスプリント完了イベントとAG-06のSprint Review開始イベントの連携を定義

4. **外部システム連携の統合**
   - AG-04のCI/CDパイプライン連携とAG-05のGatling連携の統合アーキテクチャを検討
   - ソース管理システムとの連携をAG-04/AG-05で共通化

## 次のステップ

- DDDモデル候補に基づいて、アーキテクチャ設計を進める
- モジュール境界を定義し、実装に移行する
