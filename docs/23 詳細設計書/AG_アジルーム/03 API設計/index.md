# AG_アジルーム API詳細設計

## 1. 概要

本ドキュメントは、AG_アジルーム事業領域のAPI詳細設計を定義します。**CQRS（Command Query Responsibility Segregation）**に基づき、**Command Service**（書き込み）と**Query Service**（読み込み）を分離して設計します。

アジルーム（Agiloom）は、Scrumに基づくアジャイルソフトウェア開発プロジェクトの管理をシステム化する事業領域です。

## 2. 参照

- 事業領域一覧: `docs/11 事業領域/事業領域一覧.tsv`
- 業務領域一覧: `docs/12 業務領域/業務領域一覧.tsv`
- ユースケース一覧: `docs/22 要件定義書/AG アジルーム/03 機能要件/ユースケース一覧.md`
- 情報一覧: `docs/22 要件定義書/AG アジルーム/03 機能要件/*/04.システム/01.情報/情報一覧.md`
- 画面属性一覧: `docs/22 要件定義書/AG アジルーム/03 機能要件/*/04.システム/04.画面/SCR-*_画面属性一覧.md`

---

## 3. CQRS設計

### 3.1 CQRSの基本概念

CQRS（Command Query Responsibility Segregation）は、**書き込み操作（Command）**と**読み込み操作（Query）**を分離する設計パターンです。

| 区分 | Command Service | Query Service |
|:---|:---|:---|
| 責務 | データの作成・更新・削除 | データの読み込み・検索 |
| HTTPメソッド | POST, PUT, PATCH, DELETE | GET |
| 戻り値 | 処理結果（ステータスコード・ID） | データ（エンティティ・一覧） |
| 最適化対象 | 一貫性・ビジネスルール | パフォーマンス・キャッシュ |
| トランザクション | 必須 | 不要（スナップショット読み込み可能） |
| キャッシュ | 不要 | 積極的に活用 |

### 3.2 Command Service 設計

**書き込み操作を担当するサービス**

- **パッケージ構成**: `undecided.ag.<module>.useCase.command`
- **命名規則**: `[Entity]CommandService`（例: `SprintCommandService`）
- **メソッド命名**:
  - 作成: `create[Entity](Command)` → `createSprint(CreateSprintCommand)`
  - 更新: `update[Entity](Command)` → `updateSprint(UpdateSprintCommand)`
  - 削除: `delete[Entity](Command)` → `deleteSprint(DeleteSprintCommand)`
  - 状態遷移: `[action][Entity](Command)` → `startSprint(StartSprintCommand)`
- **Commandオブジェクト**: 書き込みリクエストを表現する値オブジェクト
  - 不変（`final` クラス + `final` フィールド）
  - バリデーションアノテーションを付与
  - 例: `CreateSprintCommand`, `StartSprintCommand`
- **戻り値**: 処理結果のIDまたはステータスを返す

### 3.3 Query Service 設計

**読み込み操作を担当するサービス**

- **パッケージ構成**: `undecided.ag.<module>.useCase.query`
- **命名規則**: `[Entity]QueryService`（例: `SprintQueryService`）
- **メソッド命名**:
  - 単一取得: `get[Entity](Query)` → `getSprint(GetSprintQuery)`
  - 一覧取得: `list[Entity](Query)` → `listSprints(ListSprintsQuery)`
  - 検索: `search[Entity](Query)` → `searchSprints(SearchSprintsQuery)`
  - カウント: `count[Entity](Query)` → `countSprints(CountSprintsQuery)`
- **Queryオブジェクト**: 読み込みリクエストを表現する値オブジェクト
  - 不変（`final` クラス + `final` フィールド）
  - 検索条件・ページネーションパラメータを含む
  - 例: `GetSprintQuery`, `ListSprintsQuery`
- **戻り値**: 読み込み結果のDTOを返す
  - 単一取得: `Optional<[Entity]Dto>` または `[Entity]Dto`
  - 一覧取得: `Page<[Entity]Dto>` または `List<[Entity]Dto>`

### 3.4 Controller 層の分離

プレゼンテーション層では、CommandとQueryでコントローラーを分離します。

| コントローラー | 責務 | 例 |
|:---|:---|:---|
| `[Entity]CommandController` | 書き込みAPI | `SprintCommandController` |
| `[Entity]QueryController` | 読み込みAPI | `SprintQueryController` |

