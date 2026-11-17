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

## 8. ビジネスルール（ドメインサービス候補）

バリエーション条件図で定義されたビジネスルールを、ドメインサービス候補としてマッピングします。

### 8.1 AG-01 プロダクトディスカバリー

| 条件ID | 条件名 | 英語名 | 種類 | 関連アグリゲート | 説明 |
|--------|--------|--------|------|------------------|------|
| COND-001 | 仮説検証ルール | Hypothesis Validation | ポリシー | Hypothesis | 仮説は検証可能で測定可能であること |
| COND-002 | 仮説更新ルール | Hypothesis Update | オペレーション | Hypothesis | 検証結果に基づいて仮説を更新する |
| COND-003 | 課題登録ルール | Problem Registration | ポリシー | UserProblem | 課題は影響範囲を特定して登録する |
| COND-004 | 要望分類ルール | Requirement Classification | 意思決定支援 | KanoEvaluation | 要望はKano評価に基づいて分類する |
| COND-005 | ステークホルダー管理ルール | Stakeholder Management | ポリシー | Stakeholder | ステークホルダーは関心事を整理して管理する |
| COND-006 | 成功指標定義ルール | Success Metric Definition | ポリシー | SuccessMetric | 成功指標は測定可能で目標値を設定する |
| COND-007 | 実験計画ルール | Experiment Planning | ポリシー | ExperimentPlan | 実験計画は仮説と関連付けて作成する |
| COND-008 | 実験結果記録ルール | Experiment Result Recording | オペレーション | ExperimentPlan | 実験結果は仮説の検証結果として記録する |

### 8.2 AG-02 要求バックログ管理

| 条件ID | 条件名 | 英語名 | 種類 | 関連アグリゲート | 説明 |
|--------|--------|--------|------|------------------|------|
| COND-BL-01 | 必須情報チェック | Required Info Check | ポリシー | BacklogItem | 課題登録時に必須情報が揃っているか確認 |
| COND-BL-02 | 優先順位一意性 | Priority Uniqueness | 制約 | BacklogItem | 重複する優先順位は不可 |
| COND-BL-03 | 編集権限チェック | Edit Permission Check | ポリシー | BacklogItem | 編集権限があるか確認 |
| COND-BL-04 | 受入条件必須 | Acceptance Criterion Required | ポリシー | AcceptanceCriterion | Storyには受入条件の定義が必須 |
| COND-BL-05 | 準備完了条件 | Ready Condition | 意思決定支援 | BacklogItem | 受入条件・見積もり・依存関係が整った項目のみReadyにできる |

### 8.3 AG-03 優先順位決定

| 条件ID | 条件名 | 英語名 | 種類 | 関連アグリゲート | 説明 |
|--------|--------|--------|------|------------------|------|
| COND-PR-001 | 評価基準定義ルール | Evaluation Criteria Definition | ポリシー | EvaluationCriteria | 評価基準は価値、Kano、リスク、コストを含むこと |
| COND-PR-002 | Kano分類ルール | Kano Classification | ポリシー | KanoClassification | 要求項目はKanoカテゴリに分類すること |
| COND-PR-003 | リスク評価ルール | Risk Assessment | ポリシー | RiskAssessment | リスクは発生確率と影響度を評価すること |
| COND-PR-004 | 優先順位計算ルール | Priority Calculation | オペレーション | PriorityScore | 評価基準の重み付けに基づいて優先順位を計算する |
| COND-PR-005 | 優先順位決定ルール | Priority Decision | ポリシー | PriorityScore | 優先順位の決定はプロダクトオーナーのみが行う |
| COND-PR-006 | 依存関係定義ルール | Dependency Definition | 制約 | Dependency | 循環依存は定義できない |
| COND-PR-007 | 投資判断記録ルール | Investment Decision Recording | ポリシー | InvestmentDecision | 投資判断の理由を必ず記録する |
| COND-PR-008 | 変更履歴追跡ルール | Change History Tracking | オペレーション | PriorityScore | 優先順位変更の履歴・理由を追跡する |

### 8.4 AG-04 スプリント実行

