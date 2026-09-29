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
import undecided.generic.addressReg.internal.batch.dto.ChouAzaCsvDto;
import undecided.generic.addressReg.internal.batch.processor.ChouAzaItemProcessor;
import undecided.generic.addressReg.internal.batch.reader.ChouAzaCsvItemReader;
import undecided.generic.addressReg.internal.batch.writer.ChouAzaItemWriter;
import undecided.generic.addressReg.spi.ChouAza;

/** 町字マスターデータ自動インポート用 Spring Batch 設定クラス。 */
@Configuration
public class ChouAzaImportBatchConfig {

  public static final String JOB_NAME = "chouAzaImportJob";
  public static final String STEP_NAME = "chouAzaImportStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public ChouAzaCsvItemReader chouAzaCsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      if (inputFile.startsWith("classpath:")) {
        resource = new ClassPathResource(inputFile.substring("classpath:".length()));
      } else {
        resource = new FileSystemResource(inputFile);
      }
    } else {
      resource = new ClassPathResource("data/chou_aza.csv");
    }
    return new ChouAzaCsvItemReader(resource);
  }

  @Bean
  public Step chouAzaImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ChouAzaCsvItemReader reader,
      ChouAzaItemProcessor processor,
      ChouAzaItemWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<ChouAzaCsvDto, ChouAza>chunk(CHUNK_SIZE, transactionManager)
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
  public Job chouAzaImportJob(JobRepository jobRepository, Step chouAzaImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository).start(chouAzaImportStep).build();
  }
}
