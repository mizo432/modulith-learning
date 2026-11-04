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

各事業領域は独立したデータベーススキーマ（例: `undecided_ag`, `undecided_ef`）で分離され、Flyway
によるマイグレーションが個別に管理されています。

## 🛠️ 技術スタック

### バックエンド

| カテゴリ                 | 技術                                       |
|--------------------------|--------------------------------------------|
| **ランタイム**           | Java 25                                    |
| **フレームワーク**       | Spring Boot 4.1.2, Spring Modulith 2.1.0   |
| **アーキテクチャ**       | jMolecules オンイオンアーキテクチャ, DDD   |
| **データベース**         | PostgreSQL                                 |
| **ORM**                  | Spring Data JPA (Hibernate)                |
| **DBマイグレーション**   | Flyway                                     |
| **キャッシュ**           | Redis + Caffeine                           |
| **バッチ処理**           | Spring Batch + Super CSV                   |
| **APIドキュメント**      | SpringDoc OpenAPI                          |
| **サービスディスカバリ** | Netflix Eureka (Spring Cloud)              |
| **認証・認可**           | Spring Authorization Server, OAuth2        |
| **テスト**               | JUnit 6, AssertJ, Testcontainers, ArchUnit, Pitest |
| **コード品質**           | JaCoCo, Lombok, JSpecify, SonarQube        |
| **ビルド**               | Gradle (Kotlin DSL)                        |

### フロントエンド

| カテゴリ              | 技術                        |
|-----------------------|-----------------------------|
| **フレームワーク**    | React 18                    |
| **言語**              | TypeScript                  |
| **UI コンポーネント** | Material-UI (MUI)           |
| **ルーティング**      | React Router                |
| **HTTP クライアント** | Axios                       |
| **テスト**            | Jest, React Testing Library |

## 📁 プロジェクト構造

```
modulith-learning/
├── backend/                  # コア業務アプリケーション (Spring Modulith)
│   ├── src/main/java/undecided/
│   │   ├── agiloom/          # アジルーム (AG) - Scrum プロジェクト管理
│   │   │   ├── productDiscovery/   # プロダクトディスカバリー
│   │   │   ├── requirementBacklog/ # 要求バックログ管理
│   │   │   ├── priorityDecision/   # 優先順位決定
│   │   │   ├── sprintExecution/    # スプリント実行
│   │   │   ├── qualityVerification/# 品質検証
│   │   │   └── outcomeAdaptation/  # 成果適応
│   │   ├── association/      # 関連付け (AS)
│   │   │   ├── humanResourceMgmt/  # 人材管理
│   │   │   └── relationshipMgmt/   # 関連付け管理
│   │   ├── jobmanagement/    # ジョブ管理 (JM)
│   │   │   ├── jobdefinition/      # ジョブ定義
│   │   │   ├── jobexecution/       # ジョブ実行
│   │   │   └── permissionmanagement/# 権限管理
│   │   ├── generic/          # 汎用サブドメイン
│   │   │   ├── accountMgmt/  # アカウント管理
│   │   │   ├── greeting/     # サンプル機能
│   │   │   ├── addressReg/   # 住所レジストリ
│   │   │   ├── bankReg/      # 金融機関レジストリ
│   │   │   ├── calendarReg/  # カレンダーレジストリ
│   │   │   ├── japaneseEraReg/ # 元号レジストリ
│   │   │   ├── taxPercentageReg/ # 税率レジストリ
│   │   │   ├── sampleBatch/  # バッチ処理サンプル
│   │   │   └── largeRecordBatch/ # 大規模レコードバッチ
│   │   ├── supporting/       # 支援サブドメイン
│   │   │   ├── databaseDef/  # データベース定義
│   │   │   └── snowflake/    # Snowflake ID
│   │   └── config/           # 設定
│   └── src/main/resources/
│       └── db/migration/     # Flyway マイグレーションスクリプト
├── service-registry/         # Eureka サービスレジストリ
├── api-gateway/              # API ゲートウェイ
├── authorization/            # OAuth2 認可サーバー
├── frontend/                 # React フロントエンド
├── mizo432-shared-common/    # 共通ユーティリティ・共有コンポーネント
├── docs/                     # ドキュメント
│   ├── 05 バッチガイド/      # バッチ処理ガイド
│   ├── 11 事業領域/          # 事業領域定義
│   ├── 12 業務領域/          # 業務領域定義
│   ├── 13 非機能要件項目/    # 非機能要件
│   ├── 14 ADR/               # アーキテクチャ判断記録
│   ├── 14 用語分類/          # 用語分類
│   ├── 21 要望/              # 機能要望
│   ├── 22 要件定義書/        # 要件定義
│   ├── 23 詳細設計書/        # 詳細設計
│   ├── 24 coding/            # コーディング関連
│   ├── 25 data/              # データ関連
│   ├── 90 RESTful api/       # REST API 設計
│   ├── 91 resource/          # リソース
│   ├── 95 Architecture decision record (ADR)/  # ADR (英語)
│   ├── 96 addressInfo/       # 住所情報
│   ├── 97 spring cloud/      # Spring Cloud 関連
│   ├── 98 Jpa/               # JPA 関連
│   └── 99 Dictionary/        # 用語辞書
└── gradle/                   # Gradle ラッパー
```

