package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ScrumTeamSubsetのチームメンバー判定テスト")
class ScrumTeamSubsetMemberTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(999L);

  private ScrumTeamSubset teamWithMembers() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .poEmployeeId(PO_ID)
        .poName("PO Name")
        .smEmployeeId(SM_ID)
        .smName("SM Name")
        .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
        .build();
  }

  private ScrumTeamSubset teamWithPoSmOnly() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .poEmployeeId(PO_ID)
        .poName("PO Name")
        .smEmployeeId(SM_ID)
        .smName("SM Name")
        .memberEmployeeIds(List.of())
        .build();
  }

  @Nested
  @DisplayName("isTeamMemberメソッドのテスト")
  class IsTeamMemberTest {

    @Test
    @DisplayName("isTeamMember()がPOの従業員IDに対してtrueを返すこと")
    void shouldReturnTrueForPo() {
      ScrumTeamSubset subset = teamWithMembers();

      assertThat(subset.isTeamMember(PO_ID)).isTrue();
    }

    @Test
    @DisplayName("isTeamMember()がSMの従業員IDに対してtrueを返すこと")
    void shouldReturnTrueForSm() {
      ScrumTeamSubset subset = teamWithMembers();

      assertThat(subset.isTeamMember(SM_ID)).isTrue();
    }

    @Test
    @DisplayName("isTeamMember()がメンバーリストに含まれる従業員IDに対してtrueを返すこと")
    void shouldReturnTrueForTeamMember() {
      ScrumTeamSubset subset = teamWithMembers();

      assertThat(subset.isTeamMember(MEMBER_ID)).isTrue();
    }

    @Test
    @DisplayName("isTeamMember()がメンバーリストに含まれない従業員IDに対してfalseを返すこと")
    void shouldReturnFalseForOutsider() {
      ScrumTeamSubset subset = teamWithMembers();

      assertThat(subset.isTeamMember(OUTSIDER_ID)).isFalse();
    }

    @Test
    @DisplayName("isTeamMember()がnullの従業員IDに対してfalseを返すこと")
    void shouldReturnFalseForNull() {
      ScrumTeamSubset subset = teamWithMembers();

      assertThat(subset.isTeamMember(null)).isFalse();
    }

    @Test
    @DisplayName("memberEmployeeIdsが空リストの場合、PO/SMのみがisTeamMemberでtrueとなること")
    void shouldReturnTrueOnlyForPoSmWhenMemberListIsEmpty() {
      ScrumTeamSubset subset = teamWithPoSmOnly();

      assertThat(subset.isTeamMember(PO_ID)).isTrue();
      assertThat(subset.isTeamMember(SM_ID)).isTrue();
      assertThat(subset.isTeamMember(MEMBER_ID)).isFalse();
    }
  }

  @Nested
  @DisplayName("applyメソッドのメンバーIDリスト反映テスト")
  class ApplyMemberIdsTest {

    @Test
    @DisplayName("apply()でScrumTeamChangedイベントのメンバーIDリストが反映されること")
    void shouldApplyMemberEmployeeIdsFromEvent() {
      ScrumTeamSubset subset = ScrumTeamSubset.builder()
          .id(TEAM_ID)
          .name("Old Team")
          .poEmployeeId(PO_ID)
          .poName("PO Name")
          .smEmployeeId(SM_ID)
          .smName("SM Name")
          .build();

      ScrumTeamChanged event = new ScrumTeamChanged(
          TEAM_ID,
          SnowflakeId.of(1L),
          "New Team",
          "Business Domain",
          PO_ID,
          "PO Name",
          SM_ID,
          "SM Name",
          List.of(PO_ID, SM_ID, MEMBER_ID),
          List.of(),
          false);

      subset.apply(event);

      assertThat(subset.getMemberEmployeeIds()).containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }
  }

  @Nested
  @DisplayName("fromメソッドのメンバーIDリストセットテスト")
  class FromMemberIdsTest {

    @Test
    @DisplayName("from()でScrumTeamChangedイベントのメンバーIDリストがセットされること")
    void shouldSetMemberEmployeeIdsFromEvent() {
      ScrumTeamChanged event = new ScrumTeamChanged(
          TEAM_ID,
          SnowflakeId.of(1L),
          "Test Team",
          "Business Domain",
          PO_ID,
          "PO Name",
          SM_ID,
          "SM Name",
          List.of(PO_ID, SM_ID, MEMBER_ID),
          List.of(),
          false);

      ScrumTeamSubset subset = ScrumTeamSubset.from(event);

      assertThat(subset.getMemberEmployeeIds()).containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }
  }
}
