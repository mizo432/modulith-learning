package undecided.agiloom.scrumTeamManagement.internal.command;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.scrumTeamManagement.internal.ScrumTeamRepository;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeamStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateScrumTeamCommandServiceのテスト")
class UpdateScrumTeamCommandServiceTest {

  @Mock private ScrumTeamRepository scrumTeamRepository;

  @InjectMocks private UpdateScrumTeamCommandService service;

  private SnowflakeId teamId;
  private UUID productId;
  private ScrumTeam existingTeam;

  @BeforeEach
  void setUp() {
    teamId = SnowflakeId.of(1L);
    productId = UUID.randomUUID();
    existingTeam =
        ScrumTeam.builder()
            .id(teamId)
            .productId(productId)
            .teamName("Old Team")
            .businessDomain("Old Domain")
            .description("Old description")
            .status(ScrumTeamStatus.ACTIVE)
            .build();
  }

  @Nested
  @DisplayName("executeメソッドのテスト")
  class ExecuteMethodTest {

    @Test
    @DisplayName("スクラムチームが更新される")
    void shouldUpdateScrumTeam() {
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      UpdateScrumTeamCommand command =
          UpdateScrumTeamCommand.builder()
              .id(teamId)
              .teamName("New Team")
              .businessDomain("New Domain")
              .description("New description")
              .build();

      assertThatCode(() -> service.execute(command)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("存在しないチームは更新できない")
    void shouldFailWhenTeamNotFound() {
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      UpdateScrumTeamCommand command =
          UpdateScrumTeamCommand.builder().id(teamId).teamName("New Team").build();

      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }

  @Nested
  @DisplayName("assignPoメソッドのテスト")
  class AssignPoTest {

    @Test
    @DisplayName("POが割り当てられる")
    void shouldAssignPo() {
      UUID poEmployeeId = UUID.randomUUID();
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      service.assignPo(teamId, poEmployeeId);

      verify(scrumTeamRepository).findById(teamId);
    }
  }

  @Nested
  @DisplayName("unassignPoメソッドのテスト")
  class UnassignPoTest {

    @Test
    @DisplayName("POが解除される")
    void shouldUnassignPo() {
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      service.unassignPo(teamId);

      verify(scrumTeamRepository).findById(teamId);
    }
  }

  @Nested
  @DisplayName("archiveメソッドのテスト")
  class ArchiveTest {

    @Test
    @DisplayName("チームがアーカイブされる")
    void shouldArchive() {
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      service.archive(teamId);

      verify(scrumTeamRepository).findById(teamId);
    }
  }

  @Nested
  @DisplayName("deleteメソッドのテスト")
  class DeleteTest {

    @Test
    @DisplayName("チームが削除される")
    void shouldDelete() {
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      service.delete(teamId);

      verify(scrumTeamRepository).findById(teamId);
    }
  }
}
