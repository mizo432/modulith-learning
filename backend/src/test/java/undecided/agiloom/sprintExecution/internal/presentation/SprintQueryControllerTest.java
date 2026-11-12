package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import com.github.mizo432.shared.exception.BusinessException;
import undecided.supporting.snowflake.SnowflakeId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.data.domain.Pageable;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.query.SprintQuery;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintQueryControllerの単体テスト")
@ExtendWith(MockitoExtension.class)
class SprintQueryControllerTest {

  @Mock private SprintQuery sprintQueryService;
  private SprintResponseAssembler sprintResponseAssembler;

  private SprintQueryController controller;

  private static final UUID FIXED_PRODUCT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FIXED_TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  @BeforeEach
  void setUp() {
    sprintResponseAssembler = new SprintResponseAssembler();
    controller = new SprintQueryController(sprintQueryService, sprintResponseAssembler);
  }

  private SprintDTO createSprintDTO(SnowflakeId id, SprintStatus status) {
    return SprintDTO.builder()
        .id(id)
        .productId(FIXED_PRODUCT_ID)
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Goal 1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .status(status)
        .teamId(FIXED_TEAM_ID)
        .teamName("Team A")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("getSprints メソッドのテスト")
  class GetSprintsTest {

    @Test
    @DisplayName("Sprintのリストを返すこと")
    void shouldReturnListOfSprints() {
      // Arrange
      SnowflakeId snowflakeId1 = SnowflakeId.of(1234567890L);
      SnowflakeId snowflakeId2 = SnowflakeId.of(9876543210L);
      SprintDTO sprint1 = createSprintDTO(snowflakeId1, SprintStatus.PLANNED);
      SprintDTO sprint2 = createSprintDTO(snowflakeId2, SprintStatus.ACTIVE);
      List<SprintDTO> sprints = List.of(sprint1, sprint2);

      when(sprintQueryService.findAll(any(Pageable.class))).thenReturn(sprints);

      // Act
      List<SprintResponse> result = controller.getSprints(0, 20);

      // Assert
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Sprintが存在しない場合は空のリストを返すこと")
    void shouldReturnEmptyListWhenNoSprints() {
      // Arrange
      when(sprintQueryService.findAll(any(Pageable.class))).thenReturn(List.of());

      // Act
      List<SprintResponse> result = controller.getSprints(0, 20);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("getSprint メソッドのテスト")
  class GetSprintTest {

    @Test
    @DisplayName("Sprintが見つかった場合はSprintResponseを返すこと")
    void shouldReturnSprintResponseWhenFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      SprintDTO sprint = createSprintDTO(snowflakeId, SprintStatus.PLANNED);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.of(sprint));

      // Act
      SprintResponse result = controller.getSprint(String.valueOf(snowflakeId.value()));

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(snowflakeId);
      assertThat(result.getTitle()).isEqualTo("Sprint 1");
    }

    @Test
    @DisplayName("Sprintが見つからない場合はBusinessExceptionをスローすること")
    void shouldThrowExceptionWhenNotFound() {
      // Arrange
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("1234567890"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint not found");
    }
  }
}
