# テスト駆動開発（TDD）ガイドライン

このドキュメントは、 **modulith-learning**プロジェクトにおけるテスト駆動開発（Test-Driven Development:
TDD）の実践方法を定義するガイドラインです。Spring Modulith を活用したモジュラーモノリスアーキテクチャにおいて、TDD
を通じて信頼性の高いソフトウェアを継続的に提供するためのアプローチを記載します。

---

## 1. TDD の目的と基本概念

### 1.1 TDD とは

TDD は、 **本番コードを書く前にテストを書く**という開発手法です。以下のサイクルを短い間隔で繰り返すことで、設計の質を高めながら機能を構築します。

1. **Red（赤）**: 失敗するテストを書く
2. **Green（緑）**: テストが通る最小限の実装を書く
3. **Refactor（リファクタリング）**: コードを整理する

### 1.2 TDD の目的

TDD は単なるテスト手法ではなく、 **設計手法**です。以下の目的を達成するために実践します。

- **ボトムアップの設計**: 小さなテストから始めて、ボトムアップで設計を構築する
- **信頼の構築**: テストスイートを通じて、システムへの信頼を継続的に高める
- **オーバーコーディングの防止**: 必要最小限のコードだけを書くことを強制する
- **リファクタリングの安全網**: 大胆なコード整理をテストが保証する

### 1.3 TDD の価値

TDD の価値は、以下の要素の **掛け算**として考えます。

| 要素                         | 問い                                               | 説明                                     |
|:-----------------------------|:---------------------------------------------------|:-----------------------------------------|
| **退行に対する保護**         | 本体のコードを壊したら、テストは赤くなるか         | バグの再発を検知できるか                 |
| **リファクタリングへの耐性** | 振る舞いを変えずに整理しても、テストは通り続けるか | 内部実装の変化に敏感になりすぎていないか |
| **迅速なフィードバック**     | テストはすぐに終わり、すぐに結果が分かるか         | 開発フローを阻害していないか             |
| **保守のしやすさ**           | テストは読めば意図が分かり、動かす準備が少ないか   | 将来のメンテナンスコストが低い           |

### 1.4 核心原則

> 「テストとは、動くことを証明するものではない。間違いを見つけるためのものだ。」

---

## 2. TDD の基本サイクル

### 2.1 Red-Green-Refactor

TDD の基本サイクルは、以下の3つのフェーズから成ります。

#### Red（赤）フェーズ

1. 新しい機能や修正に対する **失敗するテスト**を書く
2. テストを実行し、 **確実に失敗すること**を確認する
3. 失敗理由がテストの意図通りであることを確認する

**重要なポイント**:

- テストが最初から緑になる場合は、テストが意味をなしていない可能性があります
- 失敗するテストを書くことで、機能の要件を明確に定義する

#### Green（緑）フェーズ

1. テストが通る **最小限の実装**を書く
2. 完璧な実装を目指さない — テストが通れば十分
3. ハードコーディングでも構わない（リファクタリングフェーズで整理する）

**重要なポイント**:

- 「テストが通る」ことに集中し、設計の完璧さを追求しない
- 一度に多くの機能を实装しようとせず、最小単位で進める

#### Refactor（リファクタリング）フェーズ

1. テストが緑のまま、コードの品質を向上させる
2. 重複の削除、命名の改善、構造の整理を行う
3. テストコード自体もリファクタリングの対象とする

**重要なポイント**:

- 外部からの振る舞いを変えずに整理する
- テストが緑のままであることを確認しながら進める

### 2.2 サイクルの反復

新しい機能が追加されるたびに、上記のサイクルを繰り返します。

- 小さなステップで進める — 一度に大きな変更をしない
- 各ステップでテストが緑であることを確認する
- 進捗に応じて、必要に応じてテストを追加・修正する

### 2.3 TDD手順（Red/Green TDDサイクル厳守）

1. 押さえておきたいテストシナリオを洗い出し、リスト（テストリスト）にまとめる
2. テストリストの中から「ひとつだけ」選び出し、実際に、具体的で、実行可能なテストコードに翻訳し、テストが失敗することを確認する
    - `test:` をつけてコミット
