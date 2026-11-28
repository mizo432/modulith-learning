package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;
import java.time.Clock;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureBacklogItem;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("AssignBacklogItemToFeatureCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AssignBacklogItemToFeatureCommandServiceTest {

  @Mock private FeatureRepository featureRepository;

  @Mock private BacklogItemRepository backlogItemRepository;

  @Mock private FeatureBacklogItemRepository featureBacklogItemRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  private AssignBacklogItemToFeatureCommandService service;

  @BeforeEach
  void setUp() {
    service = new AssignBacklogItemToFeatureCommandService(
        featureRepository, backlogItemRepository, featureBacklogItemRepository, scrumTeamSubsetRepository, Clock.systemDefaultZone());
  }

  private Feature createFeature() {
    return Feature.builder()
        .id(SnowflakeId.of(100L))
        .name("Test Feature")
        .status(FeatureStatus.PLANNED)
        .scrumTeamId(SnowflakeId.of(10L))
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private BacklogItem createBacklogItem() {
    return BacklogItem.builder()
        .id(SnowflakeId.of(200L))
        .title("Test Item")
        .creatorName("Creator")
        .scrumTeamId(SnowflakeId.of(10L))
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("バックログアイテムをフィーチャーに割り当てたとき、関連が作成されること")
    void shouldCreateAssociation() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId backlogItemId = SnowflakeId.of(200L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      BacklogItem backlogItem = createBacklogItem();
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).updatedAt(LocalDateTime.now()).build();
      AssignBacklogItemToFeatureCommand command = AssignBacklogItemToFeatureCommand.builder()
          .featureId(featureId)
          .backlogItemId(backlogItemId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(backlogItemRepository.findById(backlogItemId)).thenReturn(Optional.of(backlogItem));
      when(featureBacklogItemRepository.findByBacklogItemId(backlogItemId)).thenReturn(Optional.empty());
      when(featureBacklogItemRepository.save(any(FeatureBacklogItem.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      verify(featureBacklogItemRepository).save(any(FeatureBacklogItem.class));
      ArgumentCaptor<FeatureBacklogItem> captor = ArgumentCaptor.forClass(FeatureBacklogItem.class);
      verify(featureBacklogItemRepository).save(captor.capture());
      assertThat(captor.getValue().getFeatureId()).isEqualTo(featureId);
      assertThat(captor.getValue().getBacklogItemId()).isEqualTo(backlogItemId);
    }

    @Test
    @DisplayName("既存のバックログアイテムをフィーチャーに割り当てたとき、関連が作成されること（遡及割り当て）")
    void shouldAssignExistingBacklogItem() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId backlogItemId = SnowflakeId.of(200L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      BacklogItem backlogItem = BacklogItem.builder()
          .id(backlogItemId)
          .title("Existing Item")
          .creatorName("Creator")
          .scrumTeamId(teamId)
          .status(BacklogItemStatus.ACCEPTED)
          .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).updatedAt(LocalDateTime.now()).build();
      AssignBacklogItemToFeatureCommand command = AssignBacklogItemToFeatureCommand.builder()
          .featureId(featureId)
          .backlogItemId(backlogItemId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(backlogItemRepository.findById(backlogItemId)).thenReturn(Optional.of(backlogItem));
      when(featureBacklogItemRepository.findByBacklogItemId(backlogItemId)).thenReturn(Optional.empty());
      when(featureBacklogItemRepository.save(any(FeatureBacklogItem.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      verify(featureBacklogItemRepository).save(any(FeatureBacklogItem.class));
    }

    @Test
    @DisplayName("存在しないフィーチャーに割り当てようとしたとき、例外がスローされること")
    void shouldThrowWhenFeatureNotFound() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).updatedAt(LocalDateTime.now()).build();
      AssignBacklogItemToFeatureCommand command = AssignBacklogItemToFeatureCommand.builder()
          .featureId(featureId)
          .backlogItemId(SnowflakeId.of(200L))
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();
      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureBacklogItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("存在しないバックログアイテムを割り当てようとしたとき、例外がスローされること")
    void shouldThrowWhenBacklogItemNotFound() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId backlogItemId = SnowflakeId.of(200L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).updatedAt(LocalDateTime.now()).build();
      AssignBacklogItemToFeatureCommand command = AssignBacklogItemToFeatureCommand.builder()
          .featureId(featureId)
          .backlogItemId(backlogItemId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();
      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(backlogItemRepository.findById(backlogItemId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureBacklogItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("同じフィーチャーへの再割り当ては冪等に成功すること")
    void shouldSucceedOnIdempotentReassign() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId backlogItemId = SnowflakeId.of(200L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      BacklogItem backlogItem = createBacklogItem();
      FeatureBacklogItem existing = FeatureBacklogItem.builder()
          .featureId(featureId)
          .backlogItemId(backlogItemId)
          .createdAt(LocalDateTime.now()).build();
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).updatedAt(LocalDateTime.now()).build();
      AssignBacklogItemToFeatureCommand command = AssignBacklogItemToFeatureCommand.builder()
          .featureId(featureId)
          .backlogItemId(backlogItemId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(backlogItemRepository.findById(backlogItemId)).thenReturn(Optional.of(backlogItem));
      when(featureBacklogItemRepository.findByBacklogItemId(backlogItemId))
          .thenReturn(Optional.of(existing));

      // Act
      service.execute(command);

      // Assert - 例外がスローされず、saveも呼ばれない（冪等）
      verify(featureBacklogItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("別のフィーチャーに割り当て済みのバックログアイテムを割り当てたとき、409エラーすること")
    void shouldThrowConflictWhenAssignedToDifferentFeature() {
      // Arrange
      SnowflakeId featureId = SnowflakeId.of(100L);
      SnowflakeId otherFeatureId = SnowflakeId.of(101L);
      SnowflakeId backlogItemId = SnowflakeId.of(200L);
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      Feature feature = createFeature();
      BacklogItem backlogItem = createBacklogItem();
      FeatureBacklogItem existing = FeatureBacklogItem.builder()
          .featureId(otherFeatureId)
          .backlogItemId(backlogItemId)
          .createdAt(LocalDateTime.now()).build();
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").memberEmployeeIds(List.of(actorId)).updatedAt(LocalDateTime.now()).build();
      AssignBacklogItemToFeatureCommand command = AssignBacklogItemToFeatureCommand.builder()
          .featureId(featureId)
          .backlogItemId(backlogItemId)
          .scrumTeamId(teamId)
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(featureRepository.findById(featureId)).thenReturn(Optional.of(feature));
      when(backlogItemRepository.findById(backlogItemId)).thenReturn(Optional.of(backlogItem));
      when(featureBacklogItemRepository.findByBacklogItemId(backlogItemId))
          .thenReturn(Optional.of(existing));

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureBacklogItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("チーム外のユーザーが割り当てを試みたとき、例外がスローされること")
    void shouldThrowWhenNotTeamMember() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      ScrumTeamSubset team = org.mockito.Mockito.mock(ScrumTeamSubset.class);
      AssignBacklogItemToFeatureCommand command = AssignBacklogItemToFeatureCommand.builder()
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
      verify(featureBacklogItemRepository, never()).save(any());
    }
  }
}
