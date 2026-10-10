# モジュラーモノリス開発ガイドライン (Modular Monolith Development Guidelines)

このドキュメントは、 **modulith-learning**
プロジェクトにおけるアーキテクチャ設計、モジュール設計、実装標準、テスト規約、およびビルド手順を定義する開発ガイドラインです。開発者およびAIエージェントは、本規約に厳格に従って開発・保守を行ってください。

---

## 1. プロジェクト概要 (Project Overview)

本プロジェクトは、 **Spring Modulith** を活用したモジュラーモノリス（Modular
Monolith）アーキテクチャを実践・学習するためのシステムです。
ドメイン駆動設計（DDD）およびオニオンアーキテクチャの原則に基づき、モジュール間の疎結合と高い凝集度を保ちながら、単一リポジトリ内での高い開発効率と保守性を実現します。

### 1.1 主な設計思想

- **明確なモジュール境界**: Spring Modulith による公開API/SPIと内部実装の完全な分離
- **ドメイン中心設計**: ビジネスロジックを外部フレームワークから隔離し、純粋なドメインモデルとして保護
- **厳格な品質保証**: 単体（Small）、統合（Medium）、システム（Large）の明確なテスト分離と厳格なテスト規約

---

## 2. システム構成と技術スタック (Architecture & Tech Stack)

### 2.1 システムコンポーネント

1. **バックエンド (`backend`)**:
    - コア業務ロジック、データ永続化、REST API を提供する Spring Modulith アプリケーション
2. **フロントエンド (`frontend`)**:
    - React / MUI (Material-UI) による SPA (Single Page Application)
3. **APIゲートウェイ (`api-gateway`)**:
    - ルーティングおよびクライアントリクエストの集約
4. **サービスレジストリ (`service-registry`)**:
    - Netflix Eureka によるサービスディスカバリー
5. **認証・認可 (`authorization`)**:
    - セキュリティおよびアクセス制御

### 2.2 バックエンド技術スタック

- **言語 / ランタイム**: Java 25 (OpenJDK - Gradle Toolchain)
- **コアフレームワーク**:
    - Spring Boot 4.1.x
    - Spring Modulith 2.1.x
    - Spring Data JPA
    - Spring Cloud 2025.x (Eureka Client)
- **データベース**:
    - PostgreSQL (本番・開発)
    - Testcontainers + PostgreSQL 17 (統合テスト `@Tag("medium")` 用。Docker が必要)
    - Flyway (DBマイグレーション。モジュールごとに DB スキーマを分離)
- **バッチ処理**:
    - Spring Batch (マスタデータのインポート等)
    - Super CSV (CSV 読み込み)
- **主要ライブラリ**:
    - **Lombok**: ボイラープレートコード削減 (`@Getter`, `@Setter`, `@RequiredArgsConstructor`,
      `@UtilityClass` 等)
    - **JSpecify**: Null 安全性アノテーション (`@NonNull`, `@Nullable`)
    - **jMolecules**: オニオンアーキテクチャ・DDD 構造定義
    - **YAVI**: 型安全なバリデーションライブラリ
    - **Google Guava / ICU4J / libphonenumber / Apache POI**: 共通ユーティリティ
    - **Caffeine**: インメモリキャッシュ
    - **Jilt**: ビルダー生成 (アノテーションプロセッサ)
    - **springdoc-openapi**: OpenAPI / Swagger UI
    - **Spring Modulith Actuator / Observability / Insight**: モジュール構造・イベントの可視化
- **テストフレームワーク**:
    - JUnit 5 (JUnit Jupiter)
    - AssertJ, AssertJ-DB
    - Spring Modulith Test (`ApplicationModules`, `Documenter`)
    - Spring Batch Test
    - Testcontainers (PostgreSQL)
- **カバレッジ**: JaCoCo (`./gradlew :backend:test` 実行後に `backend/build/reports/jacoco` に出力)

### 2.3 フロントエンド技術スタック

- React 18 / TypeScript 4.9 / Create React App (`react-scripts`)
- MUI v5 (`@mui/material`, `@mui/icons-material`) + Emotion
- React Router v6 / axios
- テーマは `frontend/src/theme.ts` に集約し、環境別の色は `.env.*` (`REACT_APP_*`) で上書きする
- **テストは Storybook を利用する**（ストーリー + `play` 関数によるインタラクションテスト）。
  Storybook は未導入のため、最初にテストを追加する際に導入する
