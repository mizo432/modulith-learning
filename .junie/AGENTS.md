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
