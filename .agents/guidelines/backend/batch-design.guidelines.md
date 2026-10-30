# バッチ設計ガイド

本ガイドでは、Spring Batch を用いたバッチ処理の設計・実装におけるベストプラクティスを定義します。

## 1. 概要

大量のマスターデータや取引データを CSV ファイルから読み込み、データベースに一括登録・更新するバッチ処理は、以下の点に注意して設計・実装します。

- **メモリ効率**: 全データをメモリに読み込まないストリーミング方式を採用
- **トランザクション管理**: チャンク単位でコミットし、ロールバックの影響範囲を最小化
- **エラー耐性**: 個々のレコードエラーが全体処理を停止しないフォールトトレラント設計
- **パフォーマンス**: チャンクサイズやリトライ設定を適切に調整

## 2. バッチ構成の基本パターン

バッチはモジュールの `internal/batch` 配下に配置し、役割ごとのサブパッケージで分離します。

| パッケージ  | 役割                                      |
|:------------|:------------------------------------------|
| `config`    | Job / Step の定義 (`*ImportBatchConfig`)  |
| `reader`    | 入力の読み込み（CSV は Super CSV を利用） |
| `processor` | 入力 DTO からドメインモデルへの変換・検証 |
| `writer`    | 永続化                                    |
| `dto`       | 入力レコードの DTO                        |
| `operator`  | Job の起動 (`*ImportJobOperator`)         |

### 2.1 処理フロー

```
CSV ファイル → Reader (ストリーミング読み込み) → Processor (変換・検証) → Writer (一括保存) → データベース
```

各コンポーネントは以下の役割を持ちます。

1. **Reader**: CSV ファイルをストリーミング方式で1件ずつ読み込み、DTO に変換
2. **Processor**: DTO を検証し、既存データと照合してエンティティを生成・更新
3. **Writer**: チャンク単位でエンティティを一括保存

## 3. チャンクサイズの設定

チャンクサイズは、1つのトランザクションで処理するレコード数を意味します。適切な設定がパフォーマンスとメモリ使用量のバランスに直結します。

### 3.1 デフォルト値

- デフォルトのチャンクサイズは `50` とします。
- チャンクサイズは `*ImportBatchConfig` で `CHUNK_SIZE` 定数として定義し、一元的に管理します。

```java
private static final int CHUNK_SIZE = 50;
```

### 3.2 調整ガイドライン

| レコード数    | 推奨チャンクサイズ | 備考                     |
|:--------------|:-------------------|:-------------------------|
| 1,000 件未満  | 50                 | デフォルトで十分         |
| 1,000 〜 10万 | 100 〜 200         | パフォーマンスを優先     |
| 10万 〜 100万 | 200 〜 500         | メモリ使用量に注意       |
| 100万 件超    | 500 〜 1,000       | 負荷テストで最適値を確認 |

### 3.3 調整時の考慮点

- **メモリ使用量**: チャンクサイズを大きくすると、1つのトランザクションで保持するオブジェクト数が増加
- **コミット頻度**: チャンクサイズを小さくすると、コミット頻度が増加し、トランザクションオーバーヘッドが増加
- **ロールバック影響**: チャンクサイズを大きくすると、ロールバック時の影響範囲が拡大

## 4. エラー処理とフォールトトレランス

大量レコード処理では、個々のレコードエラーが全体処理を停止しないように設計します。

### 4.1 Step のフォールトトレランス設定

**⚠️ 重要**: `.skip(Exception.class)` と `.retry(Exception.class)` は、`OutOfMemoryError` や `NullPointerException` などの致命的な例外までスキップ・リトライ対象にしてしまいます。スキップ・リトライ対象を**明示的に限定**してください。

```java
return new StepBuilder(STEP_NAME, jobRepository)
    .<CsvDto, Entity>chunk(CHUNK_SIZE)
    .transactionManager(transactionManager)
    .reader(reader)
    .processor(processor)
    .writer(writer)
    .faultTolerant()
    // スキップ対象を限定（検証エラーなどリカバリ不可能な例外のみ）
    .skip(BatchSkipException.class)
    .skip(TransientDataAccessResourceException.class)
    .skipLimit(10)
    // リトライ対象を限定（一時的なネットワークエラー、デッドロックなど）
    .retry(OptimisticLockingFailureException.class)
    .retry(TransientDataAccessResourceException.class)
    .retryLimit(3)
    .build();
```

**スキップ対象例外の選択基準**:

- `BatchSkipException`（独自例外）: 検証エラー、必須項目欠落など、リカバリ不可能なデータエラー
- `TransientDataAccessResourceException`: 一時的なデータベース接続エラー
- **除外**: `OutOfMemoryError`、`StackOverflowError`、`NullPointerException` などの致命的な例外

**リトライ対象例外の選択基準**:

- `OptimisticLockingFailureException`: 楽観ロック競合（一時的な競合）
- `TransientDataAccessResourceException`: 一時的なネットワークエラー
- **除外**: 検証エラー、一意制約違反など、リトライしても成功しない例外

### 4.2 スキップとリトライの設定

| 設定       | 推奨値 | 説明                               |
|:-----------|:-------|:-----------------------------------|
| skipLimit  | 10     | スキップする例外の上限数           |
| retryLimit | 3      | 一時的なエラーに対するリトライ回数 |

**skipLimit の調整ガイドライン**:

- `skipLimit` は硬编码（ハードコード）せず、ジョブパラメータ化することを推奨します。
- CSV行数に対するパーセンテージベースで設定することを検討してください（例: 100万件のCSVでは1,000件までスキップ可能）。
- テストデータでは、すべてのスキップを許容しないことで、データ品質の問題に気づけるようにします。

### 4.3 Processor でのエラー処理

Processor では、不正なデータを検出したらログに記録して `null` を返します（スキップ）。

**⚠️ 重要**: スキップしたレコードはログに記録されるだけでなく、**エラーCSV**や**エラーテーブル**に出力することを推奨します。業務担当者はログファイルを確認できないため、エラーレコードの集約と出力先を明確に定義してください。

```java
@Override
public @Nullable Entity process(@NonNull CsvDto item) {
  // 必須項目の検証
  String requiredField = item.getRequiredField();
  if (requiredField == null || requiredField.isBlank()) {
    log.warn("Skipping record due to missing requiredField: {}", item);
    return null;  // null を返すとスキップされる
  }
  // ... 変換処理
}
```

## 5. メモリ効率の最適化

### 5.1 Reader のストリーミング方式

Reader は `ItemStreamReader` インターフェースを実装し、1件ずつストリーミング方式で読み込みます。全データをメモリに読み込まないよう注意します。

- Super CSV の `CsvBeanReader` を使用し、行単位で読み込む
- `BufferedReader` で UTF-8 文字セットを明示的に指定

### 5.2 Writer の一括保存

Writer は `saveAll()` などの一括保存メソッドを使用し、トランザクション内でまとめて永続化します。

**⚠️ 重要**: Spring Data JPA の `saveAll()` は**バッチINSERT**を保証しません。大量データ（10,000件以上）の場合は、`JpaBatchItemWriter` を使用し、バッチINSERTの設定を明示してください。

```java
@Override
public void write(@NonNull Chunk<? extends Entity> chunk) {
  repository.saveAll(chunk.getItems());
}
```

**バッチINSERTの設定例**:

```java
@Bean
public JpaBatchItemWriter<Entity> writer(EntityManagerFactory emf) {
  JpaBatchItemWriter<Entity> writer = new JpaBatchItemWriter<>();
  writer.setEntityManagerFactory(emf);
  writer.setFlushThreshold(CHUNK_SIZE);
  return writer;
}
```

**Hibernateのバッチ設定**:

```properties
# application.properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

### 5.3 Processor の中間データ管理

Processor では不要な中間データを保持せず、変換済みのオブジェクトのみを返します。

**⚠️ 重要**: Processor で既存データを1件ずつ `findById()` で検索すると、N+1問題が発生します。バッチ読み込み（`findByCodesIn()`）や `ItemStream` のキャッシュ戦略を使用してください。

**「大量データ」の定義**: 本ガイドでは、**10,000件以上**を「大量データ」と定義します。10,000件以上のデータ処理時は、バッチINSERT、N+1問題対策、キャッシュ戦略などの最適化を適用してください。

## 6. パフォーマンス測定とログ出力

### 6.1 ログ出力のベストプラクティス

- Job の開始・終了、各 Step の処理件数、スキップ件数、エラー件数をログに出力
- 大量レコード処理の場合は、一定間隔（例: 1,000件ごと）で処理進捗をログに出力
- 処理時間の計測を行い、パフォーマンスのボトルネックを特定できるようにする

### 6.2 パフォーマンス計測

```java
long startTime = System.currentTimeMillis();
// ... バッチ処理
long elapsed = System.currentTimeMillis() - startTime;
log.info("Batch completed: {} records in {} ms", count, elapsed);
```

## 7. データ検証と整合性

### 7.1 入力データの検証

Processor で入力データの必須項目を検証し、不正なデータはログに記録してスキップします。

- 必須項目の null チェック
- 文字列の空白チェック
- 数値・日付の形式検証

### 7.2 既存データとの整合性

- 既存データとの整合性を確認し、重複登録や不一致を防ぐ
- 外部キー参照先の存在確認は、Processor で事前に実行し、欠落している場合は**スキップ**または**エラー終了**を選択
    - **オプションフィールド**: スキップして処理を継続
    - **必須フィールド**: エラー終了してジョブを停止

### 7.3 日付・数値のパーサー

日付や数値の文字列変換では、例外をキャッチしてデフォルト値を返すようにします。

**⚠️ 重要**: デフォルト値を返すことで、**データの不整合に気づかない**リスクがあります。以下のルールに従ってください：

- **オプションフィールド**: デフォルト値を返して処理を継続
- **必須フィールド**: スキップまたはエラー終了
- 日付パース失敗時は、ログに警告を記録し、原因を特定できるようにする

```java
private LocalDate parseDate(@Nullable String dateStr, @NonNull LocalDate defaultDate) {
  if (dateStr == null || dateStr.trim().isEmpty()) {
    return defaultDate;
  }
  try {
    return LocalDate.parse(dateStr.trim().replace('/', '-'), DateTimeFormatter.ISO_LOCAL_DATE);
  } catch (DateTimeParseException e) {
    log.warn("Failed to parse date '{}', using default '{}'", dateStr, defaultDate);
    return defaultDate;
  }
}
```

## 8. テスト戦略

### 8.1 単体テスト（Small）

- Processor の変換ロジック、境界値、異常系を網羅的にテスト
- 必須項目の null チェック、空文字列、空白文字の処理
- 日付・数値のパーサーの異常系テスト

### 8.2 統合テスト（Medium）

- 実際の CSV ファイルを使用して、Reader → Processor → Writer のフルフローを検証
- Testcontainers で PostgreSQL を使用し、本番と同じ環境でテスト
- スキップ・リトライの動作確認
- **限界値テスト**: スキップLimitを超えた場合のテストケースを追加
- **リトライ後の最終的な失敗ケース**: リトライLimitを超えた場合の動作を確認

### 8.3 負荷テスト（Large）

- 大量レコードでのチャンクサイズやメモリ使用量の影響を確認
- 処理時間の計測とボトルネックの特定
- **CSV生成ツール**: テスト用の大量CSVを生成するスクリプトを用意
- **メモリ使用量の計測**: JVMヒープ使用量を監視し、メモリリークがないことを確認
- **評価基準**: 処理時間、メモリ使用量、CPU使用量を計測し、閾値を設定

## 9. 冪等性（Idempotency）の設計

**冪等性**とは、同じ操作を何度実行しても同じ結果になる性質です。バッチ処理では、同じCSVを2回実行した場合に**重複登録**を防ぐ仕組みが必須です。

### 9.1 ジョブ実行の制御

`JobParametersBuilder` を用いた実行制御により、同じジョブの重複実行を防ぎます。

```java
public JobExecution run(String inputFile) throws Exception {
  JobParametersBuilder builder =
      new JobParametersBuilder()
          .addLong("timestamp", System.currentTimeMillis())
          .addString("inputFile", inputFile);
  JobParameters parameters = builder.toJobParameters();
  return jobOperator.start(job, parameters);
}
```

**⚠️ 重要**: `timestamp` パラメータを追加することで、同じジョブの重複実行を防ぎます。ただし、再実行が必要な場合は、`timestamp` を除外するか、`JobInstanceAlreadyStartedException` をハンドリングしてください。

### 9.2 UPSERTロジックの設計

Processor で既存データを検索し、存在する場合は更新、不存在の場合は新規作成するUPSERTロジックを実装します。

```java
@Override
public @Nullable Entity process(@NonNull CsvDto item) {
  Entity existing = repository.findByCode(item.getCode()).orElse(null);
  Entity entity = existing != null ? existing : new Entity();
  // フィールドの設定
  return entity;
}
```

### 9.3 重複登録の防止

- **主キー制約**: データベースの主キー制約により、重複登録を物理的に防止
- **一意制約**: 一意キー（例: コード）に一意制約を設定し、重複を防止
- **UPSERTロジック**: Processor で既存データを検索し、更新または新規作成を判断

## 10. スケジューリングと並列実行防止

### 10.1 スケジューリング戦略

| スケジューラ | 使用シーン | 備考 |
|:-------------|:-----------|:-----|
| `@Scheduled` | 単一サーバーでの定期実行 | シンプルだが、分散環境では不十分 |
| `Spring Batch JobLauncher` | 外部トリガーによる実行 | REST APIやメッセージキューから起動 |
| Quartz | 分散環境でのスケジューリング | クラスタリングサポート |
| XXL-JOB | 大規模分散環境 | 監視・アラート機能付き |

### 10.2 並列実行防止

分散環境では、同じジョブが複数インスタンスで同時に実行されるリスクがあります。分散ロックを使用して、並列実行を防止してください。

```java
@Scheduled(cron = "${batch.prefecture-import.cron:0 0 3 * * ?}")
public void scheduledRun() {
  // 分散ロックの取得（例: Redis、データベース）
  if (lock.tryLock("prefecture-import-job")) {
    try {
      log.info("Triggering scheduled import job");
      run();
    } catch (Exception e) {
      log.error("Failed to run scheduled import job", e);
    } finally {
      lock.unlock("prefecture-import-job");
    }
  } else {
    log.warn("Skipping scheduled job: another instance is running");
  }
}
```

### 10.3 通知処理

ジョブ実行結果の通知は、以下の方法で実装します。

| 通知方法 | 使用シーン | 備考 |
|:---------|:-----------|:-----|
| メール通知 | 失敗時の通知 | 簡易的なアラート |
| Slack Webhook | チームへの通知 | リアルタイム通知 |
| Webhook | 外部システム連携 | カスタム処理 |
| 監視ツール連携 | 本番環境 | Prometheus、Grafanaなど |

**通知のタイミング**:

- **成功時**: 処理件数、処理時間などを通知
- **失敗時**: エラー内容、失敗したレコード数を通知
- **警告時**: スキップ件数が閾値を超えた場合を通知

## 11. セキュリティ対策

### 11.1 CSVファイルのパス注入対策

CSVファイルのパスが外部入力から来る場合、**パストラバーサル攻撃**のリスクがあります。以下の対策を講じてください。

- **ファイルパスの検証**: 許可されたディレクトリ内のみを参照可能にする
- **許可リスト**: 許可されたファイルパスのリストを維持し、それ以外を拒否
- **パスの正規化**: `File.getCanonicalPath()` を使用し、`../` などの相対パスを正規化

```java
public void validateFilePath(String inputPath) throws IOException {
  File file = new File(inputPath).getCanonicalFile();
  if (!file.getPath().startsWith(ALLOWED_DIRECTORY)) {
    throw new SecurityException("Invalid file path: " + inputPath);
  }
}
```

### 11.2 SQLインジェクション対策

生のSQL文を直接使用する場合、テーブル名が動的に変数から来る場合、SQLインジェクションのリスクがあります。

- **テーブル名の許可リスト**: 許可されたテーブル名のリストを維持し、それ以外を拒否
- **パラメータ化クエリ**: 可能な限りパラメータ化クエリを使用

```java
private static final Set<String> ALLOWED_TABLES = Set.of("bank", "branch", "prefecture");