3. プロダクトコードを変更し、いま書いたテスト（および、それまでに書いたすべてのテスト）を成功させる
   （その過程で気づいたことはテストリストに追加する）
    - `feat:` をつけてコミット
4. 必要に応じてリファクタリングを行い、実装の設計を改善する
    - `refactor:` をつけてコミット
5. テストリストが空になるまでステップ2に戻って繰り返す

### 2.4 テストリスト

- 実装前にテストケースをリストアップ（テストリスト作成）
- 「〜したとき、〜となる」形式で振る舞いを分解
- 最も単純なケースから始める
- テストリストは途中で変更OK（柔軟に修正）

### 2.5 AAAパターン

- **Arrange（準備）:** テスト対象と入力データを準備
- **Act（実行）:** テスト対象のメソッドを実行
- **Assert（検証）:** 期待する結果を検証
- 3つのブロックを空行で明確に分離

### 2.6 テストの質

- 振る舞いをテストする（実装詳細をテストしない）
- 境界値・異常系を必ずカバー
- 1テスト1アサーション（原則）
- テスト間の依存を避ける（各テストは独立）
- 過度なモックを避ける（外部境界のみモック化）
- テストは読みやすさを優先（書きやすさより）

### 2.7 よくある過ち（禁止）

- アサーションを削除してテストが成功したふり
- テスト対象の実行値をコピーして期待値にペースト
- レッド→グリーンの過程でリファクタリングを混ぜ込む
- 早すぎる抽象化（重複はヒントであって指令ではない）
- 巨大なテスト（3アサーション超えは要注意）

---

## 3. TDD の実践手順

### 3.1 問題の分析

TDD を始める前に、問題を分析し、小さなステップに分割します。

1. **要件を理解する**: ビジネス観点から、何を解決したいかを明確にする
2. **入力・出力を定義する**: 各シナリオの入力と期待される出力を整理する
3. **小さなステップに分割する**: 実装の詳細に深入りせず、連続した完全なステップに分解する

### 3.2 テストの作成

1. **ユースケースからテストケースを導く**: 各ユースケースに対応するテストケースを作成する
2. **境界値・異常値を考慮する**: null、空文字、境界値、例外ケースをテストに含める
3. **読みやすいテストを書く**: プログラマー以外でもテストを読めるようにする

### 3.3 実装の進め方

1. **最小実装から始める**: テストが通る最小のコードを書く
2. **インクリメンタルに改善する**: 機能ごとにテストを追加し、実装を進める
3. **リファクタリングを継続する**: 緑になったら、コードの品質を向上させる

---

## 4. Spring Modulith における TDD

### 4.1 モジュール単位のテスト戦略

Spring Modulith では、モジュールごとにテストを分離し、依存関係を明確にします。

#### モジュール内テスト

- モジュール内のドメインロジックを単体テストで検証する
- Spring コンテキストを起動しない高速テストを優先する
- `@Tag("small")` でタグ付けする

#### モジュール間統合テスト

- モジュール間の連携を統合テストで検証する
- Spring Modulith Test を活用し、モジュールの依存関係をテストする
- `@Tag("medium")` でタグ付けする

#### システム全体テスト

- E2E テストでシステム全体の振る舞いを検証する
- `@Tag("large")` でタグ付けする

### 4.2 Spring Modulith Test の活用

Spring Modulith は、モジュールの依存関係やイベントのフローをテストするためのツールを提供します。

#### モジュール構造のテスト

```java
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.docs.Documenter;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Events;

import static org.springframework.modulith.test.Assertions.*;

@ApplicationModuleTest
class ModuleStructureTest {

  @Test
  void shouldVerifyModuleDependencies() {
    ApplicationModule module = ApplicationModule.of(TaskService.class);
    
    // モジュールの依存関係を検証
    assertThat(module).dependsOn(UserService.class);
    assertThat(module).doesNotDependOn(OrderService.class);
  }
}
```

