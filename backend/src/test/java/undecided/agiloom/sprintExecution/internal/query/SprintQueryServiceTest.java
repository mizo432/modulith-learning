package undecided.agiloom.sprintExecution.internal.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import undecided.agiloom.sprintExecution.internal.Sprint;
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
  @Mock private SprintResponseAssembler sprintResponseAssembler;

  private SprintQueryService service;

  @BeforeEach
  void setUp() {
    service = new SprintQueryService(sprintRepository, sprintResponseAssembler);
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
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
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
      SprintDTO expectedDto = SprintDTO.builder()
          .id(snowflakeId)
          .productId(sprint.getProductId())
          .sprintNumber(sprint.getSprintNumber())
          .title(sprint.getTitle())
          .goal(sprint.getGoal())
          .startDate(sprint.getStartDate())
          .endDate(sprint.getEndDate())
          .status(sprint.getStatus())
          .teamId(sprint.getTeamId())
          .teamName(sprint.getTeamName())
          .createdAt(sprint.getCreatedAt())
          .updatedAt(sprint.getUpdatedAt())
          .build();
      when(sprintRepository.findByIdAndDeletedAtIsNull(snowflakeId)).thenReturn(Optional.of(sprint));
      when(sprintResponseAssembler.toDTO(any(Sprint.class))).thenReturn(expectedDto);

      // Act
      Optional<SprintDTO> result = service.findById(snowflakeId);

      // Assert
      assertThat(result).isPresent();
      assertThat(result.get().getId()).isEqualTo(snowflakeId);
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
      SprintDTO dto1 = SprintDTO.builder()
          .id(snowflakeId1)
          .productId(sprint1.getProductId())
          .sprintNumber(sprint1.getSprintNumber())
          .title(sprint1.getTitle())
          .goal(sprint1.getGoal())
          .startDate(sprint1.getStartDate())
          .endDate(sprint1.getEndDate())
          .status(sprint1.getStatus())
          .teamId(sprint1.getTeamId())
          .teamName(sprint1.getTeamName())
          .createdAt(sprint1.getCreatedAt())
          .updatedAt(sprint1.getUpdatedAt())
          .build();
      SprintDTO dto2 = SprintDTO.builder()
          .id(snowflakeId2)
          .productId(sprint2.getProductId())
          .sprintNumber(sprint2.getSprintNumber())
          .title(sprint2.getTitle())
          .goal(sprint2.getGoal())
          .startDate(sprint2.getStartDate())
          .endDate(sprint2.getEndDate())
          .status(sprint2.getStatus())
          .teamId(sprint2.getTeamId())
          .teamName(sprint2.getTeamName())
          .createdAt(sprint2.getCreatedAt())
          .updatedAt(sprint2.getUpdatedAt())
          .build();
      when(sprintResponseAssembler.toDTO(sprint1)).thenReturn(dto1);
      when(sprintResponseAssembler.toDTO(sprint2)).thenReturn(dto2);

      // Act
      List<SprintDTO> result = service.findAll(pageable);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getId()).isEqualTo(snowflakeId1);
      assertThat(result.get(0).getTitle()).isEqualTo("Sprint 1");
      assertThat(result.get(0).getStatus()).isEqualTo(SprintStatus.PLANNED);
      assertThat(result.get(1).getId()).isEqualTo(snowflakeId2);
      assertThat(result.get(1).getTitle()).isEqualTo("Sprint 1");
      assertThat(result.get(1).getStatus()).isEqualTo(SprintStatus.ACTIVE);
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
      when(sprintResponseAssembler.toDTO(sprint1)).thenReturn(
          SprintDTO.builder()
              .id(snowflakeId1)
              .productId(sprint1.getProductId())
              .sprintNumber(sprint1.getSprintNumber())
              .title(sprint1.getTitle())
              .goal(sprint1.getGoal())
              .startDate(sprint1.getStartDate())
              .endDate(sprint1.getEndDate())
              .status(sprint1.getStatus())
              .teamId(sprint1.getTeamId())
              .teamName(sprint1.getTeamName())
              .createdAt(sprint1.getCreatedAt())
              .updatedAt(sprint1.getUpdatedAt())
              .build()
      );
      when(sprintResponseAssembler.toDTO(sprint2)).thenReturn(
          SprintDTO.builder()
              .id(snowflakeId2)
              .productId(sprint2.getProductId())
              .sprintNumber(sprint2.getSprintNumber())
              .title(sprint2.getTitle())
              .goal(sprint2.getGoal())
              .startDate(sprint2.getStartDate())
              .endDate(sprint2.getEndDate())
              .status(sprint2.getStatus())
              .teamId(sprint2.getTeamId())
              .teamName(sprint2.getTeamName())
              .createdAt(sprint2.getCreatedAt())
              .updatedAt(sprint2.getUpdatedAt())
              .build()
      );

      // Act
      List<SprintDTO> result = service.findByStatus(SprintStatus.PLANNED);

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result).allMatch(dto -> dto.getStatus() == SprintStatus.PLANNED);
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
