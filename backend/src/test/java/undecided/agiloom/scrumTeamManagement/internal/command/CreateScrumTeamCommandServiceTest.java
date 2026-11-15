package undecided.agiloom.scrumTeamManagement.internal.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateScrumTeamCommandServiceのテスト")
class CreateScrumTeamCommandServiceTest {

  @Mock private ScrumTeamRepository scrumTeamRepository;

  @InjectMocks private CreateScrumTeamCommandService service;

  private SnowflakeId teamId;
  private UUID productId;

  @BeforeEach
  void setUp() {
    teamId = SnowflakeId.of(1L);
    productId = UUID.randomUUID();
  }

  @Nested
  @DisplayName("executeメソッドのテスト")
  class ExecuteMethodTest {

    @Test
    @DisplayName("スクラムチームが作成されIDが返される")
    void shouldCreateScrumTeamAndReturnId() {
      CreateScrumTeamCommand command =
          CreateScrumTeamCommand.builder()
              .productId(productId)
              .teamName("Frontend Team")
              .businessDomain("Web")
              .description("フロントエンドチーム")
              .build();

      ScrumTeam saved =
          ScrumTeam.builder()
              .id(teamId)
              .productId(productId)
              .teamName("Frontend Team")
              .businessDomain("Web")
              .description("フロントエンドチーム")
              .build();
      when(scrumTeamRepository.save(any(ScrumTeam.class))).thenReturn(saved);

      SnowflakeId result = service.execute(command);

      assertThat(result).isEqualTo(teamId);
      verify(scrumTeamRepository).save(any(ScrumTeam.class));
    }

    @Test
    @DisplayName("PO付きでスクラムチームが作成される")
    void shouldCreateScrumTeamWithPo() {
      UUID poEmployeeId = UUID.randomUUID();
      CreateScrumTeamCommand command =
          CreateScrumTeamCommand.builder()
              .productId(productId)
              .teamName("Backend Team")
              .businessDomain("API")
              .poEmployeeId(poEmployeeId)
              .build();

      ScrumTeam saved =
          ScrumTeam.builder()
              .id(teamId)
              .productId(productId)
              .teamName("Backend Team")
              .businessDomain("API")
              .poEmployeeId(poEmployeeId)
              .build();
      when(scrumTeamRepository.save(any(ScrumTeam.class))).thenReturn(saved);

      SnowflakeId result = service.execute(command);

      assertThat(result).isEqualTo(teamId);
      verify(scrumTeamRepository).save(any(ScrumTeam.class));
    }
  }
}