### 3.5 APIエンドポイントの設計ルール

CQRS分離に基づくAPIエンドポイントの設計ルール：

| 操作 | HTTPメソッド | エンドポイント | Service | Command/Query |
|:---|:---|:---|:---|:---|
| 一覧取得 | GET | `/api/v1/[resources]` | QueryService | Query |
| 単一取得 | GET | `/api/v1/[resources]/{id}` | QueryService | Query |
| 作成 | POST | `/api/v1/[resources]` | CommandService | Command |
| 全体更新 | PUT | `/api/v1/[resources]/{id}` | CommandService | Command |
| 部分更新 | PATCH | `/api/v1/[resources]/{id}` | CommandService | Command |
| 削除 | DELETE | `/api/v1/[resources]/{id}` | CommandService | Command |
| 状態遷移 | PATCH | `/api/v1/[resources]/{id}/[action]` | CommandService | Command |

---

## 4. UCからCommand/Queryの分類

AG_アジルームの120ユースケースをCommand/Queryに分類します。

### 4.1 AG-01 プロダクトディスカバリー (26UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-DIS-001 | Product Visionを作成する | Command | ProductVisionCommandService | POST |
| UC-DIS-002 | Product Visionを更新する | Command | ProductVisionCommandService | PUT |
| UC-DIS-003 | Product Visionを参照する | Query | ProductVisionQueryService | GET |
| UC-DIS-004 | Product Goalを作成する | Command | ProductGoalCommandService | POST |
| UC-DIS-005 | Product Goalを更新する | Command | ProductGoalCommandService | PUT |
| UC-DIS-006 | Product Goalを参照する | Query | ProductGoalQueryService | GET |
| UC-DIS-007 | 課題を登録する | Command | IssueCommandService | POST |
| UC-DIS-008 | 課題を更新する | Command | IssueCommandService | PUT |
| UC-DIS-009 | 課題を参照する | Query | IssueQueryService | GET |
| UC-DIS-010 | 仮説を作成する | Command | HypothesisCommandService | POST |
| UC-DIS-011 | 仮説を更新する | Command | HypothesisCommandService | PUT |
| UC-DIS-012 | 仮説を参照する | Query | HypothesisQueryService | GET |
| UC-DIS-013 | 要望を登録する | Command | RequestCommandService | POST |
| UC-DIS-014 | ステークホルダーを登録する | Command | StakeholderCommandService | POST |
| UC-DIS-015 | ステークホルダーを更新する | Command | StakeholderCommandService | PUT |
| UC-DIS-016 | ステークホルダーを参照する | Query | StakeholderQueryService | GET |
| UC-DIS-017 | ペルソナを作成する | Command | PersonaCommandService | POST |
| UC-DIS-018 | ペルソナを更新する | Command | PersonaCommandService | PUT |
| UC-DIS-019 | ユーザージャーニーを作成する | Command | UserJourneyCommandService | POST |
| UC-DIS-020 | 成功指標を登録する | Command | SuccessMetricCommandService | POST |
| UC-DIS-021 | 成功指標を更新する | Command | SuccessMetricCommandService | PUT |
| UC-DIS-022 | Kano質問票を作成する | Command | KanoQuestionCommandService | POST |
| UC-DIS-023 | Kano評価を実施する | Command | KanoEvaluationCommandService | POST |
| UC-DIS-024 | Kano評価結果を参照する | Query | KanoEvaluationQueryService | GET |
| UC-DIS-025 | 実験計画を作成する | Command | ExperimentCommandService | POST |
| UC-DIS-026 | 実験結果を記録する | Command | ExperimentCommandService | PUT |

