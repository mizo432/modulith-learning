package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("バックログアイテム分割のDB連携")
class SplitBacklogItemMediumTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(519001L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(519002L);
  private static final SnowflakeId PARENT_ID = SnowflakeId.of(519003L);
  private static final SnowflakeId OTHER_PARENT_ID = SnowflakeId.of(519004L);

  @Autowired private SplitBacklogItemCommandService service;

  @Autowired private BacklogItemRepository backlogItemRepository;

  @Autowired private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  @Autowired private EntityManager entityManager;

  @BeforeEach
  void setUp() {
    scrumTeamSubsetRepository.save(
        ScrumTeamSubset.builder().id(TEAM_ID).name("分割テストチーム").poEmployeeId(PO_ID).updatedAt(LocalDateTime.now()).build());
    saveParent(PARENT_ID, "分割元");
    entityManager.flush();
    entityManager.clear();
  }

  @Nested
  @DisplayName("分割の永続化")
  class ExecuteTest {

    @Test
    @DisplayName("分割した子アイテムがそれぞれの属性とともにDBに保存されること")
    void shouldPersistSplitChildrenInDatabase() {
      service.execute(new SplitBacklogItemCommand(
          PARENT_ID,
          PO_ID,
          List.of(
              new SplitBacklogItemCommand.Child("子A", 5),
              new SplitBacklogItemCommand.Child("子B", null))));
      entityManager.flush();
      entityManager.clear();

      List<BacklogItem> rows = allChildren();
      assertThat(rows).hasSize(2);
      assertThat(rows).extracting(BacklogItem::getId).doesNotHaveDuplicates().doesNotContain(PARENT_ID);
      assertThat(rows)
          .extracting(BacklogItem::getTitle, BacklogItem::getStatus,
              BacklogItem::getScrumTeamId, BacklogItem::getStoryPoint)
          .containsExactlyInAnyOrder(
              tuple("子A", BacklogItemStatus.SUGGESTED, TEAM_ID, 5),
              tuple("子B", BacklogItemStatus.SUGGESTED, TEAM_ID, null));
      assertThat(rows).extracting(BacklogItem::getParentItemId).containsOnly(PARENT_ID);
      assertThat(rows).extracting(BacklogItem::getId).allMatch(id -> id != null && id.value() > 0);
    }

    @Test
    @DisplayName("分割後の元アイテムのDONE状態がDBに保存されること")
    void shouldPersistParentDoneStatusInDatabase() {
      assertThat(backlogItemRepository.findById(PARENT_ID).orElseThrow().getStatus())
          .isEqualTo(BacklogItemStatus.ESTIMATED);

      split(PARENT_ID, "子A", "子B");
      entityManager.flush();
      entityManager.clear();

      assertThat(backlogItemRepository.findById(PARENT_ID).orElseThrow().getStatus())
          .isEqualTo(BacklogItemStatus.DONE);
    }

    @Test
    @DisplayName("親IDによる検索は対象の子だけをDBから返すこと")
    void shouldFindOnlyChildrenOfSpecifiedParent() {
      saveParent(OTHER_PARENT_ID, "別の分割元");
      entityManager.flush();
      entityManager.clear();
      split(PARENT_ID, "子A", "子B");
      split(OTHER_PARENT_ID, "別の子A", "別の子B");
      entityManager.flush();
      entityManager.clear();

      List<BacklogItem> expectedRows = allChildren().stream()
          .filter(item -> PARENT_ID.equals(item.getParentItemId())).toList();
      List<BacklogItem> otherRows = allChildren().stream()
          .filter(item -> OTHER_PARENT_ID.equals(item.getParentItemId())).toList();
      assertThat(expectedRows).hasSize(2);
      assertThat(otherRows).hasSize(2);

      List<BacklogItem> children = backlogItemRepository.findByParentItemId(PARENT_ID);
      assertThat(children).extracting(BacklogItem::getId)
          .containsExactlyInAnyOrderElementsOf(expectedRows.stream().map(BacklogItem::getId).toList())
          .doesNotContainAnyElementsOf(otherRows.stream().map(BacklogItem::getId).toList());
      assertThat(children).extracting(BacklogItem::getTitle).containsExactlyInAnyOrder("子A", "子B");
      assertThat(children).extracting(BacklogItem::getParentItemId).containsOnly(PARENT_ID);
      assertThat(backlogItemRepository.findByParentItemId(SnowflakeId.of(519999L))).isEmpty();
    }
  }

  private void saveParent(SnowflakeId id, String title) {
    backlogItemRepository.save(
        BacklogItem.builder()
            .id(id)
            .title(title)
            .creatorName("作成者")
            .scrumTeamId(TEAM_ID)
            .status(BacklogItemStatus.ESTIMATED)
            .storyPoint(13)
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build());
  }

  private void split(SnowflakeId parentId, String firstTitle, String secondTitle) {
    service.execute(new SplitBacklogItemCommand(
        parentId,
        PO_ID,
        List.of(
            new SplitBacklogItemCommand.Child(firstTitle, null),
            new SplitBacklogItemCommand.Child(secondTitle, null))));
  }

  private List<BacklogItem> allChildren() {
    return backlogItemRepository.findByScrumTeamId(TEAM_ID).stream()
        .filter(item -> !item.getId().equals(PARENT_ID) && !item.getId().equals(OTHER_PARENT_ID))
        .toList();
  }
}