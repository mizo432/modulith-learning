---
sessionId: session-261109-152821-vhfp
---

# Requirements

### Overview & Goals

JB ジョブ管理（Jobloom）事業領域の詳細設計書を作成します。Jobloomは、複数の業務システムに組み込まれたジョブを、統一的に起動・監視・管理するための分散ジョブ実行・運用管理システムです。

### Scope

**In Scope:**
- JB ジョブ管理事業領域の詳細設計書の作成
- データベース設計（17個のテーブル）
- API設計（27個のUCに対応）
- バッチ設計（スケジュール起動、通知送信）
- フロントエンド設計（26個の画面）

**Out of Scope:**
- 実装コードの作成
- テストコードの作成
- 他の事業領域の詳細設計

### User Stories

- As a システム管理者, I want ジョブ定義を登録・変更・無効化できるAPI so that ジョブの管理ができる
- As a 運用管理者, I want ジョブを手動起動・再実行・キャンセルできるAPI so that ジョブの復旧操作ができる
- As a 運用管理者, I want 実行状況・履歴を確認できるAPI so that ジョブの監視ができる
- As a システム管理者, I want スケジュールを登録・変更できるAPI so that 定期実行の設定ができる
- As a システム管理者, I want 通知設定を管理できるAPI so that 通知の配信設定ができる

### Functional Requirements

1. **ジョブ定義管理**: ジョブの定義情報（コード、パラメータ、実行条件、バージョン）のCRUD
2. **ジョブ実行管理**: ジョブの起動・監視・キャンセル・再実行・途中再開
3. **スケジュール管理**: 定期実行のスケジュール登録・変更・参照
4. **実行履歴管理**: 実行結果・履歴の検索・参照
5. **通知管理**: 通知設定の管理と通知履歴の確認
6. **ユーザー・権限管理**: ユーザーのロール、会社・店舗の参照権限の設定
7. **監査ログ**: 操作履歴の記録と確認
8. **サーバー配備管理**: アプリケーションサーバーとジョブの配備情報の管理

# Technical Design

### Current Implementation

- 要件定義書は `docs/22 要件定義書/JB ジョブ管理/` に存在
- 詳細設計書は `docs/23 詳細設計書/JB_ジョブ管理/` に作成予定
- 既存の詳細設計書のフォーマットを参照（AS_関連付け、AG_アジルーム）

### Key Decisions

1. **スキーマ名**: `undecided_jb` を使用
2. **テーブル命名**: 情報のエンティティ名を使用（例: `job_definition`, `job_execution`）
3. **APIパス**: リソース名（名詞の複数形）を使用（例: `/api/jb/job-definitions`）
4. **フレームワーク**: Spring Boot (Backend), MUI v5 (Frontend)
5. **データベース**: PostgreSQL

### Proposed Changes

1. **データベース設計**: 17個のテーブルを定義
   - `job_definition`: ジョブ定義情報
   - `job_deployment`: ジョブ配備情報
   - `application_server`: アプリケーションサーバー情報
   - `job_execution`: ジョブ実行情報
   - `execution_slot`: 実行枠管理
   - `execution_event`: 実行イベント
   - `company_permission`: 会社権限
   - `store_permission`: 店舗権限
   - `notification_setting`: 通知設定
   - `upload_file`: アップロードファイル
   - `audit_log`: 監査ログ
   - `job_execution_status`: ジョブ実行状態（マスター）
   - `schedule_definition`: スケジュール定義
   - `schedule_fire_record`: スケジュール発火記録
   - `user_info`: ユーザー情報
   - `notification_history`: 通知履歴
   - `execution_file_relation`: 実行とファイルの関連

2. **API設計**: 27個のUCに対応するREST APIを定義
   - ジョブ定義管理API（CRUD）
   - ジョブ実行管理API（起動・監視・キャンセル・再実行）
   - スケジュール管理API（CRUD）
   - 実行履歴API（検索・参照）
   - 通知管理API（設定・履歴）
   - ユーザー・権限管理API
   - 監査ログAPI
   - サーバー配備管理API

