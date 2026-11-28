package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependency;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("BacklogItemDependencyQueryServiceのテスト")
class BacklogItemDependencyQueryServiceTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId ITEM_A = SnowflakeId.of(100L);
  private static final SnowflakeId ITEM_B = SnowflakeId.of(101L);
  private static final SnowflakeId ITEM_C = SnowflakeId.of(102L);
  private static final SnowflakeId ITEM_D = SnowflakeId.of(103L);

  private final BacklogItemRepository backlogItemRepository = mock(BacklogItemRepository.class);
  private final BacklogItemDependencyRepository dependencyRepository =
      mock(BacklogItemDependencyRepository.class);
  private final BacklogItemDependencyQueryService service =
      new BacklogItemDependencyQueryService(backlogItemRepository, dependencyRepository);

  private BacklogItem item(SnowflakeId id, BacklogItemStatus status) {
    return BacklogItem.builder()
        .id(id)
        .title("Item " + id.value())
        .creatorName("Creator")
        .scrumTeamId(TEAM_ID)
        .status(status)
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
  }

  private BacklogItemDependency dep(SnowflakeId from, SnowflakeId to) {
    return BacklogItemDependency.builder()
        .id(SnowflakeId.of(800L))
        .backlogItemId(from)
        .dependencyItemId(to)
        .createdBy(SnowflakeId.of(1001L))
        .createdAt(LocalDateTime.now()).build();
  }

  @Test
  @DisplayName("依存先がすべてDONEであるとき、依存は充足していると判定される")
  void satisfiedWhenAllDependenciesDone() {
    when(dependencyRepository.findByBacklogItemId(ITEM_A)).thenReturn(List.of(dep(ITEM_A, ITEM_B)));
    when(dependencyRepository.findByDependencyItemId(ITEM_A)).thenReturn(List.of());
    when(backlogItemRepository.findById(ITEM_B))
        .thenReturn(Optional.of(item(ITEM_B, BacklogItemStatus.DONE)));

    BacklogItemDependenciesResponse response = service.getDependencies(ITEM_A);

    assertThat(response.isDependenciesSatisfied()).isTrue();
    assertThat(response.getUnmetDependencyCount()).isZero();
  }

  @Test
  @DisplayName("依存先にDONEでないものがあるとき、依存は未充足と判定される")
  void unsatisfiedWhenAnyDependencyNotDone() {
    when(dependencyRepository.findByBacklogItemId(ITEM_A))
        .thenReturn(List.of(dep(ITEM_A, ITEM_B), dep(ITEM_A, ITEM_C)));
    when(dependencyRepository.findByDependencyItemId(ITEM_A)).thenReturn(List.of());
    when(backlogItemRepository.findById(ITEM_B))
        .thenReturn(Optional.of(item(ITEM_B, BacklogItemStatus.DONE)));
    when(backlogItemRepository.findById(ITEM_C))
        .thenReturn(Optional.of(item(ITEM_C, BacklogItemStatus.READY)));

    BacklogItemDependenciesResponse response = service.getDependencies(ITEM_A);

    assertThat(response.isDependenciesSatisfied()).isFalse();
    assertThat(response.getUnmetDependencyCount()).isEqualTo(1);
  }

  @Test
  @DisplayName("依存関係を取得したとき、依存先と依存元が状態と充足可否つきで返る")
  void returnsDependenciesAndDependents() {
    when(dependencyRepository.findByBacklogItemId(ITEM_A))
        .thenReturn(List.of(dep(ITEM_A, ITEM_B), dep(ITEM_A, ITEM_C)));
    when(dependencyRepository.findByDependencyItemId(ITEM_A)).thenReturn(List.of(dep(ITEM_D, ITEM_A)));
    when(backlogItemRepository.findById(ITEM_A))
        .thenReturn(Optional.of(item(ITEM_A, BacklogItemStatus.ACCEPTED)));
    when(backlogItemRepository.findById(ITEM_B))
        .thenReturn(Optional.of(item(ITEM_B, BacklogItemStatus.DONE)));
    when(backlogItemRepository.findById(ITEM_C))
        .thenReturn(Optional.of(item(ITEM_C, BacklogItemStatus.READY)));
    when(backlogItemRepository.findById(ITEM_D))
        .thenReturn(Optional.of(item(ITEM_D, BacklogItemStatus.ESTIMATED)));

    BacklogItemDependenciesResponse response = service.getDependencies(ITEM_A);

    assertThat(response.getDependencies()).hasSize(2);
    assertThat(response.getDependencies().get(0).getBacklogItemId()).isEqualTo(ITEM_B);
    assertThat(response.getDependencies().get(0).getStatus()).isEqualTo(BacklogItemStatus.DONE);
    assertThat(response.getDependencies().get(0).isSatisfied()).isTrue();
    assertThat(response.getDependencies().get(1).getBacklogItemId()).isEqualTo(ITEM_C);
    assertThat(response.getDependencies().get(1).isSatisfied()).isFalse();
    assertThat(response.getDependents()).hasSize(1);
    assertThat(response.getDependents().get(0).getBacklogItemId()).isEqualTo(ITEM_D);
    assertThat(response.getDependents().get(0).getStatus()).isEqualTo(BacklogItemStatus.ESTIMATED);
    // 依存元Dの依存先はAであり、AはDONEでないため未充足
    assertThat(response.getDependents().get(0).isSatisfied()).isFalse();
  }
}
