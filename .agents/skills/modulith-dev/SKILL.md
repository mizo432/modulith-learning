---
name: modulith-dev
description: Spring Modulithアーキテクチャ、オニオンアーキテクチャ、DDD、厳格なJUnit 6/AssertJテスト規約、例外処理、Lombok/JSpecifyを活用した開発・保守・テスト作成のためのプロジェクト特化型スキル。ユースケース開発にはcommand-usecase-devまたはquery-usecase-devスキルを使用してください。
---

### Modulith Development & Testing Guide (`modulith-dev`)

このスキルは、 **modulith-learning**
リポジトリにおいて、機能追加、リファクタリング、テスト作成、アーキテクチャ検証、バグ修正などを行う際に適用する開発・テストガイドラインです。

**ユースケース開発の場合**:

- **Commandユースケース**（状態変更処理）の開発には、`command-usecase-dev`スキルを使用してください。
- **Queryユースケース**（データ読み取り処理）の開発には、`query-usecase-dev`スキルを使用してください。

---

### 1. いつこのスキルを使用するか (When To Use)

- GitHub Issue に基づく機能追加、リファクタリング、テスト作成、バグ修正を実施するとき
- Spring Modulithおよびバックエンド (`backend`) の機能を開発、修正、拡張するとき
- 単体テスト（Small）、統合テスト（Medium）、大規模テスト（Large）を新規作成または更新するとき
- モジュール境界やオニオンアーキテクチャ（Domain / useCase / Infrastructure /
  Presentation）に沿った設計・実装を行うとき
- 例外処理（`BusinessException`, `SystemException`, `ResultMessage` 等）や事前条件チェック（
  `*Precondition`）を組み込むとき
- プロジェクト固有のコーディング規約（Lombok, JSpecify, Google Java Style）に従って実装するとき
- **ユースケース開発**: `command-usecase-dev`または`query-usecase-dev`スキルを参照

---

### 2. GitHub Issue を起点とした開発フロー (Issue-Driven Workflow)

1. **Issue の確認と要件把握**:
    - 対象 GitHub Issue の課題内容、要件、受け入れ基準を明確化し、作業スコープを決定。
2. **feature ブランチの作成**:
    - 最新のメインブランチから作業用 feature ブランチ（例:
      `feature/issue-<number>-<short-description>`）を作成して切り替え。
3. **設計・テスト方針の策定**:
    - 変更モジュールおよび層（Domain / useCase / Infrastructure / Presentation）を特定。
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

#### 3.2 パッケージ命名ルール (Package Naming Convention)

プロジェクトでは、パッケージ命名に以下のルールを適用します。

**ルートの命名規則**

- 新規モジュール・パッケージは必ず `undecided.*` 配下に配置します。
- `com.github.mizo432.shared.*` 配下は既存の共通モジュールとして残しますが、新規追加は行いません。
- 既存の `com.github.mizo432.shared.*` 配下のパッケージを整理する場合は、`undecided.*` 配下に移動します。

**パッケージ階層の命名規則**

| レベル | 命名規則 | 例 |
|:---|:---|:---|
| ルート | `undecided` | `undecided` |
| 事業領域 | 事業領域コード (汎用場合は `generic`) | `undecided.generic` |
| 業務領域 | 業務領域コード | `undecided.generic.accountMgmt` |
| モジュール | モジュール名 | `undecided.generic.accountMgmt.internal` |
| 層 | domain / useCase / infrastructure / presentation / internal | `undecided.generic.accountMgmt.internal.user` |
| SPI | `spi` (公開API) | `undecided.generic.accountMgmt.spi` |

**`com.github.mizo432.shared.*` から `undecided.*` への移行ルール**

既存の `com.github.mizo432.shared.*` 配下のパッケージを `undecided.*` へ移行する場合は、以下のマッピングに従います。

| 既存パッケージ | 移行先パッケージ | モジュールタイプ |
|:---|:---|:---|
| `com.github.mizo432.shared.config` | `undecided.config` | OPEN |
| `com.github.mizo432.shared.exception` | `undecided.supporting.exception` (検討中) | OPEN |
| `com.github.mizo432.shared.message` | `undecided.supporting.message` (検討中) | OPEN |
| `com.github.mizo432.shared.entity` | `undecided.supporting.entity` (検討中) | OPEN |
| `com.github.mizo432.shared.logging` | `undecided.supporting.logging` (検討中) | OPEN |
| `com.github.mizo432.shared.converter` | `undecided.supporting.converter` (検討中) | OPEN |
| `com.github.mizo432.shared.logger` | `undecided.supporting.logger` (検討中) | OPEN |
| `com.github.mizo432.shared.application` | `undecided.supporting.application` (検討中) | OPEN |
| `com.github.mizo432.shared.sensitive` | `undecided.supporting.sensitive` (検討中) | OPEN |
| `com.github.mizo432.shared.value` | `undecided.supporting.value` (検討中) | OPEN |

