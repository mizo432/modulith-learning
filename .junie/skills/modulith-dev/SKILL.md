---
name: modulith-dev
description: Spring Modulithアーキテクチャ、オニオンアーキテクチャ、DDD、厳格なJUnit 5/AssertJテスト規約、例外処理、Lombok/JSpecifyを活用した開発・保守・テスト作成のためのプロジェクト特化型スキル。
---

### Modulith Development & Testing Guide (`modulith-dev`)

このスキルは、 **modulith-learning**
リポジトリにおいて、機能追加、リファクタリング、テスト作成、アーキテクチャ検証、バグ修正などを行う際に適用する開発・テストガイドラインです。

---

### 1. いつこのスキルを使用するか (When To Use)

- GitHub Issue に基づく機能追加、リファクタリング、テスト作成、バグ修正を実施するとき
- Spring Modulithおよびバックエンド (`backend`) の機能を開発、修正、拡張するとき
- 単体テスト（Small）、統合テスト（Medium）、大規模テスト（Large）を新規作成または更新するとき
- モジュール境界やオニオンアーキテクチャ（Domain / Business / Infrastructure /
  Presentation）に沿った設計・実装を行うとき
- 例外処理（`BusinessException`, `SystemException`, `ResultMessage` 等）や事前条件チェック（
  `*Precondition`）を組み込むとき
- プロジェクト固有のコーディング規約（Lombok, JSpecify, Google Java Style）に従って実装するとき

---

### 2. GitHub Issue を起点とした開発フロー (Issue-Driven Workflow)

1. **Issue の確認と要件把握**:
    - 対象 GitHub Issue の課題内容、要件、受け入れ基準を明確化し、作業スコープを決定。
2. **feature ブランチの作成**:
    - 最新のメインブランチから作業用 feature ブランチ（例:
      `feature/issue-<number>-<short-description>`）を作成して切り替え。
3. **設計・テスト方針の策定**:
    - 変更モジュールおよび層（Domain / Business / Infrastructure / Presentation）を特定。
    - `should` で始まるテストケースを事前に整理。
4. **実装とテストの作成**:
    - モジュール境界（公開 API/SPI と `internal` の厳格な分離）を遵守。
    - 境界値・異常値系を含む網羅的なテストを作成。
5. **検証と Issue 紐付けコミット**:
    - `./gradlew :backend:test` や `./gradlew :backend:mediumTest` で検証。
    - コミットメッセージに対象 Issue 番号（例: `refs #XX`, `closes #XX`）を記載。
6. **Pull Request (PR) の作成**:
    - feature ブランチからベースブランチへの PR を作成。
    - 変更概要、関連 Issue、テスト・検証結果を記載。
7. **PR レビュースキルによるレビュー**:
    - PR レビュースキル (`pr-review`) を呼び出し、Spring Modulith 境界、オニオンアーキテクチャ、テスト規約（
      `should` 命名、日本語 `@DisplayName`、`@Nested`、境界値網羅）、Null 安全性、例外処理等の観点で多角的にレビュー・検証を実施。
8. **レビュー指摘事項の修正と3回の反復検証**:
    - レビュー結果で指摘された要修正点（規約違反、境界値テスト漏れ、例外ハンドリングの不備等）を修正。
    - 再度 `./gradlew :backend:test` や `./gradlew :backend:mediumTest` を実行して全件成功を確認し、コミットして
      PR を更新。
    - レビューと指摘事項修正の反復サイクルを3回実施し、潜在リスクの低減とコードの洗練を効率的・確実に達成。

---

### 3. アーキテクチャとモジュール設計規則 (Architecture & Modulith Rules)

#### 3.1 モジュール境界 (Spring Modulith)

- 各モジュールはルートパッケージ（例: `undecided.generic.rerlationshipMgmt`）の下に独立して配置されます。
- **公開API/SPIと内部実装の分離**:
    - 他モジュールから利用されるインターフェースやクラスは公開パッケージ（または `spi` パッケージ）に配置します。
    - モジュール内部のロジックは `internal` パッケージに配置し、他モジュールから直接参照してはなりません。
- **モジュール検証**:
    - `ApplicationModules.of(ModulithDemoApplication.class).verify()`
      によってモジュール循環や不正な依存関係がないことを検証します。

#### 3.2 オニオンアーキテクチャ / DDD の層構成

1. **Domain 層**:
    - エンティティ、値オブジェクト（Value Object）、ドメインイベント、ドメインサービス、リポジトリインターフェース
    - 外部フレームワークに依存せず、ビジネスルール・不変条件を保護
