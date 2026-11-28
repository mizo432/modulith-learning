package undecided.agiloom.requirementBacklog.internal;
import java.time.LocalDateTime;

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
import undecided.agiloom.requirementBacklog.spi.BacklogItemDependency;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("RemoveBacklogItemDependencyCommandServiceのテスト")
class RemoveBacklogItemDependencyCommandServiceTest {

  private static final SnowflakeId TEAM_ID = SnowflakeId.of(10L);
  private static final SnowflakeId PO_ID = SnowflakeId.of(1001L);
  private static final SnowflakeId SM_ID = SnowflakeId.of(1002L);
  private static final SnowflakeId MEMBER_ID = SnowflakeId.of(1003L);
  private static final SnowflakeId ITEM_A = SnowflakeId.of(100L);
  private static final SnowflakeId ITEM_B = SnowflakeId.of(101L);

  private final BacklogItemRepository backlogItemRepository = mock(BacklogItemRepository.class);
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository =
      mock(ScrumTeamSubsetRepository.class);
  private final BacklogItemDependencyRepository dependencyRepository =
      mock(BacklogItemDependencyRepository.class);
  private final RemoveBacklogItemDependencyCommandService service =
      new RemoveBacklogItemDependencyCommandService(
          backlogItemRepository, scrumTeamSubsetRepository, dependencyRepository);

  private final BacklogItemDependency existing =
      BacklogItemDependency.builder()
          .id(SnowflakeId.of(800L))
          .backlogItemId(ITEM_A)
          .dependencyItemId(ITEM_B)
          .createdBy(PO_ID)
          .createdAt(LocalDateTime.now()).build();

  private void given() {
    when(backlogItemRepository.findById(ITEM_A))
        .thenReturn(
            Optional.of(
                BacklogItem.builder()
                    .id(ITEM_A)
                    .title("Item A")
                    .creatorName("Creator")
                    .scrumTeamId(TEAM_ID)
                    .status(BacklogItemStatus.ACCEPTED)
                    .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build()));
    when(scrumTeamSubsetRepository.findById(TEAM_ID))
        .thenReturn(
            Optional.of(
                ScrumTeamSubset.builder()
                    .id(TEAM_ID)
                    .name("Team")
                    .poEmployeeId(PO_ID)
                    .smEmployeeId(SM_ID)
                    .memberEmployeeIds(List.of(PO_ID, SM_ID, MEMBER_ID))
                    .updatedAt(LocalDateTime.now()).build()));
    when(dependencyRepository.findByBacklogItemIdAndDependencyItemId(ITEM_A, ITEM_B))
        .thenReturn(Optional.of(existing));
  }

  private RemoveBacklogItemDependencyCommand command(SnowflakeId actor) {
    return RemoveBacklogItemDependencyCommand.builder()
        .backlogItemId(ITEM_A)
        .dependencyItemId(ITEM_B)
        .actorEmployeeId(actor)
        .build();
  }

  @Test
  @DisplayName("POが依存関係を削除したとき、依存関係が削除される")
  void poRemovesDependency() {
    given();

    service.execute(command(PO_ID));

    verify(dependencyRepository).delete(existing);
  }

  @Test
  @DisplayName("POでもSMでもないメンバーが依存関係を削除したとき、権限エラーとなり削除されない")
  void memberCannotRemove() {
    given();

    assertThatThrownBy(() -> service.execute(command(MEMBER_ID)))
        .isInstanceOf(BacklogItemPermissionDeniedException.class);
    verify(dependencyRepository, never()).delete(org.mockito.ArgumentMatchers.any());
  }
}
