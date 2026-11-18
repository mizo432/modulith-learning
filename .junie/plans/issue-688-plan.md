# Issue #688 計画書：NullAway の段階導入（Error Prone＋JSpecify、OnlyNullMarked で開始）

## 前提・依存

- **PR #689（Issue #687）のマージを前提とする**。PR #689 では `SnowflakeId.java` に `SnowflakeIdListConverter`（`List<SnowflakeId>` ⇔ `Long[]` の AttributeConverter）が追加される。本計画はマージ後の状態を前提に記述している。
- **実装は PR #689 マージ後に開始する**。本計画書は plan ステップの成果物であり、実装・コミットは行わない。

## 1. backend/build.gradle.kts への設定案

### 1.1 プラグイン追加

```kotlin
plugins {
    // 既存のプラグイン...
    id("net.ltgt.errorprone") version "4.1.0"  // Error Prone Gradle プラグインを追加
}
```

### 1.2 バージョン定義（extra プロパティに追記）

```kotlin
extra["errorpronePluginVersion"] = "4.1.0"
extra["nullawayVersion"] = "0.11.0"  // 最新安定版を確認し適用
```

### 1.3 依存関係追加（dependencies ブロックに追記）

```kotlin
errorprone("com.uber.nullaway:nullaway:${property("nullawayVersion")}")
errorproneJavac("com.google.errorprone:javac:9+181-b49-4624207")  // Java 25 対応の javac
```

### 1.4 コンパイラオプション設定（tasks.withType<JavaCompile> に追記）

```kotlin
tasks {
    withType<JavaCompile> {
        options.compilerArgs.addAll(
            listOf(
                "-Xep:NullAway:ERROR",              // NullAway のみ ERROR
                "-XepExcludedPaths:.*/src/test/.*",  // テストコードは対象外
                "-XepOpt:NullAway:OnlyNullMarked=true",  // @NullMarked パッケージのみ検査
                "-XepOpt:NullAway:JSpecifyMode=true",    // JSpecify モード有効化
                // 他の Error Prone チェックを全て無効化
                "-XepDisableAllChecks",
            )
        )
    }
}
```

### 1.5 設定のポイント

- `-XepDisableAllChecks` で全ての Error Prone チェックを無効化し、`-Xep:NullAway:ERROR` で NullAway のみを ERROR にする
- `OnlyNullMarked=true` で `@NullMarked` が付与されたパッケージのみを検査対象とする
- `JSpecifyMode=true` で JSpecify のアノテーション（`@NonNull`、`@Nullable`、`@NullMarked`）を解釈する
- `-XepExcludedPaths` で `src/test/` 以下のテストコードを除外

## 2. 第1段階の対象パッケージ：undecided.supporting.snowflake

### 2.1 対象パッケージのクラス一覧

| クラス | パッケージ | 種別 |
|--------|-----------|------|
| `SnowflakeId` | `undecided.supporting.snowflake` | public 値オブジェクト |
| `SnowflakeId.SnowflakeIdConverter` | `undecided.supporting.snowflake` | public 内部クラス（AttributeConverter） |
| `SnowflakeId.SnowflakeIdListConverter` | `undecided.supporting.snowflake` | public 内部クラス（#689 で追加予定） |
| `SnowflakeIdGenerator` | `undecided.supporting.snowflake` | public インターフェース |
| `NodeId` | `undecided.supporting.snowflake.internal` | パッケージプライベート |
| `SnowflakeIdGeneratorImpl` | `undecided.supporting.snowflake.internal` | public（internal パッケージ内） |
| `SnowflakeNode` | `undecided.supporting.snowflake.internal` | public JPA エンティティ |
| `SnowflakeNodeAllocator` | `undecided.supporting.snowflake.internal` | public Spring コンポーネント |
| `SnowflakeNodeJpaRepository` | `undecided.supporting.snowflake.internal` | public Spring リポジトリ |
| `SnowflakeNodeLeaseManager` | `undecided.supporting.snowflake.internal` | public Spring コンポーネント |
| `SnowflakeNodeLeaseScheduler` | `undecided.supporting.snowflake.internal` | public Spring コンポーネント |
| `TimeConfiguation` | `undecided.supporting.snowflake.internal` | public Spring 設定 |
| `SnowflakeConfiguration` | `undecided.supporting.snowflake.config` | public Spring 設定 |

### 2.2 package-info.java への @NullMarked 追加

#### 2.2.1 `undecided.supporting.snowflake` パッケージ

既存の `package-info.java` に `@NullMarked` を追加：

```java
@NullMarked
@NamedInterface(propagate = false)
package undecided.supporting.snowflake;

import org.jspecify.annotations.NullMarked;
import org.springframework.modulith.NamedInterface;
```