- 画面・コンポーネント設計の詳細は `.claude/skills/frontend-design/SKILL.md` を参照する

---

## 3. モジュール構造とパッケージ規約 (Module & Package Rules)

バックエンド (`backend/src/main/java/undecided`) は、以下の構造でモジュール化されています。

### 3.1 モジュール区分

- **汎用・業務モジュール (`undecided.generic.*`)**:
    - `addressReg`: 住所管理（都道府県・市区町村・町字。DB スキーマ `address_reg`、CSV インポートバッチあり）
    - `calendarReg`: カレンダー管理（祝日。DB スキーマ `calendar_reg`、祝日インポートバッチあり）
    - `bankReg`: 銀行・支店管理
    - `relationshipMgmt`: 顧客・組織・従業員関係管理（party / partyRole パターン）
        - `productSaleMgmt`: 商品販売管理
- **業務ドメインモジュール (`undecided.*` 直下)**:
    - `cashSaleMgmt`: 現金販売管理（パッケージのみ）
    - `customerAccountMgmt`: 顧客口座管理（パッケージのみ）
    - `shared`: 共通モジュール（タイプ OPEN、`annotation` パッケージ、`dateProvider` パッケージ、`io`
      パッケージ、`precondition` パッケージ、`primitive` パッケージ、`primitiveOld` パッケージ、
      `builder`
      パッケージ、`ipaddress` パッケージ、`exception` パッケージ、`logger` パッケージ、`functional`
      パッケージ、
      `uuidV7Provider` パッケージ、`message` パッケージ、`application` パッケージ、`entity` パッケージ）
- **ERP モジュール (`undecided.erp.*`)**:
    - 業務処理・メッセージング機能
- **基盤・共通モジュール (`undecided.supporting.*`)**:
    - `snowflake`: 分散ID生成（DB スキーマ `id_mgmt`）
    - その他: `presentation`
      など

新しいモジュールを追加したら、本節の一覧も更新してください。

### 3.2 パッケージ境界ルール

1. **公開インターフェース / SPI (`spi` またはモジュールのルート直下)**:
    - 他モジュールから呼び出し可能な DTO、Query/Command インターフェース、ドメインモデルを配置します。
    - 各パッケージの `package-info.java` に `@NamedInterface` を付与し、公開範囲を明示します。
2. **内部実装 (`internal`)**:
    - モジュール内部でのみ利用されるリポジトリ実装、サービスクラス、コントローラーを配置します。
    - **他モジュールから `internal` パッケージのクラスを直接 import / 参照することは禁止**です。
3. **モジュール検証**:
    - `ModulithTest.java` 内の `ApplicationModules.of(ModulithDemoApplication.class).verify()`
      により、依存関係違反や循環参照がないかを常時検証します。

---

## 4. レイヤードアーキテクチャ規約 (Layering Architecture)

各モジュール内部はオニオンアーキテクチャ / クリーンアーキテクチャの原則に従って層を分離します。

1. **Domain 層**:
    - 業務ルール、エンティティ、値オブジェクト（Value Object）、ドメインサービス、リポジトリインターフェース
    - 外部ライブラリやフレームワークに依存せず、不変条件は `*Precondition` 等で保護します。
2. **Business / Application 層**:
    - ユースケースの調整、Command/Query ハンドラー、トランザクション境界
3. **Infrastructure 層**:
    - JPA エンティティ、Spring Data JPA リポジトリ実装、外部通信、ファイル・DB アクセス
4. **Presentation 層**:
    - REST コントローラー (`@RestController`)、リクエスト/レスポンス DTO、バリデーション、例外ハンドリング

### 4.1 バッチ処理の配置 (Spring Batch)

インポート等のバッチはモジュールの `internal/batch` 配下に、役割ごとのサブパッケージで配置します
（参考: `addressReg`, `calendarReg`）。

