package undecided.generic.documentMgmt.internal.batch.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import undecided.config.batch.BatchJobListener;
import undecided.config.batch.BatchStepListener;
import undecided.generic.documentMgmt.internal.DocumentRepository;
import undecided.generic.documentMgmt.internal.batch.processor.DocumentPhysicalDeleteProcessor;
import undecided.generic.documentMgmt.internal.batch.reader.DocumentPhysicalDeleteReader;
import undecided.generic.documentMgmt.internal.batch.writer.DocumentPhysicalDeleteWriter;
import undecided.generic.documentMgmt.spi.Document;

/**
 * 論理削除から1年経過した帳票を物理削除する Spring Batch 設定クラス。
 *
 * <p>単一のチャンクステップで、対象帳票の読み込み → ストレージからの物理削除 → DB レコード削除を行う。
 * ストレージ削除に失敗した場合はチャンクがロールバックされ、DB レコードは削除されない。
 */
@Configuration
public class DocumentPhysicalDeleteBatchConfig {

  public static final String JOB_NAME = "documentPhysicalDeleteJob";
  public static final String STEP_NAME = "documentPhysicalDeleteStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public DocumentPhysicalDeleteReader documentPhysicalDeleteReader(
      DocumentRepository documentRepository) {
    return new DocumentPhysicalDeleteReader(documentRepository);
  }

  @Bean
  public Step documentPhysicalDeleteStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      DocumentPhysicalDeleteReader reader,
      DocumentPhysicalDeleteProcessor processor,
      DocumentPhysicalDeleteWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<Document, Document>chunk(CHUNK_SIZE)
        .transactionManager(transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .listener(new BatchStepListener())
        .build();
  }

  @Bean
  public Job documentPhysicalDeleteJob(JobRepository jobRepository, Step documentPhysicalDeleteStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(documentPhysicalDeleteStep)
        .listener(new BatchJobListener())
        .build();
  }
}