## 🚀 ローカル開発環境のセットアップ

### 前提条件

- **Java 25** (JDK)
- **PostgreSQL** (ローカルまたは Docker)
- **Redis** (ローカルまたは Docker)
- **Node.js 22** (フロントエンド用)
- **Gradle 9.8.1** (ラッパー同梱)

### Docker Compose での依存サービス起動

```bash
# PostgreSQL と Redis を起動（backend/compose.ymlを使用）
cd backend && docker compose up -d
```

または、手動で起動する場合:

```bash
# PostgreSQL と Redis を起動
docker run -d --name postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres
docker run -d --name redis -p 6379:6379 redis
```

### アプリケーションの起動

```bash
# 1. 依存サービスを起動（PostgreSQL, Redis）
cd backend && docker compose up -d

# 2. サービスレジストリを起動
./gradlew :service-registry:bootRun

# 3. バックエンドを起動
./gradlew :backend:bootRun

# 4. API ゲートウェイを起動
./gradlew :api-gateway:bootRun

# 5. フロントエンドを起動
cd frontend && npm install && npm start
```

### 環境変数

| 変数                | 説明                   | デフォルト値                                |
|---------------------|------------------------|---------------------------------------------|
| `SYSTEM_JDBC_URL`   | データベース接続URL    | `jdbc:postgresql://localhost:5432/postgres` |
| `POSTGRES_USER`     | データベースユーザー   | `postgres`                                  |
| `POSTGRES_PASSWORD` | データベースパスワード | `postgres`                                  |
| `REDIS_HOST`        | Redis ホスト           | `localhost`                                 |
| `REDIS_PORT`        | Redis ポート           | `6379`                                      |

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

