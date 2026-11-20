package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("UpdateFeatureCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class UpdateFeatureCommandServiceTest {

  @Mock private FeatureRepository featureRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  private UpdateFeatureCommandService service;

  @BeforeEach
  void setUp() {
    service = new UpdateFeatureCommandService(featureRepository, scrumTeamSubsetRepository);
  }

  private Feature createFeature(FeatureStatus status) {
    return Feature.builder()
        .id(SnowflakeId.of(100L))
        .name("Original Name")
        .status(status)
        .scrumTeamId(SnowflakeId.of(10L))
        .build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("フィーチャーの名前を更新したとき、新しい名前で保存されること")
    void shouldUpdateName() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      Feature feature = createFeature(FeatureStatus.PLANNED);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      UpdateFeatureCommand command = UpdateFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .name("New Name")
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(featureRepository.save(any(Feature.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);
      verify(featureRepository).save(captor.capture());
      assertThat(captor.getValue().getName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("フィーチャーの説明を更新したとき、新しい説明で保存されること")
    void shouldUpdateDescription() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      Feature feature = createFeature(FeatureStatus.PLANNED);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      UpdateFeatureCommand command = UpdateFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .description("New Description")
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(featureRepository.save(any(Feature.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);
      verify(featureRepository).save(captor.capture());
      assertThat(captor.getValue().getDescription()).isEqualTo("New Description");
    }

    @Test
    @DisplayName("存在しないフィーチャーを更新したとき、例外がスローされること")
    void shouldThrowWhenFeatureNotFound() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      UpdateFeatureCommand command = UpdateFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(SnowflakeId.of(10L))
          .name("New Name")
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();
      when(featureRepository.findById(featureId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureRepository, never()).save(any());
    }

    @Test
    @DisplayName("チーム外のユーザーがフィーチャーを更新したとき、例外がスローされること")
    void shouldThrowWhenNotTeamMember() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature(FeatureStatus.PLANNED);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      UpdateFeatureCommand command = UpdateFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .name("New Name")
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(team.isTeamMember(actorId)).thenReturn(false);

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(featureRepository, never()).save(any());
    }

    @Test
    @DisplayName("ステータスをPLANNEDからIN_PROGRESSに変更したとき、新しいステータスが保存されること")
    void shouldChangeStatusFromPlannedToInProgress() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      Feature feature = createFeature(FeatureStatus.PLANNED);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      UpdateFeatureCommand command = UpdateFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .status(FeatureStatus.IN_PROGRESS)
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(featureRepository.save(any(Feature.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);
      verify(featureRepository).save(captor.capture());
      assertThat(captor.getValue().getStatus()).isEqualTo(FeatureStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("ステータスをIN_PROGRESSからDONEに変更したとき、新しいステータスが保存されること")
    void shouldChangeStatusFromInProgressToDone() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      Feature feature = createFeature(FeatureStatus.IN_PROGRESS);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      UpdateFeatureCommand command = UpdateFeatureCommand.builder()
          .featureId(featureId)
          .scrumTeamId(teamId)
          .status(FeatureStatus.DONE)
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(featureRepository.save(any(Feature.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);
      verify(featureRepository).save(captor.capture());
      assertThat(captor.getValue().getStatus()).isEqualTo(FeatureStatus.DONE);
    }
  }
}
