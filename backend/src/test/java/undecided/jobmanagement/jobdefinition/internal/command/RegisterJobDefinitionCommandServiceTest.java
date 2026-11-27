package undecided.jobmanagement.jobdefinition.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import undecided.jobmanagement.jobdefinition.internal.repository.JobDefinitionRepository;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.JobDefinition;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
class RegisterJobDefinitionCommandServiceTest {

  private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");
  private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 11, 27, 12, 0);
  private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME.atZone(ZONE).toInstant(), ZONE);

  @Test
  void 固定Clockで登録したとき作成時刻は固定時刻となる() {
    JobDefinitionRepository repository = mock(JobDefinitionRepository.class);
    SnowflakeIdGenerator generator = mock(SnowflakeIdGenerator.class);
    when(generator.nextId()).thenReturn(1L);
    RegisterJobDefinitionCommandService service =
        new RegisterJobDefinitionCommandService(repository, generator, FIXED_CLOCK);

    JobDefinitionDTO dto = new JobDefinitionDTO();
    dto.setJobCode("sampleJob");
    dto.setVersion("1");
    dto.setParameterSpec("");
    dto.setMaxConcurrentExecutions(1);
    dto.setTimeoutSeconds(300);
    dto.setCreatedBy("100");
    service.register(dto);

    ArgumentCaptor<JobDefinition> captor = ArgumentCaptor.forClass(JobDefinition.class);
    verify(repository).save(captor.capture());
    assertThat(captor.getValue().getCreatedAt()).isEqualTo(FIXED_TIME);
  }
}
