package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureBacklogItem;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("RemoveBacklogItemFromFeatureCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RemoveBacklogItemFromFeatureCommandServiceTest {

  @Mock private FeatureRepository featureRepository;

  @Mock private FeatureBacklogItemRepository featureBacklogItemRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  private RemoveBacklogItemFromFeatureCommandService service;

  @BeforeEach
  void setUp() {
    service = new RemoveBacklogItemFromFeatureCommandService(
        featureRepository, featureBacklogItemRepository, scrumTeamSubsetRepository);
  }

  private Feature createFeature() {
    return Feature.builder()
        .id(SnowflakeId.of(100L))
        .name("Test Feature")
        .status(FeatureStatus.PLANNED)
        .scrumTeamId(SnowflakeId.of(10L))
        .build();
  }

  private FeatureBacklogItem createAssociation(SnowflakeId featureId, SnowflakeId backlogItemId) {
    return FeatureBacklogItem.builder()
        .featureId(featureId)
        .backlogItemId(backlogItemId)
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("バックログアイテムをフィーチャーから外したとき、関連が削除されること")
    void shouldRemoveAssociation() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId backlogItemId = SnowflakeId.of(200L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      FeatureBacklogItem association = createAssociation(featureId, backlogItemId);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).build();
      RemoveBacklogItemFromFeatureCommand command = RemoveBacklogItemFromFeatureCommand.builder()
          .featureId(featureId)
          .backlogItemId(backlogItemId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(featureBacklogItemRepository.findByBacklogItemId(backlogItemId))
          .thenReturn(Optional.of(association));

      // Act
      service.execute(command);

      // Assert
      verify(featureBacklogItemRepository).delete(association);
    }

    @Test
    @DisplayName("チーム外のユーザーが外しを試みたとき、例外がスローされること")
    void shouldThrowWhenNotTeamMember() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      ScrumTeamSubset team = org.mockito.Mockito.mock(ScrumTeamSubset.class);
      RemoveBacklogItemFromFeatureCommand command = RemoveBacklogItemFromFeatureCommand.builder()
          .featureId(SnowflakeId.of(100L))
          .backlogItemId(SnowflakeId.of(200L))
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(team.isTeamMember(actorId)).thenReturn(false);

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(featureBacklogItemRepository, never()).delete(any());
    }
  }
}
