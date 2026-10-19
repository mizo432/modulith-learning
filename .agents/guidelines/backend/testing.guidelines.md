# テスト戦略とテスト作成規約 (Testing Guidelines)

## 1. テストサイズと実行区分

テストは JUnit 5 の `@Tag` アノテーションを用いて 3 段階に分類します。

| タグ             | 対象                | 特徴                                                         | 実行コマンド                    |
|:-----------------|:--------------------|:-------------------------------------------------------------|:--------------------------------|
| `@Tag("small")`  | 単体テスト          | Spring コンテキスト起動なし、高速実行、モック利用            | `./gradlew :backend:test`       |
| `@Tag("medium")` | 統合テスト          | Spring コンテキスト起動、DB/リポジトリ連携、モジュール間結合 | `./gradlew :backend:mediumTest` |
| `@Tag("large")`  | システム/負荷テスト | E2E、パフォーマンステスト、外部システム連携                  | `./gradlew :backend:largeTest`  |

- `./gradlew :backend:test` はタグなしと `small` を実行し、`medium` / `large` を除外します（タイムアウト
  60 秒）。

### Medium テスト（統合テスト）の構成

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

### フロントエンドのテスト

- フロントエンド (`frontend`) のテストは **Storybook** で記述します。
- コンポーネントと同じディレクトリに `*.stories.tsx` を置き、loading / error / empty / success
  の各状態をストーリーにします。
- 操作の検証は `play` 関数で行います。Jest 単体の `*.test.tsx` を新規に作る前に、Storybook
  で書けるか検討してください。

## 2. 良い単体テストの4つの柱

本書『単体テストの考え方/使い方』（Vladimir Khorikov）に基づき、良い単体テストが備えるべき性質として4つの柱を定義します。
テストの価値は、4つの柱の **掛け算**として考えます。どれか1つでも欠けると価値は0になります。

| 柱                           | 問い                                                         | 説明                                     |
|:-----------------------------|:-------------------------------------------------------------|:-----------------------------------------|
| **退行に対する保護**         | 本体のコードを壊したら、このテストは赤くなるか               | バグの再発を検知できるか                 |
| **リファクタリングへの耐性** | 振る舞いを変えずに中身を整理しても、このテストは通り続けるか | 内部実装の変化に敏感になりすぎていないか |
| **迅速なフィードバック**     | このテストはすぐに終わり、すぐに結果が分かるか               | 開発フローを阻害していないか             |
| **保守のしやすさ**           | このテストは読めば意図が分かり、動かすための準備が少ないか   | 将来のメンテナンスコストが低い           |

## 3. AIによるテスト生成の注意点

AI（Claude Code など）を使ってテストを生成する際は、以下の点に注意します。

- **AIは実装をなぞるテストを書きやすい**:
  仕様を渡さずに「テストを書いて」と頼むと、AIは目の前の実装だけが手がかりになり、内部の呼び出し順まで確かめるテストや、誤った結果を期待値として固定するテストが生まれやすい。
- **網羅率だけを上げるテスト**: 結果をほとんど確かめていなくても、網羅率は100%になります。網羅率は低い場所に気づくための合図として使い、目標の数字にはしません。
- **人の検証が不可欠**:
  AIへの指示で「内部の作りではなく振る舞いを確かめること」「期待値は仕様から決めること」と伝えれば、出てくるテストは良くなります。ただし、出てきたテストに兆候が無いかを確かめるのは人の仕事です。

## 4. レビューで気づくための兆候

テストコードのレビュー時、以下の兆候がないか確認します。

### 誤報の兆候（False Positive）

- 本体のコードを壊しても緑のままになるテスト
- 取ってくるだけの処理（スタブ）の戻り値を、そのままアサーションしているテスト
- モックの呼び出し順や引数まで厳密に検証し、振る舞いを変えずに整理しただけで赤くなるテスト

### 見逃しの兆候（False Negative）

- 外へ出ていく副作用（DB書き込み、メール送信など）をモックで置き換えているのに、呼び出しがあることしか確かめていないテスト
- 書き込みを確かめていても、「何かしら書いた」ことしか見ておらず、中身が正しいかは見ていないテスト
- 期待値を実装と同じ計算式で作っているテスト（実装が間違っていても一致する）

### 守る価値がない兆候

- 単純な値の受け渡しのように、壊れようのない部分だけをなぞっているテスト
- 赤にも緑にも意味がなく、維持する手間だけが残るテスト

## 5. テストを足場に設計を整える

良いテストが書けるかどうかは、テストの書き方だけでは決まりません。テストの書きにくさは、設計をどこで分ければよいかを教えてくれる合図です。

- **判断を切り出す**:
  業務の判断を外とのやり取り（データベースの読み書きなど）から分離します。判断は、受け取った値だけで答えを返す形にすれば、「入力を渡して返ってきた値だけを確かめる」出力値ベースのテストが書けます。
- **コマンドクエリ分離（CQS）**:
  メソッドを「値を返すだけ（クエリ）」と「状態を変えるだけ（コマンド）」に分けます。取ってくるだけの処理はスタブ、外へ出ていく副作用はモックという使い分けも、この区別に基づきます。
- **切り出すことと、中身を晒すことは違う**:
  テストを書きやすくするために、本来隠しておくべき中身を外から触れるようにしてはいけません。切り出した判断は「この入力ならこの結果」という約束を持つ新しい部品ですが、晒した中身は約束ではなく作りの詳細です。

## 6. テストコード作成の厳格ルール

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
    - `null` 引数、空文字 `""`、空白文字 `" "`、境界値（最大/最小）、異常系の例外スローを必ずテストする。

## 7. テストコード標準例 (Demonstration Example)

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