### 4.2 AG-02 要求バックログ管理 (25UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-BL-001 | 課題を登録する | Command | BacklogIssueCommandService | POST |
| UC-BL-002 | 課題を更新する | Command | BacklogIssueCommandService | PUT |
| UC-BL-003 | 課題を参照する | Query | BacklogIssueQueryService | GET |
| UC-BL-004 | フィードバックを登録する | Command | FeedbackCommandService | POST |
| UC-BL-005 | フィードバックを更新する | Command | FeedbackCommandService | PUT |
| UC-BL-006 | フィードバックを参照する | Query | FeedbackQueryService | GET |
| UC-BL-007 | バックログ項目を作成する | Command | BacklogItemCommandService | POST |
| UC-BL-008 | バックログ項目を更新する | Command | BacklogItemCommandService | PUT |
| UC-BL-009 | バックログ項目を参照する | Query | BacklogItemQueryService | GET |
| UC-BL-010 | バックログ項目を分割する | Command | BacklogItemCommandService | POST |
| UC-BL-011 | バックログ階層を設定する | Command | BacklogHierarchyCommandService | POST |
| UC-BL-012 | 優先順位を設定する | Command | PriorityCommandService | POST |
| UC-BL-013 | 優先順位を更新する | Command | PriorityCommandService | PUT |
| UC-BL-014 | 業務ルールを登録する | Command | BusinessRuleCommandService | POST |
| UC-BL-015 | 受入条件を定義する | Command | AcceptanceCriteriaCommandService | POST |
| UC-BL-016 | コメントを投稿する | Command | CommentCommandService | POST |
| UC-BL-017 | メンション通知を送信する | Command | NotificationCommandService | POST |
| UC-BL-018 | バックログリファインメントを行う | Command | BacklogRefinementCommandService | POST |
| UC-BL-019 | 依存関係を定義する | Command | DependencyCommandService | POST |
| UC-BL-020 | 変更履歴を参照する | Query | ChangeHistoryQueryService | GET |
| UC-BL-021 | 要求の関連を設定する | Command | RequirementRelationCommandService | POST |
| UC-BL-022 | 意思決定を記録する | Command | DecisionCommandService | POST |
| UC-BL-023 | Story Mappingを行う | Command | StoryMapCommandService | POST |
| UC-BL-024 | 見積もりを記録する | Command | EstimateCommandService | POST |
| UC-BL-025 | ソース管理情報を関連付ける | Command | SourceControlCommandService | POST |

### 4.3 AG-03 優先順位決定 (10UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-PR-001 | 優先順位を設定する | Command | PriorityCommandService | POST |
| UC-PR-002 | 優先順位を更新する | Command | PriorityCommandService | PUT |
| UC-PR-003 | 優先順位を参照する | Query | PriorityQueryService | GET |
| UC-PR-004 | 判断理由を記録する | Command | PriorityReasonCommandService | POST |
| UC-PR-005 | 判断理由を参照する | Query | PriorityReasonQueryService | GET |
| UC-PR-006 | Kano質問票を作成する | Command | KanoQuestionCommandService | POST |
| UC-PR-007 | Kano評価を実施する | Command | KanoEvaluationCommandService | POST |
| UC-PR-008 | Kano評価結果を参照する | Query | KanoEvaluationQueryService | GET |
| UC-PR-009 | 価値・リスク・コストを比較する | Query | ValueRiskCostQueryService | GET |
| UC-PR-010 | Kano分類を用いて優先順位を検討する | Query | KanoPriorityQueryService | GET |

### 4.4 AG-04 スプリント実行 (19UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-SP-001 | スプリントを作成する | Command | SprintCommandService | POST |
| UC-SP-002 | スプリントを更新する | Command | SprintCommandService | PUT |
| UC-SP-003 | スプリントを参照する | Query | SprintQueryService | GET |
| UC-SP-004 | Sprint Goalを設定する | Command | SprintGoalCommandService | POST |
| UC-SP-005 | Sprint Goalを更新する | Command | SprintGoalCommandService | PUT |
| UC-SP-006 | Sprint Backlogを作成する | Command | SprintBacklogCommandService | POST |
| UC-SP-007 | Sprint Backlogを更新する | Command | SprintBacklogCommandService | PUT |
| UC-SP-008 | ボードを更新する | Command | BoardCommandService | PATCH |
| UC-SP-009 | 障害を登録する | Command | ImpedimentCommandService | POST |
| UC-SP-010 | 障害を更新する | Command | ImpedimentCommandService | PUT |
| UC-SP-011 | 障害を参照する | Query | ImpedimentQueryService | GET |
| UC-SP-012 | 依存関係を定義する | Command | DependencyCommandService | POST |
| UC-SP-013 | 通知を送信する | Command | NotificationCommandService | POST |
| UC-SP-014 | スプリント進捗を確認する | Query | SprintProgressQueryService | GET |
| UC-SP-015 | インクリメントを完成確認する | Command | IncrementCommandService | PATCH |
| UC-SP-016 | デイリースクラムを記録する | Command | DailyScrumCommandService | POST |
| UC-SP-017 | 作業負荷を確認する | Query | WorkloadQueryService | GET |
| UC-SP-018 | ソース管理情報を関連付ける | Command | SourceControlCommandService | POST |
| UC-SP-019 | ビルド・デプロイ状況を確認する | Query | BuildDeployQueryService | GET |

