package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import undecided.ModulithDemoApplication;
import undecided.TestcontainersConfiguration;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("スプリント割り当てのDB連携")
class BacklogItemSprintAssignmentMediumTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(525010L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(525011L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(525100L);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(525201L);

  @Autowired private AssignBacklogItemSprintCommandService service;
  @Autowired private BacklogItemRepository backlogItemRepository;
  @Autowired private ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  @Autowired private SprintRepository sprintRepository;
  @Autowired private EntityManager entityManager;

  private static SnowflakeId sprintId(int number) {
    return SnowflakeId.of(525100L + number);
  }

  @BeforeEach
  void setUp() {
    scrumTeamSubsetRepository.save(
        ScrumTeamSubset.builder()
            .id(TEAM_ID)
            .name("割当テストチーム")
            .memberEmployeeIds(List.of(MEMBER_ID))
            .build());
    for (int number = 1; number <= 5; number++) {
      sprintRepository.save(
          Sprint.builder()
              .id(sprintId(number))
              .productId(PRODUCT_ID)
              .sprintNumber(number)
              .title("Sprint " + number)
              .status(number == 1 ? SprintStatus.ACTIVE : SprintStatus.PLANNED)
              .teamId(TEAM_ID)
              .teamName("割当テストチーム")
              .startDate(LocalDate.of(2026, 11, 16).plusWeeks(2L * (number - 1)))
              .endDate(LocalDate.of(2026, 11, 27).plusWeeks(2L * (number - 1)))
              .build());
    }
    backlogItemRepository.save(
        BacklogItem.builder()
            .id(ITEM_ID)
            .title("割当対象アイテム")
            .creatorName("作成者")
            .scrumTeamId(TEAM_ID)
            .status(BacklogItemStatus.ESTIMATED)
            .storyPoint(5)
            .build());
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("スプリントに割り当てたとき、sprintIdがDBに保存される")
  void persistsAssignment() {
    service.assign(
        AssignBacklogItemSprintCommand.builder()
            .backlogItemId(ITEM_ID)
            .sprintId(sprintId(3))
            .actorEmployeeId(MEMBER_ID)
            .build());
    entityManager.flush();
    entityManager.clear();

    assertThat(backlogItemRepository.findById(ITEM_ID).orElseThrow().getSprintId())
        .isEqualTo(sprintId(3));
  }

  @Test
  @DisplayName("未来4スプリントの窓の外のスプリントに割り当てたとき、例外となりsprintIdは保存されない")
  void rejectsSprintOutsideWindow() {
    assertThatThrownBy(
            () ->
                service.assign(
                    AssignBacklogItemSprintCommand.builder()
                        .backlogItemId(ITEM_ID)
                        .sprintId(sprintId(5))
                        .actorEmployeeId(MEMBER_ID)
                        .build()))
        .isInstanceOf(IllegalArgumentException.class);
    entityManager.flush();
    entityManager.clear();

    assertThat(backlogItemRepository.findById(ITEM_ID).orElseThrow().getSprintId()).isNull();
  }
}
