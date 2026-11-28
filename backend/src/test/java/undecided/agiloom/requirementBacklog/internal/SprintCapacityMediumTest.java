package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
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
import undecided.agiloom.sprintExecution.internal.query.SprintCapacity;
import undecided.agiloom.sprintExecution.internal.query.SprintCapacityQueryService;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("スプリント容量のDB連携")
class SprintCapacityMediumTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(528010L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(528100L);
  private static final SnowflakeId DONE_SPRINT_ID = SnowflakeId.of(528101L);
  private static final SnowflakeId TARGET_SPRINT_ID = SnowflakeId.of(528102L);

  @Autowired private SprintCapacityQueryService sprintCapacityQueryService;
  @Autowired private BacklogItemRepository backlogItemRepository;
  @Autowired private SprintRepository sprintRepository;
  @Autowired private EntityManager entityManager;

  private void saveSprint(
      SnowflakeId id, int number, SprintStatus status, LocalDate start, LocalDate end) {
    sprintRepository.save(
        Sprint.builder()
            .id(id)
            .productId(PRODUCT_ID)
            .sprintNumber(number)
            .title("Sprint " + number)
            .status(status)
            .teamId(TEAM_ID)
            .teamName("容量テストチーム")
            .startDate(start)
            .endDate(end)
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
  }

  private void saveItem(long id, BacklogItemStatus status, Integer storyPoint, SnowflakeId sprintId) {
    backlogItemRepository.save(
        BacklogItem.builder()
            .id(SnowflakeId.of(id))
            .title("アイテム" + id)
            .creatorName("作成者")
            .scrumTeamId(TEAM_ID)
            .status(status)
            .storyPoint(storyPoint)
            .sprintId(sprintId)
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
  }

  @Test
  @DisplayName("完了スプリントの実績から容量を計算し、超過を検出する")
  void calculatesCapacityFromCompletedSprint() {
    // 完了スプリント: 2026-09-07(月)〜09-18(金) = 営業日10日、完了SP 30+20=50
    saveSprint(
        DONE_SPRINT_ID,
        1,
        SprintStatus.COMPLETED,
        LocalDate.of(2026, 9, 7),
        LocalDate.of(2026, 9, 18));
    saveItem(528201L, BacklogItemStatus.DONE, 30, DONE_SPRINT_ID);
    saveItem(528202L, BacklogItemStatus.DONE, 20, DONE_SPRINT_ID);
    saveItem(528203L, BacklogItemStatus.IN_PROGRESS, 13, DONE_SPRINT_ID);
    // 対象スプリント: 2026-11-02(月)〜11-13(金) = 営業日10日、割り当て済みSP 55
    saveSprint(
        TARGET_SPRINT_ID,
        2,
        SprintStatus.PLANNED,
        LocalDate.of(2026, 11, 2),
        LocalDate.of(2026, 11, 13));
    saveItem(528204L, BacklogItemStatus.ESTIMATED, 55, TARGET_SPRINT_ID);
    entityManager.flush();
    entityManager.clear();

    SprintCapacity capacity = sprintCapacityQueryService.getCapacity(TARGET_SPRINT_ID);

    assertThat(capacity.businessDays()).isEqualTo(10);
    assertThat(capacity.velocityPerBusinessDay()).isEqualTo(5.0);
    assertThat(capacity.assignablePoints()).isEqualTo(50L);
    assertThat(capacity.assignedPoints()).isEqualTo(55L);
    assertThat(capacity.overAllocated()).isTrue();
  }

  @Test
  @DisplayName("完了スプリントが無いとき、ベロシティ0で容量を返す")
  void returnsZeroVelocityWithoutCompletedSprints() {
    saveSprint(
        TARGET_SPRINT_ID,
        1,
        SprintStatus.PLANNED,
        LocalDate.of(2026, 11, 2),
        LocalDate.of(2026, 11, 13));
    saveItem(528211L, BacklogItemStatus.ESTIMATED, 5, TARGET_SPRINT_ID);
    entityManager.flush();
    entityManager.clear();

    SprintCapacity capacity = sprintCapacityQueryService.getCapacity(TARGET_SPRINT_ID);

    assertThat(capacity.velocityPerBusinessDay()).isZero();
    assertThat(capacity.assignablePoints()).isZero();
    assertThat(capacity.assignedPoints()).isEqualTo(5L);
    assertThat(capacity.overAllocated()).isTrue();
  }
}
