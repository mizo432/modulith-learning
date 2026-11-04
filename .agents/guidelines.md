# モジュラーモノリス開発ガイドライン (Modular Monolith Development Guidelines)

このドキュメントは、 **modulith-learning** プロジェクトの開発ガイドラインのインデックスです。
各トピックの詳細は、以下のファイルをご覧ください。

## プロジェクト概要

プロジェクトの全体像、アーキテクチャ、技術スタック、セットアップ方法については、[README.md](README.md) を参照してください。

### 主な特徴

- **Spring Modulith** を採用したモジュラーモノリスアーキテクチャ
- **オニオンアーキテクチャ** と **DDD（ドメイン駆動設計）** をベースとした設計
- **Spring Cloud** を基盤としたマイクロサービス構成
- 事業領域ごとにモジュールを分離した設計

### モジュール構成

| モジュール               | 説明                                                                                                                      | ポート |
|--------------------------|---------------------------------------------------------------------------------------------------------------------------|--------|
| **backend**              | Spring Modulith を採用したコア業務アプリケーション。オニオンアーキテクチャとDDDに基づき、事業領域ごとにサブドメインを分離 | 8080   |
| **service-registry**     | Netflix Eureka Server を用いたサービスレジストリ                                                                          | 8761   |
| **api-gateway**          | Spring Cloud Gateway によるAPIゲートウェイ。OAuth2 リソースサーバーとして認証・認可を担う                                 | 8085   |
| **authorization**        | Spring Authorization Server によるOAuth2 認可サーバー                                                                     | 8443   |
| **frontend**             | React + TypeScript + Material-UI によるフロントエンドアプリケーション                                                     | 3000   |
| **mizo432-shared-common**| 共通ユーティリティ・共有コンポーネント                                                                                     | -      |

### 事業領域（サブドメイン）

| コード | 事業領域名                  | 区分 | 説明                   |
|--------|-----------------------------|------|------------------------|
| AG     | アジルーム (Agiloom)        | コア | Scrum プロジェクト管理 |
| EF     | 電子帳票 (Electronic Forms) | 汎用 | 帳票処理               |
| AS     | 関連付け (Association)      | 支援 | 関連付け管理           |
| JM     | ジョブ管理 (Job Management) | 汎用 | ジョブ・権限管理       |
| AR     | 承認申請 (Approval Request) | 汎用 | 承認ワークフロー       |
| AZ     | 認可 (Authorization)        | 支援 | 認可管理               |
| NT     | 通知 (Notification)         | 汎用 | 通知処理               |
| DO     | 開発運用 (DevOps)           | 支援 | 開発・運用支援         |
| CA     | カレンダーレジストリ        | 汎用 | カレンダー管理         |
| TA     | 税率レジストリ              | 汎用 | 税率管理               |
| AD     | 住所レジストリ              | 汎用 | 住所管理               |
| ER     | 元号レジストリ              | 汎用 | 元号管理               |
| FI     | 金融機関レジストリ          | 汎用 | 金融機関情報管理       |
| EX     | サンプル (Example)          | 支援 | 実装サンプル           |

---

## ガイドラインファイル一覧

### バックエンド関連

| ファイル                                                                                                        | 説明                                             |
|-----------------------------------------------------------------------------------------------------------------|--------------------------------------------------|
| [backend/projects.instructions.md](guidelines/backend/projects.instructions.md)                                 | プロジェクト概要、アーキテクチャ、モジュール構造 |
| [backend/commands.instructions.md](guidelines/backend/commands.instructions.md)                                 | ビルド・起動コマンド、テストコマンド             |
| [backend/developers.instructions.md](guidelines/backend/developers.instructions.md)                             | 技術スタック、開発環境の要件                     |
| [backend/environments.instructions.md](guidelines/backend/environments.instructions.md)                         | 環境構築方法、トラブルシューティング             |
| [backend/testing.guidelines.md](guidelines/backend/testing.guidelines.md)                                       | テスト戦略、テスト作成規約                       |
| [backend/jpa.guidelines.md](guidelines/backend/jpa.guidelines.md)                                               | Spring Data JPA 実装規則                         |
| [backend/spring-modulith.guidelines.md](guidelines/backend/spring-modulith.guidelines.md)                       | Spring Modulith 実装規則                         |
| [backend/flyway-migration-checklist.guidelines.md](guidelines/backend/flyway-migration-checklist.guidelines.md) | Flyway マイグレーションスクリプト チェックリスト |