### 4.5 AG-05 品質検証 (19UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-QL-001 | Definition of Doneを定義する | Command | DefinitionOfDoneCommandService | POST |
| UC-QL-002 | Definition of Doneを更新する | Command | DefinitionOfDoneCommandService | PUT |
| UC-QL-003 | Definition of Doneを参照する | Query | DefinitionOfDoneQueryService | GET |
| UC-QL-004 | 欠陥を登録する | Command | DefectCommandService | POST |
| UC-QL-005 | 欠陥を更新する | Command | DefectCommandService | PUT |
| UC-QL-006 | 欠陥を参照する | Query | DefectQueryService | GET |
| UC-QL-007 | テストケースを作成する | Command | TestCaseCommandService | POST |
| UC-QL-008 | テストケースを更新する | Command | TestCaseCommandService | PUT |
| UC-QL-009 | テストケースを参照する | Query | TestCaseQueryService | GET |
| UC-QL-010 | 受入テストを実行する | Command | AcceptanceTestCommandService | POST |
| UC-QL-011 | テスト結果を記録する | Command | TestResultCommandService | POST |
| UC-QL-012 | テスト結果を参照する | Query | TestResultQueryService | GET |
| UC-QL-013 | 性能要件を定義する | Command | PerformanceRequirementCommandService | POST |
| UC-QL-014 | 性能テストを実行する | Command | PerformanceTestCommandService | POST |
| UC-QL-015 | 性能テスト結果を参照する | Query | PerformanceTestResultQueryService | GET |
| UC-QL-016 | Gatlingレポートを取り込む | Command | GatlingReportCommandService | POST |
| UC-QL-017 | セキュリティリスクを登録する | Command | SecurityRiskCommandService | POST |
| UC-QL-018 | セキュリティリスクを更新する | Command | SecurityRiskCommandService | PUT |
| UC-QL-019 | セキュリティリスクを参照する | Query | SecurityRiskQueryService | GET |

### 4.6 AG-06 成果適応 (21UC)

| ユースケースID | ユースケース名 | Command/Query | 対応Service | HTTPメソッド |
|----------------|----------------|---------------|-------------|-------------|
| UC-LR-001 | Sprint Reviewを記録する | Command | SprintReviewCommandService | POST |
| UC-LR-002 | Sprint Reviewを参照する | Query | SprintReviewQueryService | GET |
| UC-LR-003 | Sprint Retrospectiveを記録する | Command | RetrospectiveCommandService | POST |
| UC-LR-004 | Sprint Retrospectiveを参照する | Query | RetrospectiveQueryService | GET |
| UC-LR-005 | 改善アクションを登録する | Command | ImprovementActionCommandService | POST |
| UC-LR-006 | 改善アクションを更新する | Command | ImprovementActionCommandService | PUT |
| UC-LR-007 | 改善アクションを参照する | Query | ImprovementActionQueryService | GET |
| UC-LR-008 | 改善効果を確認する | Query | ImprovementEffectQueryService | GET |
| UC-LR-009 | ダッシュボードを表示する | Query | DashboardQueryService | GET |
| UC-LR-010 | プロジェクト進捗を確認する | Query | ProjectProgressQueryService | GET |
| UC-LR-011 | リスク一覧を確認する | Query | RiskListQueryService | GET |
| UC-LR-012 | 判断待ち一覧を確認する | Query | PendingDecisionQueryService | GET |
| UC-LR-013 | 利用者レビューを計画する | Command | UserReviewCommandService | POST |
| UC-LR-014 | 利用者レビューを実行する | Command | UserReviewCommandService | POST |
| UC-LR-015 | 利用者レビュー結果を参照する | Query | UserReviewResultQueryService | GET |
| UC-LR-016 | Product Goal進捗を確認する | Query | ProductGoalProgressQueryService | GET |
| UC-LR-017 | 成果指標を測定する | Query | OutcomeMetricQueryService | GET |
| UC-LR-018 | レポートを出力する | Query | ReportQueryService | GET |
| UC-LR-019 | Epic／Releaseを振り返る | Command | EpicReleaseReviewCommandService | POST |
| UC-LR-020 | 品質・性能Insightsを確認する | Query | QualityPerformanceInsightsQueryService | GET |
| UC-LR-021 | 稼働状況を確認する | Query | OperationStatusQueryService | GET |

