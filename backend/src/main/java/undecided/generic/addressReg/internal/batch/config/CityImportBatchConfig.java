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
import undecided.generic.addressReg.internal.batch.dto.CityCsvDto;
import undecided.generic.addressReg.internal.batch.processor.CityItemProcessor;
import undecided.generic.addressReg.internal.batch.reader.CityCsvItemReader;
import undecided.generic.addressReg.internal.batch.writer.CityItemWriter;
import undecided.generic.addressReg.spi.City;

/** 市区町村マスターデータ自動インポート用 Spring Batch 設定クラス。 */
@Configuration
public class CityImportBatchConfig {

  public static final String JOB_NAME = "cityImportJob";
  public static final String STEP_NAME = "cityImportStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public CityCsvItemReader cityCsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      if (inputFile.startsWith("classpath:")) {
        resource = new ClassPathResource(inputFile.substring("classpath:".length()));
      } else {
        resource = new FileSystemResource(inputFile);
      }
    } else {
      resource = new ClassPathResource("data/cities.csv");
    }
    return new CityCsvItemReader(resource);
  }

  @Bean
  public Step cityImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      CityCsvItemReader reader,
      CityItemProcessor processor,
      CityItemWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<CityCsvDto, City>chunk(CHUNK_SIZE)
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
  public Job cityImportJob(JobRepository jobRepository, Step cityImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository).start(cityImportStep).build();
  }
}
