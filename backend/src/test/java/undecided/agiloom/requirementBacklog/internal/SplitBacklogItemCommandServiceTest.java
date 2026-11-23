package undecided.agiloom.requirementBacklog.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSplit;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatusChanged;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("バックログアイテム分割コマンド")
@ExtendWith(MockitoExtension.class)
class SplitBacklogItemCommandServiceTest {
  private static final SnowflakeId ITEM_ID = SnowflakeId.of(100L);
  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(101L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(102L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(103L);
  private static final SnowflakeId OUTSIDER_ID = SnowflakeId.of(104L);
  private static final SnowflakeId SPRINT_ID = SnowflakeId.of(105L);

  @Mock private BacklogItemRepository items;
  @Mock private ScrumTeamSubsetRepository teams;
  @Mock private SnowflakeIdGenerator ids;
  @Mock private ApplicationEventPublisher events;

  private final List<BacklogItem> saved = new ArrayList<>();
  private SplitBacklogItemCommandService service;

  @BeforeEach
  void setUp() {
    service = new SplitBacklogItemCommandService(items, teams, ids, events);
    ScrumTeamSubset team = ScrumTeamSubset.builder()
        .id(TEAM_ID).name("チーム").poEmployeeId(PO_ID).smEmployeeId(SM_ID)
        .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID)).build();
    lenient().when(teams.findById(TEAM_ID)).thenReturn(Optional.of(team));
    AtomicLong next = new AtomicLong(1000);
    lenient().when(ids.nextIdValue()).thenAnswer(invocation -> SnowflakeId.of(next.getAndIncrement()));
    lenient().when(items.save(any(BacklogItem.class))).thenAnswer(invocation -> {
      BacklogItem item = invocation.getArgument(0);
      saved.add(item);
      return item;
    });
    lenient().when(items.saveAll(any())).thenAnswer(invocation -> {
      Iterable<BacklogItem> batch = invocation.getArgument(0);
      batch.forEach(saved::add);
      return saved;
    });
  }

  private BacklogItem parent(Integer storyPoint) {
    BacklogItem item = BacklogItem.builder().id(ITEM_ID).scrumTeamId(TEAM_ID)
        .title("親タイトル").description("親の説明").creatorName("作成者")
        .priority(3).value(50).sprintId(SPRINT_ID).storyPoint(storyPoint).build();
    if (storyPoint != null) {
      item.changeStatus(BacklogItemStatus.ACCEPTED);
      item.changeStatus(BacklogItemStatus.READY);
      item.changeStatus(BacklogItemStatus.ESTIMATED);
    }
    when(items.findById(ITEM_ID)).thenReturn(Optional.of(item));
    return item;
  }

  private SplitBacklogItemCommand command(SnowflakeId actor, List<SplitBacklogItemCommand.Child> children) {
    return new SplitBacklogItemCommand(ITEM_ID, actor, children);
  }

  private List<SplitBacklogItemCommand.Child> children(int count) {
    return IntStream.range(0, count)
        .mapToObj(index -> new SplitBacklogItemCommand.Child("子" + index, null)).toList();
  }

  private List<BacklogItem> savedChildren() {
    return saved.stream().filter(item -> !ITEM_ID.equals(item.getId())).toList();
  }

  @Nested
  @DisplayName("権限")
  class Permission {
    @Test @DisplayName("1 POは分割できる")
    void shouldSplitByPo() {
      parent(13);
      service.execute(command(PO_ID, children(1)));
      assertThat(savedChildren()).hasSize(1);
    }

    @Test @DisplayName("2 SMは分割できる")
    void shouldSplitBySm() {
      parent(13);
      service.execute(command(SM_ID, children(1)));
      assertThat(savedChildren()).hasSize(1);
    }

    @Test @DisplayName("3 POでもSMでもないメンバーは分割できない")
    void shouldRejectMember() {
      BacklogItem item = parent(13);
      assertThatThrownBy(() -> service.execute(command(MEMBER_ID, children(1))))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
      verify(items, never()).save(any());
      verify(items, never()).saveAll(any());
      verify(events, never()).publishEvent(any(Object.class));
    }

    @Test @DisplayName("4 チーム外の従業員は分割できない")
    void shouldRejectOutsider() {
      parent(13);
      assertThatThrownBy(() -> service.execute(command(OUTSIDER_ID, children(1))))
          .isInstanceOf(BacklogItemPermissionDeniedException.class);
      assertThat(saved).isEmpty();
    }
  }