public void truncateTable(String tableName) {
  if (!ALLOWED_TABLES.contains(tableName)) {
    throw new SecurityException("Invalid table name: " + tableName);
  }
  // TRUNCATE処理
}
```

## 12. 参考実装

本プロジェクトの以下のモジュールが、大量レコード処理の参考実装となります。

- `undecided.generic.addressReg.internal.batch`: 都道府県・市区町村・町字マスターインポート
- `undecided.generic.calendarReg.internal.batch`: 祝日マスターインポート
- `undecided.generic.bankReg.internal.batch`: 銀行・支店マスターインポート

## 13. テンプレート

バッチ処理のテンプレートは、`.agents/skills/modulith-dev/templates/` に配置されています。

| テンプレート                      | 用途                               |
|:----------------------------------|:-----------------------------------|
| `BatchConfigTemplate.java`        | Job / Step の設定                  |
| `BatchItemProcessorTemplate.java` | DTO からエンティティへの変換・検証 |
| `BatchItemWriterTemplate.java`    | エンティティの一括永続化           |
| `BatchCsvItemReaderTemplate.java` | CSV ストリーミング読み込み         |
| `BatchCsvDtoTemplate.java`        | CSV 入力レコードの DTO             |

## 14. タスクレット利用ガイド

タスクレット（Tasklet）は、Spring Batch でチャンク指向の処理（Reader → Processor → Writer）ではなく、
**単一のタスクを実行する際に使用**
するステップタイプです。テーブルのトランケート、再インデックス化、メタデータの記録、外部リソースのクリーンアップなど、一度きりの処理に適しています。

### 14.1 タスクレットの基本概念

タスクレットは `Tasklet` インターフェースを実装し、`execute` メソッド内で処理を記述します。

```java
public interface Tasklet {
  RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception;
}
```

- **RepeatStatus.FINISHED**: タスクが正常に完了したことを示す
- **RepeatStatus.CONTINUABLE**: タスクを再実行する必要があることを示す（ループ処理などで使用）
- **StepContribution**: ステップの実行結果（読み込み数、書き込み数など）を記録する

### 14.2 タスクレットの使用シーン

タスクレットは以下のシーンで適しています。

| シーン                       | 説明                                          |
|:-----------------------------|:----------------------------------------------|
| テーブルのトランケート       | バッチ処理前にデータを全削除して初期化        |
| 再インデックス化             | 大量データ投入後にインデックスを再構築        |
| メタデータの記録             | インポート完了時刻、件数などを記録            |
| 外部リソースのクリーンアップ | 一時ファイルの削除、ダウンロード済みZIPの削除 |
| データの検証                 | インポート前のデータ整合性チェック            |
| 通知処理                     | 処理完了後のメール通知、Webhook 送信          |

### 14.3 テーブルのトランケート処理

大量レコードのインポート前に、既存のデータを全削除してテーブルを初期化する際に使用します。

#### 14.3.1 JPA Repository を利用したトランケート

```java
@Bean
@StepScope
public Tasklet truncateTasklet(BankRepository bankRepository) {
  return (contribution, chunkContext) -> {
    long count = bankRepository.count();
    bankRepository.deleteAll();
    contribution.incrementWriteCount((int) count);
    log.info("Truncated table: {} records deleted", count);
    return RepeatStatus.FINISHED;
  };
}
```

**注意点**:

- `deleteAll()` はエンティティを1件ずつ削除するため、**1,000件以上**でパフォーマンスが顕著に劣化します。1,000件以上はJDBCの `TRUNCATE` 文を使用してください。
- 外部キー制約がある場合、子テーブルから削除する必要があります。

#### 14.3.2 JDBC を利用した TRUNCATE 文

大量データの場合は、JDBC を直接使用して `TRUNCATE` 文を実行するのが効率的です。

```java
@Bean
@StepScope
public Tasklet truncateTasklet(DataSource dataSource) {
  return (contribution, chunkContext) -> {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      // 外部キー制約を一時的に無効化（PostgreSQL 例）
      stmt.execute("SET CONSTRAINTS ALL DEFERRED");
      
      // テーブルのトランケート
      stmt.execute("TRUNCATE TABLE bank CASCADE");
      
      log.info("Truncated table: bank");
      return RepeatStatus.FINISHED;
    }
  };
}
```

**データベース別の TRUNCATE 文**:

| データベース | TRUNCATE 文                         |
|:-------------|:------------------------------------|
| PostgreSQL   | `TRUNCATE TABLE table_name CASCADE` |
| MySQL        | `TRUNCATE TABLE table_name`         |
| H2           | `TRUNCATE TABLE table_name`         |
| Oracle       | `TRUNCATE TABLE table_name`         |

**外部キー制約の処理**:

外部キー制約がある場合、以下のいずれかの方法で処理します。

1. **CASCADE オプション**: 関連テーブルも一緒にトランケートする
2. **制約の一時的無効化**: トランケート後に再有効化する
3. **削除順序の制御**: 子テーブルから順に削除する

```java
@Bean
@StepScope
public Tasklet truncateWithForeignKeyTasklet(DataSource dataSource) {
  return (contribution, chunkContext) -> {
    try (Connection conn = dataSource.getConnection()) {
      conn.setAutoCommit(false);
      try (Statement stmt = conn.createStatement()) {
        // PostgreSQL: 外部キー制約を一時的に無効化
        stmt.execute("ALTER TABLE bank DISABLE TRIGGER ALL");
        stmt.execute("TRUNCATE TABLE bank");
        stmt.execute("ALTER TABLE bank ENABLE TRIGGER ALL");
        conn.commit();
        log.info("Truncated table: bank with foreign key constraints handled");
      } catch (SQLException e) {
        conn.rollback();
        throw e;
      }
    }
    return RepeatStatus.FINISHED;
  };
}
```

### 14.4 再インデックス化処理

大量のデータを投入した後、インデックスを再構築してパフォーマンスを最適化します。

#### 14.4.1 PostgreSQL での再インデックス化

```java
@Bean
@StepScope
public Tasklet reindexTasklet(DataSource dataSource) {
  return (contribution, chunkContext) -> {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      // テーブルの再インデックス化
      stmt.execute("REINDEX TABLE bank");
      
      // 統計情報の更新（クエリプランナーの最適化のため）
      stmt.execute("ANALYZE bank");
      
      log.info("Reindexed table: bank");
      return RepeatStatus.FINISHED;
    }
  };
}
```

#### 14.4.2 特定のインデックスの再構築

```java
@Bean
@StepScope
public Tasklet reindexSpecificIndexTasklet(DataSource dataSource) {
  return (contribution, chunkContext) -> {
    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
      // 特定のインデックスを再構築
      stmt.execute("REINDEX INDEX idx_bank_code");
      stmt.execute("REINDEX INDEX idx_bank_name");
      
      log.info("Reindexed specific indexes on bank table");
      return RepeatStatus.FINISHED;
    }
  };
}
```

#### 14.4.3 再インデックス化のタイミング

| タイミング   | 推奨ケース                                   |
|:-------------|:---------------------------------------------|
| インポート前 | テーブルの初期化後、インデックスを再構築     |
| インポート後 | 大量データ投入後、インデックスの断片化を解消 |
| 定期バッチ   | 夜間バッチなどで定期的に実行                 |

### 14.5 タスクレットを利用した Job の構成例

トランケート → インポート → 再インデックス化の流れを構成する例です。

```java
@Bean
public Job bankImportJob(
    JobRepository jobRepository,
    Step truncateStep,
    Step importStep,
    Step reindexStep,
    Step recordImportStep) {
  return new JobBuilder(JOB_NAME, jobRepository)
      .start(truncateStep)          // 1. テーブルのトランケート
      .next(importStep)             // 2. CSV インポート
      .next(reindexStep)            // 3. 再インデックス化
      .next(recordImportStep)       // 4. インポート記録の保存
      .build();
}