| 条件ID | 条件名 | 英語名 | 種類 | 関連アグリゲート | 説明 |
|--------|--------|--------|------|------------------|------|
| COND-SP-001 | スプリント開始条件 | Sprint Start Condition | ポリシー | Sprint | Sprint Backlogが準備完了のスプリントのみ開始可能 |
| COND-SP-002 | 作業着手条件 | Work Start Condition | ポリシー | SprintBacklogItem | 依存関係が解消された項目のみ着手可能 |
| COND-SP-003 | 障害エスカレーションルール | Impediment Escalation | オペレーション | Impediment | 期限を過ぎた障害はエスカレーションする |
| COND-SP-004 | インクリメント確認ルール | Increment Verification | ポリシー | Sprint | Definition of Doneを満たすことを確認する |

### 8.5 AG-05 品質検証

| 条件ID | 条件名 | 英語名 | 種類 | 関連アグリゲート | 説明 |
|--------|--------|--------|------|------------------|------|
| COND-QL-001 | テストケース必須ルール | Test Case Required | ポリシー | TestCase | 欠陥はテストケースと関連付けて登録する |
| COND-QL-002 | 欠陥严重度ルール | Defect Severity | ポリシー | Defect | 欠陥は严重度に基づいて優先度を設定する |
| COND-QL-003 | 性能テスト実行条件 | Performance Test Condition | 意思決定支援 | PerformanceRequirement | 性能要件を満たさない場合は再テストする |
| COND-QL-004 | セキュリティリスク対応ルール | Security Risk Response | ポリシー | SecurityQualityRisk | 許容範囲を超えるリスクは対応策を実行する |

### 8.6 AG-06 成果適応

| 条件ID | 条件名 | 英語名 | 種類 | 関連アグリゲート | 説明 |
|--------|--------|--------|------|------------------|------|
| COND-009 | 改善アクション追跡ルール | Improvement Action Tracking | オペレーション | ImprovementAction | 期限までに改善効果を確かめる |
| COND-010 | リスク対応ルール | Risk Response | ポリシー | Risk | 許容範囲を超えるリスクは対応策を実行する |
| COND-015 | 権限ベースの画面表示ルール | Role-based View | ポリシー | Dashboard | ロールに応じた情報だけを表示する |
| COND-016 | フィードバック分類ルール | Feedback Classification | 意思決定支援 | ReviewFeedback | フィードバックを種別ごとに分類する |
| COND-017 | 対応方針決定ルール | Response Decision | 意思決定支援 | ReviewFeedback | フィードバックの優先度に基づいて対応方針を決定する |
| COND-018 | Sprint Review開始条件 | Sprint Review Start Condition | ポリシー | SprintReview | 準備情報が揃っていること |
| COND-019 | Sprint Review終了条件 | Sprint Review End Condition | ポリシー | SprintReview | 決定事項が記録されていること |
| COND-020 | 改善アクション期限ルール | Improvement Action Deadline | オペレーション | ImprovementAction | 期限を過ぎた改善アクションはエスカレーションする |

## 9. モジュール構成候補

Spring Modulith のモジュール構成候補を、サブドメインごとに提案します。

### 9.1 AG-01 プロダクトディスカバリー

| モジュール名 | バウンデッドコンテキスト | 種類 | 概要 |
|-------------|------------------------|------|------|
| product-vision | Product Vision Context | コアドメイン | Product Vision, Product Goal, Success Metric の管理 |
| problem-discovery | Problem Discovery Context | コアドメイン | 課題・要望の発見・整理 |
| hypothesis-testing | Hypothesis Testing Context | コアドメイン | 仮説検証の管理 |
| user-understanding | User Understanding Context | サポートリングドメイン | ステークホルダー・ペルソナ・ユーザージャーニー管理 |
| kano-evaluation | Kano Evaluation Context | サポートリングドメイン | Kano評価の管理 |

### 9.2 AG-02 要求バックログ管理

| モジュール名 | バウンデッドコンテキスト | 種類 | 概要 |
|-------------|------------------------|------|------|
| backlog-management | Backlog Management Context | コアドメイン | バックログ項目の管理 |
| feedback-management | Feedback Management Context | サポートリングドメイン | フィードバック管理 |
| discussion | Discussion Context | サポートリングドメイン | 議論・意思決定 |
| traceability | Traceability Context | サポートリングドメイン | 関連付け・トレーサビリティ |

### 9.3 AG-03 優先順位決定

| モジュール名 | バウンデッドコンテキスト | 種類 | 概要 |
|-------------|------------------------|------|------|
| evaluation-criteria | Evaluation Criteria Context | コアドメイン | 評価基準の定義・管理 |
| kano-classification | Kano Classification Context | サポートリングドメイン | Kano分類の評価 |
| risk-assessment | Risk Assessment Context | サポートリングドメイン | リスク評価・対応策 |
| investment-decision | Investment Decision Context | サポートリングドメイン | 投資判断・依存関係 |