| パッケージ  | 役割                                      |
|:------------|:------------------------------------------|
| `config`    | Job / Step の定義 (`*ImportBatchConfig`)  |
| `reader`    | 入力の読み込み（CSV は Super CSV を利用） |
| `processor` | 入力 DTO からドメインモデルへの変換・検証 |
| `writer`    | 永続化                                    |
| `dto`       | 入力レコードの DTO                        |
| `launcher`  | Job の起動 (`*ImportJobLauncher`)         |

- 取り込み元データは `backend/src/main/resources/data/` に配置します。
- `application-test.yml` では `spring.batch.job.enabled: false` とし、テストから明示的に起動します。

### 4.2 データベースマイグレーション (Flyway)

- マイグレーションは `backend/src/main/resources/db/migration/` に
  `V<yyyyMMddHHmmss>__<snake_case の説明>.sql` の形式で追加します。
- テーブルはモジュールごとの DB スキーマに配置します（例: `address_reg`, `calendar_reg`, `id_mgmt`）。
  `public` スキーマには新規テーブルを作りません。
- 適用済みのマイグレーションは変更せず、修正は新しいマイグレーションで行います。

---

## 5. テスト戦略とテスト作成規約 (Testing Guidelines)

### 5.1 テストサイズと実行区分

テストは JUnit 5 の `@Tag` アノテーションを用いて 3 段階に分類します。

| タグ             | 対象                | 特徴                                                         | 実行コマンド                    |
|:-----------------|:--------------------|:-------------------------------------------------------------|:--------------------------------|
| `@Tag("small")`  | 単体テスト          | Spring コンテキスト起動なし、高速実行、モック利用            | `./gradlew :backend:test`       |
| `@Tag("medium")` | 統合テスト          | Spring コンテキスト起動、DB/リポジトリ連携、モジュール間結合 | `./gradlew :backend:mediumTest` |
| `@Tag("large")`  | システム/負荷テスト | E2E、パフォーマンステスト、外部システム連携                  | `./gradlew :backend:largeTest`  |

- `./gradlew :backend:test` はタグなしと `small` を実行し、`medium` / `large` を除外します（タイムアウト
  60 秒）。

#### Medium テスト（統合テスト）の構成

Medium テストは Testcontainers の PostgreSQL 17 を使い、本番と同じ DB 方言で検証します。 **実行には
Docker が起動している必要があります。**

```java

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("HolidayImportBatchConfigの統合テスト")
class HolidayImportBatchConfigTest {
  // ...
}
```

- `undecided.TestcontainersConfiguration` が `@ServiceConnection` で DataSource
  を自動構成します。独自にコンテナを定義しないでください。
- `test` プロファイル (`backend/src/test/resources/application-test.yml`) では、スキーマを
  `ddl-auto: create-drop` + `create_namespaces: true` で生成し、Eureka クライアントを無効化しています。

#### フロントエンドのテスト

- フロントエンド (`frontend`) のテストは **Storybook** で記述します。
- コンポーネントと同じディレクトリに `*.stories.tsx` を置き、loading / error / empty / success
  の各状態をストーリーにします。
- 操作の検証は `play` 関数で行います。Jest 単体の `*.test.tsx` を新規に作る前に、Storybook
  で書けるか検討してください。

### 5.2 テストコード作成の厳格ルール

新規にテストを作成・修正する際は、以下のルールを遵守してください。

1. **メソッド命名規則**:
    - メソッド名は必ず **`should`** で開始する。
    - キャメルケースを使用し、 **アンダースコア（`_`）は絶対に使用しない**（例:
      `shouldReturnTrueWhenInputIsNull`）。
2. **`@DisplayName` アノテーション**:
    - **テストクラスおよびテストメソッドの両方に必ず日本語の説明を付与する**。
3. **構造化（`@Nested`）**:
    - テスト対象のメソッドごとに `@Nested` クラスを作成してテストケースをグループ化する。
4. **アサーション**:
    - **AssertJ** (`assertThat`, `assertThatThrownBy`) を使用する。
    - アサーションには極力 `.as("...")` で意図を明記する。
5. **可視性**:
    - テストクラス、ネストクラス、テストメソッドはすべて **`package-private`（アクセス修飾子なし）**
      とする。
6. **境界値・異常値テストの網羅**:
    - `null` 引数、空文字 `""`、空白文字 `" "`、境界値（最大/最小）、異常���の例外スローを必ずテストする。

