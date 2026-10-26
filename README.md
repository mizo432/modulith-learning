# modulith-learning

Spring Modulith、オニオンアーキテクチャ、DDD（ドメイン駆動設計）を学習・実践するためのデモプロジェクトです。

Spring Cloud を基盤としたマイクロサービスアーキテクチャで構成され、事業領域ごとにモジュールを分離する設計を採用しています。

## 🏗️ アーキテクチャ

### 全体構成

```
┌─────────────────────────────────────────────────────────┐
│                    フロントエンド                        │
│              (React + TypeScript + MUI)                  │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                   API Gateway                            │
│         (Spring Cloud Gateway + OAuth2)                  │
└────┬──────────────────────┬─────────────────────────────┘
     │                      │
┌────▼─────┐        ┌───────▼────────┐
│  Backend │        │ Authorization  │
│(Modulith)│        │  (OAuth2 AS)   │
└────┬─────┘        └────────────────┘
     │
┌────▼────────────────────────────────────────────────────┐
│              Service Registry (Eureka)                   │
└─────────────────────────────────────────────────────────┘
```

### モジュール構成

| モジュール | 説明 | ポート |
|---|---|---|
| **backend** | Spring Modulith を採用したコア業務アプリケーション。オニオンアーキテクチャとDDDに基づき、事業領域ごとにサブドメインを分離 | 8080 |
| **service-registry** | Netflix Eureka Server を用いたサービスレジストリ | 8761 |
| **api-gateway** | Spring Cloud Gateway によるAPIゲートウェイ。OAuth2 リソースサーバーとして認証・認可を担う | 8085 |
| **authorization** | Spring Authorization Server によるOAuth2 認可サーバー | 8443 |
| **frontend** | React + TypeScript + Material-UI によるフロントエンドアプリケーション | 3000 |

### 事業領域（サブドメイン）

| コード | 事業領域名 | 区分 | 説明 |
|---|---|---|---|
| AG | アジルーム (Agiloom) | コア | Scrum プロジェクト管理 |
| EF | 電子帳票 (Electronic Forms) | 汎用 | 帳票処理 |
| AS | 関連付け (Association) | 支援 | 関連付け管理 |
| AR | 承認申請 (Approval Request) | 汎用 | 承認ワークフロー |
| AZ | 認可 (Authorization) | 支援 | 認可管理 |
| NT | 通知 (Notification) | 汎用 | 通知処理 |
| DO | 開発運用 (DevOps) | 支援 | 開発・運用支援 |
| CA | カレンダーレジストリ | 汎用 | カレンダー管理 |
| TA | 税率レジストリ | 汎用 | 税率管理 |
| AD | 住所レジストリ | 汎用 | 住所管理 |
| ER | 元号レジストリ | 汎用 | 元号管理 |
| FI | 金融機関レジストリ | 汎用 | 金融機関情報管理 |
| EX | サンプル (Example) | 支援 | 実装サンプル |

各事業領域は独立したデータベーススキーマ（例: `undecided_ag`, `undecided_ef`）で分離され、Flyway によるマイグレーションが個別に管理されています。

## 🛠️ 技術スタック

### バックエンド

| カテゴリ | 技術 |
|---|---|
| **ランタイム** | Java 25 |
| **フレームワーク** | Spring Boot 4.1.1, Spring Modulith 2.1.0 |
| **アーキテクチャ** | jMolecules オンイオンアーキテクチャ, DDD |
| **データベース** | PostgreSQL |
| **ORM** | Spring Data JPA (Hibernate) |
| **DBマイグレーション** | Flyway |
| **キャッシュ** | Redis + Caffeine |
| **バッチ処理** | Spring Batch + Super CSV |
| **APIドキュメント** | SpringDoc OpenAPI |
| **サービスディスカバリ** | Netflix Eureka (Spring Cloud) |
| **認証・認可** | Spring Authorization Server, OAuth2 |
| **テスト** | JUnit 5, AssertJ, Testcontainers, ArchUnit |
| **コード品質** | JaCoCo, Lombok, JSpecify |
| **ビルド** | Gradle (Kotlin DSL) |

### フロントエンド

| カテゴリ | 技術 |
|---|---|
| **フレームワーク** | React 18 |
| **言語** | TypeScript |
| **UI コンポーネント** | Material-UI (MUI) |
| **ルーティング** | React Router |
| **HTTP クライアント** | Axios |
| **テスト** | Jest, React Testing Library |

## 📁 プロジェクト構造

