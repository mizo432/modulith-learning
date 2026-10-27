package undecided.generic.{module}.internal.batch.config;

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
import undecided.generic.{module}.internal.batch.dto.{Entity}CsvDto;
import undecided.generic.{module}.internal.batch.processor.{Entity}ItemProcessor;
import undecided.generic.{module}.internal.batch.reader.{Entity}CsvItemReader;
import undecided.generic.{module}.internal.batch.writer.{Entity}ItemWriter;
import undecided.generic.{module}.spi.{Entity};

/** {Entity}マスターデータ自動インポート用 Spring Batch 設定クラス。 */
@Configuration
public class {Entity}ImportBatchConfig {

  public static final String JOB_NAME = "{entity}ImportJob";
  public static final String STEP_NAME = "{entity}ImportStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public {Entity}CsvItemReader {entity}CsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      if (inputFile.startsWith("classpath:")) {
        resource = new ClassPathResource(inputFile.substring("classpath:".length()));
      } else {
        resource = new FileSystemResource(inputFile);
      }
    } else {
      resource = new ClassPathResource("data/{entity}.csv");
    }
    return new {Entity}CsvItemReader(resource);
  }

  @Bean
  public Step {entity}ImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      {Entity}CsvItemReader reader,
      {Entity}ItemProcessor processor,
      {Entity}ItemWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<{Entity}CsvDto, {Entity}>chunk(CHUNK_SIZE)
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
  }

  @Bean
  public Job {entity}ImportJob(JobRepository jobRepository, Step {entity}ImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository).start({entity}ImportStep).build();
  }
}
