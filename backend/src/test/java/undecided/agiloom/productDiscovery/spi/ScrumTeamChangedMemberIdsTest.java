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
@DisplayName("ScrumTeamChangedイベントのmemberEmployeeIdsテスト")
class ScrumTeamChangedMemberIdsTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(1L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);

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
  @DisplayName("memberEmployeeIdsフィールドのテスト")
  class MemberIdsFieldTest {

    @Test
    @DisplayName("ScrumTeamChangedにmemberEmployeeIdsが含まれること")
    void shouldContainMemberEmployeeIds() {
      List<SnowflakeId> memberIds = List.of(PO_ID, SM_ID, MEMBER_ID);

      ScrumTeamChanged event = new ScrumTeamChanged(
          TEAM_ID,
          PRODUCT_ID,
          "Test Team",
          "Test Domain",
          PO_ID,
          "PO Name",
          SM_ID,
          "SM Name",
          memberIds,
          List.of(),
          false);

      assertThat(event.memberEmployeeIds()).containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }
  }

  @Nested
  @DisplayName("of(ScrumTeam)メソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("of(ScrumTeam)でScrumTeamのメンバーIDリストが正しくセットされること")
    void shouldSetMemberIdsFromScrumTeam() {
      ScrumTeam team = scrumTeam();
      team.setMemberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID), LocalDateTime.now());

      ScrumTeamChanged event = ScrumTeamChanged.of(team);

      assertThat(event.memberEmployeeIds()).containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }
  }

  @Nested
  @DisplayName("removed(ScrumTeam)メソッドのテスト")
  class RemovedMethodTest {

    @Test
    @DisplayName("removed(ScrumTeam)でもメンバーIDリストが含まれること")
    void shouldIncludeMemberIdsInRemovedEvent() {
      ScrumTeam team = scrumTeam();
      team.setMemberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID), LocalDateTime.now());

      ScrumTeamChanged event = ScrumTeamChanged.removed(team);

      assertThat(event.removed()).isTrue();
      assertThat(event.memberEmployeeIds()).containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }
  }

  @Nested
  @DisplayName("union処理後の状態をカバーするテスト")
  class UnionAfterUpdateTest {

    @Test
    @DisplayName("ScrumTeamChanged.of()でmemberEmployeeIdsにPO/SMが含まれるScrumTeamを渡したとき、イベントのmemberEmployeeIdsにPO/SMが含まれる")
    void shouldIncludePoAndSmInEventAfterUnion() {
      ScrumTeam team = scrumTeam();
      // union処理後、memberEmployeeIdsにPO/SMと他のメンバーが含まれる状態
      team.setMemberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID), LocalDateTime.now());

      ScrumTeamChanged event = ScrumTeamChanged.of(team);

      assertThat(event.memberEmployeeIds()).containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }
  }
}