### 5.3 テストコード標準例 (Demonstration Example)

```java
package undecided.supporting.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Strings2.IsEmptyのテスト")
class Strings2IsEmptyTest {

  private final Strings2.IsEmpty isEmpty = new Strings2.IsEmpty();

  @Nested
  @DisplayName("testメソッドのテスト")
  class TestMethodTest {

    @Test
    @DisplayName("nullが渡された場合、trueを返すこと")
    void shouldReturnTrueWhenInputIsNull() {
      // Act
      boolean result = isEmpty.test(null);

      // Assert
      assertThat(result)
          .as("nullに対するIsEmpty判定はtrueであること")
          .isTrue();
    }

    @Test
    @DisplayName("空文字列が渡された場合、trueを返すこと")
    void shouldReturnTrueWhenInputIsEmpty() {
      // Act
      boolean result = isEmpty.test("");

      // Assert
      assertThat(result)
          .as("空文字列に対するIsEmpty判定はtrueであること")
          .isTrue();
    }

    @Test
    @DisplayName("空白文字のみの文字列が渡された場合、falseを返すこと")
    void shouldReturnFalseWhenInputIsWhitespace() {
      // Act
      boolean result = isEmpty.test("   ");

      // Assert
      assertThat(result)
          .as("空白文字のみの文字列に対するIsEmpty判定はfalseであること")
          .isFalse();
    }
  }
}
```

---

## 6. コーディング規約と例外処理 (Coding Standards & Exceptions)

### 6.1 コードスタイル

- **Google Java Style** を基本とし、インデントや命名規則をプロジェクト既存コードに統一します。
- イミュータブルなデータ構造には Java `record` を積極的に使用します。
- Lombok アノテーション（`@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@UtilityClass`
  等）でボイラープレートを排除します。
- JSpecify アノテーション（`@NonNull`, `@Nullable`）を付与し、Null 安全性を保証します。

### 6.2 例外処理ポリシー

- **業務エラー (想定される例外)**:
    - `BusinessException`, `NotFoundBusinessException`, `ResultMessagesNotificationException`
    - HTTP 4xx（400 Bad Request, 404 Not Found, 409 Conflict 等）としてクライアントに安全なエラーメッセージを返却。
- **システム障害 (予期せぬ例外)**:
    - `SystemException`
    - HTTP 500 として扱い、内部スタックトレースや機密情報をレスポンスに直接露出させない。
- **事前条件検証**:
    - `undecided.supporting.precondition.*` パッケージのクラスを活用し、メソッドの先頭で引数の妥当性をチェックする。

### 6.3 コメントの原則 (Comment Principles)

コードや関連アーティファクトには、それぞれの媒体が最も効果的に伝えられる情報を記載します。

| 媒体           | 記載内容 | 説明                                                                             |
|:---------------|:---------|:---------------------------------------------------------------------------------|
| コード         | How      | 実装自体が「どのように」動作するかを明確に表現する                               |
| テストコード   | What     | テストが「何を」検証しているかを明確に表現する                                   |
| コミットログ   | Why      | 変更の背景や意図、つまり「なぜ」変更したかを記載する                             |
| コードコメント | Why not  | 実装の選択理由や、代替案を採用しなかった理由（「なぜそれではないか」）を記載する |

- コード自体は読みやすいように整理し、実装が自明な部分には不要なコメントを追加しません。
- テストコードでは、メソッド名 (`should...`)、`@DisplayName`、アサーションの `.as("...")`
  を通じて検証対象を明確にします。
- コミットログでは、変更内容だけでなく、変更の動機や背景を記載し、将来のレビュアーが文脈を理解できるようにします。
- コードコメントは、実装から読み取れない「なぜこのアプローチを選んだか」「なぜ別の方法を捨てたか」を記録します。

---

## 7. ビルド & 開発コマンド (Commands & Workflow)