---

## 5. APIエンドポイント一覧

### 5.1 プロダクト管理系

#### Product Vision

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/product-visions` | POST | Product Visionを作成する | UC-DIS-001 | ProductVisionCommandService |
| `/api/v1/product-visions/{id}` | PUT | Product Visionを更新する | UC-DIS-002 | ProductVisionCommandService |
| `/api/v1/product-visions/{id}` | GET | Product Visionを参照する | UC-DIS-003 | ProductVisionQueryService |

#### Product Goal

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/product-goals` | POST | Product Goalを作成する | UC-DIS-004 | ProductGoalCommandService |
| `/api/v1/product-goals/{id}` | PUT | Product Goalを更新する | UC-DIS-005 | ProductGoalCommandService |
| `/api/v1/product-goals/{id}` | GET | Product Goalを参照する | UC-DIS-006 | ProductGoalQueryService |

### 5.2 スプリント実行系

#### Sprints

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/sprints` | POST | スプリントを作成する | UC-SP-001 | SprintCommandService |
| `/api/v1/sprints/{id}` | PUT | スプリントを更新する | UC-SP-002 | SprintCommandService |
| `/api/v1/sprints` | GET | スプリント一覧を取得 | UC-SP-003 | SprintQueryService |
| `/api/v1/sprints/{id}` | GET | スプリント詳細を取得 | UC-SP-003 | SprintQueryService |
| `/api/v1/sprints/{id}/start` | PATCH | スプリントを開始する | UC-SP-002 | SprintCommandService |
| `/api/v1/sprints/{id}/complete` | PATCH | スプリントを完了する | UC-SP-002 | SprintCommandService |
| `/api/v1/sprints/{id}/stats` | GET | スプリント統計情報を取得 | UC-SP-014 | SprintProgressQueryService |

#### Tasks

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/tasks` | POST | タスクを作成 | UC-SP-006 | TaskCommandService |
| `/api/v1/tasks/{id}` | PUT | タスクを更新 | UC-SP-007 | TaskCommandService |
| `/api/v1/tasks` | GET | タスク一覧を取得 | UC-SP-014 | TaskQueryService |
| `/api/v1/tasks/{id}` | GET | タスク詳細を取得 | UC-SP-014 | TaskQueryService |
| `/api/v1/tasks/{id}/status` | PATCH | タスク状態を変更 | UC-SP-008 | TaskCommandService |
| `/api/v1/tasks/{id}` | DELETE | タスクを削除 | UC-SP-007 | TaskCommandService |
| `/api/v1/sprints/{sprintId}/tasks` | GET | スプリントのタスク一覧を取得 | UC-SP-014 | TaskQueryService |

#### Impediments

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/impediments` | POST | 障害を登録 | UC-SP-009 | ImpedimentCommandService |
| `/api/v1/impediments/{id}` | PUT | 障害を更新 | UC-SP-010 | ImpedimentCommandService |
| `/api/v1/impediments` | GET | 障害一覧を取得 | UC-SP-011 | ImpedimentQueryService |
| `/api/v1/impediments/{id}` | GET | 障害詳細を取得 | UC-SP-011 | ImpedimentQueryService |
| `/api/v1/impediments/{id}/status` | PATCH | 障害状態を変更 | UC-SP-010 | ImpedimentCommandService |
| `/api/v1/sprints/{sprintId}/impediments` | GET | スプリントの障害一覧を取得 | UC-SP-011 | ImpedimentQueryService |

#### Dependencies

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/dependencies` | POST | 依存関係を定義 | UC-SP-012 | DependencyCommandService |
| `/api/v1/dependencies` | GET | 依存関係一覧を取得 | UC-SP-012 | DependencyQueryService |
| `/api/v1/dependencies/{id}` | GET | 依存関係詳細を取得 | UC-SP-012 | DependencyQueryService |
| `/api/v1/dependencies/{id}` | PUT | 依存関係を更新 | UC-SP-012 | DependencyCommandService |
| `/api/v1/dependencies/{id}` | DELETE | 依存関係を削除 | UC-SP-012 | DependencyCommandService |