### Java 関連

| ファイル                                                                              | 説明                                                   |
|---------------------------------------------------------------------------------------|--------------------------------------------------------|
| [java/coding-standards.guidelines.md](guidelines/java/coding-standards.guidelines.md) | コーディング規約、レイヤードアーキテクチャ、バッチ処理 |
| [java/review.guidelines.md](guidelines/java/review.guidelines.md)                     | コードレビュー手順とチェックポイント                   |

### DDD 関連

| ファイル                                                                                    | 説明                               |
|---------------------------------------------------------------------------------------------|------------------------------------|
| [ddd/ddd.guideline.md](guidelines/ddd/ddd.guideline.md)                                     | ドメイン駆動設計の基本原則         |
| [ddd/immutable-data-model.guidelines.md](guidelines/ddd/immutable-data-model.guidelines.md) | イミュータブルデータモデル設計指針 |
| [ddd/rdra-model.guidelines.md](guidelines/rdra/rdra-model.guidelines.md)                    | RDRAモデルによる要件定義           |

### ドキュメント関連

| ファイル                                                                          | 説明                                   |
|-----------------------------------------------------------------------------------|----------------------------------------|
| [docs/directory-name.guidelines.md](guidelines/docs/directory-name.guidelines.md) | ドキュメント格納先ディレクトリ命名規則 |

### TypeScript 関連

| ファイル                                                                                          | 説明                          |
|---------------------------------------------------------------------------------------------------|-------------------------------|
| [typescript/coding-standards.guidelines.md](guidelines/typescript/coding-standards.guidelines.md) | TypeScript コーディング規約   |
| [typescript/review.guidelines.md](guidelines/typescript/review.guidelines.md)                     | TypeScript コードレビュー手順 |

### ワークフロー関連

| ファイル                                                                                                      | 説明                         |
|---------------------------------------------------------------------------------------------------------------|------------------------------|
| [workflow/issue-driven-development.guidelines.md](guidelines/workflow/issue-driven-development.guidelines.md) | GitHub Issueベースの開発手順 |

### フロントエンド関連

| ファイル                                                                                  | 説明                           |
|-------------------------------------------------------------------------------------------|--------------------------------|
| [frontend/projects.instructions.md](guidelines/frontend/projects.instructions.md)         | フロントエンドプロジェクト概要 |
| [frontend/commands.instructions.md](guidelines/frontend/commands.instructions.md)         | フロントエンドコマンド         |
| [frontend/developers.instructions.md](guidelines/frontend/developers.instructions.md)     | フロントエンド技術スタック     |
| [frontend/environments.instructions.md](guidelines/frontend/environments.instructions.md) | フロントエンド環境構築         |

### API ゲートウェイ関連

| ファイル                                                                                        | 説明                            |
|-------------------------------------------------------------------------------------------------|---------------------------------|
| [api-gateway/projects.instructions.md](guidelines/api-gateway/projects.instructions.md)         | APIゲートウェイプロジェクト概要 |
| [api-gateway/commands.instructions.md](guidelines/api-gateway/commands.instructions.md)         | APIゲートウェイコマンド         |
| [api-gateway/developers.instructions.md](guidelines/api-gateway/developers.instructions.md)     | APIゲートウェイ技術スタック     |
| [api-gateway/environments.instructions.md](guidelines/api-gateway/environments.instructions.md) | APIゲートウェイ環境構築         |

### サービスレジストリ関連

| ファイル                                                                                                  | 説明                               |
|-----------------------------------------------------------------------------------------------------------|------------------------------------|
| [service-registry/projects.instructions.md](guidelines/service-registry/projects.instructions.md)         | サービスレジストリプロジェクト概要 |
| [service-registry/commands.instructions.md](guidelines/service-registry/commands.instructions.md)         | サービスレジストリコマンド         |
| [service-registry/developers.instructions.md](guidelines/service-registry/developers.instructions.md)     | サービスレジストリ技術スタック     |
| [service-registry/environments.instructions.md](guidelines/service-registry/environments.instructions.md) | サービスレジストリ環境構築         |

