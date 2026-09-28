# AIエージェント向けプロジェクト情報 (Project Information for Agents)

このドキュメントは、本プロジェクトで作業する開発者およびAIエージェント向けの技術仕様および開発情報を提供します。

## ビルドと構成 (Build and Configuration)

- **ビルドシステム**: Gradle
- **Javaバージョン**: OpenJDK 25 (`backend/build.gradle.kts` 内の Gradle Toolchain で構成)
- **プロジェクト構造**: Spring Modulith を使用したモジュラーモノリス (Modular Monolith)
    - `backend`: コア業務ロジックおよびインフラストラクチャ
    - `frontend`: React / MUI による SPA
    - `api-gateway`: ルーティング
    - `service-registry`: Netflix Eureka
    - `authorization`: セキュリティ / 認証・認可
- **データベース**: PostgreSQL (一部モジュールで Flyway により管理)

### 主要コマンド (Key Commands)

- プロジェクトのビルド: `./gradlew build`
- バックエンドの起動: `./gradlew :backend:bootRun`
- 単体テスト (Small) の実行: `./gradlew :backend:test`
- 統合テスト (Medium) の実行: `./gradlew :backend:mediumTest`
- システム/負荷テスト (Large) の実行: `./gradlew :backend:largeTest`

## テスト情報 (Testing Information)

### テスト戦略 (Test Strategy)

- **フレームワーク**: JUnit 5, AssertJ
- **テスト分類**: テストは `@Tag("small")`, `@Tag("medium")`, `@Tag("large")` でタグ付け
- **モジュリス境界検証**: `SpringModulithTest` を使用してモジュール境界を検証

### テスト作成規約 (Guidelines for Adding Tests)

- **命名規則**: メソッド名は必ず `should` で開始し、アンダースコアは使用しない (例:
  `shouldReturnCorrectValue`)
- **アノテーション**:
    - クラスおよびメソッドの両方に `@DisplayName` で日本語の説明を付与する
    - `@Tag` でテストサイズを指定する
- **構造化**:
    - テスト対象メソッドごとに `@Nested` クラスでネストする (例: `class AddMethodTest`)
    - テストクラスおよびメソッドは原則として `package-private` (修飾子なし) とする
- **網羅性**: `null` 引数などの境界値テストを必ず含める

### テストコード作成例 (Demonstration Test Example)

```java

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
      boolean result = isEmpty.test(null);
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("空文字列が渡された場合、trueを返すこと")
    void shouldReturnTrueWhenInputIsEmpty() {
      boolean result = isEmpty.test("");
      assertThat(result).isTrue();
    }
  }
}
```

## 開発手順 (Development Procedure)

本プロジェクトの開発作業（機能追加、リファクタリング、バグ修正、テスト作成等）は、原則として **GitHub
Issue** を起点に進行します。

1. **Issue の選定・把握**: 対象 Issue の要件・受け入れ基準を確認。
2. **feature ブランチの作成**: 作業用の feature ブランチ（例: `feature/issue-<number>-<description>`
   ）を作成して作業開始。
3. **設計・テスト計画**: モジュール境界（Spring Modulith）および層構成（オニオンアーキテクチャ）に準拠した設計とテストケース策定。
4. **実装・テスト作成**: Google Java Style、Lombok、JSpecify、事前条件検証 (`*Precondition`)
   、および厳格なテスト規約（`should` 命名、日本語 `@DisplayName`、`@Nested` 構造）に従ったコード作成。
5. **テスト・検証**: `./gradlew :backend:test` や `./gradlew :backend:mediumTest` による検証。
6. **Issue 番号の紐付けとコミット**: コミットメッセージに対象 Issue 番号（例: `refs #XX`,
   `closes #XX`）を記載。
7. **PR の作成**: 実装と検証完了後、レビューおよびマージのための Pull Request (PR)
   を作成（概要・関連Issue・テスト結果を記載）。
8. **レビューSKILLによるPRレビュー**: PR レビュースキル (`pr-review`)
   を活用し、モジュール境界、オニオンアーキテクチャ、テスト規約、Null 安全性、例外処理等の観点でコードを多角的にレビュー・検証。
9. **レビュー指摘の修正**: レビューで指摘された要修正点を修正し、テストを再実行してパスすることを確認。

## 追加の開発情報 (Additional Development Information)

- **コードスタイル**:
    - Google Java Style に準拠
    - Lombok の積極的な活用 (`@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@UtilityClass`)
    - JSpecify アノテーションによる Null 安全性の明示 (`@NonNull`)
- **アーキテクチャパターン**:
    - オニオンアーキテクチャ (Domain, Business, Infrastructure, Presentation)
    - ドメイン駆動設計 (DDD) の原則
- **モジュリスドキュメント**: `./gradlew :backend:test` を実行すると
  `backend/build/spring-modulith-docs` にドキュメントが生成される
- **共通ユーティリティ**: 頻繁に使用されるユーティリティクラスは `undecided.supporting.primitive`
  に配置 (例: `Strings2`, `Ints`, `Objects2`)
