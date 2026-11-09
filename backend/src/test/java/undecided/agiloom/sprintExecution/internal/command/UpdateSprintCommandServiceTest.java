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
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("UpdateSprintCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class UpdateSprintCommandServiceTest {

  @Mock private SprintRepository sprintRepository;
  @Mock private SprintResponseAssembler sprintResponseAssembler;

  private UpdateSprintCommandService service;

  @BeforeEach
  void setUp() {
    service = new UpdateSprintCommandService(sprintRepository, sprintResponseAssembler);
  }

  private Sprint createSprint(SnowflakeId id, SprintStatus status) {
    return Sprint.builder()
        .id(id)
        .productId(UUID.randomUUID())
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Goal 1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .teamId(UUID.randomUUID())
        .teamName("Team A")
        .status(status)
        .createdAt(java.time.LocalDateTime.now())
        .updatedAt(java.time.LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("Sprintを更新すること")
    void shouldUpdateSprint() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint existingSprint = createSprint(snowflakeId, SprintStatus.PLANNED);

      UpdateSprintCommand command = UpdateSprintCommand.builder()
          .id(snowflakeId)
          .title("Updated Title")
          .goal("Updated Goal")
          .endDate(LocalDate.now().plusWeeks(3))
          .notes("Updated notes")
          .build();

      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.of(existingSprint));
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(null);

      // Act
      service.execute(command);

      // Assert
      assertThat(existingSprint.getTitle()).isEqualTo("Updated Title");
      assertThat(existingSprint.getGoal()).isEqualTo("Updated Goal");
      assertThat(existingSprint.getEndDate()).isEqualTo(LocalDate.now().plusWeeks(3));
      assertThat(existingSprint.getNotes()).isEqualTo("Updated notes");
    }

    @Test
    @DisplayName("Sprintが見つからない場合はIllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      UpdateSprintCommand command = UpdateSprintCommand.builder()
          .id(snowflakeId)
          .title("Updated Title")
          .build();

      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Sprint not found");
    }

    @Test
    @DisplayName("null以外のフィールドのみを更新すること")
    void shouldUpdateOnlyNonNullFields() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint existingSprint = createSprint(snowflakeId, SprintStatus.PLANNED);

      UpdateSprintCommand command = UpdateSprintCommand.builder()
          .id(snowflakeId)
          .title("Updated Title")
          .build();

      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.of(existingSprint));
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(null);

      // Act
      service.execute(command);

      // Assert
      assertThat(existingSprint.getTitle()).isEqualTo("Updated Title");
      assertThat(existingSprint.getGoal()).isEqualTo("Goal 1");
    }
  }

  @Nested
  @DisplayName("changeStatus メソッドのテスト")
  class ChangeStatusTest {

    @Test
    @DisplayName("SprintをACTIVEに変更すること")
    void shouldChangeStatusToActive() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId, SprintStatus.PLANNED);
      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.of(sprint));
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(null);

      // Act
      service.changeStatus(snowflakeId, SprintStatus.ACTIVE);

      // Assert
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.ACTIVE);
    }

    @Test
    @DisplayName("SprintをCOMPLETEDに変更すること")
    void shouldChangeStatusToCompleted() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId, SprintStatus.PLANNED);
      sprint.start();
      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.of(sprint));
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(null);

      // Act
      service.changeStatus(snowflakeId, SprintStatus.COMPLETED);

      // Assert
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.COMPLETED);
    }

    @Test
    @DisplayName("SprintをCANCELLEDに変更すること")
    void shouldChangeStatusToCancelled() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId, SprintStatus.PLANNED);
      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.of(sprint));
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(null);

      // Act
      service.changeStatus(snowflakeId, SprintStatus.CANCELLED);

      // Assert
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.CANCELLED);
    }

    @Test
    @DisplayName("Sprintが見つからない場合はIllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.changeStatus(snowflakeId, SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Sprint not found");
    }

    @Test
    @DisplayName("無効なステータスの場合はIllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenInvalidStatus() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId, SprintStatus.PLANNED);
      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.of(sprint));

      // Act & Assert
      assertThatThrownBy(() -> service.changeStatus(snowflakeId, SprintStatus.PLANNED))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Invalid status");
    }
  }
}
