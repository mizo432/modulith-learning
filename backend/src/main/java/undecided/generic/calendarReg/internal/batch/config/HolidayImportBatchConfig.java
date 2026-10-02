package undecided.generic.calendarReg.internal.batch.config;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import undecided.generic.calendarReg.internal.batch.dto.HolidayCsvDto;
import undecided.generic.calendarReg.internal.batch.processor.HolidayItemProcessor;
import undecided.generic.calendarReg.internal.batch.reader.HolidayCsvItemReader;
import undecided.generic.calendarReg.internal.batch.writer.HolidayItemWriter;
import undecided.generic.calendarReg.spi.Holiday;

/** 祝日マスターデータ自動インポート用 Spring Batch 設定クラス。 */
@Configuration
public class HolidayImportBatchConfig {

  public static final String JOB_NAME = "holidayImportJob";
  public static final String STEP_NAME = "holidayImportStep";
  private static final int CHUNK_SIZE = 50;

  @Bean
  @StepScope
  public HolidayCsvItemReader holidayCsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile,
      @Value("#{jobParameters['charset']}") String charsetName) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      if (inputFile.startsWith("classpath:")) {
        resource = new ClassPathResource(inputFile.substring("classpath:".length()));
      } else {
        resource = new FileSystemResource(inputFile);
      }
    } else {
      resource = new ClassPathResource("data/syukujitsu.csv");
    }

    Charset charset = StandardCharsets.UTF_8;
    if (charsetName != null && !charsetName.isBlank()) {
      try {
        charset = Charset.forName(charsetName.trim());
      } catch (Exception ignored) {
        charset = StandardCharsets.UTF_8;
      }
    }

    return new HolidayCsvItemReader(resource, charset);
  }

  @Bean
  public Step holidayImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      HolidayCsvItemReader reader,
      HolidayItemProcessor processor,
      HolidayItemWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<HolidayCsvDto, Holiday>chunk(CHUNK_SIZE)
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
  public Job holidayImportJob(JobRepository jobRepository, Step holidayImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository).start(holidayImportStep).build();
  }
}
