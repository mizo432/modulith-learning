package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import undecided.agiloom.sprintExecution.spi.SprintCancelled;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("スプリントキャンセル時のスライドのDB連携")
class SprintCancelledSlideMediumTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(525510L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(525600L);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(525701L);

  @Autowired private SprintFacade sprintFacade;
  @Autowired private BacklogItemRepository backlogItemRepository;
  @Autowired private SprintRepository sprintRepository;
  @Autowired private EntityManager entityManager;

  private static SnowflakeId sprintId(int number) {
    return SnowflakeId.of(525600L + number);
  }

  private SprintCancelledSlideListener listener;

  @BeforeEach
  void setUp() {
    // @ApplicationModuleListener は非同期実行のため、検証では実リポジトリで直接生成して同期に呼ぶ
    listener = new SprintCancelledSlideListener(backlogItemRepository, sprintFacade);
    for (int number = 1; number <= 2; number++) {
      sprintRepository.save(
          Sprint.builder()
              .id(sprintId(number))
              .productId(PRODUCT_ID)
              .sprintNumber(number)
              .title("Sprint " + number)
              .status(number == 1 ? SprintStatus.ACTIVE : SprintStatus.PLANNED)
              .teamId(TEAM_ID)
              .teamName("スライドテストチーム")
              .startDate(LocalDate.of(2026, 11, 16).plusWeeks(2L * (number - 1)))
              .endDate(LocalDate.of(2026, 11, 27).plusWeeks(2L * (number - 1)))
              .build());
    }
    entityManager.flush();
    entityManager.clear();
  }

  private void saveItemAssignedTo(int sprintNumber) {
    backlogItemRepository.save(
        BacklogItem.builder()
            .id(ITEM_ID)
            .title("スライド対象アイテム")
            .creatorName("作成者")
            .scrumTeamId(TEAM_ID)
            .status(BacklogItemStatus.ESTIMATED)
            .storyPoint(5)
            .sprintId(sprintId(sprintNumber))
            .build());
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("スプリントがキャンセルされたとき、割り当て済みアイテムは次スプリントへスライドして保存される")
  void slidesAssignedItemToNextSprint() {
    saveItemAssignedTo(1);

    listener.onSprintCancelled(
        new SprintCancelled(sprintId(1), TEAM_ID, 1, LocalDateTime.of(2026, 11, 24, 10, 0)));
    entityManager.flush();
    entityManager.clear();

    assertThat(backlogItemRepository.findById(ITEM_ID).orElseThrow().getSprintId())
        .isEqualTo(sprintId(2));
  }

  @Test
  @DisplayName("次スプリントがないとき、割り当て済みアイテムの割り当ては解除されて保存される")
  void unassignsItemWhenThereIsNoNextSprint() {
    saveItemAssignedTo(2);

    listener.onSprintCancelled(
        new SprintCancelled(sprintId(2), TEAM_ID, 2, LocalDateTime.of(2026, 11, 24, 10, 0)));
    entityManager.flush();
    entityManager.clear();

    SnowflakeId assignedSprintId = backlogItemRepository.findById(ITEM_ID).orElseThrow().getSprintId();
    assertThat(assignedSprintId == null || assignedSprintId.isEmpty()).isTrue();
  }
}
