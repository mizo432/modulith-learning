---
name: batch-design
description: Spring Batchをを使用したバッチ処理設計、CSVインポート、ジョブ・ステップ構成、エラー処理、スケジューリング、テスト戦略の設計ルールを提供。
---

### Batch Design Guide (`batch-design`)

このスキルは、 **modulith-learning** リポジトリにおいて、Spring
Batchを使用したバッチ処理の設計・実装を行う際に適用する設計ガイドラインです。
CSVインポート、マスターデータ取り込み、定期実行バッチ、Taskletベースの処理など、プロジェクトのバッチ処理パターンを網羅します。

---

### 1. いつこのスキルを使用するか (When To Use)

- 新規バッチジョブの設計・実装を行うとき
- CSVファイルからのデータインポート処理を開発するとき
- 外部APIからのデータ取得とインポートを連携するバッチを設計するとき
- 定期実行バッチ（スケジューリング）を設定するとき
- 大量データのバッチ処理のパフォーマンスを最適化するとき
- バッチ処理のエラーハンドリング・フォールトトレランスを設計するとき
- バッチ処理のテスト（単体・統合）を作成するとき

---

### 2. バッチアーキテクチャ設計原則 (Batch Architecture Principles)

#### 2.1 モジュール内配置規則

- バッチ処理はモジュールの `internal/batch` 配下に配置します。
- 役割ごとのサブパッケージで分離し、責務を明確にします。

| パッケージ  | 役割                                      |
|:------------|:------------------------------------------|
| `config`    | Job / Step の定義 (`*ImportBatchConfig`)  |
| `reader`    | 入力の読み込み（CSV は Super CSV を利用） |
| `processor` | 入力 DTO からドメインモデルへの変換・検証 |
| `writer`    | 永続化                                    |
| `dto`       | 入力レコードの DTO                        |
| `operator`  | Job の起動 (`*ImportJobOperator`)         |
| `client`    | 外部API連携（必要に応じて）               |

#### 2.2 バッチ処理の2つのパターン

プロジェクトでは、以下の2つのバッチパターンを採用しています。

**パターン1: シンプルCSVインポート**

- 単一のStepでCSVを読み込み、変換、永続化するパターン。
- 例: `addressReg` モジュール（都道府県、市区町村、町名小字）
- 構成: 1つのChunk-oriented Step

**パターン2: マルチStepインポート**

- 複数のStepを連鎖させ、ダウンロード、インポート、クリーンアップ、記録を行うパターン。
- 例: `bankReg` モジュール（金融機関、支店）
- 構成: downloadStep → importStep → cleanupStep → recordImportStep

#### 2.3 Spring Batch のコアコンセプト

- **Job**: バッチ処理の全体を定義する単位。
- **Step**: Jobを構成する個別の処理ステップ。
- **Chunk**: Reader → Processor → Writer の処理単位。チャンクサイズごとにコミット。
- **Tasklet**: 単一のタスクを実行するStep（クリーンアップ、記録など）。
- **JobRepository**: ジョブ実行メタデータの永続化。
- **JobParameters**: ジョブ実行時のパラメータ渡しの仕組み。

---

### 3. バッチ構成設計 (Batch Configuration Design)

#### 3.1 Job / Step の定義

- **設定クラス**: `@Configuration` アノテーションを付与し、`*ImportBatchConfig` の命名規則に従う。
- **Job名・Step名**: 定数として定義し、一貫性を保つ。
- **チャンクサイズ**: デフォルトは `50` とし、メモリ使用量とトランザクションのバランスを取る。

```java
public static final String JOB_NAME = "prefectureImportJob";
public static final String STEP_NAME = "prefectureImportStep";
private static final int CHUNK_SIZE = 50;
```

#### 3.2 Chunk-oriented Step の設計

- Reader → Processor → Writer のパイプラインを定義。
- `.faultTolerant()` を必ず設定し、エラー耐性を確保。
- `.skip(Exception.class)` と `.skipLimit(N)` でスキップする例外と上限を定義。
- `.retry(Exception.class)` と `.retryLimit(N)` でリトライを設定。

