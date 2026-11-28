package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemQueryServiceの割り当て可能スプリント取得のテスト")
class BacklogItemQueryServiceAssignableSprintsTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(2001L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(2999L);
  private static final SnowflakeId SPRINT_A_ID = SnowflakeId.of(3001L);
  private static final SnowflakeId SPRINT_B_ID = SnowflakeId.of(3002L);
  private static final SnowflakeId OLD_SPRINT_ID = SnowflakeId.of(3009L);

  private final BacklogItemRepository backlogItemRepository = mock(BacklogItemRepository.class);
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository =
      mock(ScrumTeamSubsetRepository.class);
  private final SprintFacade sprintFacade = mock(SprintFacade.class);
  private final BacklogItemQueryService service =
      new BacklogItemQueryService(
          backlogItemRepository, null, null, scrumTeamSubsetRepository, null, sprintFacade);

  private Sprint sprint(SnowflakeId id, int number, SprintStatus status) {
    return Sprint.builder()
        .id(id)
        .productId(SnowflakeId.of(500L))
        .sprintNumber(number)
        .title("Sprint " + number)
        .status(status)
        .teamId(TEAM_ID)
        .startDate(LocalDate.of(2026, 11, 16))
        .endDate(LocalDate.of(2026, 11, 27))
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private void givenItem(SnowflakeId assignedSprintId) {
    BacklogItem item =
        BacklogItem.builder()
            .id(ITEM_ID)
            .title("Item")
            .creatorName("Creator")
            .scrumTeamId(TEAM_ID)
            .status(BacklogItemStatus.ESTIMATED)
            .sprintId(assignedSprintId)
            .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
    when(scrumTeamSubsetRepository.findById(TEAM_ID))
        .thenReturn(
            Optional.of(
                ScrumTeamSubset.builder()
                    .id(TEAM_ID)
                    .name("Team")
                    .memberEmployeeIds(List.of(MEMBER_ID))
                    .updatedAt(LocalDateTime.now()).build()));
    when(sprintFacade.findAssignableSprints(TEAM_ID))
        .thenReturn(
            List.of(
                sprint(SPRINT_A_ID, 2, SprintStatus.ACTIVE),
                sprint(SPRINT_B_ID, 3, SprintStatus.PLANNED)));
  }

  @Test
  @DisplayName("チームメンバーのとき、窓内のスプリントが返る")
  void returnsWindowSprintsForMember() {
    givenItem(null);

    List<AssignableSprintResponse> result = service.findAssignableSprints(ITEM_ID, MEMBER_ID);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).sprintId()).isEqualTo("3001");
    assertThat(result.get(0).sprintNumber()).isEqualTo(2);
    assertThat(result.get(0).status()).isEqualTo("ACTIVE");
    assertThat(result.get(0).assigned()).isFalse();
    assertThat(result.get(1).assigned()).isFalse();
  }

  @Test
  @DisplayName("現在の割り当て先が窓内にあるとき、そのスプリントにassignedが付く")
  void marksAssignedSprintInWindow() {
    givenItem(SPRINT_B_ID);

    List<AssignableSprintResponse> result = service.findAssignableSprints(ITEM_ID, MEMBER_ID);

    assertThat(result).hasSize(2);
    assertThat(result.get(0).assigned()).isFalse();
    assertThat(result.get(1).assigned()).isTrue();
  }

  @Test
  @DisplayName("現在の割り当て先が窓の外にあるとき、そのスプリントも含めて返る")
  void includesAssignedSprintOutsideWindow() {
    givenItem(OLD_SPRINT_ID);
    when(sprintFacade.findById(OLD_SPRINT_ID))
        .thenReturn(Optional.of(sprint(OLD_SPRINT_ID, 1, SprintStatus.COMPLETED)));

    List<AssignableSprintResponse> result = service.findAssignableSprints(ITEM_ID, MEMBER_ID);

    assertThat(result).hasSize(3);
    assertThat(result.get(2).sprintId()).isEqualTo("3009");
    assertThat(result.get(2).assigned()).isTrue();
  }

  @Test
  @DisplayName("チームメンバーでないとき、権限例外となる")
  void rejectsNonMember() {
    givenItem(null);

    assertThatThrownBy(() -> service.findAssignableSprints(ITEM_ID, OUTSIDER_ID))
        .isInstanceOf(BacklogItemPermissionDeniedException.class);
  }
}