### 9.4 AG-04 スプリント実行

| モジュール名 | バウンデッドコンテキスト | 種類 | 概要 |
|-------------|------------------------|------|------|
| sprint-management | Sprint Management Context | コアドメイン | スプリントの計画・実行 |
| sprint-backlog | Sprint Backlog Context | サポートリングドメイン | Sprint Backlogの管理 |
| impediment | Impediment Context | サポートリングドメイン | 障害の管理 |

### 9.5 AG-05 品質検証

| モジュール名 | バウンデッドコンテキスト | 種類 | 概要 |
|-------------|------------------------|------|------|
| definition-of-done | Definition of Done Context | サポートリングドメイン | 完成基準の定義 |
| test-management | Test Management Context | コアドメイン | テストケース・実行結果 |
| defect-management | Defect Management Context | コアドメイン | 欠陥の管理 |
| performance-testing | Performance Testing Context | サポートリングドメイン | 性能テストの管理 |
| security-risk | Security Risk Context | サポートリングドメイン | セキュリティリスク管理 |

### 9.6 AG-06 成果適応

| モジュール名 | バウンデッドコンテキスト | 種類 | 概要 |
|-------------|------------------------|------|------|
| sprint-review | Sprint Review Context | コアドメイン | Sprint Review管理 |
| retrospective | Retrospective Context | コアドメイン | 振り返り管理 |
| monitoring | Monitoring Context | サポートリングドメイン | 状況監視 |
| user-review | User Review Context | サポートリングドメイン | 利用者レビュー |
| outcome-measurement | Outcome Measurement Context | サポートリングドメイン | 成果測定 |

## 10. 補足事項

### 状態遷移のenum化

各業務領域で定義された状態遷移は、DDDモデルでは値オブジェクトとしてenum化することを提案します：

| 状態一覧 | 値オブジェクト名 | 業務領域 |
|---------|----------------|---------|
| ST-DIS-* | HypothesisStatus, ProblemStatus | AG-01 |
| ST-BL-* | BacklogItemStatus, FeedbackStatus | AG-02 |
| ST-PR-* | PriorityStatus, RiskAssessmentStatus | AG-03 |
| ST-SP-* | SprintStatus, WorkStatus, ImpedimentStatus | AG-04 |
| ST-QL-* | TestCaseStatus, DefectStatus | AG-05 |
| ST-LR-* | ImprovementActionStatus, RiskStatus | AG-06 |

### ドメインイベントの扱い

各業務領域で定義されたイベント一覧は、ドメインイベントとして扱います。イベントソーシングを採用する場合、各アグリゲートの状態遷移をイベントとして記録します。

### 監査ログの扱い

監査ログは、ドメインイベントを基に生成する副産物（Side Effect）として扱います。監査ログアグリゲートは、ドメインイベントを購読してログを記録する役割を担います。

### 共通マスターの扱い

KanoCategory、BacklogItemType、Priorityなどの共通マスターは、各バウンデッドコンテキストで独立して管理するか、共通モジュールとして統合するかを検討します。

## 11. 総合評価

| 評価項目 | 結果 |
|----------|------|
| バウンデッドコンテキスト抽出 | **合格** |
| エンティティ分類 | **合格** |
| ドメインサービス抽出 | **合格** |
| アプリケーションサービス抽出 | **合格** |
| リポジトリ候補抽出 | **合格** |
| ドメインイベント候補抽出 | **合格** |
| コンテキストマップ作成 | **合格** |
| ビジネスルール抽出 | **合格** |
| モジュール構成提案 | **合格** |

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

5. **AG-04, AG-05のバリエーション条件図追加**
   - AG-04, AG-05にはバリエーション条件図が存在しないため、ビジネスルールを明確化するために追加を推奨
   - スプリント実行の開始条件、作業着手条件、障害エスカレーションルールなどを定義
   - 品質検証のテストケース必須ルール、欠陥严重度ルールなどを定義

## 次のステップ

- DDDモデル候補に基づいて、アーキテクチャ設計を進める
- モジュール境界を定義し、実装に移行する
- AG-04, AG-05のバリエーション条件図を追加して、ビジネスルールを明確化
