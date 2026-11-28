package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureBacklogItem;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("FeatureQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class FeatureQueryServiceTest {

  @Mock private FeatureRepository featureRepository;

  @Mock private FeatureBacklogItemRepository featureBacklogItemRepository;

  @Mock private BacklogItemRepository backlogItemRepository;

  private FeatureQueryService service;

  @BeforeEach
  void setUp() {
    service = new FeatureQueryService(
        featureRepository, featureBacklogItemRepository, backlogItemRepository);
  }

  private Feature createFeature() {
    return Feature.builder()
        .id(SnowflakeId.of(100L))
        .name("Test Feature")
        .status(FeatureStatus.PLANNED)
        .scrumTeamId(SnowflakeId.of(10L))
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private BacklogItem createBacklogItem(SnowflakeId id, BacklogItemStatus status, Integer storyPoint) {
    return BacklogItem.builder()
        .id(id)
        .title("Item " + id.value())
        .creatorName("Creator")
        .scrumTeamId(SnowflakeId.of(10L))
        .status(status)
        .storyPoint(storyPoint)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private FeatureBacklogItem createAssociation(SnowflakeId featureId, SnowflakeId backlogItemId) {
    return FeatureBacklogItem.builder()
        .featureId(featureId)
        .backlogItemId(backlogItemId)
        .createdAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("findByScrumTeamId メソッドのテスト")
  class FindByScrumTeamIdTest {

    @Test
    @DisplayName("チームのフィーチャー一覧を取得したとき、チームに属するフィーチャーが返されること")
    void shouldReturnFeaturesForTeam() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      Feature feature = createFeature();
      when(featureRepository.findByScrumTeamId(teamId)).thenReturn(List.of(feature));

      // Act
      List<Feature> result = service.findByScrumTeamId(teamId);

      // Assert
      assertThat(result).hasSize(1);
      assertThat(result.get(0).getId()).isEqualTo(feature.getId());
    }
  }

  @Nested
  @DisplayName("findById メソッドのテスト")
  class FindByIdTest {

    @Test
    @DisplayName("フィーチャーの詳細を取得したとき、紐付くバックログアイテム一覧と進捗情報が含まれること")
    void shouldReturnFeatureWithDetails() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      Feature feature = createFeature();
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));

      // Act
      Optional<Feature> result = service.findById(featureId);

      // Assert
      assertThat(result).isPresent();
      assertThat(result.get().getId()).isEqualTo(featureId);
    }

    @Test
    @DisplayName("存在しないフィーチャーの詳細を取得したとき、空が返されること")
    void shouldReturnEmptyWhenFeatureNotFound() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      when(featureRepository.findById(featureId)).thenReturn(Optional.empty());

      // Act
      Optional<Feature> result = service.findById(featureId);

      // Assert
      assertThat(result).isEmpty();
    }
  }

  @Nested
  @DisplayName("calculateProgress メソッドのテスト")
  class CalculateProgressTest {

    @Test
    @DisplayName("紐付けのバックログアイテムがないフィーチャーの進捗を取得したとき、進捗は0%になること")
    void shouldReturnZeroProgressWhenNoAssociations() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      when(featureBacklogItemRepository.findByFeatureId(featureId)).thenReturn(List.of());

      // Act
      FeatureQueryService.FeatureProgress progress = service.calculateProgress(featureId);

      // Assert
      assertThat(progress.progressPercentage()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("ストーリーポイントが0のバックログアイテムのみが紐付いているフィーチャーの進捗を取得したとき、進捗は0%になること")
    void shouldReturnZeroProgressWhenAllStoryPointsZero() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId backlogItemId = SnowflakeId.of(200L);
      FeatureBacklogItem association = createAssociation(featureId, backlogItemId);
      BacklogItem backlogItem = createBacklogItem(backlogItemId, BacklogItemStatus.READY, 0);

      when(featureBacklogItemRepository.findByFeatureId(featureId)).thenReturn(List.of(association));
      when(backlogItemRepository.findById(backlogItemId)).thenReturn(Optional.of(backlogItem));

      // Act
      FeatureQueryService.FeatureProgress progress = service.calculateProgress(featureId);

      // Assert
      assertThat(progress.progressPercentage()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("完了した（DONE）バックログアイテムのSPが総SPの半分であるフィーチャーの進捗を取得したとき、進捗は50%になること")
    void shouldReturn50PercentWhenHalfDone() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId doneItemId = SnowflakeId.of(200L);
      SnowflakeId readyItemId = SnowflakeId.of(201L);
      FeatureBacklogItem doneAssoc = createAssociation(featureId, doneItemId);
      FeatureBacklogItem readyAssoc = createAssociation(featureId, readyItemId);
      BacklogItem doneItem = createBacklogItem(doneItemId, BacklogItemStatus.DONE, 5);
      BacklogItem readyItem = createBacklogItem(readyItemId, BacklogItemStatus.READY, 5);

      when(featureBacklogItemRepository.findByFeatureId(featureId))
          .thenReturn(List.of(doneAssoc, readyAssoc));
      when(backlogItemRepository.findById(doneItemId)).thenReturn(Optional.of(doneItem));
      when(backlogItemRepository.findById(readyItemId)).thenReturn(Optional.of(readyItem));

      // Act
      FeatureQueryService.FeatureProgress progress = service.calculateProgress(featureId);

      // Assert
      assertThat(progress.completedStoryPoints()).isEqualTo(5);
      assertThat(progress.totalStoryPoints()).isEqualTo(10);
      assertThat(progress.progressPercentage()).isEqualTo(50.0);
    }

    @Test
    @DisplayName("全バックログアイテムが完了（DONE）しているフィーチャーの進捗を取得したとき、進捗は100%になること")
    void shouldReturn100PercentWhenAllDone() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId itemId1 = SnowflakeId.of(200L);
      SnowflakeId itemId2 = SnowflakeId.of(201L);
      FeatureBacklogItem assoc1 = createAssociation(featureId, itemId1);
      FeatureBacklogItem assoc2 = createAssociation(featureId, itemId2);
      BacklogItem item1 = createBacklogItem(itemId1, BacklogItemStatus.DONE, 3);
      BacklogItem item2 = createBacklogItem(itemId2, BacklogItemStatus.DONE, 5);

      when(featureBacklogItemRepository.findByFeatureId(featureId))
          .thenReturn(List.of(assoc1, assoc2));
      when(backlogItemRepository.findById(itemId1)).thenReturn(Optional.of(item1));
      when(backlogItemRepository.findById(itemId2)).thenReturn(Optional.of(item2));

      // Act
      FeatureQueryService.FeatureProgress progress = service.calculateProgress(featureId);

      // Assert
      assertThat(progress.completedStoryPoints()).isEqualTo(8);
      assertThat(progress.totalStoryPoints()).isEqualTo(8);
      assertThat(progress.progressPercentage()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("ストーリーポイントがnullのバックログアイテムが混在するフィーチャーの進捗を取得したとき、nullは0として扱われること")
    void shouldTreatNullStoryPointAsZero() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId nullSpItemId = SnowflakeId.of(200L);
      SnowflakeId doneItemId = SnowflakeId.of(201L);
      FeatureBacklogItem nullSpAssoc = createAssociation(featureId, nullSpItemId);
      FeatureBacklogItem doneAssoc = createAssociation(featureId, doneItemId);
      BacklogItem nullSpItem = createBacklogItem(nullSpItemId, BacklogItemStatus.READY, null);
      BacklogItem doneItem = createBacklogItem(doneItemId, BacklogItemStatus.DONE, 5);

      when(featureBacklogItemRepository.findByFeatureId(featureId))
          .thenReturn(List.of(nullSpAssoc, doneAssoc));
      when(backlogItemRepository.findById(nullSpItemId)).thenReturn(Optional.of(nullSpItem));
      when(backlogItemRepository.findById(doneItemId)).thenReturn(Optional.of(doneItem));

      // Act
      FeatureQueryService.FeatureProgress progress = service.calculateProgress(featureId);

      // Assert
      assertThat(progress.completedStoryPoints()).isEqualTo(5);
      assertThat(progress.totalStoryPoints()).isEqualTo(5);
      assertThat(progress.progressPercentage()).isEqualTo(100.0);
    }
  }
}
