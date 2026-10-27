package undecided.generic.largeRecordBatch.internal.batch.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import undecided.generic.largeRecordBatch.internal.batch.dto.LargeRecordCsvDto;
import undecided.shared.config.BatchJobListener;
import undecided.shared.config.BatchStepListener;
import undecided.generic.largeRecordBatch.internal.batch.processor.LargeRecordProcessor;
import undecided.generic.largeRecordBatch.internal.batch.reader.LargeRecordCsvItemReader;
import undecided.generic.largeRecordBatch.internal.batch.writer.LargeRecordWriter;
import undecided.generic.largeRecordBatch.spi.LargeRecord;

/**
 * 大量レコード処理バッチサンプル用Spring Batch設定クラス。
 *
 * <p>大量データ処理パターンとして、以下の主要パターンをデモンストレーションします：
 * <ul>
 *   <li><b>バッチサイズ</b>: 大量レコード向けの最適化されたチャンクサイズ（200）の設定</li>
 *   <li><b>チャンク処理</b>: 大量データをチャンク単位で処理するパターン</li>
 *   <li><b>エラー処理</b>: バッチ処理中のエラーハンドリングパターン（skip/retry）</li>
 *   <li><b>フォールトトレランス</b>: 失敗からの回復パターン（faultTolerant設定）</li>
 * </ul>
 */
@Configuration
public class LargeRecordImportBatchConfig {

  public static final String JOB_NAME = "largeRecordImportJob";
  public static final String STEP_NAME = "largeRecordImportStep";

  // 大量レコード処理向けのチャンクサイズ（デフォルト50に対して200に設定）
  // チャンクサイズはメモリ使用量とトランザクションのバランスを考慮して設定
  private static final int CHUNK_SIZE = 200;

  // スキップ上限（大量データ処理ではスキップ上限を高く設定）
  private static final int SKIP_LIMIT = 100;

  // リトライ上限
  private static final int RETRY_LIMIT = 3;

  @Bean
  @StepScope
  public LargeRecordCsvItemReader largeRecordCsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      if (inputFile.startsWith("classpath:")) {
        resource = new ClassPathResource(inputFile.substring("classpath:".length()));
      } else {
        resource = new FileSystemResource(inputFile);
      }
    } else {
      resource = new ClassPathResource("data/large-records.csv");
    }
    return new LargeRecordCsvItemReader(resource);
  }

  @Bean
  public Step largeRecordImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      LargeRecordCsvItemReader reader,
      LargeRecordProcessor processor,
      LargeRecordWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<LargeRecordCsvDto, LargeRecord>chunk(CHUNK_SIZE)
        .transactionManager(transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .faultTolerant()
        // エラー処理パターン: 不正なデータをスキップして処理を継続
        .skip(Exception.class)
        .skipLimit(SKIP_LIMIT)
        // フォールトトレランスパターン: 一時的なエラーに対してリトライ
        .retry(Exception.class)
        .retryLimit(RETRY_LIMIT)
        .listener(new BatchStepListener())
        .build();
  }

  @Bean
  public Job largeRecordImportJob(JobRepository jobRepository, Step largeRecordImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(largeRecordImportStep)
        .listener(new BatchJobListener())
        .build();
  }
}
