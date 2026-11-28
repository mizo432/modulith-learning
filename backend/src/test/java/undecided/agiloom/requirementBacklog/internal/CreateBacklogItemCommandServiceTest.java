package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;
import java.time.LocalDateTime;

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
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.QualityCategory;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreateBacklogItemCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateBacklogItemCommandServiceTest {

  @Mock private BacklogItemRepository backlogItemRepository;

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  @Mock private SnowflakeIdGenerator snowflakeIdGenerator;

  private CreateBacklogItemCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new CreateBacklogItemCommandService(
            backlogItemRepository, scrumTeamSubsetRepository, snowflakeIdGenerator, Clock.systemDefaultZone());
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("バックログアイテムを作成して保存し、IDを返すこと。初期状態はSUGGESTED")
    void shouldCreateWithSuggestedStatus() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").updatedAt(LocalDateTime.now()).build();
      CreateBacklogItemCommand command =
          CreateBacklogItemCommand.builder()
              .scrumTeamId(teamId)
              .title("New Item")
              .creatorName("Creator")
              .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(SnowflakeId.of(100L));
      when(backlogItemRepository.save(any(BacklogItem.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      SnowflakeId result = service.execute(command);

      // Assert
      assertThat(result.value()).isEqualTo(100L);
      ArgumentCaptor<BacklogItem> captor = ArgumentCaptor.forClass(BacklogItem.class);
      verify(backlogItemRepository).save(captor.capture());
      assertThat(captor.getValue().getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
      assertThat(captor.getValue().getScrumTeamId()).isEqualTo(teamId);
      assertThat(captor.getValue().getQualityCategory()).isNull();
    }

    @Test
    @DisplayName("creatorEmployeeIdがSnowflakeIdで設定されること")
    void shouldSetCreatorEmployeeIdAsSnowflakeId() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      SnowflakeId employeeId = SnowflakeId.of(999L);
      ScrumTeamSubset team = ScrumTeamSubset.builder().id(teamId).name("Test Team").updatedAt(LocalDateTime.now()).build();
      CreateBacklogItemCommand command =
          CreateBacklogItemCommand.builder()
              .scrumTeamId(teamId)
              .title("New Item")
              .creatorName("Creator")
              .creatorEmployeeId(employeeId)
              .build();

      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.of(team));
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(SnowflakeId.of(100L));
      when(backlogItemRepository.save(any(BacklogItem.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // Act
      service.execute(command);

      // Assert
      ArgumentCaptor<BacklogItem> captor = ArgumentCaptor.forClass(BacklogItem.class);
      verify(backlogItemRepository).save(captor.capture());
      assertThat(captor.getValue().getCreatorEmployeeId()).isEqualTo(employeeId);
    }

    @Test
    @DisplayName("チームのサブセットが存在しない場合、例外がスローされること")
    void shouldThrowWhenTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(10L);
      CreateBacklogItemCommand command =
          CreateBacklogItemCommand.builder()
              .scrumTeamId(teamId)
              .title("New Item")
              .creatorName("Creator")
              .build();
      when(scrumTeamSubsetRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
      verify(backlogItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("タイトルが空の場合、例外がスローされること")
    void shouldThrowWhenTitleIsEmpty() {
      // Arrange
      CreateBacklogItemCommand command =
          CreateBacklogItemCommand.builder()
              .scrumTeamId(SnowflakeId.of(10L))
              .title("")
              .creatorName("Creator")
              .build();

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("must not be empty");
      verify(backlogItemRepository, never()).save(any());
    }
  }
}
