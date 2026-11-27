package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSprintAssignmentChanged;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("AssignBacklogItemSprintCommandServiceのテスト")
class AssignBacklogItemSprintCommandServiceTest {

  private static final ZoneId ZONE = ZoneId.of("Asia/Tokyo");
  private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 11, 27, 12, 0);
  private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_TIME.atZone(ZONE).toInstant(), ZONE);

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId OTHER_TEAM_ID = SnowflakeId.of(11L);
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(2001L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(2999L);
  private static final SnowflakeId SPRINT_A_ID = SnowflakeId.of(3001L);
  private static final SnowflakeId SPRINT_B_ID = SnowflakeId.of(3002L);
  private static final SnowflakeId SPRINT_ZERO_ID = SnowflakeId.of(3000L);

  private final BacklogItemRepository backlogItemRepository = mock(BacklogItemRepository.class);
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository =
      mock(ScrumTeamSubsetRepository.class);
  private final SprintFacade sprintFacade = mock(SprintFacade.class);
  private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
  private final AssignBacklogItemSprintCommandService service =
      new AssignBacklogItemSprintCommandService(
          backlogItemRepository, scrumTeamSubsetRepository, sprintFacade, eventPublisher, FIXED_CLOCK);

  private BacklogItem item(BacklogItemStatus status, SnowflakeId sprintId) {
    return BacklogItem.builder()
        .id(ITEM_ID)
        .title("Item")
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .status(status)
        .storyPoint(5)
        .sprintId(sprintId)
        .build();
  }

  private ScrumTeamSubset team() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Team")
        .memberEmployeeIds(List.of(MEMBER_ID))
        .build();
  }

  private Sprint sprint(SnowflakeId id, int number, SprintStatus status, SnowflakeId teamId) {
    return Sprint.builder()
        .id(id)
        .productId(SnowflakeId.of(500L))
        .sprintNumber(number)
        .title("Sprint " + number)
        .status(status)
        .teamId(teamId)
        .build();
  }

  private Sprint sprintA() {
    return sprint(SPRINT_A_ID, 2, SprintStatus.ACTIVE, TEAM_ID);
  }

  private Sprint sprintB() {
    return sprint(SPRINT_B_ID, 3, SprintStatus.PLANNED, TEAM_ID);
  }

  private void givenItem(BacklogItem item) {
    when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
    when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));
  }

  private BacklogItemSprintAssignmentChanged publishedEvent() {
    ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
    verify(eventPublisher).publishEvent(captor.capture());
    return (BacklogItemSprintAssignmentChanged) captor.getValue();
  }

  @Nested
  @DisplayName("assignメソッドのテスト")
  class AssignTest {

    @Test
    @DisplayName("チームメンバーがESTIMATEDのアイテムを窓内のスプリントに割り当てたとき、sprintIdが設定されイベントが発行される")
    void assignsEstimatedItemToSprintInWindow() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, null);
      givenItem(item);
      when(sprintFacade.findById(SPRINT_B_ID)).thenReturn(Optional.of(sprintB()));
      when(sprintFacade.findAssignableSprints(TEAM_ID)).thenReturn(List.of(sprintA(), sprintB()));

      service.assign(
          AssignBacklogItemSprintCommand.builder()
              .backlogItemId(ITEM_ID)
              .sprintId(SPRINT_B_ID)
              .actorEmployeeId(MEMBER_ID)
              .build());

      assertThat(item.getSprintId()).isEqualTo(SPRINT_B_ID);
      verify(backlogItemRepository).save(item);
      BacklogItemSprintAssignmentChanged event = publishedEvent();
      assertThat(event.backlogItemId()).isEqualTo(ITEM_ID);
      assertThat(event.previousSprintId()).isNull();
      assertThat(event.sprintId()).isEqualTo(SPRINT_B_ID);
      assertThat(event.actorEmployeeId()).isEqualTo(MEMBER_ID);
    }

    @Test
    @DisplayName("チームメンバーでない者が割り当てたとき、権限例外となりsprintIdは変わらない")
    void rejectsNonMember() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, null);
      givenItem(item);

      assertThatThrownBy(
              () ->
                  service.assign(
                      AssignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .sprintId(SPRINT_B_ID)
                          .actorEmployeeId(OUTSIDER_ID)
                          .build()))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getSprintId()).isNull();
      verify(backlogItemRepository, never()).save(item);
    }

    @Test
    @DisplayName("ESTIMATEDでないアイテムを割り当てたとき、状態例外となり割り当てられない")
    void rejectsNonEstimatedItem() {
      BacklogItem item = item(BacklogItemStatus.READY, null);
      givenItem(item);

      assertThatThrownBy(
              () ->
                  service.assign(
                      AssignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .sprintId(SPRINT_B_ID)
                          .actorEmployeeId(MEMBER_ID)
                          .build()))
          .isInstanceOf(IllegalStateException.class);
      assertThat(item.getSprintId()).isNull();
    }

    @Test
    @DisplayName("窓の外のスプリントに割り当てたとき、引数例外となり割り当てられない")
    void rejectsSprintOutsideWindow() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, null);
      givenItem(item);
      when(sprintFacade.findById(SPRINT_B_ID)).thenReturn(Optional.of(sprintB()));
      when(sprintFacade.findAssignableSprints(TEAM_ID)).thenReturn(List.of(sprintA()));

      assertThatThrownBy(
              () ->
                  service.assign(
                      AssignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .sprintId(SPRINT_B_ID)
                          .actorEmployeeId(MEMBER_ID)
                          .build()))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(item.getSprintId()).isNull();
    }

    @Test
    @DisplayName("他チームのスプリントに割り当てたとき、引数例外となり割り当てられない")
    void rejectsOtherTeamsSprint() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, null);
      givenItem(item);
      when(sprintFacade.findById(SPRINT_B_ID))
          .thenReturn(Optional.of(sprint(SPRINT_B_ID, 3, SprintStatus.PLANNED, OTHER_TEAM_ID)));
      when(sprintFacade.findAssignableSprints(TEAM_ID)).thenReturn(List.of(sprintA(), sprintB()));

      assertThatThrownBy(
              () ->
                  service.assign(
                      AssignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .sprintId(SPRINT_B_ID)
                          .actorEmployeeId(MEMBER_ID)
                          .build()))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(item.getSprintId()).isNull();
    }

    @Test
    @DisplayName("スプリント0に割り当てたとき、引数例外となり割り当てられない")
    void rejectsSprintZero() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, null);
      givenItem(item);
      Sprint zero =
          Sprint.builder()
              .id(SPRINT_ZERO_ID)
              .productId(SnowflakeId.of(500L))
              .sprintNumber(0)
              .title("Sprint 0")
              .status(SprintStatus.PLANNED)
              .teamId(TEAM_ID)
              .isSprintZero(true)
              .build();
      when(sprintFacade.findById(SPRINT_ZERO_ID)).thenReturn(Optional.of(zero));
      when(sprintFacade.findAssignableSprints(TEAM_ID)).thenReturn(List.of(sprintA(), sprintB()));

      assertThatThrownBy(
              () ->
                  service.assign(
                      AssignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .sprintId(SPRINT_ZERO_ID)
                          .actorEmployeeId(MEMBER_ID)
                          .build()))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(item.getSprintId()).isNull();
    }

    @Test
    @DisplayName("存在しないスプリントに割り当てたとき、引数例外となり割り当てられない")
    void rejectsUnknownSprint() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, null);
      givenItem(item);
      when(sprintFacade.findById(SPRINT_B_ID)).thenReturn(Optional.empty());

      assertThatThrownBy(
              () ->
                  service.assign(
                      AssignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .sprintId(SPRINT_B_ID)
                          .actorEmployeeId(MEMBER_ID)
                          .build()))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(item.getSprintId()).isNull();
    }

    @Test
    @DisplayName("割り当て済みアイテムを別のスプリントに変更したとき、sprintIdが新しいスプリントになりイベントに前スプリントが含まれる")
    void changesAssignmentToAnotherSprint() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, SPRINT_A_ID);
      givenItem(item);
      when(sprintFacade.findById(SPRINT_B_ID)).thenReturn(Optional.of(sprintB()));
      when(sprintFacade.findAssignableSprints(TEAM_ID)).thenReturn(List.of(sprintA(), sprintB()));

      service.assign(
          AssignBacklogItemSprintCommand.builder()
              .backlogItemId(ITEM_ID)
              .sprintId(SPRINT_B_ID)
              .actorEmployeeId(MEMBER_ID)
              .build());

      assertThat(item.getSprintId()).isEqualTo(SPRINT_B_ID);
      BacklogItemSprintAssignmentChanged event = publishedEvent();
      assertThat(event.previousSprintId()).isEqualTo(SPRINT_A_ID);
      assertThat(event.sprintId()).isEqualTo(SPRINT_B_ID);
    }

    @Test
    @DisplayName("同じスプリントに再割り当てしたとき、成功しイベントは発行されない")
    void reassigningSameSprintIsIdempotent() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, SPRINT_B_ID);
      givenItem(item);
      when(sprintFacade.findById(SPRINT_B_ID)).thenReturn(Optional.of(sprintB()));
      when(sprintFacade.findAssignableSprints(TEAM_ID)).thenReturn(List.of(sprintA(), sprintB()));

      service.assign(
          AssignBacklogItemSprintCommand.builder()
              .backlogItemId(ITEM_ID)
              .sprintId(SPRINT_B_ID)
              .actorEmployeeId(MEMBER_ID)
              .build());

      assertThat(item.getSprintId()).isEqualTo(SPRINT_B_ID);
      verify(eventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.any());
    }
  }

  @Nested
  @DisplayName("unassignメソッドのテスト")
  class UnassignTest {

    @Test
    @DisplayName("割り当てを解除したとき、sprintIdがnullになりイベントが発行される")
    void unassignsSprint() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, SPRINT_A_ID);
      givenItem(item);

      service.unassign(
          UnassignBacklogItemSprintCommand.builder()
              .backlogItemId(ITEM_ID)
              .actorEmployeeId(MEMBER_ID)
              .build());

      assertThat(item.getSprintId()).isNull();
      verify(backlogItemRepository).save(item);
      BacklogItemSprintAssignmentChanged event = publishedEvent();
      assertThat(event.previousSprintId()).isEqualTo(SPRINT_A_ID);
      assertThat(event.sprintId()).isNull();
    }

    @Test
    @DisplayName("割り当てがないアイテムを解除したとき、状態例外となる")
    void rejectsUnassignWithoutAssignment() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, null);
      givenItem(item);

      assertThatThrownBy(
              () ->
                  service.unassign(
                      UnassignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .actorEmployeeId(MEMBER_ID)
                          .build()))
          .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("チームメンバーでない者が解除したとき、権限例外となりsprintIdは変わらない")
    void rejectsUnassignByNonMember() {
      BacklogItem item = item(BacklogItemStatus.ESTIMATED, SPRINT_A_ID);
      givenItem(item);

      assertThatThrownBy(
              () ->
                  service.unassign(
                      UnassignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .actorEmployeeId(OUTSIDER_ID)
                          .build()))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getSprintId()).isEqualTo(SPRINT_A_ID);
    }

    @Test
    @DisplayName("ESTIMATEDでないアイテムを解除したとき、状態例外となる")
    void rejectsUnassignOfNonEstimatedItem() {
      BacklogItem item = item(BacklogItemStatus.READY, SPRINT_A_ID);
      givenItem(item);

      assertThatThrownBy(
              () ->
                  service.unassign(
                      UnassignBacklogItemSprintCommand.builder()
                          .backlogItemId(ITEM_ID)
                          .actorEmployeeId(MEMBER_ID)
                          .build()))
          .isInstanceOf(IllegalStateException.class);
      assertThat(item.getSprintId()).isEqualTo(SPRINT_A_ID);
    }
  }
}
