package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
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
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest(classes = ModulithDemoApplication.class)
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
@DisplayName("フィーチャーのDB連携")
class FeatureMediumTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(538010L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(538011L);
  private static final SnowflakeId NON_MEMBER_ID = SnowflakeId.of(538012L);
  private static final SnowflakeId DONE_ITEM_ID = SnowflakeId.of(538201L);
  private static final SnowflakeId OPEN_ITEM_ID = SnowflakeId.of(538202L);

  @Autowired private CreateFeatureCommandService createFeatureCommandService;
  @Autowired private UpdateFeatureCommandService updateFeatureCommandService;
  @Autowired private DeleteFeatureCommandService deleteFeatureCommandService;
  @Autowired private AssignBacklogItemToFeatureCommandService assignService;
  @Autowired private FeatureQueryService featureQueryService;
  @Autowired private FeatureRepository featureRepository;
  @Autowired private FeatureBacklogItemRepository featureBacklogItemRepository;
  @Autowired private BacklogItemRepository backlogItemRepository;
  @Autowired private ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  @Autowired private EntityManager entityManager;

  @BeforeEach
  void setUp() {
    scrumTeamSubsetRepository.save(
        ScrumTeamSubset.builder()
            .id(TEAM_ID)
            .name("フィーチャーテストチーム")
            .memberEmployeeIds(List.of(MEMBER_ID))
            .updatedAt(LocalDateTime.now())
            .build());
    backlogItemRepository.save(
        BacklogItem.builder()
            .id(DONE_ITEM_ID)
            .title("完了アイテム")
            .creatorName("作成者")
            .scrumTeamId(TEAM_ID)
            .status(BacklogItemStatus.DONE)
            .storyPoint(5)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    backlogItemRepository.save(
        BacklogItem.builder()
            .id(OPEN_ITEM_ID)
            .title("未完了アイテム")
            .creatorName("作成者")
            .scrumTeamId(TEAM_ID)
            .status(BacklogItemStatus.ESTIMATED)
            .storyPoint(5)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build());
    entityManager.flush();
    entityManager.clear();
  }

  private SnowflakeId createFeature(String name) {
    SnowflakeId featureId =
        createFeatureCommandService.execute(
            CreateFeatureCommand.builder()
                .scrumTeamId(TEAM_ID)
                .name(name)
                .description("説明")
                .actorEmployeeId(MEMBER_ID)
                .build());
    entityManager.flush();
    entityManager.clear();
    return featureId;
  }

  private void assign(SnowflakeId featureId, SnowflakeId backlogItemId) {
    assignService.execute(
        AssignBacklogItemToFeatureCommand.builder()
            .featureId(featureId)
            .backlogItemId(backlogItemId)
            .scrumTeamId(TEAM_ID)
            .actorEmployeeId(MEMBER_ID)
            .build());
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("フィーチャーを作成したとき、DBに保存され状態はPLANNEDとなる")
  void createsFeatureInPlannedStatus() {
    SnowflakeId featureId = createFeature("フィーチャー1");

    assertThat(featureRepository.findById(featureId).orElseThrow().getStatus())
        .isEqualTo(FeatureStatus.PLANNED);
    assertThat(featureQueryService.findByScrumTeamId(TEAM_ID))
        .extracting(f -> f.getId())
        .contains(featureId);
  }

  @Test
  @DisplayName("バックログアイテムをフィーチャーに割り当てたとき、関連がDBに保存される")
  void persistsAssignment() {
    SnowflakeId featureId = createFeature("フィーチャー1");

    assign(featureId, DONE_ITEM_ID);

    assertThat(featureBacklogItemRepository.findByFeatureId(featureId))
        .extracting(a -> a.getBacklogItemId())
        .containsExactly(DONE_ITEM_ID);
    assertThat(featureQueryService.findBacklogItemIds(featureId)).containsExactly(DONE_ITEM_ID);
  }

  @Test
  @DisplayName("別のフィーチャーに割り当て済みのバックログアイテムを割り当てたとき、例外となり関連は増えない")
  void rejectsAssignmentToAnotherFeature() {
    SnowflakeId featureId = createFeature("フィーチャー1");
    SnowflakeId otherFeatureId = createFeature("フィーチャー2");
    assign(featureId, DONE_ITEM_ID);

    assertThatThrownBy(() -> assign(otherFeatureId, DONE_ITEM_ID))
        .isInstanceOf(BacklogItemAlreadyAssignedToFeatureException.class);

    assertThat(featureBacklogItemRepository.findByFeatureId(otherFeatureId)).isEmpty();
    assertThat(featureBacklogItemRepository.findByFeatureId(featureId)).hasSize(1);
  }

  @Test
  @DisplayName("DONEのバックログアイテムのSPが総SPの半分のとき、進捗は50%となる")
  void calculatesProgressFromPersistedItems() {
    SnowflakeId featureId = createFeature("フィーチャー1");
    assign(featureId, DONE_ITEM_ID);
    assign(featureId, OPEN_ITEM_ID);

    FeatureQueryService.FeatureProgress progress = featureQueryService.calculateProgress(featureId);

    assertThat(progress.completedStoryPoints()).isEqualTo(5);
    assertThat(progress.totalStoryPoints()).isEqualTo(10);
    assertThat(progress.progressPercentage()).isEqualTo(50.0);
  }

  @Test
  @DisplayName("フィーチャーを削除したとき、関連は削除されバックログアイテムは残る")
  void deletesOnlyAssociationsOnDelete() {
    SnowflakeId featureId = createFeature("フィーチャー1");
    assign(featureId, DONE_ITEM_ID);

    deleteFeatureCommandService.execute(
        DeleteFeatureCommand.builder()
            .featureId(featureId)
            .scrumTeamId(TEAM_ID)
            .actorEmployeeId(MEMBER_ID)
            .build());
    entityManager.flush();
    entityManager.clear();

    assertThat(featureRepository.findById(featureId)).isEmpty();
    assertThat(featureBacklogItemRepository.findByFeatureId(featureId)).isEmpty();
    assertThat(backlogItemRepository.findById(DONE_ITEM_ID)).isPresent();
  }

  @Test
  @DisplayName("PLANNEDからDONEへの直接遷移をしたとき、例外となり状態は変わらない")
  void rejectsDirectTransitionFromPlannedToDone() {
    SnowflakeId featureId = createFeature("フィーチャー1");

    assertThatThrownBy(
            () ->
                updateFeatureCommandService.execute(
                    UpdateFeatureCommand.builder()
                        .featureId(featureId)
                        .scrumTeamId(TEAM_ID)
                        .status(FeatureStatus.DONE)
                        .actorEmployeeId(MEMBER_ID)
                        .build()))
        .isInstanceOf(IllegalStateException.class);
    entityManager.flush();
    entityManager.clear();

    assertThat(featureRepository.findById(featureId).orElseThrow().getStatus())
        .isEqualTo(FeatureStatus.PLANNED);
  }

  @Test
  @DisplayName("チームメンバーでないユーザーがフィーチャーを作成したとき、例外となり保存されない")
  void rejectsCreateByNonMember() {
    assertThatThrownBy(
            () ->
                createFeatureCommandService.execute(
                    CreateFeatureCommand.builder()
                        .scrumTeamId(TEAM_ID)
                        .name("不正フィーチャー")
                        .actorEmployeeId(NON_MEMBER_ID)
                        .build()))
        .isInstanceOf(BacklogItemPermissionDeniedException.class);

    assertThat(featureQueryService.findByScrumTeamId(TEAM_ID)).isEmpty();
  }
}
