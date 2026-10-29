# フェーズ5：システム設計の実装

## 概要

フェーズ4（DDDモデル候補）を基に、実際のシステム設計を実装します。
4つのモジュール（job-definition, job-execution, execution-monitoring, permission-management）のコード構造を作成します。

## 実装計画

### Step 1: 既存のプロジェクト構造確認
backendディレクトリの既存構造を確認し、実装の起点を把握します。

### Step 2: job-definitionモジュール実装
- ドメイン層：JobDefinition, JobDeployment, Scheduleのエンティティ・値オブジェクト・リポジトリ
- アプリケーション層：コマンド・クエリ・ハンドラー
- インフラストラクチャ層：JPAエンティティ・リポジトリ実装
- プレゼンテーション層：RESTコントローラー・DTO

### Step 3: job-executionモジュール実装
- ドメイン層：JobExecution, ExecutionSlotのエンティティ・値オブジェクト・リポジトリ
- ドメインサービス：IdempotencyCheck, ConcurrentExecutionLimit, ReExecutionEligibility, ResumeEligibility, UnknownResultRecovery
- アプリケーション層：コマンド・クエリ・ハンドラー
- インフラストラクチャ層：JPAエンティティ・リポジトリ実装
- プレゼンテーション層：RESTコントローラー・DTO

### Step 4: execution-monitoringモジュール実装
- ドメイン層：NotificationSettingのエンティティ・値オブジェクト・リポジトリ
- アプリケーション層：コマンド・クエリ・ハンドラー
- インフラストラクチャ層：JPAエンティティ・リポジトリ実装、通知サービス（Email, Slack, Webhook）
- プレゼンテーション層：RESTコントローラー・DTO

### Step 5: permission-managementモジュール実装
- ドメイン層：UserPermission, Company, Store, AuditLogのエンティティ・値オブジェクト・リポジトリ
- ドメインサービス：ExecutionPermissionCheck, CompanyStoreFilter
- アプリケーション層：コマンド・クエリ・ハンドラー
- インフラストラクチャ層：JPAエンティティ・リポジトリ実装
- プレゼンテーション層：RESTコントローラー・DTO

### Step 6: モジュール間の統合
- ドメインイベントの連携
- リポジトリインターフェースの共有
- ドメインサービスの連携

### Step 7: ビルド確認
- Gradleビルドの実行
- コンパイルエラーの確認と修正

## 成果物

- 4つのモジュールのコード構造
- 各モジュールのドメインモデル
- アプリケーション層のCQRS構造
- インフラストラクチャ層のJPA実装
- プレゼンテーション層のREST API
