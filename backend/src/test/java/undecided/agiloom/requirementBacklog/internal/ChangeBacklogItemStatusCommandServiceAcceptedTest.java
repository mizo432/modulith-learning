package undecided.agiloom.requirementBacklog.internal;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatusChanged;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ChangeBacklogItemStatusCommandServiceのREADY→ACCEPTED逆遷移テスト")
@ExtendWith(MockitoExtension.class)
class ChangeBacklogItemStatusCommandServiceAcceptedTest {

  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(100L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(200L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(300L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(999L);

  @Mock private BacklogItemRepository backlogItemRepository;
  @Mock private ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  @Mock private ApplicationEventPublisher eventPublisher;

  private ChangeBacklogItemStatusCommandService service;

  @BeforeEach
  void setUp() {
    service =
        new ChangeBacklogItemStatusCommandService(
            backlogItemRepository, scrumTeamSubsetRepository, eventPublisher, Clock.systemDefaultZone());
  }

  private BacklogItem readyItemWithStoryPoint() {
    BacklogItem item =
        BacklogItem.builder()
            .id(ITEM_ID)
            .title("Test Item")
            .creatorName("Creator")
            .scrumTeamId(TEAM_ID)
            .value(50)
            .build();
    item.changeStatus(BacklogItemStatus.ACCEPTED);
    item.changeStatus(BacklogItemStatus.READY);
    item.setStoryPoint(5);
    return item;
  }

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

  private ChangeBacklogItemStatusCommand command(SnowflakeId actorId) {
    return ChangeBacklogItemStatusCommand.builder()
        .backlogItemId(ITEM_ID)
        .targetStatus(BacklogItemStatus.ACCEPTED)
        .actorEmployeeId(actorId)
        .build();
  }

  private void stubRepositories(BacklogItem item) {
    when(backlogItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
    when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(teamWithMembers()));
  }

  @Test
  @DisplayName("一般メンバーがReadyのアイテムをAcceptedへ戻したとき、状態がAcceptedになりストーリーポイントがクリアされ、BacklogItemStatusChangedイベント（READY→ACCEPTED）が発行される")
  void shouldMemberRevertReadyToAcceptedClearingStoryPointAndPublishingEvent() {
    BacklogItem item = readyItemWithStoryPoint();
    stubRepositories(item);

    service.execute(command(MEMBER_ID));

    assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    assertThat(item.getStoryPoint()).isNull();
    verify(backlogItemRepository).save(item);
    ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    BacklogItemStatusChanged event = (BacklogItemStatusChanged) eventCaptor.getValue();
    assertThat(event.fromStatus()).isEqualTo(BacklogItemStatus.READY);
    assertThat(event.toStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
  }

  @Test
  @DisplayName("POがReadyのアイテムをAcceptedへ戻したとき、遷移が成功する")
  void shouldPoRevertReadyToAccepted() {
    BacklogItem item = readyItemWithStoryPoint();
    stubRepositories(item);

    service.execute(command(PO_ID));

    assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    assertThat(item.getStoryPoint()).isNull();
    verify(backlogItemRepository).save(item);
  }

  @Test
  @DisplayName("SMがReadyのアイテムをAcceptedへ戻したとき、遷移が成功する")
  void shouldSmRevertReadyToAccepted() {
    BacklogItem item = readyItemWithStoryPoint();
    stubRepositories(item);

    service.execute(command(SM_ID));

    assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ACCEPTED);
    assertThat(item.getStoryPoint()).isNull();
    verify(backlogItemRepository).save(item);
  }

  @Test
  @DisplayName("チームメンバーでない従業員がReadyのアイテムをAcceptedへ戻そうとしたとき、BacklogItemPermissionDeniedExceptionとなり、状態とストーリーポイントは変わらない")
  void shouldDenyOutsiderRevertReadyToAccepted() {
    BacklogItem item = readyItemWithStoryPoint();
    stubRepositories(item);

    assertThatThrownBy(() -> service.execute(command(OUTSIDER_ID)))
        .isInstanceOf(BacklogItemPermissionDeniedException.class);
    assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.READY);
    assertThat(item.getStoryPoint()).isEqualTo(5);
    verify(backlogItemRepository, never()).save(any());
    verify(eventPublisher, never()).publishEvent(any());
  }
}
