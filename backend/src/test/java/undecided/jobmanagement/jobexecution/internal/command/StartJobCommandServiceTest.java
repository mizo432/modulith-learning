package undecided.jobmanagement.jobexecution.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.context.ApplicationEventPublisher;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.ExecutionConstraint;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobCode;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.jobmanagement.jobexecution.internal.BatchJobResolver;
import undecided.jobmanagement.jobexecution.internal.JobDefinitionNotFoundException;
import undecided.jobmanagement.jobexecution.internal.JobExecutionRunner;
import undecided.jobmanagement.jobexecution.internal.JobLaunchConflictException;
import undecided.jobmanagement.jobexecution.internal.JobParameterFormatException;
import undecided.jobmanagement.jobexecution.internal.JobPermissionDeniedException;
import undecided.jobmanagement.jobexecution.internal.repository.JobExecutionRepository;
import undecided.jobmanagement.jobexecution.spi.jobexecution.ExecutionState;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobExecution;
import undecided.jobmanagement.jobexecution.spi.jobexecution.JobManuallyStarted;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.jobmanagement.permissionmanagement.internal.JobAdminEmployeeSubsetRepository;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

class StartJobCommandServiceTest {

  private JobExecutionRepository jobExecutionRepository;
  private JobDefinitionRepository jobDefinitionRepository;
  private JobAdminEmployeeSubsetRepository adminEmployeeSubsetRepository;
  private BatchJobResolver batchJobResolver;
  private JobExecutionRunner jobExecutionRunner;
  private ApplicationEventPublisher eventPublisher;
  private StartJobCommandService service;
  private Job job;

  @BeforeEach
  void setUp() {
    jobExecutionRepository = mock(JobExecutionRepository.class);
    jobDefinitionRepository = mock(JobDefinitionRepository.class);
    adminEmployeeSubsetRepository = mock(JobAdminEmployeeSubsetRepository.class);
    batchJobResolver = mock(BatchJobResolver.class);
    jobExecutionRunner = mock(JobExecutionRunner.class);
    eventPublisher = mock(ApplicationEventPublisher.class);
    SnowflakeIdGenerator generator = mock(SnowflakeIdGenerator.class);
    when(generator.nextId()).thenReturn(999L);
    service =
        new StartJobCommandService(
            jobExecutionRepository,
            jobDefinitionRepository,
            adminEmployeeSubsetRepository,
            batchJobResolver,
            jobExecutionRunner,
            generator,
            eventPublisher);
    job = mock(Job.class);
    when(adminEmployeeSubsetRepository.existsById(any())).thenReturn(true);
    when(jobDefinitionRepository.findByJobCodeValue("sampleJob"))
        .thenReturn(Optional.of(definition(true, ExecutionConstraint.of(2, 300))));
    when(batchJobResolver.resolve("sampleJob")).thenReturn(Optional.of(job));
    when(jobExecutionRepository.existsByExecutionIdValue(any())).thenReturn(false);
    when(jobExecutionRepository.countByJobCodeAndStateIn(eq("sampleJob"), any())).thenReturn(0L);
  }

  private JobDefinition definition(boolean active, ExecutionConstraint constraint) {
    JobDefinition d = new JobDefinition();
    d.setJobCode(JobCode.of("sampleJob"));
    d.setIsActive(active);
    d.setExecutionConstraint(constraint);
    return d;
  }

  private JobExecutionDTO dto(String executionId, String parameters) {
    JobExecutionDTO dto = new JobExecutionDTO();
    dto.setJobCode("sampleJob");
    dto.setActorEmployeeId("100");
    dto.setExecutionId(executionId);
    dto.setParameters(parameters);
    return dto;
  }

