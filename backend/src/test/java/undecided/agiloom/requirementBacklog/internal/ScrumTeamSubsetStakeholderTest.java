package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ScrumTeamSubsetのステークホルダー判定テスト")
class ScrumTeamSubsetStakeholderTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);
  private static final SnowflakeId STAKEHOLDER_ID = SnowflakeId.of(900L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(999L);

  private ScrumTeamSubset subset() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Test Team")
        .poEmployeeId(PO_ID)
        .poName("PO Name")
        .smEmployeeId(SM_ID)
        .smName("SM Name")
        .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
        .stakeholderEmployeeIds(List.of(STAKEHOLDER_ID))
        .build();
  }

  private ScrumTeamChanged event(List<SnowflakeId> stakeholderIds) {
    return new ScrumTeamChanged(
        TEAM_ID,
        SnowflakeId.of(1L),
        "Test Team",
        "Business Domain",
        PO_ID,
        "PO Name",
        SM_ID,
        "SM Name",
        List.of(PO_ID, SM_ID, MEMBER_ID),
        stakeholderIds,
        false);
  }

  @Nested
  @DisplayName("isStakeholderのテスト")
  class IsStakeholderTest {

    @Test
    @DisplayName("ステークホルダーリストに含まれる従業員のとき、trueとなる")
    void shouldReturnTrueForStakeholder() {
      assertThat(subset().isStakeholder(STAKEHOLDER_ID)).isTrue();
    }

    @Test
    @DisplayName("チームメンバー（ステークホルダーではない）のとき、falseとなる")
    void shouldReturnFalseForTeamMember() {
      assertThat(subset().isStakeholder(MEMBER_ID)).isFalse();
    }

    @Test
    @DisplayName("チーム外の従業員のとき、falseとなる")
    void shouldReturnFalseForOutsider() {
      assertThat(subset().isStakeholder(OUTSIDER_ID)).isFalse();
    }

    @Test
    @DisplayName("nullのとき、falseとなる")
    void shouldReturnFalseForNull() {
      assertThat(subset().isStakeholder(null)).isFalse();
    }
  }

  @Nested
  @DisplayName("イベント反映のテスト")
  class EventApplyTest {

    @Test
    @DisplayName("apply()でイベントのステークホルダーリストが反映されること")
    void shouldApplyStakeholderIdsFromEvent() {
      ScrumTeamSubset subset = subset();

      subset.apply(event(List.of(SnowflakeId.of(950L))), LocalDateTime.now());

      assertThat(subset.getStakeholderEmployeeIds()).containsExactly(SnowflakeId.of(950L));
      assertThat(subset.isStakeholder(SnowflakeId.of(950L))).isTrue();
      assertThat(subset.isStakeholder(STAKEHOLDER_ID)).isFalse();
    }

    @Test
    @DisplayName("from()でイベントのステークホルダーリストがセットされること")
    void shouldSetStakeholderIdsFromEvent() {
      ScrumTeamSubset subset = ScrumTeamSubset.from(event(List.of(STAKEHOLDER_ID)), LocalDateTime.now());

      assertThat(subset.getStakeholderEmployeeIds()).containsExactly(STAKEHOLDER_ID);
      assertThat(subset.isStakeholder(STAKEHOLDER_ID)).isTrue();
    }

    @Test
    @DisplayName("イベントのステークホルダーリストがnullのとき、空リストとして反映されること")
    void shouldApplyEmptyWhenEventStakeholdersNull() {
      ScrumTeamSubset subset = subset();

      subset.apply(event(null), LocalDateTime.now());

      assertThat(subset.getStakeholderEmployeeIds()).isEmpty();
    }
  }
}
