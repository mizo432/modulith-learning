package undecided.agiloom.sprintExecution.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import undecided.agiloom.sprintExecution.internal.presentation.SprintResponseAssembler;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("UpdateSprintCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class UpdateSprintCommandServiceTest {

  private static final UUID FIXED_PRODUCT_ID =
      UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FIXED_TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  @Mock private SprintRepository sprintRepository;

  private SprintResponseAssembler sprintResponseAssembler;
  private UpdateSprintCommandService service;

  @BeforeEach
  void setUp() {
    sprintResponseAssembler = new SprintResponseAssembler();
    service = new UpdateSprintCommandService(sprintRepository, sprintResponseAssembler);
  }

  private Sprint createSprint(SnowflakeId id, SprintStatus status) {
    Sprint sprint = Sprint.of(
        id,
        FIXED_PRODUCT_ID,
        1,
        "Sprint 1",
        "Goal 1",
        LocalDate.now(),
        LocalDate.now().plusWeeks(2),
        FIXED_TEAM_ID,
        "Team A",
        null,
        status,
        LocalDateTime.now(),
        LocalDateTime.now());
    return sprint;
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

      UpdateSprintCommand command =
          UpdateSprintCommand.builder()
              .id(snowflakeId)
              .title("Updated Title")
              .goal("Updated Goal")
              .endDate(LocalDate.now().plusWeeks(3))
              .notes("Updated notes")
              .build();

      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId))
          .thenReturn(Optional.of(existingSprint));

      // Act
      service.execute(command);

      // Assert
      assertThat(existingSprint.getTitle()).isEqualTo("Updated Title");
      assertThat(existingSprint.getGoal()).isEqualTo("Updated Goal");
      assertThat(existingSprint.getEndDate()).isEqualTo(LocalDate.now().plusWeeks(3));
      assertThat(existingSprint.getNotes()).isEqualTo("Updated notes");
    }

    @Test
    @DisplayName("Sprintが見つからない場合はBusinessExceptionをスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      UpdateSprintCommand command =
          UpdateSprintCommand.builder().id(snowflakeId).title("Updated Title").build();

      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint not found");
    }

    @Test
    @DisplayName("null以外のフィールドのみを更新すること")
    void shouldUpdateOnlyNonNullFields() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint existingSprint = createSprint(snowflakeId, SprintStatus.PLANNED);

      UpdateSprintCommand command =
          UpdateSprintCommand.builder().id(snowflakeId).title("Updated Title").build();

      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId))
          .thenReturn(Optional.of(existingSprint));

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
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId))
          .thenReturn(Optional.of(sprint));

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
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId))
          .thenReturn(Optional.of(sprint));

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
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId))
          .thenReturn(Optional.of(sprint));

      // Act
      service.changeStatus(snowflakeId, SprintStatus.CANCELLED);

      // Assert
      assertThat(sprint.getStatus()).isEqualTo(SprintStatus.CANCELLED);
    }

    @Test
    @DisplayName("Sprintが見つからない場合はBusinessExceptionをスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.changeStatus(snowflakeId, SprintStatus.ACTIVE))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint not found");
    }

    @Test
    @DisplayName("無効なステータスの場合はBusinessExceptionをスローすること")
    void shouldThrowExceptionWhenInvalidStatus() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId, SprintStatus.PLANNED);
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId))
          .thenReturn(Optional.of(sprint));

      // Act & Assert
      assertThatThrownBy(() -> service.changeStatus(snowflakeId, SprintStatus.PLANNED))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Invalid status");
    }
  }
}