**移行時の手順**

1. 新パッケージに `@ApplicationModule(Type.OPEN)` アノテーション付き `package-info.java` を作成
2. クラスを新パッケージに移動
3. 全import文を更新
4. `./gradlew :backend:compileJava` でビルド検証
5. `./gradlew :backend:test` で全テスト検証
6. 旧パッケージを削除

**`mizo432-shared-common` モジュール**

- `com.github.mizo432.shared.common` 配下は独立したGradleモジュール (`mizo432-shared-common`) として存在
- precondition、primitive、annotation、functional などの共通ユーティリティを提供
- このモジュールは `undecided.*` への移行対象外とし、既存の構成を維持

#### 3.3 オニオンアーキテクチャ / DDD の層構成

1. **Domain 層**:
    - エンティティ、値オブジェクト（Value Object）、ドメインイベント、ドメインサービス、リポジトリインターフェース
    - 外部フレームワークに依存せず、ビジネスルール・不変条件を保護
2. **useCase 層**:
    - ユースケース、Command/Query サービス、アプリケーションロジック
    - **Commandユースケース**の詳細は`command-usecase-dev`スキルを参照
    - **Queryユースケース**の詳細は`query-usecase-dev`スキルを参照
3. **Infrastructure 層**:
    - JPA/Hibernate エンティティ、リポジトリ実装、外部サービス通信、Flyway マイグレーション
4. **Presentation 層**:
    - REST コントローラー (`@RestController`)、リクエスト/レスポンス DTO、バリデーション、グローバル例外ハンドラー

---

### 4. テスト作成規約 (Testing Guidelines & Standards)

プロジェクトでは厳格なテスト規約が定められています。すべてのテストはこのルールに厳密に準拠してください。

#### 4.1 テストの基本原則

- **フレームワーク**: JUnit 6 (`org.junit.jupiter.api.*`), AssertJ
  (`org.assertj.core.api.Assertions.assertThat`)
- **テストサイズのアノテーション**:
    - `@Tag("small")`: 単体テスト（Spring コンテキスト不要、高速実行）
    - `@Tag("medium")`: 統合テスト（モジュール間結合、DB/リポジトリ連携、Spring コンテキスト起動）
    - `@Tag("large")`: システムテスト / 負荷テスト
- **可視性**: テストクラスおよびテストメソッドはすべて **`package-private`**（アクセス修飾子なし）とします。

#### 4.1.1 良い単体テストの4つの柱

本書『単体テストの考え方/使い方』（Vladimir Khorikov）に基づき、良い単体テストが備えるべき性質として4つの柱を定義します。
テストの価値は、4つの柱の **掛け算**として考えます。どれか1つでも欠けると価値は0になります。

| 柱                           | 問い                                                         | 説明                                     |
|:-----------------------------|:-------------------------------------------------------------|:-----------------------------------------|
| **退行に対する保護**         | 本体のコードを壊したら、このテストは赤くなるか               | バグの再発を検知できるか                 |
| **リファクタリングへの耐性** | 振る舞いを変えずに中身を整理しても、このテストは通り続けるか | 内部実装の変化に敏感になりすぎていないか |
| **迅速なフィードバック**     | このテストはすぐに終わり、すぐに結果が分かるか               | 開発フローを阻害していないか             |
| **保守のしやすさ**           | このテストは読めば意図が分かり、動かすための準備が少ないか   | 将来のメンテナンスコストが低い           |

#### 4.1.2 AIによるテスト生成の注意点

AI（Claude Code など）を使ってテストを生成する際は、以下の点に注意します。

- **AIは実装をなぞるテストを書きやすい**:
  仕様を渡さずに「テストを書いて」と頼むと、AIは目の前の実装だけが手がかりになり、内部の呼び出し順まで確かめるテストや、誤った結果を期待値として固定するテストが生まれやすい。
