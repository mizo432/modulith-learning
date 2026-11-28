package undecided.agiloom.productDiscovery.spi;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ScrumTeamChangedイベントのstakeholderEmployeeIdsテスト")
class ScrumTeamChangedStakeholderIdsTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(1L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId STAKEHOLDER_ID = SnowflakeId.of(900L);
  private static final SnowflakeId STAKEHOLDER2_ID = SnowflakeId.of(901L);

  private ScrumTeam scrumTeam() {
    return ScrumTeam.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .businessDomain("Test Domain")
        .poEmployeeId(PO_ID)
        .poName("PO Name")
        .smEmployeeId(SM_ID)
        .smName("SM Name")
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  @Nested
  @DisplayName("stakeholderEmployeeIdsフィールドのテスト")
  class StakeholderIdsFieldTest {

    @Test
    @DisplayName("ScrumTeamChangedにstakeholderEmployeeIdsが含まれること")
    void shouldContainStakeholderEmployeeIds() {
      ScrumTeamChanged event =
          new ScrumTeamChanged(
              TEAM_ID,
              PRODUCT_ID,
              "Test Team",
              "Test Domain",
              PO_ID,
              "PO Name",
              SM_ID,
              "SM Name",
              List.of(PO_ID, SM_ID),
              List.of(STAKEHOLDER_ID, STAKEHOLDER2_ID),
              false);

      assertThat(event.stakeholderEmployeeIds())
          .containsExactlyInAnyOrder(STAKEHOLDER_ID, STAKEHOLDER2_ID);
    }
  }

  @Nested
  @DisplayName("of(ScrumTeam)メソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("of(ScrumTeam)でScrumTeamのステークホルダーIDリストが正しくセットされること")
    void shouldSetStakeholderIdsFromScrumTeam() {
      ScrumTeam team = scrumTeam();
      team.setStakeholderEmployeeIds(List.of(STAKEHOLDER_ID), LocalDateTime.now());

      ScrumTeamChanged event = ScrumTeamChanged.of(team);

      assertThat(event.stakeholderEmployeeIds()).containsExactly(STAKEHOLDER_ID);
    }

    @Test
    @DisplayName("ステークホルダー未設定のとき、of(ScrumTeam)のstakeholderEmployeeIdsが空リストになること")
    void shouldBeEmptyWhenNoStakeholders() {
      ScrumTeamChanged event = ScrumTeamChanged.of(scrumTeam());

      assertThat(event.stakeholderEmployeeIds()).isEmpty();
    }
  }

  @Nested
  @DisplayName("removed(ScrumTeam)メソッドのテスト")
  class RemovedMethodTest {

    @Test
    @DisplayName("removed(ScrumTeam)でもステークホルダーIDリストが含まれること")
    void shouldIncludeStakeholderIdsInRemovedEvent() {
      ScrumTeam team = scrumTeam();
      team.setStakeholderEmployeeIds(List.of(STAKEHOLDER_ID, STAKEHOLDER2_ID), LocalDateTime.now());

      ScrumTeamChanged event = ScrumTeamChanged.removed(team);

      assertThat(event.removed()).isTrue();
      assertThat(event.stakeholderEmployeeIds())
          .containsExactlyInAnyOrder(STAKEHOLDER_ID, STAKEHOLDER2_ID);
    }
  }
}