@Bean
public Step truncateStep(
    JobRepository jobRepository,
    PlatformTransactionManager transactionManager,
    Tasklet truncateTasklet) {
  return new StepBuilder("truncateStep", jobRepository)
      .tasklet(truncateTasklet, transactionManager)
      .build();
}

@Bean
public Step reindexStep(
    JobRepository jobRepository,
    PlatformTransactionManager transactionManager,
    Tasklet reindexTasklet) {
  return new StepBuilder("reindexStep", jobRepository)
      .tasklet(reindexTasklet, transactionManager)
      .build();
}
```

### 14.6 参考実装

本プロジェクトの以下の実装が参考となります。

- `undecided.generic.bankReg.internal.batch.config.BankImportBatchConfig`
    - `cleanupTasklet`: 古いデータセットの金融機関レコードを削除
    - `recordImportTasklet`: インポート完了イベントを記録

### 14.7 ベストプラクティスと注意事項

**トランケート処理の注意事項**

- **外部キー制約**: 子テーブルから順に削除するか、CASCADE オプションを使用
- **トランザクション管理**: トランケートはトランザクション内で実行し、失敗時にロールバックできるようにする
- **ログ出力**: 削除した件数をログに記録し、監査証跡を残す
- **テスト環境**: テストでは H2 データベースを使用する場合、`TRUNCATE` 文の構文が異なる可能性がある
- **パフォーマンス**: `deleteAll()` は1,000件以上でパフォーマンスが顕著に劣化します。1,000件以上はJDBCの `TRUNCATE` 文を使用してください。

**再インデックス化の注意事項**

- **ロックの取得**: `REINDEX` 文はテーブルロックを取得するため、本番環境ではメンテナンスウィンドウで実行
- **処理時間**: 大量データのテーブルでは再インデックス化に時間がかかる可能性がある
- **統計情報の更新**: 再インデックス化後は `ANALYZE` で統計情報を更新する

**タスクレットの一般的なベストプラクティス**

- **冪等性（Idempotency）**: 同じタスクレットを複数回実行しても同じ結果になるように設計
- **エラー処理**: 例外を適切にキャッチし、ログに記録してロールバックできるようにする
- **ジョブパラメータの利用**: `@Value("#{jobParameters['...']}")` で動的なパラメータを受け取る
- **StepContribution の活用**: 処理件数を `contribution.incrementWriteCount()` で記録する