  @Nested
  @DisplayName("分割対象")
  class Eligibility {
    @Test @DisplayName("5 SP13は分割できる")
    void shouldSplitAtThirteen() {
      parent(13);
      service.execute(command(PO_ID, children(1)));
      assertThat(savedChildren()).hasSize(1);
    }

    @Test @DisplayName("6 SP21は分割できる")
    void shouldSplitAtTwentyOne() {
      parent(21);
      service.execute(command(PO_ID, children(1)));
      assertThat(savedChildren()).hasSize(1);
    }

    @Test @DisplayName("7 SP8は分割できない")
    void shouldRejectBelowThirteen() {
      parent(8);
      assertThatThrownBy(() -> service.execute(command(PO_ID, children(1))))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(saved).isEmpty();
    }

    @Test @DisplayName("8 SP未設定は分割できない")
    void shouldRejectMissingStoryPoint() {
      parent(null);
      assertThatThrownBy(() -> service.execute(command(PO_ID, children(1))))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(saved).isEmpty();
    }
  }

  @Nested
  @DisplayName("分割数")
  class Count {
    @Test @DisplayName("9 子1件を作成できる")
    void shouldCreateOneChild() {
      parent(13);
      service.execute(command(PO_ID, children(1)));
      assertThat(savedChildren()).hasSize(1);
    }

    @Test @DisplayName("10 子20件を作成できる")
    void shouldCreateTwentyChildren() {
      parent(13);
      service.execute(command(PO_ID, children(20)));
      assertThat(savedChildren()).hasSize(20);
      assertThat(savedChildren()).extracting(BacklogItem::getId).doesNotHaveDuplicates();
    }

    @Test @DisplayName("11 子21件は分割できない")
    void shouldRejectTwentyOneChildren() {
      parent(13);
      assertThatThrownBy(() -> service.execute(command(PO_ID, children(21))))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(saved).isEmpty();
    }

    @Test @DisplayName("12 子0件は分割できない")
    void shouldRejectZeroChildren() {
      parent(13);
      assertThatThrownBy(() -> service.execute(command(PO_ID, children(0))))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(saved).isEmpty();
    }
  }

  @Nested
  @DisplayName("子の属性")
  class ChildAttributes {
    private BacklogItem splitChild(String title, Integer storyPoint) {
      parent(13);
      service.execute(command(PO_ID, List.of(new SplitBacklogItemCommand.Child(title, storyPoint))));
      assertThat(savedChildren()).hasSize(1);
      return savedChildren().getFirst();
    }

    @Test @DisplayName("13 子はSUGGESTEDになる")
    void shouldCreateSuggestedChild() {
      assertThat(splitChild("子", null).getStatus()).isEqualTo(BacklogItemStatus.SUGGESTED);
    }

    @Test @DisplayName("14 子に親IDを設定する")
    void shouldSetParentId() {
      assertThat(splitChild("子", null).getParentItemId()).isEqualTo(ITEM_ID);
    }

    @Test @DisplayName("15 子に親と同じチームIDを設定する")
    void shouldSetTeamId() {
      assertThat(splitChild("子", null).getScrumTeamId()).isEqualTo(TEAM_ID);
    }

    @Test @DisplayName("16a 指定したSPを子に設定する")
    void shouldSetSpecifiedStoryPoint() {
      assertThat(splitChild("子", 5).getStoryPoint()).isEqualTo(5);
    }

    @Test @DisplayName("16b SP未指定の子はnullになる")
    void shouldLeaveUnspecifiedStoryPointNull() {
      assertThat(splitChild("子", null).getStoryPoint()).isNull();
    }

    @Test @DisplayName("17 子タイトルはリクエスト値になる")
    void shouldSetRequestedTitle() {
      assertThat(splitChild("指定タイトル", null).getTitle()).isEqualTo("指定タイトル");
    }

    @Test @DisplayName("タイトル未指定の子は親のタイトルを継承する")
    void shouldInheritTitleWhenNotSpecified() {
      assertThat(splitChild(null, null).getTitle()).isEqualTo("親タイトル");
    }

    @Test @DisplayName("子は親の説明を継承する")
    void shouldInheritDescription() {
      assertThat(splitChild("子", null).getDescription()).isEqualTo("親の説明");
    }

