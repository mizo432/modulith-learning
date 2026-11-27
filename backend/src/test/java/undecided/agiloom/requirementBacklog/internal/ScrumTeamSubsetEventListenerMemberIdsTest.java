package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ScrumTeamSubsetEventListenerのmemberEmployeeIdsテスト")
@ExtendWith(MockitoExtension.class)
class ScrumTeamSubsetEventListenerMemberIdsTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PRODUCT_ID = SnowflakeId.of(1L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);

  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  private ScrumTeamSubsetEventListener listener;

  @BeforeEach
  void setUp() {
    listener = new ScrumTeamSubsetEventListener(scrumTeamSubsetRepository, Clock.systemDefaultZone());
  }

  private ScrumTeamChanged eventWithMembers() {
    return new ScrumTeamChanged(
        TEAM_ID,
        PRODUCT_ID,
        "Test Team",
        "Test Domain",
        PO_ID,
        "PO Name",
        SM_ID,
        "SM Name",
        List.of(PO_ID, SM_ID, MEMBER_ID),
        List.of(),
        false);
  }

  @Nested
  @DisplayName("既存チームの更新テスト")
  class UpdateExistingTeamTest {

    @Test
    @DisplayName("ScrumTeamChangedイベント受信時にmemberEmployeeIdsがサブセットに反映されること")
    void shouldReflectMemberEmployeeIdsOnEvent() {
      ScrumTeamSubset existing = ScrumTeamSubset.builder()
          .id(TEAM_ID)
          .name("Old Team")
          .poEmployeeId(PO_ID)
          .poName("PO Name")
          .smEmployeeId(SM_ID)
          .smName("SM Name")
          .build();
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(existing));

      listener.on(eventWithMembers());

      verify(scrumTeamSubsetRepository).save(any(ScrumTeamSubset.class));
      assertThat(existing.getMemberEmployeeIds())
          .containsExactlyInAnyOrder(PO_ID, SM_ID, MEMBER_ID);
    }
  }

  @Nested
  @DisplayName("新規チームの作成テスト")
  class CreateNewTeamTest {

    @Test
    @DisplayName("新規チームのイベント受信時にfrom()でmemberEmployeeIdsがセットされること")
    void shouldSetMemberEmployeeIdsOnNewTeam() {
      when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.empty());

      listener.on(eventWithMembers());

      verify(scrumTeamSubsetRepository).save(any(ScrumTeamSubset.class));
    }
  }
}
