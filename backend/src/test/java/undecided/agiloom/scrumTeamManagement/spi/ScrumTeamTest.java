package undecided.agiloom.scrumTeamManagement.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ScrumTeamエンティティのテスト")
class ScrumTeamTest {

  private final UUID productId = UUID.randomUUID();
  private final UUID poEmployeeId = UUID.randomUUID();

  @Test
  @DisplayName("ビルダーでScrumTeamが作成される")
  void shouldCreateScrumTeamWithBuilder() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(1L))
            .productId(productId)
            .teamName("Frontend Team")
            .businessDomain("Web")
            .description("フロントエンド開発チーム")
            .poEmployeeId(poEmployeeId)
            .build();

    assertThat(team.getId().value()).isEqualTo(1L);
    assertThat(team.getProductId()).isEqualTo(productId);
    assertThat(team.getTeamName()).isEqualTo("Frontend Team");
    assertThat(team.getBusinessDomain()).isEqualTo("Web");
    assertThat(team.getDescription()).isEqualTo("フロントエンド開発チーム");
    assertThat(team.getPoEmployeeId()).isEqualTo(poEmployeeId);
    assertThat(team.getStatus()).isEqualTo(ScrumTeamStatus.ACTIVE);
    assertThat(team.getCreatedAt()).isNotNull();
    assertThat(team.getUpdatedAt()).isNotNull();
    assertThat(team.getDeletedAt()).isNull();
  }

  @Test
  @DisplayName("POなしでScrumTeamが作成される")
  void shouldCreateScrumTeamWithoutPo() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(2L))
            .productId(productId)
            .teamName("Backend Team")
            .businessDomain("API")
            .build();

    assertThat(team.getPoEmployeeId()).isNull();
    assertThat(team.getStatus()).isEqualTo(ScrumTeamStatus.ACTIVE);
  }

  @Test
  @DisplayName("updateメソッドでチーム名が更新される")
  void shouldUpdateTeamName() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(3L))
            .productId(productId)
            .teamName("Old Name")
            .businessDomain("Web")
            .build();

    LocalDateTime beforeUpdate = team.getUpdatedAt();
    team.update("New Name", null, null);

    assertThat(team.getTeamName()).isEqualTo("New Name");
    assertThat(team.getBusinessDomain()).isEqualTo("Web");
  }

  @Test
  @DisplayName("updateメソッドで事業領域が更新される")
  void shouldUpdateBusinessDomain() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(4L))
            .productId(productId)
            .teamName("Team A")
            .businessDomain("Old Domain")
            .build();

    team.update(null, "New Domain", null);

    assertThat(team.getTeamName()).isEqualTo("Team A");
    assertThat(team.getBusinessDomain()).isEqualTo("New Domain");
  }

  @Test
  @DisplayName("assignPoでPOが割り当てられる")
  void shouldAssignPo() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(5L))
            .productId(productId)
            .teamName("Team A")
            .businessDomain("Web")
            .build();

    assertThat(team.getPoEmployeeId()).isNull();
    team.assignPo(poEmployeeId);
    assertThat(team.getPoEmployeeId()).isEqualTo(poEmployeeId);
  }

  @Test
  @DisplayName("unassignPoでPOが解除される")
  void shouldUnassignPo() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(6L))
            .productId(productId)
            .teamName("Team A")
            .businessDomain("Web")
            .poEmployeeId(poEmployeeId)
            .build();

    assertThat(team.getPoEmployeeId()).isEqualTo(poEmployeeId);
    team.unassignPo();
    assertThat(team.getPoEmployeeId()).isNull();
  }

  @Test
  @DisplayName("archiveでステータスがARCHIVEDになる")
  void shouldArchive() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(7L))
            .productId(productId)
            .teamName("Team A")
            .businessDomain("Web")
            .build();

    assertThat(team.getStatus()).isEqualTo(ScrumTeamStatus.ACTIVE);
    team.archive();
    assertThat(team.getStatus()).isEqualTo(ScrumTeamStatus.ARCHIVED);
  }

  @Test
  @DisplayName("ARCHIVED状態ではarchiveできない")
  void shouldNotArchiveIfAlreadyArchived() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(8L))
            .productId(productId)
            .teamName("Team A")
            .businessDomain("Web")
            .status(ScrumTeamStatus.ARCHIVED)
            .build();

    assertThatThrownBy(team::archive)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("can only be archived from ACTIVE status");
  }

  @Test
  @DisplayName("deleteでdeleted_atが設定される")
  void shouldDelete() {
    ScrumTeam team =
        ScrumTeam.builder()
            .id(SnowflakeId.of(9L))
            .productId(productId)
            .teamName("Team A")
            .businessDomain("Web")
            .build();

    assertThat(team.getDeletedAt()).isNull();
    team.delete();
    assertThat(team.getDeletedAt()).isNotNull();
  }
}