  @Test
  void adminが起動したときPENDINGで保存されランナーへ委譲される() {
    service.startJob(dto("exec-1", null));

    ArgumentCaptor<JobExecution> captor = ArgumentCaptor.forClass(JobExecution.class);
    verify(jobExecutionRepository).save(captor.capture());
    assertThat(captor.getValue().getState()).isEqualTo(ExecutionState.PENDING);
    assertThat(captor.getValue().getJobCode()).isEqualTo("sampleJob");
    assertThat(captor.getValue().getExecutionId().getValue()).isEqualTo("exec-1");
    verify(jobExecutionRunner).execute(any(), eq(job), any());
    verify(eventPublisher).publishEvent(any(JobManuallyStarted.class));
  }

  @Test
  void adminでない者が起動したとき権限例外となり保存されない() {
    when(adminEmployeeSubsetRepository.existsById(any())).thenReturn(false);

    assertThatThrownBy(() -> service.startJob(dto("exec-1", null)))
        .isInstanceOf(JobPermissionDeniedException.class);
    verify(jobExecutionRepository, never()).save(any());
  }

  @Test
  void 存在しないジョブコードのとき定義なし例外となる() {
    when(jobDefinitionRepository.findByJobCodeValue("sampleJob")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.startJob(dto("exec-1", null)))
        .isInstanceOf(JobDefinitionNotFoundException.class);
    verify(jobExecutionRepository, never()).save(any());
  }

  @Test
  void 無効化された定義のとき競合例外となる() {
    when(jobDefinitionRepository.findByJobCodeValue("sampleJob"))
        .thenReturn(Optional.of(definition(false, ExecutionConstraint.of(2, 300))));

    assertThatThrownBy(() -> service.startJob(dto("exec-1", null)))
        .isInstanceOf(JobLaunchConflictException.class);
  }

  @Test
  void 対応するバッチJobがないとき競合例外となる() {
    when(batchJobResolver.resolve("sampleJob")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.startJob(dto("exec-1", null)))
        .isInstanceOf(JobLaunchConflictException.class);
  }

  @Test
  void 実行中件数が上限に達しているとき競合例外となる() {
    when(jobExecutionRepository.countByJobCodeAndStateIn(eq("sampleJob"), any())).thenReturn(2L);

    assertThatThrownBy(() -> service.startJob(dto("exec-1", null)))
        .isInstanceOf(JobLaunchConflictException.class);
  }

  @Test
  void 上限が未設定のとき上限は1として判定される() {
    when(jobDefinitionRepository.findByJobCodeValue("sampleJob"))
        .thenReturn(Optional.of(definition(true, ExecutionConstraint.of(null, null))));
    when(jobExecutionRepository.countByJobCodeAndStateIn(eq("sampleJob"), any())).thenReturn(1L);

    assertThatThrownBy(() -> service.startJob(dto("exec-1", null)))
        .isInstanceOf(JobLaunchConflictException.class);
  }

  @Test
  void 同じ実行IDで再度起動したとき競合例外となる() {
    when(jobExecutionRepository.existsByExecutionIdValue("exec-1")).thenReturn(true);

    assertThatThrownBy(() -> service.startJob(dto("exec-1", null)))
        .isInstanceOf(JobLaunchConflictException.class);
  }

  @Test
  void パラメータの形式が不正なとき形式例外となる() {
    assertThatThrownBy(() -> service.startJob(dto("exec-1", "not-a-pair")))
        .isInstanceOf(JobParameterFormatException.class);
    verify(jobExecutionRepository, never()).save(any());
  }

  @Test
  void パラメータがJobParametersに変換されてランナーへ渡される() {
    service.startJob(dto("exec-1", "foo=bar\nbaz=qux"));

    ArgumentCaptor<JobParameters> captor = ArgumentCaptor.forClass(JobParameters.class);
    verify(jobExecutionRunner).execute(any(), eq(job), captor.capture());
    assertThat(captor.getValue().getString("foo")).isEqualTo("bar");
    assertThat(captor.getValue().getString("baz")).isEqualTo("qux");
  }
}