- **網羅率だけを上げるテスト**: 結果をほとんど確かめていなくても、網羅率は100%になります。網羅率は低い場所に気づくための合図として使い、目標の数字にはしません。
- **人の検証が不可欠**:
  AIへの指示で「内部の作りではなく振る舞いを確かめること」「期待値は仕様から決めること」と伝えれば、出てくるテストは良くなります。ただし、出てきたテストに兆候が無いかを確かめるのは人の仕事です。

#### 4.1.3 レビューで気づくための兆候

テストコードのレビュー時、以下の兆候がないか確認します。

**誤報の兆候（False Positive）**

- 本体のコードを壊しても緑のままになるテスト
- 取ってくるだけの処理（スタブ）の戻り値を、そのままアサーションしているテスト
- モックの呼び出し順や引数まで厳密に検証し、振る舞いを変えずに整理しただけで赤くなるテスト

**見逃しの兆候（False Negative）**

- 外へ出ていく副作用（DB書き込み、メール送信など）をモックで置き換えているのに、呼び出しがあることしか確かめていないテスト
- 書き込みを確かめていても、「何かしら書いた」ことしか見ておらず、中身が正しいかは見ていないテスト
- 期待値を実装と同じ計算式で作っているテスト（実装が間違っていても一致する）

**守る価値がない兆候**

- 単純な値の受け渡しのように、壊れようのない部分だけをなぞっているテスト
- 赤にも緑にも意味がなく、維持する手間だけが残るテスト

#### 4.1.4 テストを足場に設計を整える

良いテストが書けるかどうかは、テストの書き方だけでは決まりません。テストの書きにくさは、設計をどこで分ければよいかを教えてくれる合図です。

- **判断を切り出す**:
  業務の判断を外とのやり取り（データベースの読み書きなど）から分離します。判断は、受け取った値だけで答えを返す形にすれば、「入力を渡して返ってきた値だけを確かめる」出力値ベースのテストが書けます。
- **コマンドクエリ分離（CQS）**:
  メソッドを「値を返すだけ（クエリ）」と「状態を変えるだけ（コマンド）」に分けます。取ってくるだけの処理はスタブ、外へ出ていく副作用はモックという使い分けも、この区別に基づきます。
    - **Commandユースケース**の詳細な開発ガイドは`command-usecase-dev`スキルを参照
    - **Queryユースケース**の詳細な開発ガイドは`query-usecase-dev`スキルを参照
- **切り出すことと、中身を晒すことは違う**:
  テストを書きやすくするために、本来隠しておくべき中身を外から触れるようにしてはいけません。切り出した判断は「この入力ならこの結果」という約束を持つ新しい部品ですが、晒した中身は約束ではなく作りの詳細です。

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

### 6. 値オブジェクト（Value Object）の作成規約

値オブジェクトはドメイン層の核心要素であり、不変（immutable）で属性のみで定義される型です。
以下の規約とテンプレートに従って作成してください。

#### 6.1 値オブジェクトの基本原則

- **インターフェース階層**:
    - `ValueObject` → `SingleValue<T>` → `StringValue<VO>` → `StringCode<VO>`（文字列ベース）
    - `ValueObject` → `Value<T>` → `DateTimeValue`（日付・時刻ベース）
- **不変性**: 値オブジェクトは一度作成されると変更できません。`final` フィールドを使用します。
- **値の等価性**: `@EqualsAndHashCode` (Lombok) を付与して、値の内容に基づいた等価比較を有効にします。
- **JPA プロキシ対応**: `protected` または `public` の引数なしコンストラクタを定義します。
- **ファクトリメソッド**:
    - `of(...)`: バリデーション付きのインスタンス作成
    - `empty()`: 空のインスタンス作成
    - `current()`: 現在時刻のインスタンス作成（日付・時刻ベースの場合）
- **JPA コンバーター**: `@Converter(autoApply = true)` を付与した内部クラスでデータベースとの変換を実装します。
- **バリデーション**: ファクトリメソッド内で `IllegalArgumentException` をスローして入力を検証します。
- **ユーティリティ使用**:
    - 文字列操作には `Strings2.isEmpty()`, `Strings2.toString()` を使用
    - 日付取得には `DateProvider.currentLocalDateTime()` を使用

#### 6.2 値オブジェクトテンプレート

- **文字列ベースのテンプレート**:
    - [StringValueObjectTemplate.java](templates/StringValueObjectTemplate.java): `StringCode`
      を実装する文字列型値オブジェクトの標準テンプレート（バリデーション付き）