```
modulith-learning/
├── backend/                  # コア業務アプリケーション (Spring Modulith)
│   ├── src/main/java/undecided/
│   │   ├── agiloom/          # アジルーム (AG) - Scrum プロジェクト管理
│   │   ├── association/      # 関連付け (AS)
│   │   ├── generic/          # 汎用サブドメイン
│   │   │   ├── accountMgmt/  # アカウント管理
│   │   │   ├── greeting/     # サンプル機能
│   │   │   ├── addressReg/   # 住所レジストリ
│   │   │   ├── bankReg/      # 金融機関レジストリ
│   │   │   ├── calendarReg/  # カレンダーレジストリ
│   │   │   ├── japaneseEraReg/ # 元号レジストリ
│   │   │   ├── taxPercentageReg/ # 税率レジストリ
│   │   │   └── sampleBatch/  # バッチ処理サンプル
│   │   ├── shared/           # 共有コンポーネント
│   │   └── supporting/       # 支援サブドメイン
│   └── src/main/resources/
│       └── db/migration/     # Flyway マイグレーションスクリプト
├── service-registry/         # Eureka サービスレジストリ
├── api-gateway/              # API ゲートウェイ
├── authorization/            # OAuth2 認可サーバー
├── frontend/                 # React フロントエンド
├── docs/                     # ドキュメント
│   ├── 11 事業領域/          # 事業領域定義
│   ├── 12 業務領域/          # 業務領域定義
│   ├── 21 要望/              # 機能要望
│   ├── 22 要件定義書/        # 要件定義
│   ├── 23 詳細設計書/        # 詳細設計
│   └── 95 Architecture decision record (ADR)/  # 技術判断記録
└── gradle/                   # Gradle ラッパー
```

## 🚀 ローカル開発環境のセットアップ

### 前提条件

- **Java 25** (JDK)
- **PostgreSQL** (ローカルまたは Docker)
- **Redis** (ローカルまたは Docker)
- **Node.js 22** (フロントエンド用)
- **Gradle 8.x** (ラッパー同梱)

### Docker Compose での依存サービス起動

```bash
# PostgreSQL と Redis を起動
docker run -d --name postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres
docker run -d --name redis -p 6379:6379 redis
```

### アプリケーションの起動

```bash
# 1. サービスレジストリを起動
./gradlew :service-registry:bootRun

# 2. バックエンドを起動
./gradlew :backend:bootRun

# 3. API ゲートウェイを起動
./gradlew :api-gateway:bootRun

# 4. フロントエンドを起動
cd frontend && npm install && npm start
```

### 環境変数

| 変数 | 説明 | デフォルト値 |
|---|---|---|
| `SYSTEM_JDBC_URL` | データベース接続URL | `jdbc:postgresql://localhost:5432/postgres` |
| `POSTGRES_USER` | データベースユーザー | `postgres` |
| `POSTGRES_PASSWORD` | データベースパスワード | `postgres` |
| `REDIS_HOST` | Redis ホスト | `localhost` |
| `REDIS_PORT` | Redis ポート | `6379` |

## 🧪 テストの実行

```bash
# ユニットテスト (small)
./gradlew :backend:test

# ミディアムサイズ統合テスト
./gradlew :backend:mediumTest

# 大規模システムテスト
./gradlew :backend:largeTest

# カバレッジレポート生成
./gradlew :backend:jacocoTestReport
```

テストは JUnit 5 の `@Tag` アノテーションでサイズ分類されています:

- **small** (デフォルト): ユニットテスト。`test` タスクで実行
- **medium**: 統合テスト。`mediumTest` タスクで実行
- **large**: システムテスト。`largeTest` タスクで実行

## 📖 ドキュメント

プロジェクトのドキュメントは [`docs/`](docs/) ディレクトリに格納されています:

- **[事業領域一覧](docs/11%20事業領域/事業領域一覧.tsv)** — サブドメイン定義と分類
- **[業務領域一覧](docs/12%20業務領域/業務領域一覧.tsv)** — 業務機能の分類
- **[機能要望](docs/21%20要望/)** — 機能要件と要望
- **[要件定義書](docs/22%20要件定義書/)** — 詳細な要件定義
- **[詳細設計書](docs/23%20詳細設計書/)** — 技術設計ドキュメント
- **[ADR](docs/95%20Architecture%20decision%20record%20(ADR)/)** — アーキテクチャ判断記録

## 📋 設計方針

- **オニオンアーキテクチャ**: ドメイン層を中心に、インフラストラクチャ層、アプリケーション層、プレゼンテーション層を同心円状に配置
- **DDD (ドメイン駆動設計)**: バウンデッドコンテキストごとにモジュールを分離し、ドメインモデルを豊かにする
- **Spring Modulith**: モジュール間の依存関係を明示的に管理し、モジュール境界の違反を検出
- **事業領域別スキーマ分離**: 各事業領域が独立したデータベーススキーマを持ち、データ層での分離を実現
- **jMolecules**: アーキテクチャパターンをコードレベルで表現し、コンパイル時の検証を可能にする

## 📄 License

This project is licensed under the [GNU Affero General Public License v3.0](LICENSE).