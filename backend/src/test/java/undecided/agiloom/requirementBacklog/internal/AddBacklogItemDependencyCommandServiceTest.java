package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;
import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependency;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependencyDefined;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("AddBacklogItemDependencyCommandServiceのテスト")
class AddBacklogItemDependencyCommandServiceTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId OTHER_TEAM_ID = SnowflakeId.of(11L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(1001L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(1002L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(1003L);
  private static final SnowflakeId ITEM_A = SnowflakeId.of(100L);
  private static final SnowflakeId ITEM_B = SnowflakeId.of(101L);
  private static final SnowflakeId ITEM_C = SnowflakeId.of(102L);
  private static final SnowflakeId NEW_ID = SnowflakeId.of(900L);

  private final BacklogItemRepository backlogItemRepository = mock(BacklogItemRepository.class);
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository =
      mock(ScrumTeamSubsetRepository.class);
  private final BacklogItemDependencyRepository dependencyRepository =
      mock(BacklogItemDependencyRepository.class);
  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
  private final AddBacklogItemDependencyCommandService service =
      new AddBacklogItemDependencyCommandService(
          backlogItemRepository,
          scrumTeamSubsetRepository,
          dependencyRepository,
          snowflakeIdGenerator,
          eventPublisher,
          Clock.systemDefaultZone());

  private BacklogItem item(SnowflakeId id, SnowflakeId teamId) {
    return BacklogItem.builder()
        .id(id)
        .title("Item " + id.value())
        .creatorName("Creator")
        .scrumTeamId(teamId)
        .status(BacklogItemStatus.ACCEPTED)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private ScrumTeamSubset team() {
    return ScrumTeamSubset.builder()
        .id(TEAM_ID)
        .name("Team")
        .poEmployeeId(PO_ID)
        .smEmployeeId(SM_ID)
        .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
        .updatedAt(LocalDateTime.now()).build();
  }

  private BacklogItemDependency dep(SnowflakeId from, SnowflakeId to) {
    return BacklogItemDependency.builder()
        .id(SnowflakeId.of(800L))
        .backlogItemId(from)
        .dependencyItemId(to)
        .createdBy(PO_ID)
        .createdAt(LocalDateTime.now()).build();
  }

  private void givenItems(SnowflakeId fromId, SnowflakeId toId, SnowflakeId toTeamId) {
    when(backlogItemRepository.findById(fromId)).thenReturn(Optional.of(item(fromId, TEAM_ID)));
    when(backlogItemRepository.findById(toId)).thenReturn(Optional.of(item(toId, toTeamId)));
    when(scrumTeamSubsetRepository.findById(TEAM_ID)).thenReturn(Optional.of(team()));
    when(snowflakeIdGenerator.nextIdValue()).thenReturn(NEW_ID);
  }

  private AddBacklogItemDependencyCommand command(
      SnowflakeId from, SnowflakeId to, SnowflakeId actor) {
    return AddBacklogItemDependencyCommand.builder()
        .backlogItemId(from)
        .dependencyItemId(to)
        .actorEmployeeId(actor)
        .build();
  }

  @Test
  @DisplayName("POが依存関係を追加したとき、依存関係が保存され、定義イベントが発行される")
  void poAddsDependency() {
    givenItems(ITEM_A, ITEM_B, TEAM_ID);
    when(dependencyRepository.findByBacklogItemIdAndDependencyItemId(ITEM_A, ITEM_B))
        .thenReturn(Optional.empty());
    when(dependencyRepository.findByBacklogItemId(ITEM_B)).thenReturn(List.of());

    SnowflakeId id = service.execute(command(ITEM_A, ITEM_B, PO_ID));

    assertThat(id).isEqualTo(NEW_ID);
    ArgumentCaptor<BacklogItemDependency> captor =
        ArgumentCaptor.forClass(BacklogItemDependency.class);
    verify(dependencyRepository).save(captor.capture());
    assertThat(captor.getValue().getBacklogItemId()).isEqualTo(ITEM_A);
    assertThat(captor.getValue().getDependencyItemId()).isEqualTo(ITEM_B);
    assertThat(captor.getValue().getCreatedBy()).isEqualTo(PO_ID);
    ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
    verify(eventPublisher).publishEvent(eventCaptor.capture());
    BacklogItemDependencyDefined event = (BacklogItemDependencyDefined) eventCaptor.getValue();
    assertThat(event.backlogItemId()).isEqualTo(ITEM_A);
    assertThat(event.dependencyItemId()).isEqualTo(ITEM_B);
    assertThat(event.scrumTeamId()).isEqualTo(TEAM_ID);
    assertThat(event.actorEmployeeId()).isEqualTo(PO_ID);
  }

  @Test
  @DisplayName("SMが依存関係を追加したとき、依存関係が保存される")
  void smAddsDependency() {
    givenItems(ITEM_A, ITEM_B, TEAM_ID);
    when(dependencyRepository.findByBacklogItemIdAndDependencyItemId(ITEM_A, ITEM_B))
        .thenReturn(Optional.empty());
    when(dependencyRepository.findByBacklogItemId(ITEM_B)).thenReturn(List.of());

    service.execute(command(ITEM_A, ITEM_B, SM_ID));

    verify(dependencyRepository).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  @DisplayName("POでもSMでもないメンバーが依存関係を追加したとき、権限エラーとなり保存されない")
  void memberCannotAdd() {
    givenItems(ITEM_A, ITEM_B, TEAM_ID);

    assertThatThrownBy(() -> service.execute(command(ITEM_A, ITEM_B, MEMBER_ID)))
        .isInstanceOf(BacklogItemPermissionDeniedException.class);
    verify(dependencyRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  @DisplayName("自分自身への依存を追加したとき、エラーとなり保存されない")
  void selfDependencyRejected() {
    givenItems(ITEM_A, ITEM_A, TEAM_ID);

    assertThatThrownBy(() -> service.execute(command(ITEM_A, ITEM_A, PO_ID)))
        .isInstanceOf(IllegalArgumentException.class);
    verify(dependencyRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  @DisplayName("同じ依存を重複して追加したとき、エラーとなり保存されない")
  void duplicateRejected() {
    givenItems(ITEM_A, ITEM_B, TEAM_ID);
    when(dependencyRepository.findByBacklogItemIdAndDependencyItemId(ITEM_A, ITEM_B))
        .thenReturn(Optional.of(dep(ITEM_A, ITEM_B)));

    assertThatThrownBy(() -> service.execute(command(ITEM_A, ITEM_B, PO_ID)))
        .isInstanceOf(IllegalStateException.class);
    verify(dependencyRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  @DisplayName("相互依存になる依存を追加したとき、循環エラーとなり保存されない")
  void mutualDependencyRejected() {
    givenItems(ITEM_A, ITEM_B, TEAM_ID);
    when(dependencyRepository.findByBacklogItemIdAndDependencyItemId(ITEM_A, ITEM_B))
        .thenReturn(Optional.empty());
    when(dependencyRepository.findByBacklogItemId(ITEM_B)).thenReturn(List.of(dep(ITEM_B, ITEM_A)));

    assertThatThrownBy(() -> service.execute(command(ITEM_A, ITEM_B, PO_ID)))
        .isInstanceOf(IllegalStateException.class);
    verify(dependencyRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  @DisplayName("連鎖の先で循環する依存を追加したとき、循環エラーとなり保存されない")
  void chainedCycleRejected() {
    givenItems(ITEM_C, ITEM_A, TEAM_ID);
    when(dependencyRepository.findByBacklogItemIdAndDependencyItemId(ITEM_C, ITEM_A))
        .thenReturn(Optional.empty());
    when(dependencyRepository.findByBacklogItemId(ITEM_A)).thenReturn(List.of(dep(ITEM_A, ITEM_B)));
    when(dependencyRepository.findByBacklogItemId(ITEM_B)).thenReturn(List.of(dep(ITEM_B, ITEM_C)));

    assertThatThrownBy(() -> service.execute(command(ITEM_C, ITEM_A, PO_ID)))
        .isInstanceOf(IllegalStateException.class);
    verify(dependencyRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }

  @Test
  @DisplayName("別のスクラムチームのアイテムへの依存を追加したとき、エラーとなり保存されない")
  void differentTeamRejected() {
    givenItems(ITEM_A, ITEM_B, OTHER_TEAM_ID);

    assertThatThrownBy(() -> service.execute(command(ITEM_A, ITEM_B, PO_ID)))
        .isInstanceOf(IllegalArgumentException.class);
    verify(dependencyRepository, never()).save(org.mockito.ArgumentMatchers.any());
  }
}
