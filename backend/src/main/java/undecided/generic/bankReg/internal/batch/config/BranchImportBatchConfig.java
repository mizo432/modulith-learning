package undecided.generic.bankReg.internal.batch.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import undecided.generic.bankReg.internal.batch.dto.BranchCsvDto;
import undecided.generic.bankReg.internal.batch.processor.BranchItemProcessor;
import undecided.generic.bankReg.internal.batch.reader.BranchCsvItemReader;
import undecided.generic.bankReg.internal.batch.writer.BranchItemWriter;
import undecided.generic.bankReg.spi.branch.Branch;

/** 金融機関支店マスターデータ自動インポート用 Spring Batch 設定クラス。 */
@Configuration
public class BranchImportBatchConfig {

  public static final String JOB_NAME = "branchImportJob";
  public static final String STEP_NAME = "branchImportStep";
  private static final int CHUNK_SIZE = 100;

  @Bean
  @StepScope
  public BranchCsvItemReader branchCsvItemReader(
      @Value("#{jobParameters['inputFile']}") String inputFile) {
    Resource resource;
    if (inputFile != null && !inputFile.isBlank()) {
      resource = new FileSystemResource(inputFile);
    } else {
      throw new IllegalArgumentException("inputFile is required for branch import job");
    }
    return new BranchCsvItemReader(resource);
  }

  @Bean
  @StepScope
  public BranchItemProcessor branchItemProcessor(
      undecided.generic.bankReg.internal.BranchRepository branchRepository,
      @Value("#{jobParameters['datasetId']}") String datasetId) {
    BranchItemProcessor processor = new BranchItemProcessor(branchRepository);
    processor.setDatasetId(datasetId);
    return processor;
  }

  @Bean
  public Step branchImportStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      BranchCsvItemReader reader,
      BranchItemProcessor processor,
      BranchItemWriter writer) {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<BranchCsvDto, Branch>chunk(CHUNK_SIZE)
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
  public Job branchImportJob(JobRepository jobRepository, Step branchImportStep) {
    return new JobBuilder(JOB_NAME, jobRepository).start(branchImportStep).build();
  }
}