- **日付・時刻ベースのテンプレート**:
    - [DateTimeValueObjectTemplate.java](templates/DateTimeValueObjectTemplate.java):
      `DateTimeValue` を実装する日付・時刻型値オブジェクトの標準テンプレート

---

### 7. バッチ処理開発ガイド (Batch Processing Development)

大量のレコードを扱うバッチ処理（Spring Batch）を開発する際は、以下の規約に従ってください。

#### 7.1 バッチ構成の基本パターン

バッチはモジュールの `internal/batch` 配下に配置し、役割ごとのサブパッケージで分離します。

| パッケージ  | 役割                                      |
|:------------|:------------------------------------------|
| `config`    | Job / Step の定義 (`*ImportBatchConfig`)  |
| `reader`    | 入力の読み込み（CSV は Super CSV を利用） |
| `processor` | 入力 DTO からドメインモデルへの変換・検証 |
| `writer`    | 永続化                                    |
| `dto`       | 入力レコードの DTO                        |
| `operator`  | Job の起動 (`*ImportJobOperator`)         |

#### 7.2 大量レコード処理のベストプラクティス

**チャンクサイズの設定**

- デフォルトのチャンクサイズは `50` とし、メモリ使用量とトランザクションのバランスを取ります。
- 大量レコードの場合は、チャンクサイズを `100` 〜 `500` の範囲で調整します。
- チャンクサイズは `*ImportBatchConfig` で `CHUNK_SIZE` 定数として定義します。

**エラー処理とフォールトトレランス**

- Step は必ず `.faultTolerant()` を設定します。
- `.skip(Exception.class)` と `.skipLimit(N)` でスキップする例外と上限を定義します。
- `.retry(Exception.class)` と `.retryLimit(N)` でリトライを設定します。

**メモリ効率の最適化**

- Reader はストリーミング方式で実装し、全データをメモリに読み込まないようにします。
- Writer は `saveAll()` などの一括保存メソッドを使用します。
- Processor では不要な中間データを保持しません。

**データ検証と整合性**

- Processor で入力データの必須項目を検証し、不正なデータはログに記録してスキップします。
- 既存データとの整合性を確認し、重複登録を防ぎます。

#### 7.3 バッチ処理のテスト戦略

- **単体テスト（Small）**: Processor の変換ロジック、境界値、異常系を網羅的にテストします。
- **統合テスト（Medium）**: 実際の CSV ファイルを使用して、Reader → Processor → Writer のフルフローを検証します。
- **負荷テスト（Large）**: 大量レコードでのチャンクサイズやメモリ使用量の影響を確認します。

---

### 8. 付属テンプレートとチェックリスト (Resources)

- **テストテンプレート**:
    - [UnitTestTemplate.java](templates/UnitTestTemplate.java): 単体テスト（Small）の標準テンプレート
    - [IntegrationTestTemplate.java](templates/IntegrationTestTemplate.java): 統合テスト（Medium）の標準テンプレート
- **値オブジェクトテンプレート**:
    - [StringValueObjectTemplate.java](templates/StringValueObjectTemplate.java):
      文字列ベースの値オブジェクトテンプレート
    - [DateTimeValueObjectTemplate.java](templates/DateTimeValueObjectTemplate.java):
      日付・時刻ベースの値オブジェクトテンプレート
- **バッチ処理テンプレート**:
    - [BatchConfigTemplate.java](templates/BatchConfigTemplate.java):
      バッチ Job / Step の設定テンプレート
    - [BatchItemProcessorTemplate.java](templates/BatchItemProcessorTemplate.java):
      CSV DTO からエンティティへの変換・検証テンプレート
    - [BatchItemWriterTemplate.java](templates/BatchItemWriterTemplate.java):
      エンティティの一括永続化テンプレート
    - [BatchCsvItemReaderTemplate.java](templates/BatchCsvItemReaderTemplate.java):
      Super CSV によるストリーミング読み込みテンプレート
    - [BatchCsvDtoTemplate.java](templates/BatchCsvDtoTemplate.java):
      CSV 入力レコードの DTO テンプレート
- **チェックリスト**:
    - [test-quality-checklist.md](checklists/test-quality-checklist.md): テスト作成時の品質確認項目
    - [architecture-checklist.md](checklists/architecture-checklist.md): モジュール分割・層構成の確認項目

---

### 9. 関連スキル

- **command-usecase-dev**: Commandユースケース（状態変更処理）の開発ガイドライン
- **query-usecase-dev**: Queryユースケース（データ読み取り処理）の開発ガイドライン