```java
return new StepBuilder(STEP_NAME, jobRepository)
    .<CsvDto, Entity>chunk(CHUNK_SIZE)
    .transactionManager(transactionManager)
    .reader(reader)
    .processor(processor)
    .writer(writer)
    .faultTolerant()
    .skip(Exception.class)
    .skipLimit(10)
    .retry(Exception.class)
    .retryLimit(3)
    .build();
```

#### 3.3 Tasklet Step の設計

- 単一のタスク（クリーンアップ、記録、ダウンロードなど）を実行。
- `@StepScope` を使用してJobParametersにアクセス。
- `RepeatStatus.FINISHED` を返してタスクの完了を示す。

```java
@Bean
@StepScope
public Tasklet cleanupTasklet(
    Repository repository,
    @Value("#{jobParameters['datasetId']}") String datasetId) {
  return (contribution, chunkContext) -> {
    // タスクの実装
    return RepeatStatus.FINISHED;
  };
}
```

#### 3.4 複数Stepの連鎖

- JobBuilderの `.start()` → `.next()` でStepを連鎖させる。
- 各Stepの責務を明確に分離する。

```java
return new JobBuilder(JOB_NAME, jobRepository)
    .start(downloadStep)
    .next(importStep)
    .next(cleanupStep)
    .next(recordImportStep)
    .build();
```

---

### 4. Reader 設計 (Reader Design)

#### 4.1 CSV Reader の基本パターン

- **Super CSV** を使用してストリーミング方式でCSVを読み込む。
- `ItemStreamReader<T>` インターフェースを実装。
- `ExecutionContext` をサポートしてチェックポイント機能を有効化。

#### 4.2 ヘッダー解決戦略

- 日本語ヘッダーと英語ヘッダーの両方をサポート。
- ヘッダーが不存在の場合はデフォルトのname mappingを使用。
- 大文字・小文字を区別せず、アンダースコア区切りも対応。

```java
private String[] resolveNameMapping(String[] headers) {
  String[] mapping = new String[headers.length];
  for (int i = 0; i < headers.length; i++) {
    String h = headers[i] != null ? headers[i].trim() : "";
    if (HEADER_JP.equals(h) || "fieldName".equalsIgnoreCase(h)) {
      mapping[i] = "fieldName";
    }
    // ...
  }
  return mapping;
}
```

#### 4.3 リソースの柔軟な対応

- `classpath:` プレフィックスでクラスパスリソースを指定可能。
- ファイルシステムパスも直接指定可能。
- デフォルトのリソースを定義し、パラメータが不存在の場合はデフォルトを使用。

#### 4.4 ZIPファイル内のCSV読み込み

- 外部APIからダウンロードしたZIPファイル内のCSVを読み込む場合、`ZipFile` を使用。
- ReaderにZIPファイルパスを設定し、内部でCSVファイルを抽出して読み込む。

---

### 5. Processor 設計 (Processor Design)

#### 5.1 変換ロジックの基本パターン

- `ItemProcessor<InputDto, OutputEntity>` インターフェースを実装。
- 入力DTOを検証し、ドメインエンティティに変換。
- 既存データの照合を行い、新規作成または更新を判断。

#### 5.2 入力検証ルール

- **nullチェック**: `ObjectPrecondition.checkNotNull()` を使用。
- **必須フィールド検証**: 必須フィールドがnullまたは空の場合、ログに記録してnullを返す（スキップ）。
- **データ形式検証**: 日付パース、数値変換などで例外が発生した場合、デフォルト値を使用またはnullを返す。

#### 5.3 既存データとの整合性

- リポジトリから既存データを検索し、存在する場合に更新、不存在の場合に新規作成。
- 主キーまたは一意キーで既存データを識別。

```java
Entity existing = repository.findByKey(key);
Entity entity = existing != null ? existing : new Entity();
// フィールドの設定
return entity;
```

#### 5.4 日付パースのフォールトトレランス

- 複数の日付形式（`yyyy-MM-dd`、`yyyy/MM/dd`、`yyyyMMdd`）に対応。
- パース失敗時はデフォルト値を使用し、ログに警告を記録。
- 特殊な日付（`9999-12-31` など）を適切に処理。