#### 2.2.2 `undecided.supporting.snowflake.internal` パッケージ

既存の `package-info.java` に `@NullMarked` を追加：

```java
@NullMarked
package undecided.supporting.snowflake.internal;

import org.jspecify.annotations.NullMarked;
```

#### 2.2.3 `undecided.supporting.snowflake.config` パッケージ

新しい `package-info.java` を作成：

```java
@NullMarked
package undecided.supporting.snowflake.config;

import org.jspecify.annotations.NullMarked;
```

### 2.3 nullability の棚卸し観点

各クラスごとに以下の観点で確認する：

#### SnowflakeId（public 値オブジェクト）
- `private final Long value`：フィールドは `@Nullable` とすべきか（EMPTY で null を許容するため）
- `of(@NonNull Long value)`：既存の `@NonNull` アノテーションは JSpecify と整合するか
- `reconstruct(Long value)`：引数が null 許容なので `@Nullable` 明示が必要
- `compareTo(@Nullable SnowflakeId other)`：既存の `@Nullable` は整合するか
- `SnowflakeIdConverter.convertToDatabaseColumn(SnowflakeId attribute)`：引数 null 許容か
- `SnowflakeIdConverter.convertToEntityAttribute(@Nullable Long dbData)`：既存の `@Nullable` は整合するか
- `SnowflakeIdListConverter`（#689 追加）：引数・戻り値の nullability を確認
- `toString()` の戻り値：`value` が null の場合 `"null"` を返す（`String.valueOf(null)` の挙動）

#### SnowflakeIdGenerator（public インターフェース）
- `nextId()`：プリミティブ long を返すため nullability 不要
- `nextIdValue()`：`SnowflakeId.of()` を経由するため null を返さない

#### NodeId（internal）
- `@Getter private Integer value`：フィールドは null 許容か（コンストラクタで設定されるが、Lombok の getter の戻り値型は `Integer`）

#### SnowflakeIdGeneratorImpl（internal）
- コンストラクタ引数：`long nodeId`、`long epochMillis` はプリミティブ
- `NodeId.getValue()` の戻り値：`Integer` で null の可能性があるか