| 操作                          | コマンド                                                                     |
|:------------------------------|:-----------------------------------------------------------------------------|
| プロジェクト全体のビルド      | `./gradlew build`                                                            |
| バックエンドの起動            | `./gradlew :backend:bootRun`                                                 |
| 単体テスト (Small) の実行     | `./gradlew :backend:test`                                                    |
| 統合テスト (Medium) の実行    | `./gradlew :backend:mediumTest`                                              |
| システムテスト (Large) の実行 | `./gradlew :backend:largeTest`                                               |
| モジュリス構造図の生成        | `./gradlew :backend:test` 実行後 `backend/build/spring-modulith-docs` に出力 |
| カバレッジレポート            | `./gradlew :backend:test` 実行後 `backend/build/reports/jacoco` に出力       |
| 依存ライブラリの更新確認      | `./gradlew :backend:dependencyUpdates`                                       |
| フロントエンドの起動          | `cd frontend && npm run start:local`（`.env.local` を使用）                  |
| フロントエンドのビルド        | `cd frontend && npm run build`                                               |

### 7.1 CI (GitHub Actions)

`.github/workflows/ci.yml` で JDK 25 (Temurin) を用いて以下を実行します。

| ジョブ        | 実行タイミング                              | 内容                   |
|:--------------|:--------------------------------------------|:-----------------------|
| `test`        | すべてのブランチへの push / PR              | `./gradlew test`       |
| `medium-test` | `main` / `develop` 向けの Pull Request のみ | `./gradlew mediumTest` |

PR を作成する前に、ローカルで `./gradlew :backend:test` と `./gradlew :backend:mediumTest`
が成功することを確認してください。

---

## 8. GitHub Issue ベースの開発手順 (GitHub Issue-Driven Development Workflow)

開発作業（機能追加、リファクタリング、バグ修正、ドキュメント更新など）は、原則として **GitHub Issue**
を起点として以下のフローで実施します。

### 8.1 開発フロー

1. **Issue の選定・確認 (Issue Identification & Scope)**
    - 対象とする GitHub Issue の番号・タイトル・本文を確認し、解決すべき課題・要件・受け入れ基準を明確化します。
    - 関連するモジュールや影響範囲を特定します。
    - 要件や受け入れ基準が曖昧な場合は、要求分析スキル (`requirement-analysis`) で要件を整理してから着手します。

2. **feature ブランチの作成 (Branching)**
    - 最新のメインブランチから作業用の feature ブランチを作成して切り替えます。
    - ブランチ命名規則の例: `feature/issue-<number>-<short-description>` (例:
      `feature/issue-139-prefecture-import`)

3. **設計・方針検討 (Design & Approach)**
    - Spring Modulith のモジュール境界（公開 API/SPI と internal の分離）およびオニオンアーキテクチャの層構造に準拠した設計を行います。
    - 必要な単体テスト（Small）および統合テスト（Medium）のテストケースを策定します。

4. **実装とテスト作成 (Implementation & Testing)**
    - プロジェクト規約（Google Java Style、Lombok、JSpecify、`undecided.supporting.precondition.*`
      による事前条件検証、例外処理方針）に従って実装します。
    - テスト作成規約（`should` 命名、日本語 `@DisplayName`、`@Nested` 構造、`package-private`
      可視性、境界値/異常系網羅）に従いテストコードを作成します。

5. **検証の実行 (Verification)**
    - `./gradlew :backend:test`（単体テストおよび Modulith 境界検証）を実行します。
    - 必要に応じて `./gradlew :backend:mediumTest`（統合テスト）を実行し、全テストの成功を確認します。

6. **Issue 番号の紐付けとコミット (Linking & Commit)**
    - コミットメッセージに対応する Issue 番号（例: `refs #XX`, `closes #XX`）を明記してコミットします。

7. **プルリクエストの作成 (Pull Request Creation)**
    - 作業完了後、作業ブランチからメインブランチ（または指定のベースブランチ）に向けて Pull Request
      (PR) を作成します。
    - PR 本文には変更概要、関連 Issue 番号（`closes #XX` / `fixes #XX`
      ）、実施したテスト・検証結果を明記します。

8. **レビューSKILLによるPRレビュー (PR Review with Review Skill)**
    - PR 作成後は、PR レビュースキル (`pr-review`) を使用して変更内容を多角的にレビューします。
    - モジュール境界（Spring Modulith）、オニオンアーキテクチャ、テスト規約（`should` 命名、日本語
      `@DisplayName`、`@Nested` 構造、境界値検証）、Null 安全性（JSpecify）、例外処理、Google Java Style
      の準拠状況を検証・確認します。