#### 5.5 ログ出力

- スキップしたレコードの詳細を `log.warn()` で記録。
- パースエラーの詳細を `log.warn()` で記録。
- 機密情報はログに含まれないように注意。

---

### 6. Writer 設計 (Writer Design)

#### 6.1 一括永続化パターン

- `ItemWriter<T>` インターフェースを実装。
- `Chunk` からアイテム一覧を取得し、リポジトリの `saveAll()` で一括保存。
- チャンクサイズのレコードを1つのトランザクションでコミット。

```java
@Override
public void write(@NonNull Chunk<? extends Entity> chunk) {
  checkNotNull(chunk, () -> new IllegalArgumentException("chunk must not be null"));
  log.info("Persisting {} records", chunk.size());
  repository.saveAll(chunk.getItems());
}
```

#### 6.2 メモリ効率の最適化

- Writerはチャンク単位の処理のみを行い、不要な中間データを保持しない。
- 一括保存メソッドを使用して、N+1問題を防ぐ。

---

### 7. CSV DTO 設計 (CSV DTO Design)

#### 7.1 DTOの基本ルール

- `Serializable` を実装し、シリアライズ可能にする。
- Lombokアノテーション（`@Getter`, `@Setter`, `@ToString`, `@EqualsAndHashCode`,
  `@NoArgsConstructor`, `@AllArgsConstructor`）を使用。
- すべてのフィールドを `@Nullable` として宣言（CSVデータは不完全な場合がある）。

#### 7.2 フィールド命名規則

- CSVのヘッダー名と対応するcamelCaseのフィールド名を使用。
- 日本語コメントでフィールドの意味を明確に記述。

#### 7.3 ビジネスロジックの含め方

- DTO自体はデータ保持に特化し、変換ロジックはProcessorに委譲。
- 単純な派生値の取得（例: コードの一部を抽出）のみをDTOに含める。

---

### 8. Job Operator 設計 (Job Operator Design)

#### 8.1 ジョブ起動の基本パターン

- `JobOperator` を使用してジョブを起動。
- `JobParametersBuilder` でパラメータを構築。
- `timestamp` パラメータを追加して、同じジョブの重複実行を防ぐ。

```java
public JobExecution run(String inputFile) throws Exception {
  JobParametersBuilder builder =
      new JobParametersBuilder().addLong("timestamp", System.currentTimeMillis());
  if (inputFile != null && !inputFile.isBlank()) {
    builder.addString("inputFile", inputFile);
  }
  JobParameters parameters = builder.toJobParameters();
  return jobOperator.start(job, parameters);
}
```

#### 8.2 スケジューリング

- `@Scheduled` アノテーションを使用して定期実行を設定。
- cron式はプロパティファイルで外部化し、環境ごとに調整可能にする。

```java
@Scheduled(cron = "${batch.prefecture-import.cron:0 0 3 * * ?}")
public void scheduledRun() {
  try {
    log.info("Triggering scheduled import job");
    run();
  } catch (Exception e) {
    log.error("Failed to run scheduled import job", e);
  }
}
```

#### 8.3 エラーハンドリング

- スケジュール実行時の例外をキャッチし、ログに記録。
- ジョブの失敗がアプリケーションのクラッシュにつながらないようにする。

---

### 9. 大量レコード処理のベストプラクティス (Large Scale Processing)

#### 9.1 チャンクサイズの調整

- デフォルトのチャンクサイズは `50`。
- 大量レコードの場合は、チャンクサイズを `100` 〜 `500` の範囲で調整。
- チャンクサイズは `*ImportBatchConfig` で `CHUNK_SIZE` 定数として定義。

#### 9.2 メモリ効率の最適化

- Readerはストリーミング方式で実装し、全データをメモリに読み込まない。
- Writerは `saveAll()` などの一括保存メソッドを使用。
- Processorでは不要な中間データを保持しない。

#### 9.3 トランザクション管理

- チャンクごとにトランザクションをコミット。
- 1つのレコードのエラーが全体の処理に影響しないように `.faultTolerant()` を設定。

#### 9.4 パフォーマンス監視