2. **Business / Application 層**:
    - ユースケース、Command/Query サービス、アプリケーションロジック
3. **Infrastructure 層**:
    - JPA/Hibernate エンティティ、リポジトリ実装、外部サービス通信、Flyway マイグレーション
4. **Presentation 層**:
    - REST コントローラー (`@RestController`)、リクエスト/レスポンス DTO、バリデーション、グローバル例外ハンドラー

---

### 4. テスト作成規約 (Testing Guidelines & Standards)

プロジェクトでは厳格なテスト規約が定められています。すべてのテストはこのルールに厳密に準拠してください。

#### 4.1 テストの基本原則

- **フレームワーク**: JUnit 5 (`org.junit.jupiter.api.*`), AssertJ
  (`org.assertj.core.api.Assertions.assertThat`)
- **テストサイズのアノテーション**:
    - `@Tag("small")`: 単体テスト（Spring コンテキスト不要、高速実行）
    - `@Tag("medium")`: 統合テスト（モジュール間結合、DB/リポジトリ連携、Spring コンテキスト起動）
    - `@Tag("large")`: システムテスト / 負荷テスト
- **可視性**: テストクラスおよびテストメソッドはすべて **`package-private`**（アクセス修飾子なし）とします。

#### 4.2 命名規則と DisplayName

- **テストメソッド名**:
    - 必ず `should` で開始する。
    - キャメルケースで記述し、 **アンダースコア（`_`）は絶対に使用しない**。
    - 例: `shouldReturnTrueWhenInputIsNull()`, `shouldThrowExceptionWhenArgumentIsEmpty()`
- **`@DisplayName`**:
    - **テストクラスおよびテストメソッドの両方に必ず付与する**。
    - 日本語で簡潔かつ具体的に振る舞いを記述する。

#### 4.3 クラス構造とネスト

- 対象メソッドごとに `@Nested` クラスを作成してテストを構造化する。
    - 例: `class AddMethodTest` や `class TestMethodTest`
- メソッド内の構造は **Arrange / Act / Assert** (Given / When / Then) を意識して整理する。

#### 4.4 網羅性と境界値テスト

- 引数が `null` のケース、空文字列 `""` や空コレクションのケースなど、境界値テストを必ず含める。
- 例外の検証には `assertThatThrownBy(...)` を使用する。

#### 4.5 テスト実行コマンド

```bash
# Smallテスト（単体テスト）の実行
./gradlew :backend:test

# Mediumテスト（統合テスト）の実行
./gradlew :backend:mediumTest

# Largeテスト（システム・負荷テスト）の実行
./gradlew :backend:largeTest

# プロジェクト全体のビルドと検証
./gradlew build
```

---

### 5. コーディング規約とユーティリティ (Coding Standards)

#### 5.1 Java言語仕様とスタイル

- **Java バージョン**: OpenJDK 25 / 26
- **レコード・Sealed型**: イミュータブルなデータ構造には Java `record` を積極的に活用。
- **Google Java Style**: インデント、命名規則、import 順序を既存コードに合わせる。

#### 5.2 アノテーション標準

- **Lombok**:
    - ボイラープレート削減のために `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@UtilityClass`
      などを適切に使用。
- **JSpecify**:
    - Null 安全性を明示するため、`@NonNull`, `@Nullable` を適切に付与。

#### 5.3 例外処理とメッセージング

- **例外の分類**:
    - 業務エラー（想定される例外）: `BusinessException`, `NotFoundBusinessException`
    - システム障害（予期せぬ例外）: `SystemException`
    - 複数メッセージ通知: `ResultMessagesNotificationException`, `ResultMessages`, `ResultMessage`
- **事前���件チェック**:
    - 引数や状態の検証には `undecided.supporting.precondition.*`（例: `StringPrecondition`,
      `ObjectPrecondition`）や `undecided.supporting.primitive.*` を活用。

---

### 6. 付属テンプレートとチェックリスト (Resources)

- **テストテンプレート**:
    - [UnitTestTemplate.java](templates/UnitTestTemplate.java): 単体テスト（Small）の標準テンプレート
    - [IntegrationTestTemplate.java](templates/IntegrationTestTemplate.java): 統合テスト（Medium）の標準テンプレート
- **チェックリスト**:
    - [test-quality-checklist.md](checklists/test-quality-checklist.md): テスト作成時の品質確認項目
    - [architecture-checklist.md](checklists/architecture-checklist.md): モジュール分割・層構成の確認項目
