package undecided.agiloom.sprintExecution.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.Sprint;
import undecided.agiloom.sprintExecution.internal.presentation.SprintResponseAssembler;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("CreateSprintCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateSprintCommandServiceTest {

  @Mock private SprintRepository sprintRepository;
  @Mock private SprintResponseAssembler sprintResponseAssembler;

  private CreateSprintCommandService service;

  @BeforeEach
  void setUp() {
    service = new CreateSprintCommandService(sprintRepository, sprintResponseAssembler);
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("Sprintを作成してRepositoryに保存すること")
    void shouldCreateAndSaveSprint() {
      // Arrange
      UUID productId = UUID.randomUUID();
      UUID teamId = UUID.randomUUID();
      CreateSprintCommand command = CreateSprintCommand.builder()
          .productId(productId)
          .sprintNumber(1)
          .title("Sprint 1")
          .goal("Goal 1")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(teamId)
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint savedSprint = Sprint.builder()
          .id(snowflakeId)
          .productId(productId)
          .sprintNumber(1)
          .title("Sprint 1")
          .goal("Goal 1")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(teamId)
          .teamName("Team A")
          .createdAt(java.time.LocalDateTime.now())
          .updatedAt(java.time.LocalDateTime.now())
          .build();

      SprintDTO expectedDto = SprintDTO.builder()
          .id(snowflakeId)
          .productId(productId)
          .sprintNumber(1)
          .title("Sprint 1")
          .goal("Goal 1")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .status(SprintStatus.PLANNED)
          .teamId(teamId)
          .teamName("Team A")
          .createdAt(savedSprint.getCreatedAt())
          .updatedAt(savedSprint.getUpdatedAt())
          .build();

      when(sprintRepository.save(any(Sprint.class))).thenReturn(savedSprint);
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(expectedDto);

      // Act
      SprintDTO result = service.execute(command);

      // Assert
      assertThat(result).isEqualTo(expectedDto);
      verify(sprintRepository).save(any(Sprint.class));
    }

    @Test
    @DisplayName("保存されたSprintのDTOを返すこと")
    void shouldReturnSavedSprintDto() {
      // Arrange
      UUID productId = UUID.randomUUID();
      UUID teamId = UUID.randomUUID();
      CreateSprintCommand command = CreateSprintCommand.builder()
          .productId(productId)
          .sprintNumber(2)
          .title("Sprint 2")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(teamId)
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(9876543210L);
      Sprint savedSprint = Sprint.builder()
          .id(snowflakeId)
          .productId(productId)
          .sprintNumber(2)
          .title("Sprint 2")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(teamId)
          .createdAt(java.time.LocalDateTime.now())
          .updatedAt(java.time.LocalDateTime.now())
          .build();

      SprintDTO expectedDto = SprintDTO.builder()
          .id(snowflakeId)
          .productId(productId)
          .sprintNumber(2)
          .title("Sprint 2")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .status(SprintStatus.PLANNED)
          .teamId(teamId)
          .createdAt(savedSprint.getCreatedAt())
          .updatedAt(savedSprint.getUpdatedAt())
          .build();

      when(sprintRepository.save(any(Sprint.class))).thenReturn(savedSprint);
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(expectedDto);

      // Act
      SprintDTO result = service.execute(command);

      // Assert
      assertThat(result).isEqualTo(expectedDto);
    }
  }
}