    @Test @DisplayName("子はValue、優先度、スプリントを継承しない")
    void shouldNotInheritValuePriorityOrSprint() {
      BacklogItem child = splitChild("子", null);
      assertThat(child.getValue()).isNull();
      assertThat(child.getPriority()).isNull();
      assertThat(child.getSprintId()).isNull();
    }

    @Test @DisplayName("子のSP合計が親と異なっていても分割できる")
    void shouldAllowDifferentStoryPointTotal() {
      parent(13);
      service.execute(command(PO_ID, List.of(
          new SplitBacklogItemCommand.Child("子1", 1),
          new SplitBacklogItemCommand.Child("子2", 2))));
      assertThat(savedChildren()).extracting(BacklogItem::getStoryPoint).containsExactlyInAnyOrder(1, 2);
    }

    @Test @DisplayName("子の不正なSPを拒否する")
    void shouldRejectInvalidChildStoryPoint() {
      parent(13);
      assertThatThrownBy(() -> service.execute(command(PO_ID,
          List.of(new SplitBacklogItemCommand.Child("子", 4)))))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(saved).isEmpty();
    }
  }

  @Nested
  @DisplayName("親とイベント")
  class ParentAndEvents {
    @Test @DisplayName("18 分割後の親はDONEになる")
    void shouldMarkParentDone() {
      BacklogItem item = parent(13);
      service.execute(command(PO_ID, children(1)));
      assertThat(item.getStatus()).isEqualTo(BacklogItemStatus.DONE);
      assertThat(saved).contains(item);
    }

    @Test @DisplayName("19 状態遷移イベントを発行する")
    void shouldPublishStatusChanged() {
      parent(13);
      service.execute(command(PO_ID, children(1)));
      ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
      verify(events, org.mockito.Mockito.atLeastOnce()).publishEvent(captor.capture());
      assertThat(captor.getAllValues()).filteredOn(BacklogItemStatusChanged.class::isInstance)
          .singleElement().satisfies(value -> {
            BacklogItemStatusChanged event = (BacklogItemStatusChanged) value;
            assertThat(event.backlogItemId()).isEqualTo(ITEM_ID);
            assertThat(event.scrumTeamId()).isEqualTo(TEAM_ID);
            assertThat(event.fromStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
            assertThat(event.toStatus()).isEqualTo(BacklogItemStatus.DONE);
            assertThat(event.actorEmployeeId()).isEqualTo(PO_ID);
            assertThat(event.occurredAt()).isNotNull();
          });
    }

    @Test @DisplayName("分割完了イベントに親・チーム・子・操作者を含めて発行する")
    void shouldPublishSplitEventForNotification() {
      parent(13);
      service.execute(command(PO_ID, children(2)));
      assertThat(savedChildren()).hasSize(2);
      ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
      verify(events, org.mockito.Mockito.atLeastOnce()).publishEvent(captor.capture());
      assertThat(captor.getAllValues()).filteredOn(BacklogItemSplit.class::isInstance)
          .singleElement().satisfies(value -> {
            BacklogItemSplit event = (BacklogItemSplit) value;
            assertThat(event.backlogItemId()).isEqualTo(ITEM_ID);
            assertThat(event.scrumTeamId()).isEqualTo(TEAM_ID);
            assertThat(event.actorEmployeeId()).isEqualTo(PO_ID);
            assertThat(event.childItemIds()).containsExactlyInAnyOrderElementsOf(
                savedChildren().stream().map(BacklogItem::getId).toList());
            assertThat(event.occurredAt()).isNotNull();
          });
    }

    @Test @DisplayName("20 存在しない親は分割できない")
    void shouldRejectMissingParent() {
      when(items.findById(ITEM_ID)).thenReturn(Optional.empty());
      assertThatThrownBy(() -> service.execute(command(PO_ID, children(1))))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(saved).isEmpty();
    }

    @Test @DisplayName("21 DONEの親は再分割できない")
    void shouldRejectDoneParent() {
      BacklogItem done = BacklogItem.builder().id(ITEM_ID).scrumTeamId(TEAM_ID)
          .title("親タイトル").creatorName("作成者").storyPoint(13)
          .status(BacklogItemStatus.DONE).build();
      when(items.findById(ITEM_ID)).thenReturn(Optional.of(done));
      assertThatThrownBy(() -> service.execute(command(PO_ID, children(1))))
          .isInstanceOf(IllegalStateException.class);
      assertThat(saved).isEmpty();
    }
  }
}