# ミューテーションテスト (Pitest)
./gradlew :backend:pitest
```

テストは JUnit 6 の `@Tag` アノテーションでサイズ分類されています:

- **small** (デフォルト): ユニットテスト。`test` タスクで実行
- **medium**: 統合テスト。`mediumTest` タスクで実行
- **large**: システムテスト。`largeTest` タスクで実行

ミューテーションテストには **Pitest** を採用しており、テストの品質を遺伝的アルゴリズムで評価します。

## 📖 ドキュメント

プロジェクトのドキュメントは [`docs/`](docs/) ディレクトリに格納されています:

- **[事業領域一覧](docs/11%20事業領域/事業領域一覧.md)** — サブドメイン定義と分類
- **[業務領域一覧](docs/12%20業務領域/業務領域一覧.md)** — 業務機能の分類
- **[非機能要件項目](docs/13%20非機能要件項目/)** — 非機能要件の定義
- **[ADR](docs/14%20ADR/)** — アーキテクチャ判断記録
- **[機能要望](docs/21%20要望/)** — 機能要件と要望
- **[要件定義書](docs/22%20要件定義書/)** — 詳細な要件定義
- **[詳細設計書](docs/23%20詳細設計書/)** — 技術設計ドキュメント
- **[用語辞書](docs/99%20Dictionary/)** — 用語定義

### 📐 RDRA (リレーションシップ駆動要件分析)

要件分析にはRDRA（Relationship-Driven Requirements
Analysis）を採用しています。RDRAはPlantUMLを使用してシステム価値、システム外部環境、システム境界、システムレイヤーの4つのレイヤーで要件を定義します。

- **[RDRAダイアグラム例](.agents/skills/to-RDRA-model/templates/RDRAダイアグラム例/README.md)** —
  図書館システムを題材にしたダイアグラム例
  -
  **[利用シーン図](.agents/skills/to-RDRA-model/templates/RDRAダイアグラム例/01.利用シーン図.md)** —
  ユースケース図の例
  -
  **[バリエーション条件図](.agents/skills/to-RDRA-model/templates/RDRAダイアグラム例/02.バリエーション条件図.md)** —
  バリエーションと条件の定義例
  -
  **[ユースケース複合図](.agents/skills/to-RDRA-model/templates/RDRAダイアグラム例/03.ユースケース複合図.md)** —
  UC複合図の例
  -
  **[ステートマシン図](.agents/skills/to-RDRA-model/templates/RDRAダイアグラム例/04.ステートマシン図.md)** —
  状態遷移図の例
  - **[用語集](.agents/skills/to-RDRA-model/templates/RDRAダイアグラム例/05.用語集.md)** — 用語集の例

#### Agiloom (AG) のRDRAダイアグラム

AgiloomはScrumプロジェクト管理を支援するシステムです。6つの業務領域、31のBUC、120のユースケースから構成されています。

- **[ユースケース図](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/)** — 6業務領域分のPlantUMLユースケース図
  -
  **[AG-01 プロダクトディスカバリー](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/AG-01%20プロダクトディスカバリー/03.システム境界/01.ユースケース/ユースケース図.md)** —
  26ユースケース
    -
  **[AG-02 要求バックログ管理](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/AG-02%20要求バックログ管理/03.システム境界/01.ユースケース/ユースケース図.md)** —
  25ユースケース
    -
  **[AG-03 優先順位決定](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/AG-03%20優先順位決定/03.システム境界/01.ユースケース/ユースケース図.md)** —
  10ユースケース
    -
  **[AG-04 スプリント実行](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/AG-04%20スプリント実行/03.システム境界/01.ユースケース/ユースケース図.md)** —
  19ユースケース
    -
  **[AG-05 品質検証](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/AG-05%20品質検証/03.システム境界/01.ユースケース/ユースケース図.md)** —
  19ユースケース
    -
  **[AG-06 成果適応](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/AG-06%20成果適応/03.システム境界/01.ユースケース/ユースケース図.md)** —
  21ユースケース
- **[UC複合図](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/UC複合図.md)** —
  5業務領域の代表的なユースケースの複合図
- **[ステートマシン図](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/ステートマシン図.md)** —
  14の状態遷移図
-

**[バリエーション条件図](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/バリエーション条件図.md)** —
12バリエーション、15条件

- **[用語集](docs/22%20要件定義書/AG%20アジルーム/03%20機能要件/用語集.md)** — ビジネス用語40、システム用語30

#### Electronic Forms (EF) のRDRAダイアグラム

Electronic Forms（EF）は電子帳票基盤を構成するシステムです。5つの業務領域、80のユースケースから構成されています。

- **[EF-01 帳票基盤管理](docs/22%20要件定義書/EF%20電子帳票/03%20機能要件/EF-01%20帳票基盤管理/03.システム境界/01.ユースケース/ユースケース一覧.md)** —
  23ユースケース
  -
  **[EF-02 帳票管理](docs/22%20要件定義書/EF%20電子帳票/03%20機能要件/EF-02%20帳票管理/03.システム境界/01.ユースケース/ユースケース一覧.md)** —
  11ユースケース
  -
  **[EF-03 帳票利用](docs/22%20要件定義書/EF%20電子帳票/03%20機能要件/EF-03%20帳票利用/03.システム境界/01.ユースケース/ユースケース一覧.md)** —
  15ユースケース
  -
  **[EF-04 保管・アーカイブ](docs/22%20要件定義書/EF%20電子帳票/03%20機能要件/EF-04%20保管・アーカイブ/03.システム境界/01.ユースケース/ユースケース一覧.md)** —
  16ユースケース
  -
  **[EF-05 運用・監査](docs/22%20要件定義書/EF%20電子帳票/03%20機能要件/EF-05%20運用・監査/03.システム境界/01.ユースケース/ユースケース一覧.md)** —
  15ユースケース

## 📋 設計方針

- **オニオンアーキテクチャ**: ドメイン層を中心に、インフラストラクチャ層、アプリケーション層、プレゼンテーション層を同心円状に配置
- **DDD (ドメイン駆動設計)**: バウンデッドコンテキストごとにモジュールを分離し、ドメインモデルを豊かにする
- **Spring Modulith**: モジュール間の依存関係を明示的に管理し、モジュール境界の違反を検出
- **事業領域別スキーマ分離**: 各事業領域が独立したデータベーススキーマを持ち、データ層での分離を実現
- **jMolecules**: アーキテクチャパターンをコードレベルで表現し、コンパイル時の検証を可能にする

## 📄 License

This project is licensed under the [GNU Affero General Public License v3.0](LICENSE).