#### ドメインイベントのテスト

```java
@ApplicationModuleTest
class TaskServiceEventTest {

  @Test
  void shouldPublishTaskCreatedEvent() {
    // Act
    taskService.createTask(new CreateTaskCommand("タスク名", "説明"));

    // Assert - ドメインイベントの発行を検証
    Events.recordedEvents()
        .forApplicationModule("task")
        .shouldHaveDispatchedEventOfType(TaskCreated.class)
        .andShouldHaveNotDispatchedAnyOtherEvents();
  }
}
```

### 4.3 モジュール境界のテスト

モジュール間の境界を明確にするために、以下のテストを実装します。

1. **モジュールの依存関係テスト**: 期待しない依存関係がないことを検証する
2. **モジュール間のイベントフローテスト**: ドメインイベントが正しく伝播することを確認する
3. **モジュールの公開APIテスト**: モジュールが公開するインターフェースが期待通りであることを検証する

---

## 5. テストコードの作成ルール

### 5.1 命名規則

- テストメソッド名は必ず **`should`** で開始する
- キャメルケースを使用し、 **アンダースコア（`_`）は使用しない**
- 例: `shouldReturnTrueWhenInputIsNull`

### 5.2 アノテーション

- **テストクラスおよびテストメソッドの両方に `@DisplayName` を必ず付与する**
- 日本語で意図を明確に記述する

### 5.3 構造化

- テスト対象のメソッドごとに **`@Nested`** クラスを作成してグループ化する
- 境界値・異常値テストは必ず網羅する

### 5.4 アサーション

- **AssertJ** (`assertThat`, `assertThatThrownBy`) を使用する
- アサーションには極力 `.as("...")` で意図を明記する

### 5.5 可視性

- テストクラス、ネストクラス、テストメソッドはすべて **`package-private`** とする

---

## 6. テストコード標準例

### 6.1 単体テスト例

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

### 6.2 統合テスト例

```java
@Tag("medium")
@DisplayName("タスク作成サービスの統合テスト")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
class TaskServiceIntegrationTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  @DisplayName("タスクが正常に作成され、リポジトリに保存されること")
  void shouldCreateAndSaveTask() {
    // Arrange
    CreateTaskCommand command = new CreateTaskCommand("テストタスク", "説明");

    // Act
    TaskId taskId = taskService.createTask(command);

    // Assert
    Task task = taskRepository.findById(taskId).orElseThrow();
    assertThat(task.getName())
        .as("タスク名が正しく保存されること")
        .isEqualTo("テストタスク");
  }
}
```

### 6.3 モジュールテスト例

```java
@Tag("medium")
@DisplayName("タスクモジュールのテスト")
@ApplicationModuleTest
class TaskModuleTest {

  @Autowired
  private TaskService taskService;

  @Test
  @DisplayName("タスク作成時にドメインイベントが発行されること")
  void shouldPublishTaskCreatedEvent() {
    // Act
    taskService.createTask(new CreateTaskCommand("タスク名", "説明"));

    // Assert
    Events.recordedEvents()
        .forApplicationModule("task")
        .shouldHaveDispatchedEventOfType(TaskCreated.class);
  }
}
```

---

## 7. TDD 実践上の重要な考え方

### 7.1 テストの書きにくさは設計の合図

テストが書きにくい場合は、設計を見直すきっかけとします。

- **判断を切り出す**: 業務の判断を外とのやり取りから分離する
- **コマンドクエリ分離（CQS）**: 「値を返すだけ」と「状態を変えるだけ」を分ける
- **切り出すことと、中身を晒すことは違う**: テストを書きやすくするために、本来隠すべき中身を公開してはいけない

### 7.2 AI によるテスト生成の注意点

AI を使ってテストを生成する際は、以下の点に注意します。

- **AI は実装をなぞるテストを書きやすい**: 仕様を渡さずにテストを頼むと、内部の呼び出し順まで確かめるテストが生まれやすい
- **網羅率だけを上げるテスト**: 結果をほとんど確かめていなくても、網羅率は100%になる
- **人の検証が不可欠**: AI への指示で「内部の作りではなく振る舞いを確かめること」「期待値は仕様から決めること」と伝える

