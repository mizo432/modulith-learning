package undecided.agiloom.productDiscovery.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.productDiscovery.internal.ScrumTeamRepository;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("UpdateScrumTeamCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class UpdateScrumTeamCommandServiceTest {

  @Mock private ScrumTeamRepository scrumTeamRepository;

  private UpdateScrumTeamCommandService service;

  @BeforeEach
  void setUp() {
    service = new UpdateScrumTeamCommandService(scrumTeamRepository);
  }

  @Nested
  @DisplayName("execute メソッドのテスト")
  class ExecuteTest {

    @Test
    @DisplayName("ScrumTeamを更新すること")
    void shouldUpdateScrumTeam() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      ScrumTeam existingTeam =
          ScrumTeam.builder()
              .id(teamId)
              .name("Old Name")
              .businessDomain("Old Domain")
              .build();

      UpdateScrumTeamCommand command =
          UpdateScrumTeamCommand.builder()
              .id(teamId)
              .name("New Name")
              .businessDomain("New Domain")
              .build();

      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      // Act
      service.execute(command);

      // Assert
      assertThat(existingTeam.getName()).isEqualTo("New Name");
      assertThat(existingTeam.getBusinessDomain()).isEqualTo("New Domain");
    }

    @Test
    @DisplayName("ScrumTeamが見つからない場合、例外がスローされること")
    void shouldThrowExceptionWhenScrumTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      UpdateScrumTeamCommand command =
          UpdateScrumTeamCommand.builder().id(teamId).name("New Name").build();

      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.execute(command))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }

  @Nested
  @DisplayName("delete メソッドのテスト")
  class DeleteTest {

    @Test
    @DisplayName("ScrumTeamを削除すること")
    void shouldDeleteScrumTeam() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      ScrumTeam existingTeam =
          ScrumTeam.builder().id(teamId).name("Test Team").build();

      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      // Act
      service.delete(teamId);

      // Assert
      verify(scrumTeamRepository).delete(existingTeam);
    }

    @Test
    @DisplayName("ScrumTeamが見つからない場合、例外がスローされること")
    void shouldThrowExceptionWhenScrumTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.delete(teamId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }

  @Nested
  @DisplayName("assignPo メソッドのテスト")
  class AssignPoTest {

    @Test
    @DisplayName("POを割り当てること")
    void shouldAssignPo() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      SnowflakeId poEmployeeId = SnowflakeId.of(100L);
      String poName = "Test PO";
      ScrumTeam existingTeam =
          ScrumTeam.builder().id(teamId).name("Test Team").build();

      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      // Act
      service.assignPo(teamId, poEmployeeId, poName);

      // Assert
      assertThat(existingTeam.getPoEmployeeId()).isEqualTo(poEmployeeId);
      assertThat(existingTeam.getPoName()).isEqualTo(poName);
    }

    @Test
    @DisplayName("ScrumTeamが見つからない場合、例外がスローされること")
    void shouldThrowExceptionWhenScrumTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.assignPo(teamId, SnowflakeId.of(999L), "name"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }

  @Nested
  @DisplayName("unassignPo メソッドのテスト")
  class UnassignPoTest {

    @Test
    @DisplayName("POの割り当てを解除すること")
    void shouldUnassignPo() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      ScrumTeam existingTeam =
          ScrumTeam.builder()
              .id(teamId)
              .name("Test Team")
              .poEmployeeId(SnowflakeId.of(100L))
              .poName("Test PO")
              .build();

      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      // Act
      service.unassignPo(teamId);

      // Assert
      assertThat(existingTeam.getPoEmployeeId()).isNull();
      assertThat(existingTeam.getPoName()).isNull();
    }

    @Test
    @DisplayName("ScrumTeamが見つからない場合、例外がスローされること")
    void shouldThrowExceptionWhenScrumTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.unassignPo(teamId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }

  @Nested
  @DisplayName("assignSm メソッドのテスト")
  class AssignSmTest {

    @Test
    @DisplayName("SMを割り当てること")
    void shouldAssignSm() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      SnowflakeId smEmployeeId = SnowflakeId.of(200L);
      String smName = "Test SM";
      ScrumTeam existingTeam =
          ScrumTeam.builder().id(teamId).name("Test Team").build();

      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      // Act
      service.assignSm(teamId, smEmployeeId, smName);

      // Assert
      assertThat(existingTeam.getSmEmployeeId()).isEqualTo(smEmployeeId);
      assertThat(existingTeam.getSmName()).isEqualTo(smName);
    }

    @Test
    @DisplayName("ScrumTeamが見つからない場合、例外がスローされること")
    void shouldThrowExceptionWhenScrumTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.assignSm(teamId, SnowflakeId.of(999L), "name"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }

  @Nested
  @DisplayName("unassignSm メソッドのテスト")
  class UnassignSmTest {

    @Test
    @DisplayName("SMの割り当てを解除すること")
    void shouldUnassignSm() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      ScrumTeam existingTeam =
          ScrumTeam.builder()
              .id(teamId)
              .name("Test Team")
              .smEmployeeId(SnowflakeId.of(200L))
              .smName("Test SM")
              .build();

      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

      // Act
      service.unassignSm(teamId);

      // Assert
      assertThat(existingTeam.getSmEmployeeId()).isNull();
      assertThat(existingTeam.getSmName()).isNull();
    }

    @Test
    @DisplayName("ScrumTeamが見つからない場合、例外がスローされること")
    void shouldThrowExceptionWhenScrumTeamNotFound() {
      // Arrange
      SnowflakeId teamId = SnowflakeId.of(1L);
      when(scrumTeamRepository.findById(teamId)).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.unassignSm(teamId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ScrumTeam not found");
    }
  }
}
