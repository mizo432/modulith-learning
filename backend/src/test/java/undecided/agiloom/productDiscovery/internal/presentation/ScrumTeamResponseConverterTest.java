package undecided.agiloom.productDiscovery.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ScrumTeamResponseConverterのテスト")
class ScrumTeamResponseConverterTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);

  private ScrumTeamResponseConverter converter = new ScrumTeamResponseConverter();

  private ScrumTeam scrumTeamWithMembers() {
    return ScrumTeam.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .businessDomain("Test Domain")
        .poEmployeeId(PO_ID)
        .poName("PO Name")
        .smEmployeeId(SM_ID)
        .smName("SM Name")
        .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
        .build();
  }

  private ScrumTeam scrumTeamWithoutMembers() {
    return ScrumTeam.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .businessDomain("Test Domain")
        .poEmployeeId(PO_ID)
        .poName("PO Name")
        .smEmployeeId(SM_ID)
        .smName("SM Name")
        .build();
  }

  @Nested
  @DisplayName("toScrumTeamResponseメソッドのテスト")
  class ToScrumTeamResponseTest {

    @Test
    @DisplayName("ScrumTeamResponseConverter.toScrumTeamResponse()を呼び出したとき、memberEmployeeIdsが変換結果に含まれる")
    void shouldIncludeMemberEmployeeIdsInResponse() {
      ScrumTeam scrumTeam = scrumTeamWithMembers();

      ScrumTeamResponse response = converter.toScrumTeamResponse(scrumTeam);

      assertThat(response.getMemberEmployeeIds())
          .containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }

    @Test
    @DisplayName("ScrumTeamのmemberEmployeeIdsが空リストのとき、レスポンスのmemberEmployeeIdsも空リストになる")
    void shouldHaveEmptyMemberEmployeeIdsWhenTeamHasNone() {
      ScrumTeam scrumTeam = scrumTeamWithoutMembers();

      ScrumTeamResponse response = converter.toScrumTeamResponse(scrumTeam);

      assertThat(response.getMemberEmployeeIds()).isEmpty();
    }
  }
}
