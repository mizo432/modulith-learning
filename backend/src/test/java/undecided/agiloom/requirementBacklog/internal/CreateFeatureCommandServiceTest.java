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
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreateFeatureCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateFeatureCommandServiceTest {

  @Mock private FeatureRepository featureRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  @Mock private SnowflakeIdGenerator snowflakeIdGenerator;

  private CreateFeatureCommandService service;

  @BeforeEach
  void setUp() {
    service = new CreateFeatureCommandService(
        featureRepository, scrumTeamSubsetRepository, snowflakeIdGenerator);
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("有効な名前でフィーチャーを作成したとき、PLANNED状態で保存されること")
    void shouldCreateWithPlannedStatus() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      CreateFeatureCommand command = CreateFeatureCommand.builder()
          .scrumTeamId(teamId)
          .name("New Feature")
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(SnowflakeId.of(100L));
      when(featureRepository.save(any(Feature.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      SnowflakeId result = service.execute(command);

      // Assert
      ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);
      verify(featureRepository).save(captor.capture());
      Feature saved = captor.getValue();
      assertThat(saved.getStatus()).isEqualTo(FeatureStatus.PLANNED);
      assertThat(saved.getScrumTeamId()).isEqualTo(teamId);
      assertThat(saved.getName()).isEqualTo("New Feature");
    }

    @Test
    @DisplayName("名前が空でフィーチャーを作成したとき、例外がスローされること")
    void shouldThrowWhenNameIsEmpty() {
      // Arrange
      CreateFeatureCommand command = CreateFeatureCommand.builder()
          .scrumTeamId(SnowflakeId.of(10L))
          .name("")
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureRepository, never()).save(any());
    }

    @Test
    @DisplayName("名前が200文字でフィーチャーを作成したとき、正常に保存されること")
    void shouldCreateWhenNameIs200Characters() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      String name = "a".repeat(Feature.NAME_MAX_LENGTH);
      CreateFeatureCommand command = CreateFeatureCommand.builder()
          .scrumTeamId(teamId)
          .name(name)
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(SnowflakeId.of(100L));
      when(featureRepository.save(any(Feature.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);
      verify(featureRepository).save(captor.capture());
      assertThat(captor.getValue().getName().length()).isEqualTo(Feature.NAME_MAX_LENGTH);
    }

    @Test
    @DisplayName("名前が201文字でフィーチャーを作成したとき、例外がスローされること")
    void shouldThrowWhenNameExceeds200Characters() {
      // Arrange
      String name = "a".repeat(Feature.NAME_MAX_LENGTH + 1);
      CreateFeatureCommand command = CreateFeatureCommand.builder()
          .scrumTeamId(SnowflakeId.of(10L))
          .name(name)
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureRepository, never()).save(any());
    }

    @Test
    @DisplayName("存在しないチームでフィーチャーを作成したとき、例外がスローされること")
    void shouldThrowWhenTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      CreateFeatureCommand command = CreateFeatureCommand.builder()
          .scrumTeamId(teamId)
          .name("New Feature")
          .actorEmployeeId(SnowflakeId.of(999L))
          .build();
      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class);
      verify(featureRepository, never()).save(any());
    }

    @Test
    @DisplayName("チームメンバーがフィーチャーを作成したとき、正常に作成されること")
    void shouldCreateWhenTeamMember() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      CreateFeatureCommand command = CreateFeatureCommand.builder()
          .scrumTeamId(teamId)
          .name("New Feature")
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(SnowflakeId.of(100L));
      when(featureRepository.save(any(Feature.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      verify(featureRepository).save(any(Feature.class));
    }

    @Test
    @DisplayName("チーム外のユーザーがフィーチャーを作成したとき、例外がスローされること")
    void shouldThrowWhenNotTeamMember() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId actorId = SnowflakeId.of(999L);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").build();
      CreateFeatureCommand command = CreateFeatureCommand.builder()
          .scrumTeamId(teamId)
          .name("New Feature")
          .actorEmployeeId(actorId)
          .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(team.isTeamMember(actorId)).thenReturn(false);

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      verify(featureRepository, never()).save(any());
    }
  }
}