#### Notifications

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/notifications` | POST | 通知を送信 | UC-SP-013 | NotificationCommandService |
| `/api/v1/notifications` | GET | 通知一覧を取得 | UC-SP-013 | NotificationQueryService |
| `/api/v1/notifications/{id}` | GET | 通知詳細を取得 | UC-SP-013 | NotificationQueryService |
| `/api/v1/notifications/{id}/read` | PATCH | 通知を読み済みにする | UC-SP-013 | NotificationCommandService |
| `/api/v1/notifications/read-all` | PATCH | 全通知を読み済みにする | UC-SP-013 | NotificationCommandService |

#### Activities

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/activities` | GET | アクティビティ一覧を取得 | UC-SP-014 | ActivityQueryService |
| `/api/v1/sprints/{sprintId}/activities` | GET | スプリントのアクティビティ一覧を取得 | UC-SP-014 | ActivityQueryService |

#### Daily Scrums

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/daily-scrums` | POST | デイリースクラムを記録 | UC-SP-016 | DailyScrumCommandService |
| `/api/v1/daily-scrums` | GET | デイリースクラム一覧を取得 | UC-SP-016 | DailyScrumQueryService |
| `/api/v1/daily-scrums/{id}` | GET | デイリースクラム詳細を取得 | UC-SP-016 | DailyScrumQueryService |
| `/api/v1/daily-scrums/{id}` | PUT | デイリースクラムを更新 | UC-SP-016 | DailyScrumCommandService |
| `/api/v1/daily-scrums/{id}` | DELETE | デイリースクラムを削除 | UC-SP-016 | DailyScrumCommandService |
| `/api/v1/sprints/{sprintId}/daily-scrums` | GET | スプリントのスクラム一覧を取得 | UC-SP-016 | DailyScrumQueryService |

### 5.3 品質検証系

#### Definition of Done

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/definition-of-done` | POST | Definition of Doneを定義する | UC-QL-001 | DefinitionOfDoneCommandService |
| `/api/v1/definition-of-done/{id}` | PUT | Definition of Doneを更新する | UC-QL-002 | DefinitionOfDoneCommandService |
| `/api/v1/definition-of-done/{id}` | GET | Definition of Doneを参照する | UC-QL-003 | DefinitionOfDoneQueryService |

#### Defects

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/defects` | POST | 欠陥を登録する | UC-QL-004 | DefectCommandService |
| `/api/v1/defects/{id}` | PUT | 欠陥を更新する | UC-QL-005 | DefectCommandService |
| `/api/v1/defects` | GET | 欠陥一覧を取得する | UC-QL-006 | DefectQueryService |
| `/api/v1/defects/{id}` | GET | 欠陥詳細を取得する | UC-QL-006 | DefectQueryService |

#### Test Cases

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/test-cases` | POST | テストケースを作成する | UC-QL-007 | TestCaseCommandService |
| `/api/v1/test-cases/{id}` | PUT | テストケースを更新する | UC-QL-008 | TestCaseCommandService |
| `/api/v1/test-cases` | GET | テストケース一覧を取得する | UC-QL-009 | TestCaseQueryService |
| `/api/v1/test-cases/{id}` | GET | テストケース詳細を取得する | UC-QL-009 | TestCaseQueryService |

### 5.4 成果適応系

#### Sprint Reviews

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/sprint-reviews` | POST | Sprint Reviewを記録する | UC-LR-001 | SprintReviewCommandService |
| `/api/v1/sprint-reviews` | GET | Sprint Review一覧を取得する | UC-LR-002 | SprintReviewQueryService |
| `/api/v1/sprint-reviews/{id}` | GET | Sprint Review詳細を取得する | UC-LR-002 | SprintReviewQueryService |

#### Retrospectives

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/retrospectives` | POST | Sprint Retrospectiveを記録する | UC-LR-003 | RetrospectiveCommandService |
| `/api/v1/retrospectives` | GET | Retrospective一覧を取得する | UC-LR-004 | RetrospectiveQueryService |
| `/api/v1/retrospectives/{id}` | GET | Retrospective詳細を取得する | UC-LR-004 | RetrospectiveQueryService |

