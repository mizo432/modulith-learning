# フェーズ6 DDDモデル候補の抽出

AG-01, AG-02, AG-06の3業務領域のRDRAモデルからDDD（Domain-Driven Design）モデル候補を抽出します。

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

### 2.3 AG-06 成果適応

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

## 8. インフラストラクチャ層連携ポイント

| 外部システム | 業務領域 | 連携内容 |
|-------------|---------|---------|
| ES-07 監視・ログ基盤 | AG-06 | 稼働状況・エラー・ログの取得 |

## 9. 横断的な統合考量

### 9.1 共通エンティティ

| エンティティ | 出現業務領域 | 統合方針 |
|-------------|-------------|---------|
| **UserProblem（課題）** | AG-01, AG-02 | 共通のAggregate Rootとして統合検討 |
| **Feedback（フィードバック）** | AG-01, AG-02 | 共通のEntityとして統合検討 |
| **ProductGoal** | AG-01, AG-02（参照） | AG-01で定義、AG-02から参照 |

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

2. **バウンデッドコンテキストの境界明確化**
   - 各バウンデッドコンテキストのAPI境界を明確に定義
   - コンテキスト間のデータ連携方法を決定

3. **ドメインイベントの標準化**
   - 横断的なドメインイベントのフォーマットを統一
   - イベントソーシングの導入を検討

## 次のステップ

- DDDモデル候補に基づいて、アーキテクチャ設計を進める
- モジュール境界を定義し、実装に移行する
