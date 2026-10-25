# モジュラーモノリス開発ガイドライン (Modular Monolith Development Guidelines)

このドキュメントは、 **modulith-learning** プロジェクトの開発ガイドラインのインデックスです。
各トピックの詳細は、以下のファイルをご覧ください。

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