#### SnowflakeNode（JPA エンティティ）
- `@Id @Getter private Integer nodeId`：JPA の `@Id` は通常 null 不可だが、Lombok getter の戻り値型は `Integer`
- `@Getter private String instanceName`：null 不可か
- `@Getter private LocalDateTime leaseUntil`：null 不可か
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)`：JPA 用無引数コンストラクタでフィールドが null になる状態

#### SnowflakeNodeAllocator（Spring コンポーネント）
- `allocate(String applicationName)`：引数・戻り値の nullability
- `renew(SnowflakeNode lease)`：引数・戻り値の nullability
- `repository.findById()` の戻り値：`Optional` なので null 不可

#### SnowflakeNodeJpaRepository（Spring リポジトリ）
- `findByIdForUpdate(int nodeId)`：戻り値は `Optional`

#### SnowflakeNodeLeaseManager（Spring コンポーネント）
- `private SnowflakeNode lease`：フィールドは `@PostConstruct` で初期化されるが、宣言時は null
- `nodeId()`：`lease` が null の場合の例外処理は既存
- `renew()`：同上

#### SnowflakeNodeLeaseScheduler（Spring コンポーネント）
- `renew()`：戻り値 void、引数なし

#### TimeConfiguation（Spring 設定）
- `clock()`：戻り値 `Clock` は null 不可

#### SnowflakeConfiguration（Spring 設定）
- `snowflakeIdGenerator(SnowflakeNodeLeaseManager leaseManager)`：Spring DI の引数は null 不可

### 2.4 修正が予想される箇所

`@NullMarked` を付与すると、以下の箇所が NullAway 違反として検出される可能性がある：

1. **SnowflakeId.value フィールド**：`Long value` は `@Nullable` として明示する必要がある（EMPTY で null を許容）
2. **SnowflakeId.reconstruct(Long value)**：引数に `@Nullable` を明示
3. **SnowflakeNodeLeaseManager.lease フィールド**：`@Nullable` として明示（`@PostConstruct` 前は null）
4. **SnowflakeNode の JPA 無引数コンストラクタ生成フィールド**：一時的に null になるため、フィールドに `@Nullable` を付与するか、Lombok の生成コードに対する NullAway の除外を検討
5. **SnowflakeIdGeneratorImpl(NodeId nodeId, ...)**：`nodeId.getValue()` の戻り値が `Integer`（null 可能性）だが、プリミティブ `long` に代入

## 3. 検証手順

### 3.1 段階的な適用とビルド検証

1. **ステップ1**：`backend/build.gradle.kts` に Error Prone + NullAway 設定を追加（`@NullMarked` 未追加の状態）
   - `./gradlew :backend:compileJava` が通るか確認（NullAway は `@NullMarked` なしでは何もしないため、エラーなし）

2. **ステップ2**：`undecided.supporting.snowflake` の `package-info.java` に `@NullMarked` を追加
   - `./gradlew :backend:compileJava` を実行し、NullAway の検出結果を確認
   - 検出された違反を1つずつ修正

3. **ステップ3**：`undecided.supporting.snowflake.internal` の `package-info.java` に `@NullMarked` を追加
   - 同上

4. **ステップ4**：`undecided.supporting.snowflake.config` の `package-info.java` を作成して `@NullMarked` を付与
   - 同上

5. **最終確認**：すべてのパッケージで検出ゼロであることを確認
   - `./gradlew :backend:compileJava` がクリーンに完了すること

### 3.2 テスト検証

- `./gradlew :backend:test` が全て通ること（タイムアウト60秒、medium/large タグ除外）
- `./gradlew :backend:mediumTest` が全て通ること（タイムアウト5分）
- テストコードは NullAway 検査対象外であるため、テスト側の修正は不要

### 3.3 検出ゼロの確認方法

- `./gradlew :backend:compileJava` の出力に NullAway からのエラーメッセージが含まれていないことを確認
- ビルドが成功（exit code 0）すること

## 4. 確認項目リスト

- [ ] `backend/build.gradle.kts` に `net.ltgt.errorprone` プラグインを追加したとき、ビルドが正常に動作する
- [ ] `backend/build.gradle.kts` に NullAway 依存（`errorprone` 構成）を追加したとき、依存解決が成功する
- [ ] `-XepDisableAllChecks` と `-Xep:NullAway:ERROR` を設定したとき、NullAway のみが ERROR として機能し、他の Error Prone チェックは無効化されている
- [ ] `OnlyNullMarked=true` を設定したとき、`@NullMarked` が付与されていないパッケージは検査対象外となる
- [ ] `JSpecifyMode=true` を設定したとき、JSpecify アノテーション（`@NonNull`、`@Nullable`、`@NullMarked`）が正しく解釈される
- [ ] `-XepExcludedPaths:.*/src/test/.*` を設定したとき、テストコードは NullAway の検査対象外となる
- [ ] `undecided.supporting.snowflake` の `package-info.java` に `@NullMarked` を追加したとき、NullAway がそのパッケージを検査対象とする
- [ ] `undecided.supporting.snowflake.internal` の `package-info.java` に `@NullMarked` を追加したとき、NullAway がそのパッケージを検査対象とする
- [ ] `undecided.supporting.snowflake.config` の `package-info.java` に `@NullMarked` を追加したとき、NullAway がそのパッケージを検査対象とする
- [ ] 対象パッケージの nullability 棚卸しを行い、必要な `@Nullable` アノテーションを追加したとき、NullAway の違反が解消される
- [ ] 対象パッケージで NullAway の検出がゼロになったとき、`./gradlew :backend:compileJava` がクリーンに完了する
- [ ] `./gradlew :backend:test` を実行したとき、全てのテストがパスする
- [ ] `./gradlew :backend:mediumTest` を実行したとき、全てのテストがパスする
- [ ] PR #689 マージ後の `SnowflakeIdListConverter` の nullability が適切に宣言されている

## 5. リスクと対応

| リスク | 影響 | 対応 |
|--------|------|------|
| Error Prone javac の Java 25 対応 | コンパイルエラー | 最新バージョンの `com.google.errorprone:javac` を確認・適用 |
| Lombok 生成コードとの競合 | NullAway 違反の誤検出 | `-XepOpt:NullAway:UnannotatedSubPackages` などで除外するか、Lombok の配置アノテーションで対応 |
| JPA エンティティの無引数コンストラクタ | フィールドが null になる | フィールドに `@Nullable` を付与するか、JPA 関連のクラスを除外 |
| Spring DI によるフィールド注入 | 初期化前の null | Spring コンポーネントのフィールド注入はフィールドインジェクションで `@Nullable` を付与 |
| PR #689 のマージ遅延 | 作業開始の遅延 | 計画はマージ後を前提とし、マージ後に実装を開始 |

## 6. 次の段階への道筋

第1段階（`undecided.supporting.snowflake`）が完了した後、以下の順序で `@NullMarked` の適用範囲を広げる：

1. 小さいパッケージから順に適用（例：`undecided.supporting.*` の他のサブパッケージ）
2. 各パッケージごとに棚卸し → 修正 → 検証のサイクルを回す
3. 最終段階では `AnnotatedPackages` 方式への切替を検討（全パッケージを明示的にリストする方式）
