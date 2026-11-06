package undecided.agiloom.sprintExecution.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SprintQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class SprintQueryServiceTest {

  @Mock private SprintRepository sprintRepository;

  private SprintQueryService service;

  @BeforeEach
  void setUp() {
    service = new SprintQueryService(sprintRepository);
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
      Sprint sprint = createSprint(snowflakeId, SprintStatus.PLANNED);
      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.of(sprint));

      // Act
      Optional<Sprint> result = service.findById(snowflakeId);

      // Assert
      assertThat(result).isPresent();
      assertThat(result.get().getId()).isEqualTo(snowflakeId);
    }

    @Test
    @DisplayName("Sprintが見つからない場合は空のOptionalを返すこと")
    void shouldReturnEmptyWhenNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(sprintRepository.findById(snowflakeId)).thenReturn(Optional.empty());

      // Act
      Optional<Sprint> result = service.findById(snowflakeId);

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
      when(sprintRepository.findAll(pageable)).thenReturn(page);

      // Act
      List<Sprint> result = service.findAll(pageable);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result).contains(sprint1, sprint2);
    }

    @Test
    @DisplayName("Sprintが存在しない場合は空のリストを返すこと")
    void shouldReturnEmptyListWhenNoSprints() {
      // Arrange
      Page<Sprint> page = new PageImpl<>(List.of());
      Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 20);
      when(sprintRepository.findAll(pageable)).thenReturn(page);

      // Act
      List<Sprint> result = service.findAll(pageable);

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

      when(sprintRepository.findByStatus(SprintStatus.PLANNED)).thenReturn(sprints);

      // Act
      List<Sprint> result = service.findByStatus(SprintStatus.PLANNED);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result).allMatch(sprint -> sprint.getStatus() == SprintStatus.PLANNED);
    }

    @Test
    @DisplayName("ステータスに一致するSprintが存在しない場合は空のリストを返すこと")
    void shouldReturnEmptyListWhenNoSprintsMatchStatus() {
      // Arrange
      when(sprintRepository.findByStatus(SprintStatus.COMPLETED)).thenReturn(List.of());

      // Act
      List<Sprint> result = service.findByStatus(SprintStatus.COMPLETED);

      // Assert
      assertThat(result).isEmpty();
    }
  }
}
