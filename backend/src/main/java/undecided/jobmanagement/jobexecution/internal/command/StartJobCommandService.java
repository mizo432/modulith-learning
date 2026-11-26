package undecided.jobmanagement.jobexecution.internal.command;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.jobmanagement.jobexecution.internal.BatchJobResolver;
import undecided.jobmanagement.jobexecution.internal.JobDefinitionNotFoundException;
import undecided.jobmanagement.jobexecution.internal.JobExecutionRunner;
import undecided.jobmanagement.jobexecution.internal.JobLaunchConflictException;
import undecided.jobmanagement.jobexecution.internal.JobParameterFormatException;
import undecided.jobmanagement.jobexecution.internal.JobPermissionDeniedException;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionId;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobManuallyStarted;
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommand;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.jobmanagement.permissionmanagement.internal.JobAdminEmployeeSubsetRepository;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * ジョブ手動起動のコマンドサービス（UC-JB-008）。
 *
 * <p>ADMIN判定、ジョブ定義の検証、同時実行制御を行い、実行レコードを待機中で保存して 実バッチの実行を {@link
 * JobExecutionRunner} へ委譲します。
 */
@RequiredArgsConstructor
@Service
public class StartJobCommandService implements StartJobCommand {

  private static final int DEFAULT_MAX_CONCURRENT_EXECUTIONS = 1;

  private final JobExecutionRepository jobExecutionRepository;
  private final JobDefinitionRepository jobDefinitionRepository;
  private final JobAdminEmployeeSubsetRepository adminEmployeeSubsetRepository;
  private final BatchJobResolver batchJobResolver;
  private final JobExecutionRunner jobExecutionRunner;
  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void startJob(JobExecutionDTO jobExecutionDTO) {
    requireAdmin(jobExecutionDTO.getActorEmployeeId());

    JobDefinition definition =
        jobDefinitionRepository
            .findByJobCodeValue(jobExecutionDTO.getJobCode())
            .orElseThrow(
                () ->
                    new JobDefinitionNotFoundException(
                        "ジョブ定義が見つかりません: " + jobExecutionDTO.getJobCode()));
    if (!Boolean.TRUE.equals(definition.getIsActive())) {
      throw new JobLaunchConflictException("ジョブ定義が無効化されています: " + jobExecutionDTO.getJobCode());
    }

    Job job =
        batchJobResolver
            .resolve(jobExecutionDTO.getJobCode())
            .orElseThrow(
                () ->
                    new JobLaunchConflictException(
                        "対応するバッチジョブが存在しません: " + jobExecutionDTO.getJobCode()));

    JobParameters parameters = parseParameters(jobExecutionDTO.getParameters());

    String executionId = resolveExecutionId(jobExecutionDTO);
    if (jobExecutionRepository.existsByExecutionIdValue(executionId)) {
      throw new JobLaunchConflictException("実行IDが重複しています: " + executionId);
    }

    int limit = maxConcurrentExecutions(definition);
    long activeCount =
        jobExecutionRepository.countByJobCodeAndStateIn(
            jobExecutionDTO.getJobCode(),
            List.of(ExecutionState.PENDING, ExecutionState.RUNNING));
    if (activeCount >= limit) {
      throw new JobLaunchConflictException(
          "同時実行数の上限に達しています: " + jobExecutionDTO.getJobCode());
    }

    JobExecution execution = new JobExecution();
    execution.setId(SnowflakeId.of(snowflakeIdGenerator.nextId()));
    execution.setExecutionId(ExecutionId.of(executionId));
    execution.setJobCode(jobExecutionDTO.getJobCode());
    execution.setState(ExecutionState.PENDING);
    execution.setCreatedAt(LocalDateTime.now());
    execution.setCreatedBy(
        jobExecutionDTO.getCreatedBy() != null
            ? jobExecutionDTO.getCreatedBy()
            : jobExecutionDTO.getActorEmployeeId());
    jobExecutionRepository.save(execution);

    eventPublisher.publishEvent(
        new JobManuallyStarted(executionId, jobExecutionDTO.getJobCode(), jobExecutionDTO.getActorEmployeeId()));
    jobExecutionRunner.execute(execution.getId(), job, parameters);
  }

  private void requireAdmin(String actorEmployeeId) {
    SnowflakeId actorId;
    try {
      actorId = SnowflakeId.of(Long.parseLong(actorEmployeeId));
    } catch (RuntimeException e) {
      throw new JobPermissionDeniedException("ジョブを起動する権限がありません");
    }
    if (!adminEmployeeSubsetRepository.existsById(actorId)) {
      throw new JobPermissionDeniedException("ジョブを起動する権限がありません");
    }
  }

  private String resolveExecutionId(JobExecutionDTO jobExecutionDTO) {
    String executionId = jobExecutionDTO.getExecutionId();
    if (executionId == null || executionId.isBlank()) {
      executionId = UUID.randomUUID().toString();
      jobExecutionDTO.setExecutionId(executionId);
    }
    return executionId;
  }

  private int maxConcurrentExecutions(JobDefinition definition) {
    if (definition.getExecutionConstraint() != null
        && definition.getExecutionConstraint().getMaxConcurrentExecutions() != null) {
      return definition.getExecutionConstraint().getMaxConcurrentExecutions();
    }
    return DEFAULT_MAX_CONCURRENT_EXECUTIONS;
  }

  private JobParameters parseParameters(String parametersText) {
    JobParametersBuilder builder = new JobParametersBuilder();
    if (parametersText == null || parametersText.isBlank()) {
      return builder.toJobParameters();
    }
    for (String line : parametersText.split("\\R")) {
      String trimmed = line.trim();
      if (trimmed.isEmpty()) {
        continue;
      }
      int separatorIndex = trimmed.indexOf('=');
      if (separatorIndex <= 0) {
        throw new JobParameterFormatException(
            "パラメータの形式が不正です（key=value形式で入力してください）: " + trimmed);
      }
      builder.addString(
          trimmed.substring(0, separatorIndex).trim(), trimmed.substring(separatorIndex + 1).trim());
    }
    return builder.toJobParameters();
  }
}