#### Improvement Actions

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/improvement-actions` | POST | 改善アクションを登録する | UC-LR-005 | ImprovementActionCommandService |
| `/api/v1/improvement-actions/{id}` | PUT | 改善アクションを更新する | UC-LR-006 | ImprovementActionCommandService |
| `/api/v1/improvement-actions` | GET | 改善アクション一覧を取得する | UC-LR-007 | ImprovementActionQueryService |
| `/api/v1/improvement-actions/{id}` | GET | 改善アクション詳細を取得する | UC-LR-007 | ImprovementActionQueryService |

#### Dashboard

| エンドポイント | HTTPメソッド | 説明 | 対応UC | Service |
|---------------|-------------|------|--------|---------|
| `/api/v1/dashboard` | GET | ダッシュボードを表示する | UC-LR-009 | DashboardQueryService |
| `/api/v1/dashboard/project-progress` | GET | プロジェクト進捗を確認する | UC-LR-010 | ProjectProgressQueryService |
| `/api/v1/dashboard/risks` | GET | リスク一覧を確認する | UC-LR-011 | RiskListQueryService |
| `/api/v1/dashboard/pending-decisions` | GET | 判断待ち一覧を確認する | UC-LR-012 | PendingDecisionQueryService |

---

## 6. Command/Queryオブジェクト

### 6.1 Commandオブジェクト

書き込みリクエストを表現する不変の値オブジェクトです。

```java
// 例: CreateSprintCommand
public final class CreateSprintCommand {
    @NotNull
    private final Long productId;
    
    @NotNull
    @Min(1)
    private final Integer sprintNumber;
    
    @NotNull
    @Size(min = 1, max = 100)
    private final String name;
    
    @Size(max = 1000)
    private final String goal;
    
    @NotNull
    private final LocalDate startDate;
    
    @NotNull
    @Future
    private final LocalDate endDate;
    
    @NotNull
    private final Long teamId;
}
```

### 6.2 Queryオブジェクト

読み込みリクエストを表現する不変の値オブジェクトです。

```java
// 例: ListSprintsQuery
public final class ListSprintsQuery {
    private final Long productId;
    private final String status;
    private final String keyword;
    private final LocalDate startDateFrom;
    private final LocalDate startDateTo;
    private final int pageNumber;
    private final int pageSize;
    private final String sort;
}
```

---

## 7. 共通設計

### 7.1 リクエスト・レスポンス形式

- **リクエストボディ**: JSON形式 (`application/json`)
- **レスポンスボディ**: JSON形式 (`application/json`)
- **文字エンコーディング**: UTF-8

### 7.2 ページネーション

| パラメータ | 型 | 説明 | デフォルト | 最大値 |
|-----------|-----|------|-----------|--------|
| `pageNumber` | integer | ページ番号 (0起点) | 0 | - |
| `pageSize` | integer | ページサイズ | 20 | 100 |
| `sort` | string | ソート条件 | `createdAt,DESC` | - |

### 7.3 レスポンス形式

```json
{
  "data": [...],
  "metadata": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 100,
    "totalPages": 5
  }
}
```

### 7.4 エラーレスポンス形式

```json
{
  "error": {
    "status": 400,
    "code": "VALIDATION_ERROR",
    "message": "リクエストの検証に失敗しました",
    "details": [
      {
        "field": "title",
        "message": "タイトルは必須です"
      }
    ],
    "timestamp": "2024-01-01T00:00:00Z"
  }
}
```

---

## 8. 認証・認可

- **認証方式**: JWT (Bearer Token)
- **認可方式**: RBAC (Role-Based Access Control)
- **ヘッダー**: `Authorization: Bearer <token>`

### 8.1 役割一覧

| 役割 | 説明 |
|-----|------|
| PRODUCT_OWNER | プロダクトオーナー |
| SCRUM_MASTER | スクラムマスター |
| DEVELOPER | 開発者 |
| VIEWER | 閲覧者 |

---

## 9. バリデーション

- **Jakarta Validation**: `@NotNull`, `@Size`, `@Email` など
- **カスタムバリデーター**: ビジネスロジックに依存するバリデーション

---

## 10. 参照

- 要件定義: `docs/22 要件定義書/AG アジルーム/03 機能要件/`
- データベース設計: `docs/23 詳細設計書/AG_アジルーム/01 データベース設計/`
- フロントエンド設計: `docs/23 詳細設計書/AG_アジルーム/02 フロントエンド設計/`
- api-designスキル: `.agents/skills/api-design/SKILL.md`