9. **レビュー指摘の修正と3回の反復検証 (Review Feedback Resolution & 3-Iteration Cycle)**
    - PR レビューで検出された要修正点（モジュール境界違反、テスト規約違反、Null 安全性、例外処理等の指摘事項）を
      feature ブランチ上で速やかに修正します。
    - 修正後、再度 `./gradlew :backend:test` および `./gradlew :backend:mediumTest`
      を実行して全テストが成功することを確認し、追加コミット・PR更新を行います。
    - **レビューと指摘事項修正のサイクルは3回繰り返して実施**
      し、潜在的な不具合や設計上の課題を段階的に洗練・排除して、開発効率と長期的な品質・保守性の便益を最適化します。

---

## 9. イミュータブルデータモデル設計指針 (Immutable Data Modeling Guidelines)

本プロジェクトでは、データの信頼性、監査性、および整合性を最大化するため、データモデリングにおいて
**イミュータブルデータモデル（不変データモデル）** の設計思想を採用します。

### 9.1 基本概念と設計原則

1. **追記型データ管理 (INSERT-only)**:
    - データを「状態の上書き（UPDATE）」として扱わず、システム内で発生した「事実（イベント）の記録（INSERT）」として時系列に蓄積します。
    - 過去の任意の時点における正確な状態を完全に再現可能とし、データの非破壊性と完全な監査証跡（Auditability）を保証します。

2. **リソース (Resource) とイベント (Event) の分離**:
    - **リソース (R)**: 人、モノ、組織、場所などの実体（名詞・複数形で命名、例: `customers`, `products`,
      `employees`）。
    - **イベント (E)**: 発生した取引、判定、処理結果などの出来事（名詞句・動名詞・複数形で命名、例:
      `orders`, `payments`, `shipments`）。日時の属性を必ず保持します。

3. **訂正・キャンセルの扱い (Handling Corrections)**:
    - 過去のイベントレコードを直接 UPDATE せず、「取消イベント（赤黒処理）」や「訂正イベント（新たなバージョンのイベント）」を新規
      INSERT して表現します。
    - 業務分析・業務理解の段階ではイベントの修正（訂正イベント）を扱い、リソースの誤入力訂正や物理削除はシステム設計（CRUD・運用設計）のフェーズで分離して扱います。

4. **技術的関心事（マルチテナンシー等）の分離**:
    - 業務分析・ドメインモデリング段階では、ドメインの本質的な理解を阻害するマルチテナントID（
      `tenant_id`）や技術的カラムを排除し、詳細設計・インフラ層設計フェーズで組み込みます。

5. **命名規則と属性ルール**:
    - **自エンティティ名の省略**: 自身のエンティティ名を属性名に含めません（例: `orders` エンティティの受注日は
      `order_date` ではなく `occurred_at` または `effective_date`）。
    - **外部参照時のプレフィックス**:
      他エンティティを参照する外部キーには対象エンティティ名をプレフィックスとして明記します（例:
      `customer_id`, `product_id`）。

6. **Java / Spring Modulith 実装との統合**:
    - 不変データキャリアに Java `record` を積極的に使用し、事前条件検証（`*Precondition`）と組み合わせます。
    - 状態変更はミューテーションではなく、ドメインイベント（`@DomainEvents`
      ）の発行や新しい不変インスタンスの生成として実装します。

---

## 10. エージェント向けスキル一覧 (Agent Skills)

| スキル                 | 配置                                           | 用途                                                        |
|:-----------------------|:-----------------------------------------------|:------------------------------------------------------------|
| `modulith-dev`         | ``                                             | バックエンドの実装・テスト作成・アーキテクチャ検証          |
| `pr-review`            | ``                                             | PR のレビュー（第 8 章の反復レビュー）                      |
| `requirement-analysis` | `.claude/skills/requirement-analysis/SKILL.md` | 要求分析・要件定義・受け入れ基準の作成                      |
| `frontend-design`      | `.claude/skills/frontend-design/SKILL.md`      | フロントエンドの画面・コンポーネント設計と Storybook テスト |
