package undecided.jobmanagement.jobdefinition.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.ExecutionConstraint;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobCode;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobVersion;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.ParameterSpec;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;
import undecided.supporting.snowflake.SnowflakeId;

class JobDefinitionQueryServiceTest {

  @Test
  void 定義一覧を取得したとき登録済みの定義が返る() {
    JobDefinitionRepository repository = mock(JobDefinitionRepository.class);
    JobDefinition definition = new JobDefinition();
    definition.setId(SnowflakeId.of(1L));
    definition.setJobCode(JobCode.of("sampleJob"));
    definition.setVersion(JobVersion.of("1.0"));
    definition.setParameterSpec(ParameterSpec.of("key=value"));
    definition.setExecutionConstraint(ExecutionConstraint.of(2, 300));
    definition.setIsActive(true);
    when(repository.findAll()).thenReturn(List.of(definition));
    JobDefinitionQueryService service = new JobDefinitionQueryService(repository);

    List<JobDefinitionDTO> result = service.listDefinitions();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getJobCode()).isEqualTo("sampleJob");
    assertThat(result.get(0).getParameterSpec()).isEqualTo("key=value");
    assertThat(result.get(0).getMaxConcurrentExecutions()).isEqualTo(2);
    assertThat(result.get(0).getIsActive()).isTrue();
  }
}
