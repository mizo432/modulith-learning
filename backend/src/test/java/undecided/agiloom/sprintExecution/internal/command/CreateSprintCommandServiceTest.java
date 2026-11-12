package undecided.agiloom.sprintExecution.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreateSprintCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateSprintCommandServiceTest {

  private static final UUID FIXED_PRODUCT_ID =
      UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FIXED_TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  @Mock private SprintRepository sprintRepository;
  @Mock private SnowflakeIdGenerator snowflakeIdGenerator;
  private SprintResponseAssembler sprintResponseAssembler;

  private CreateSprintCommandService service;

  @BeforeEach
  void setUp() {
    sprintResponseAssembler = new SprintResponseAssembler();
    service =
        new CreateSprintCommandService(
            sprintRepository, sprintResponseAssembler, snowflakeIdGenerator);
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("Sprintを作成してRepositoryに保存すること")
    void shouldCreateAndSaveSprint() {
      // Arrange
      when(snowflakeIdGenerator.nextId()).thenReturn(1234567890L);
      CreateSprintCommand command =
          CreateSprintCommand.builder()
              .productId(FIXED_PRODUCT_ID)
              .sprintNumber(1)
              .title("Sprint 1")
              .goal("Goal 1")
              .startDate(LocalDate.now())
              .endDate(LocalDate.now().plusWeeks(2))
              .teamId(FIXED_TEAM_ID)
              .teamName("Team A")
              .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint savedSprint =
          Sprint.of(
              snowflakeId,
              FIXED_PRODUCT_ID,
              1,
              "Sprint 1",
              "Goal 1",
              LocalDate.now(),
              LocalDate.now().plusWeeks(2),
              FIXED_TEAM_ID,
              "Team A",
              null,
              null,
              LocalDateTime.now(),
              LocalDateTime.now());

      when(sprintRepository.save(any(Sprint.class))).thenReturn(savedSprint);

      // Act
      SprintDTO result = service.execute(command);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(snowflakeId);
      assertThat(result.getProductId()).isEqualTo(FIXED_PRODUCT_ID);
      assertThat(result.getSprintNumber()).isEqualTo(1);
      assertThat(result.getTitle()).isEqualTo("Sprint 1");
      assertThat(result.getGoal()).isEqualTo("Goal 1");
      assertThat(result.getStatus()).isEqualTo(SprintStatus.PLANNED);
      assertThat(result.getTeamId()).isEqualTo(FIXED_TEAM_ID);
      assertThat(result.getTeamName()).isEqualTo("Team A");
      verify(sprintRepository).save(any(Sprint.class));
    }

    @Test
    @DisplayName("保存されたSprintのDTOを返すこと")
    void shouldReturnSavedSprintDto() {
      // Arrange
      when(snowflakeIdGenerator.nextId()).thenReturn(9876543210L);
      CreateSprintCommand command =
          CreateSprintCommand.builder()
              .productId(FIXED_PRODUCT_ID)
              .sprintNumber(2)
              .title("Sprint 2")
              .goal("")
              .startDate(LocalDate.now())
              .endDate(LocalDate.now().plusWeeks(2))
              .teamId(FIXED_TEAM_ID)
              .build();

      SnowflakeId snowflakeId = SnowflakeId.of(9876543210L);
      Sprint savedSprint =
          Sprint.of(
              snowflakeId,
              FIXED_PRODUCT_ID,
              2,
              "Sprint 2",
              "",
              LocalDate.now(),
              LocalDate.now().plusWeeks(2),
              FIXED_TEAM_ID,
              null,
              null,
              null,
              LocalDateTime.now(),
              LocalDateTime.now());

      when(sprintRepository.save(any(Sprint.class))).thenReturn(savedSprint);

      // Act
      SprintDTO result = service.execute(command);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(snowflakeId);
      assertThat(result.getProductId()).isEqualTo(FIXED_PRODUCT_ID);
      assertThat(result.getSprintNumber()).isEqualTo(2);
      assertThat(result.getTitle()).isEqualTo("Sprint 2");
      assertThat(result.getStatus()).isEqualTo(SprintStatus.PLANNED);
      assertThat(result.getTeamId()).isEqualTo(FIXED_TEAM_ID);
    }

    @Test
    @DisplayName("endDateがstartDateより前の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
      // Arrange
      CreateSprintCommand command =
          CreateSprintCommand.builder()
              .productId(FIXED_PRODUCT_ID)
              .sprintNumber(1)
              .title("Sprint 1")
              .goal("Goal 1")
              .startDate(LocalDate.now().plusWeeks(2))
              .endDate(LocalDate.now())
              .teamId(FIXED_TEAM_ID)
              .teamName("Team A")
              .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("endDate must be after startDate");
    }

    @Test
    @DisplayName("goalがnullの場合、IllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenGoalIsNull() {
      // Arrange
      when(snowflakeIdGenerator.nextId()).thenReturn(1234567890L);
      CreateSprintCommand command =
          CreateSprintCommand.builder()
              .productId(FIXED_PRODUCT_ID)
              .sprintNumber(1)
              .title("Sprint 1")
              .goal(null)
              .startDate(LocalDate.now())
              .endDate(LocalDate.now().plusWeeks(2))
              .teamId(FIXED_TEAM_ID)
              .teamName("Team A")
              .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("goal must not be null");
    }

    @Test
    @DisplayName("titleがnullの場合、NullPointerExceptionがスローされる")
    void shouldThrowExceptionWhenTitleIsNull() {
      // CreateSprintCommandのtitleは@NonNullのため、builderでnullを設定するとNullPointerException
      assertThatThrownBy(() ->
          CreateSprintCommand.builder()
              .productId(FIXED_PRODUCT_ID)
              .sprintNumber(1)
              .title(null)
              .goal("Goal 1")
              .startDate(LocalDate.now())
              .endDate(LocalDate.now().plusWeeks(2))
              .teamId(FIXED_TEAM_ID)
              .teamName("Team A")
              .build())
          .isInstanceOf(NullPointerException.class);
    }
  }
}
