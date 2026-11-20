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
@DisplayName("DeleteFeatureCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeleteFeatureCommandServiceTest {

  @Mock private FeatureRepository featureRepository;

  @Mock private FeatureBacklogItemRepository featureBacklogItemRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  private DeleteFeatureCommandService service;

  @BeforeEach
  void setUp() {
    service = new DeleteFeatureCommandService(
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

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("紐付けのないフィーチャーを削除したとき、フィーチャーが削除されること")
    void shouldDeleteFeatureWithoutAssociations() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).build();
      DeleteFeatureCommand command = DeleteFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));

      // Act
      service.execute(command);

      // Assert
      verify(featureRepository).delete(feature);
    }

    @Test
    @DisplayName("バックログアイテムが紐付いているフィーチャーを削除したとき、フィーチャーは削除されバックログアイテムは残ること")
    void shouldDeleteFeatureKeepBacklogItems() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).build();
      DeleteFeatureCommand command = DeleteFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));

      // Act
      service.execute(command);

      // Assert
      verify(featureBacklogItemRepository).deleteByFeatureId(featureId);
      verify(featureRepository).delete(feature);
    }

    @Test
    @DisplayName("存在しないフィーチャーを削除したとき、例外がスローされること")
    void shouldThrowWhenFeatureNotFound() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      DeleteFeatureCommand command = DeleteFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(SnowflakeId.of(10L))
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();
      when(featureRepository.findById(featureId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureRepository, never()).delete(any());
    }

    @Test
    @DisplayName("チーム外のユーザーがフィーチャーを削除したとき、例外がスローされること")
    void shouldThrowWhenNotTeamMember() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      ScrumTeamSubset team = org.mockito.Mockito.mock(ScrumTeamSubset.class);
      DeleteFeatureCommand command = DeleteFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(team.isTeamMember(actorId)).thenReturn(false);

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(featureRepository, never()).delete(any());
    }
  }
}
