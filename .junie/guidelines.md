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
    - H2 Database (テスト実行用インメモリ)
    - Flyway (DBマイグレーション)
- **主要ライブラリ**:
    - **Lombok**: ボイラープレートコード削減 (`@Getter`, `@Setter`, `@RequiredArgsConstructor`,
      `@UtilityClass` 等)
    - **JSpecify**: Null 安全性アノテーション (`@NonNull`, `@Nullable`)
    - **jMolecules**: オニオンアーキテクチャ・DDD 構造定義
    - **YAVI**: 型安全なバリデーションライブラリ
    - **Google Guava / ICU4J / libphonenumber / Apache POI**: 共通ユーティリティ
    - **Caffeine**: インメモリキャッシュ
- **テストフレームワーク**:
    - JUnit 5 (JUnit Jupiter)
    - AssertJ, AssertJ-DB
    - Spring Modulith Test (`ApplicationModules`, `Documenter`)

---

## 3. モジュール構造とパッケージ規約 (Module & Package Rules)

バックエンド (`backend/src/main/java/undecided`) は、以下の構造でモジュール化されています。

### 3.1 モジュール区分

- **汎用・業務モジュール (`undecided.generic.*`)**:
    - `addressReg`: 住所・都道府県管理
    - `bankReg`: 銀行・支店管理
    - `rerlationshipMgmt`: 顧客・組織・従業員関係管理
    - `productSaleMgmt`: 商品販売管理
- **ERP モジュール (`undecided.erp.*`)**:
    - 業務処理・メッセージング機能
- **基盤・共通モジュール (`undecided.supporting.*`)**:
    - `primitive`: プリミティブ拡張ユーティリティ (`Strings2`, `Ints`, `Objects2` 等)
    - `precondition`: 引数・状態検証事前条件 (`StringPrecondition`, `ObjectPrecondition` 等)
    - `logger`: ログ出力基盤 (`LogIdBasedLogger`)
    - `snowflake`: 分散ID生成
    - `uuidV7Provider`: UUIDv7 生成プロバイダー

### 3.2 パッケージ境界ルール

1. **公開インターフェース / SPI (`spi` またはモジュールのルート直下)**:
    - 他モジュールから呼び出し可能な DTO、Query/Command インターフェース、ドメインモデルを配置します。
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

---

## 5. テスト戦略とテスト作成規約 (Testing Guidelines)

### 5.1 テストサイズと実行区分

テストは JUnit 5 の `@Tag` アノテーションを用いて 3 段階に分類します。

| タグ             | 対象                | 特徴                                                         | 実行コマンド                    |
|:-----------------|:--------------------|:-------------------------------------------------------------|:--------------------------------|
| `@Tag("small")`  | 単体テスト          | Spring コンテキスト起動なし、高速実行、モック利用            | `./gradlew :backend:test`       |
| `@Tag("medium")` | 統合テスト          | Spring コンテキスト起動、DB/リポジトリ連携、モジュール間結合 | `./gradlew :backend:mediumTest` |
| `@Tag("large")`  | システム/負荷テスト | E2E、パフォーマンステスト、外部システム連携                  | `./gradlew :backend:largeTest`  |

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
