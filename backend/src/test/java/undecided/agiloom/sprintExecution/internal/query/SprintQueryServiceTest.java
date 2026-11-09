package undecided.agiloom.sprintExecution.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.presentation.SprintResponseAssembler;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class SprintQueryServiceTest {

  @Mock private SprintRepository sprintRepository;

  private SprintResponseAssembler sprintResponseAssembler;
  private SprintQueryService service;

  @BeforeEach
  void setUp() {
    sprintResponseAssembler = new SprintResponseAssembler();
    service = new SprintQueryService(sprintRepository, sprintResponseAssembler);
  }

  private static final UUID FIXED_PRODUCT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FIXED_TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  private Sprint createSprint(SnowflakeId id, SprintStatus status) {
    return Sprint.builder()
        .id(id)
        .productId(FIXED_PRODUCT_ID)
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Goal 1")
        .startDate(LocalDate.of(2024, 1, 1))
        .endDate(LocalDate.of(2024, 1, 15))
        .teamId(FIXED_TEAM_ID)
        .teamName("Team A")
        .status(status)
        .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
        .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
        .build();
  }

  @Nested
  @DisplayName("findById メソッドのテスト")
  class FindByIdTest {

    @Test
    @DisplayName("Sprintが見つかった場合はOptionalで返すこと")
    void shouldReturnSprintWhenFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId)).thenReturn(Optional.of(createSprint(snowflakeId, SprintStatus.PLANNED)));

      // Act
      Optional<SprintDTO> result = service.findById(snowflakeId);

      // Assert
      assertThat(result).isPresent();
      SprintDTO dto = result.get();
      assertThat(dto.getId()).isEqualTo(snowflakeId);
      assertThat(dto.getProductId()).isEqualTo(FIXED_PRODUCT_ID);
      assertThat(dto.getSprintNumber()).isEqualTo(1);
      assertThat(dto.getTitle()).isEqualTo("Sprint 1");
      assertThat(dto.getGoal()).isEqualTo("Goal 1");
      assertThat(dto.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
      assertThat(dto.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 15));
      assertThat(dto.getStatus()).isEqualTo(SprintStatus.PLANNED);
      assertThat(dto.getTeamId()).isEqualTo(FIXED_TEAM_ID);
      assertThat(dto.getTeamName()).isEqualTo("Team A");
    }

    @Test
    @DisplayName("Sprintが見つからない場合は空のOptionalを返すこと")
    void shouldReturnEmptyWhenNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId)).thenReturn(Optional.empty());

      // Act
      Optional<SprintDTO> result = service.findById(snowflakeId);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findAll メソッドのテスト")
  class FindAllTest {

    @Test
    @DisplayName("Sprintのリストを返すこと")
    void shouldReturnListOfSprints() {
      // Arrange
      SnowflakeId snowflakeId1 = SnowflakeId.of(1234567890L);
      SnowflakeId snowflakeId2 = SnowflakeId.of(9876543210L);
      Sprint sprint1 = createSprint(snowflakeId1, SprintStatus.PLANNED);
      Sprint sprint2 = createSprint(snowflakeId2, SprintStatus.ACTIVE);
      List<Sprint> sprints = List.of(sprint1, sprint2);
      Page<Sprint> page = new PageImpl<>(sprints);
      Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 20);
      when(sprintRepository.findAllByDeletedAtIsNull(pageable)).thenReturn(page);

      // Act
      List<SprintDTO> result = service.findAll(pageable);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getId()).isEqualTo(snowflakeId1);
      assertThat(result.get(0).getTitle()).isEqualTo("Sprint 1");
      assertThat(result.get(0).getStatus()).isEqualTo(SprintStatus.PLANNED);
      assertThat(result.get(0).getProductId()).isEqualTo(FIXED_PRODUCT_ID);
      assertThat(result.get(1).getId()).isEqualTo(snowflakeId2);
      assertThat(result.get(1).getTitle()).isEqualTo("Sprint 1");
      assertThat(result.get(1).getStatus()).isEqualTo(SprintStatus.ACTIVE);
      assertThat(result.get(1).getProductId()).isEqualTo(FIXED_PRODUCT_ID);
    }

    @Test
    @DisplayName("Sprintが存在しない場合は空のリストを返すこと")
    void shouldReturnEmptyListWhenNoSprints() {
      // Arrange
      Page<Sprint> page = new PageImpl<>(List.of());
      Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 20);
      when(sprintRepository.findAllByDeletedAtIsNull(pageable)).thenReturn(page);

      // Act
      List<SprintDTO> result = service.findAll(pageable);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("findByStatus メソッドのテスト")
  class FindByStatusTest {

    @Test
    @DisplayName("ステータスに一致するSprintのリストを返すこと")
    void shouldReturnSprintsByStatus() {
      // Arrange
      SnowflakeId snowflakeId1 = SnowflakeId.of(1234567890L);
      SnowflakeId snowflakeId2 = SnowflakeId.of(9876543210L);
      Sprint sprint1 = createSprint(snowflakeId1, SprintStatus.PLANNED);
      Sprint sprint2 = createSprint(snowflakeId2, SprintStatus.PLANNED);
      List<Sprint> sprints = List.of(sprint1, sprint2);

      when(sprintRepository.findByStatusAndDeletedAtIsNull(SprintStatus.PLANNED)).thenReturn(sprints);

      // Act
      List<SprintDTO> result = service.findByStatus(SprintStatus.PLANNED);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result).allMatch(dto -> dto.getStatus() == SprintStatus.PLANNED);
      assertThat(result.get(0).getId()).isEqualTo(snowflakeId1);
      assertThat(result.get(0).getProductId()).isEqualTo(FIXED_PRODUCT_ID);
      assertThat(result.get(1).getId()).isEqualTo(snowflakeId2);
      assertThat(result.get(1).getProductId()).isEqualTo(FIXED_PRODUCT_ID);
    }

    @Test
    @DisplayName("ステータスに一致するSprintが存在しない場合は空のリストを返すこと")
    void shouldReturnEmptyListWhenNoSprintsMatchStatus() {
      // Arrange
      when(sprintRepository.findByStatusAndDeletedAtIsNull(SprintStatus.COMPLETED)).thenReturn(List.of());

      // Act
      List<SprintDTO> result = service.findByStatus(SprintStatus.COMPLETED);

      // Assert
      assertThat(result).isEmpty();
    }
  }
}
