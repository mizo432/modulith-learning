package undecided.generic.sampleBatch.internal.batch.config;

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
import undecided.generic.sampleBatch.internal.batch.dto.SampleItemCsvDto;
import undecided.shared.config.BatchJobListener;
import undecided.shared.config.BatchStepListener;
import undecided.generic.sampleBatch.internal.batch.processor.SampleItemProcessor;
import undecided.generic.sampleBatch.internal.batch.reader.SampleItemCsvItemReader;
import undecided.generic.sampleBatch.internal.batch.writer.SampleItemWriter;
import undecided.generic.sampleBatch.spi.SampleItem;

/**
 * サンプルアイテムマスターデータ自動インポート用Spring Batch設定クラス。
 *
 * <p>バッチ基本構成サンプルとして、JobとStepの定義パターンを示します。
 */
@Configuration
public class SampleItemImportBatchConfig {

  public static final String JOB_NAME = "sampleItemImportJob";
  public static final String STEP_NAME = "sampleItemImportStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public SampleItemCsvItemReader sampleItemCsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      if (inputFile.startsWith("classpath:")) {
        resource = new ClassPathResource(inputFile.substring("classpath:".length()));
      } else {
        resource = new FileSystemResource(inputFile);
      }
    } else {
      resource = new ClassPathResource("data/sample-items.csv");
    }
    return new SampleItemCsvItemReader(resource);
  }

  @Bean
  public Step sampleItemImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      SampleItemCsvItemReader reader,
      SampleItemProcessor processor,
      SampleItemWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<SampleItemCsvDto, SampleItem>chunk(CHUNK_SIZE)
        .transactionManager(transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .faultTolerant()
        .skip(Exception.class)
        .skipLimit(10)
        .retry(Exception.class)
        .retryLimit(3)
        .listener(new BatchStepListener())
        .build();
  }

  @Bean
  public Job sampleItemImportJob(JobRepository jobRepository, Step sampleItemImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(sampleItemImportStep)
        .listener(new BatchJobListener())
        .build();
  }
}
