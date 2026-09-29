package undecided.generic.addressReg.internal.batch.config;

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
import undecided.generic.addressReg.internal.batch.dto.PrefectureCsvDto;
import undecided.generic.addressReg.internal.batch.processor.PrefectureItemProcessor;
import undecided.generic.addressReg.internal.batch.reader.PrefectureCsvItemReader;
import undecided.generic.addressReg.internal.batch.writer.PrefectureItemWriter;
import undecided.generic.addressReg.spi.Prefecture;

/** 都道府県マスターデータ自動インポート用 Spring Batch 設定クラス。 */
@Configuration
public class PrefectureImportBatchConfig {

  public static final String JOB_NAME = "prefectureImportJob";
  public static final String STEP_NAME = "prefectureImportStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public PrefectureCsvItemReader prefectureCsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      if (inputFile.startsWith("classpath:")) {
        resource = new ClassPathResource(inputFile.substring("classpath:".length()));
      } else {
        resource = new FileSystemResource(inputFile);
      }
    } else {
      resource = new ClassPathResource("data/prefectures.csv");
    }
    return new PrefectureCsvItemReader(resource);
  }

  @Bean
  public Step prefectureImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      PrefectureCsvItemReader reader,
      PrefectureItemProcessor processor,
      PrefectureItemWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<PrefectureCsvDto, Prefecture>chunk(CHUNK_SIZE, transactionManager)
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
  public Job prefectureImportJob(JobRepository jobRepository, Step prefectureImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository).start(prefectureImportStep).build();
  }
}