### 認証・認可関連

| ファイル                                                                                            | 説明                       |
|-----------------------------------------------------------------------------------------------------|----------------------------|
| [authorization/projects.instructions.md](guidelines/authorization/projects.instructions.md)         | 認証・認可プロジェクト概要 |
| [authorization/commands.instructions.md](guidelines/authorization/commands.instructions.md)         | 認証・認可コマンド         |
| [authorization/developers.instructions.md](guidelines/authorization/developers.instructions.md)     | 認証・認可技術スタック     |
| [authorization/environments.instructions.md](guidelines/authorization/environments.instructions.md) | 認証・認可環境構築         |

---

## エージェント向けスキル一覧

| スキル          | 配置                                    | 用途                                               |
|-----------------|-----------------------------------------|----------------------------------------------------|
| `issue`         | `.agents/skills/issue/SKILL.md`         | Issueの作成と管理                                  |
| `modulith-dev`  | `.agents/skills/modulith-dev/SKILL.md`  | バックエンドの実装・テスト作成・アーキテクチャ検証 |
| `pr-review`     | `.agents/skills/pr-review/SKILL.md`     | PR のレビュー                                      |
| `pull-request`  | `.agents/skills/pull-request/SKILL.md`  | プルリクエストの作成と管理                         |
| `to-RDRA-model` | `.agents/skills/to-RDRA-model/SKILL.md` | RDRAモデルによる要件定義                           |

---

## 主な設計思想

- **オニオンアーキテクチャ**: ドメイン層を中心に、インフラストラクチャ層、アプリケーション層、プレゼンテーション層を同心円状に配置
- **DDD (ドメイン駆動設計)**: バウンデッドコンテキストごとにモジュールを分離し、ドメインモデルを豊かにする
- **Spring Modulith**: モジュール間の依存関係を明示的に管理し、モジュール境界の違反を検出
- **事業領域別スキーマ分離**: 各事業領域が独立したデータベーススキーマを持ち、データ層での分離を実現
- **jMolecules**: アーキテクチャパターンをコードレベルで表現し、コンパイル時の検証を可能にする
- **明確なモジュール境界**: Spring Modulith による公開API/SPIと内部実装の完全な分離
- **ドメイン中心設計**: ビジネスロジックを外部フレームワークから隔離し、純粋なドメインモデルとして保護
- **厳格な品質保証**: 単体（Small）、統合（Medium）、システム（Large）の明確なテスト分離と厳格なテスト規約

## 開発工程

- **要件定義**: 要件定義書の作成
- **詳細設計**: 要件を基に詳細設計書を作成
- **コーディング**: コードの実装
- **コードレビュー**: コードレビューの実施とコメントの対応
- **テスト**: 単体テスト (Small Test)、統合テスト (Medium test)、システムテスト (large test)
  の実施とカバレッジの確認
- **リリース**: リリースプロセスの実施とリリースノートの作成

## ドキュメント

プロジェクトの詳細なドキュメントは [`docs/`](docs/) ディレクトリに格納されています:

- **[事業領域一覧](docs/11%20事業領域/事業領域一覧.md)** — サブドメイン定義と分類
- **[業務領域一覧](docs/12%20業務領域/業務領域一覧.md)** — 業務機能の分類
- **[非機能要件項目](docs/13%20非機能要件項目/)** — 非機能要件の定義
- **[ADR](docs/14%20ADR/)** — アーキテクチャ判断記録
- **[機能要望](docs/21%20要望/)** — 機能要件と要望
- **[要件定義書](docs/22%20要件定義書/)** — 詳細な要件定義
- **[詳細設計書](docs/23%20詳細設計書/)** — 技術設計ドキュメント
- **[用語辞書](docs/99%20Dictionary/)** — 用語定義

### RDRA (リレーションシップ駆動要件分析)

要件分析にはRDRA（Relationship-Driven Requirements Analysis）を採用しています。RDRAはPlantUMLを使用してシステム価値、システム外部環境、システム境界、システムレイヤーの4つのレイヤーで要件を定義します。

詳細は [README.md のRDRAセクション](README.md#-rdra-リレーションシップ駆動要件分析) を参照してください。