- 各ステップの処理時間をログに記録。
- 処理件数、スキップ件数、エラー件数をログに記録。

---

### 10. エラー処理とフォールトトレランス (Error Handling & Fault Tolerance)

#### 10.1 スキップ戦略

- `.skip(Exception.class)` でスキップする例外の種類を定義。
- `.skipLimit(N)` でスキップの上限を設定（デフォルト: 10）。
- 不正なデータはログに記録してスキップし、処理を継続。

#### 10.2 リトライ戦略

- `.retry(Exception.class)` でリトライする例外の種類を定義。
- `.retryLimit(N)` でリトライの上限を設定（デフォルト: 3）。
- 一時的なエラー（ネットワーク、DB接続）に対してリトライを有効活用。

#### 10.3 エラーログの設計

- スキップしたレコードの詳細を `log.warn()` で記録。
- 例外の原因を `log.error()` で記録。
- 機密情報はログに含まれないように注意。

---

### 11. テスト戦略 (Testing Strategy)

#### 11.1 単体テスト（Small）

- **Processorテスト**: 変換ロジック、境界値、異常系を網羅的にテスト。
    - null入力の処理
    - 必須フィールドが空の場合の処理
    - 既存データの更新 vs 新規作成
    - 日付パースの各種形式
- **DTOテスト**: フィールドのgetter/setter、派生値の計算をテスト。
- **Writerテスト**: モックリポジトリを使用して、saveAllの呼び出しを検証。

#### 11.2 統合テスト（Medium）

- 実際のCSVファイルを使用して、Reader → Processor → Writer のフルフローを検証。
- Springコンテキストを起動し、実際のリポジトリと連携してテスト。
- ジョブ実行の結果（JobExecution）を検証。

#### 11.3 テスト命名規則

- テストメソッド名は `should` で開始。
- `@DisplayName` で日本語のテスト記述を付与。
- `@Nested` でメソッドごとにテストを構造化。

#### 11.4 テストデータ

- テスト用のCSVファイルを `src/test/resources/data/` に配置。
- 境界値を含むテストデータを用意（空フィールド、特殊文字、長い文字列など）。

---

### 12. 外部API連携バッチ設計 (External API Integration)

#### 12.1 APIクライアントの設計

- 外部APIとの通信は `client` パッケージに分離。
- `@ConfigurationProperties` を使用してAPI設定を外部化。
- リトライ・タイムアウト・サーキットブレーカーを適切に設定。

#### 12.2 ダウンロードStepの設計

- TaskletベースのStepでダウンロード処理を実装。
- ダウンロードしたファイルのパスをJobParametersに格納。
- チェックサム検証でファイルの完全性を確認。

#### 12.3 データセット管理

- インポートしたデータセットのメタデータを記録。
- 古いデータセットのクリーンアップを自動実行。
- インポート履歴を追跡可能にする。

---

### 13. 設計チェックリスト (Design Checklist)

バッチ処理の設計時・実装時に以下の項目を確認してください。

- [ ] バッチ処理が `internal/batch` 配下に適切に配置されている
- [ ] パッケージ構成（config/reader/processor/writer/dto/operator）が守られている
- [ ] Job名・Step名が定数として定義されている
- [ ] チャンクサイズが適切に設定されている（デフォルト50）
- [ ] `.faultTolerant()` が設定されている
- [ ] `.skip()` と `.skipLimit()` が設定されている
- [ ] `.retry()` と `.retryLimit()` が設定されている
- [ ] Readerがストリーミング方式で実装されている
- [ ] CSVヘッダーの日本語/英語両方に対応している
- [ ] Processorで入力検証が適切に行われている
- [ ] Processorで既存データとの整合性が確認されている
- [ ] Writerが一括保存（saveAll）を使用している
- [ ] DTOがSerializableを実装している
- [ ] DTOのフィールドが @Nullable として宣言されている
- [ ] OperatorでJobParametersが適切に構築されている
- [ ] スケジューリングがプロパティで外部化されている
- [ ] エラーログに機密情報が含まれていない
- [ ] 単体テストがProcessorの境界値・異常系を網羅している
- [ ] 統合テストがフルフローを検証している
- [ ] テストデータが境界値を含む