### 7.3 レビューで気づくための兆候

#### 誤報の兆候（False Positive）

- 本体のコードを壊しても緑のままになるテスト
- スタブの戻り値をそのままアサーションしているテスト
- モックの呼び出し順まで厳密に検証し、振る舞いを変えずに整理しただけで赤くなるテスト

#### 見逃しの兆候（False Negative）

- 副作用をモックで置き換えているのに、呼び出しがあることしか確かめていないテスト
- 「何かしら書いた」ことしか見ておらず、中身が正しいかは見ていないテスト
- 期待値を実装と同じ計算式で作っているテスト

---

## 8. TDD の進め方

### 8.1 小さく始めて、小さく失敗する

- 1回のテストサイクルで完全に目的を達成できる実装は作れない
- テストを書いて、実装して、気付きを反映して…と、試行錯誤のサイクルを小さくすることが重要
- いきなり大きな設計をしようとせず、小さい部分から始める

### 8.2 インクリメンタルな改善

- モデルもコードもインクリメンタルに改善していくものなので、最初から完全なものは作れない
- 各ステップでテストが緑であることを確認しながら進める
- 必要に応じて、テストを追加・修正・削除する

### 8.3 部分適用の基準

TDD を部分適用する場合、適用する基準を明文化することが必要です。基準なく担当者個別で判断すると、あとあと収集がつかなくなります。

---

## 9. レビュールール

### 9.1 レビューポイント

- テストリストが適切に分解されているか
- Red-Green-Refactorサイクルが守られているか
- テストが振る舞いをテストしているか（実装詳細ではないか）
- AAAパターンが守られているか
- 境界値・異常系がカバーされているか
- テストの読みやすさ（他者が理解できるか）
- 最小変更の原則（課題の範囲外に手を加えていないか）

### 9.2 厳格なレビュー対策

- **甘い実装は許されない**
- 境界条件のテストは必須（空入力、null、負の値、最大値など）
- 異常系のテストは省略不可
- テスト名は振る舞いを明確に記述（曖昧な命名はNG）
- 実装は最小限に（過剰な設計は却下される）
- コードの重複はリファクタリング対象
- **レビューで問題が出ないよう、事前に自己チェックを徹底する**

---

## 10. コーディングスタイル

- Javaの標準的なコーディング規約に従う
- **テストメソッド名:** メソッド名は必ず `should` で開始し、キャメルケースを使用する。アンダースコア（
  `_`）は使用しない。
- **@DisplayNameアノテーション:** 各テストメソッドに`@DisplayName`アノテーションを付け、日本語でテスト内容を記述する
    - 例: `@DisplayName("1本倒した場合、スキットル番号が点数になる")`
    - メソッド名: `shouldReturnSkittleNumberWhenSingleSkittleKnockedDown()`
- 最小変更の原則: 課題の範囲外に手を加えない
- 変数名・メソッド名は意図を明確に

---

## 11. Gitコミットルール

- Gitのコミットは Conventional Commits を使います
- コミットメッセージは以下の形式に従うこと:
    - `test:` - テストコードを追加または修正したとき
    - `feat:` - 機能を実装または修正したとき
    - `refactor:` - リファクタリングを行ったとき
- コミットメッセージは簡潔に、変更内容を明確に説明すること

---

## 12. まとめ

TDD は、テストを書くこと自体が目的ではなく、
**良い設計を通じて信頼性の高いソフトウェアを継続的に提供するための手法**です。

- Red-Green-Refactor のサイクルを短い間隔で繰り返す
- テストの書きにくさは設計の合図として捉える
- Spring Modulith の機能を活用し、モジュール単位のテストを効果的に実施する
- AI を活用しつつ、人の検証を不可欠なものとする
- テストリストを作成し、振る舞いをテストする
- 厳格なレビュー基準を意識し、品質を高める

このガイドラインを遵守し、TDD を通じてソフトウェアの質を高めましょう。