3. **バッチ設計**: 2つのバッチを定義
   - スケジュール起動バッチ
   - 通知送信バッチ

4. **フロントエンド設計**: 26個の画面を定義
   - ダッシュボード
   - ジョブ実行状況一覧・詳細
   - 実行ログ・エラー詳細
   - ジョブ実行履歴一覧・詳細
   - ジョブ定義一覧・詳細・登録・編集
   - スケジュール一覧・登録・編集
   - ジョブ手動起動画面
   - 再実行・途中再開・キャンセル確認画面
   - 復旧操作履歴一覧
   - アプリケーションサーバー一覧・詳細
   - ジョブ配備状況一覧・詳細
   - 通知一覧・設定
   - ユーザー一覧・権限編集
   - 会社・店舗管理
   - 監査ログ一覧

### Architecture Diagram

``` mermaid
graph LR
    A[Frontend] --> B[API Gateway]
    B --> C[Backend Service]
    C --> D[Database]
    C --> E[S3/MinIO]
    C --> F[OAuth2 Server]
    C --> G[Notification Service]
```

### File Structure

```
docs/23 詳細設計書/JB_ジョブ管理/
├── index.md
├── 01 データベース設計/
│   ├── index.md
│   ├── 01 table一覧.md
│   ├── 01 table一覧.tsv
│   ├── 02 テーブルカラム定義/
│   │   ├── index.md
│   │   └── [テーブル名].md
│   └── 03 viewカラム定義/
│       └── index.md
├── frontend/
│   ├── index.md
│   └── [画面ID]_画面設計.md
└── backend/
    ├── api/
    │   ├── index.md
    │   └── [API名].md
    └── batch/
        ├── index.md
        └── [バッチ名].md
```

# Delivery Steps

###   Step 1: フェーズ0: 要件定義書の読み込み
JB ジョブ管理の要件定義書を読み込み、RDRAモデルの全体像を把握します。

- アクター一覧、外部システム一覧、要求一覧を確認
- ユースケース一覧（27個のUC）を確認
- 情報一覧（17個の情報）を確認
- 画面一覧（26個の画面）を確認
- ステート一覧、バリエーション一覧を確認

###   Step 2: フェーズ1: データベース設計
情報からテーブルを導出、属性からカラムを定義します。

- テーブル一覧の作成（17個のテーブル）
- 各テーブルのカラム定義を作成
- 外部キー関係を定義
- 列挙型（Enum）を定義
- Flywayマイグレーションスクリプトを作成
- スキーマ名: `undecided_jb`

###   Step 3: フェーズ2: API設計
画面経由のUCからREST APIを導出します。

- APIアーキテクチャの定義
- セキュリティ設計（認証・認可）
- ページネーション設計
- バリデーションルール
- エラーハンドリング
- 監視・ログ設計
- 各UCに対応するAPIエンドポイントを定義

###   Step 4: フェーズ3: バッチ設計
外部システム経由のUCからバッチを導出します。

- スケジュール起動バッチ（UC-JB-010）
- 通知送信バッチ（UC-JB-019）
- バッチのトリガー・スケジュールを定義
- 入力・出力データを定義

###   Step 5: フェーズ4: フロントエンド設計
画面属性からページコンポーネントを導出します。

- 画面イメージ（ASCII ワイヤーフレーム）を描画
- MUI v5 コンポーネントを定義
- TypeScript 型定義を定義
- ステート管理を定義
- API連携ポイントを定義
- 画面遷移図（Mermaid 形式）を描画
- アクセシビリティ設計を定義

###   Step 6: フェーズ5: 整合性確認
要件定義と詳細設計の整合性を確認します。

- 全UCがAPIまたはバッチとしてカバーされているか確認
- 全情報がテーブルとして定義されているか確認
- 全画面がフロントエンド設計として定義されているか確認