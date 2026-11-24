package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.spi.SprintFacade;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムのスプリント割り当て・変更・解除を行うコマンドサービス（Issue #525）。
 *
 * <p>割り当てはチームメンバー全員が行えます（Issue #620／#717）。対象は ESTIMATED の
 * アイテムのみで、割り当て先は未来4スプリント（進行中を含む。Issue #650）の範囲内に
 * 限ります。割り当ては状態を変えません（Issue #717）。
 */
@Service
@Transactional
class AssignBacklogItemSprintCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final SprintFacade sprintFacade;
  private final ApplicationEventPublisher eventPublisher;

  AssignBacklogItemSprintCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      SprintFacade sprintFacade,
      ApplicationEventPublisher eventPublisher) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.sprintFacade = sprintFacade;
    this.eventPublisher = eventPublisher;
  }

  void assign(@NonNull AssignBacklogItemSprintCommand command) {
    throw new UnsupportedOperationException("not implemented");
  }

  void unassign(@NonNull UnassignBacklogItemSprintCommand command) {
    throw new UnsupportedOperationException("not implemented");
  }

  /** 割り当て可能なスプリント一覧の取得はクエリ側の責務だが、検証用に窓を公開する。 */
  List<SnowflakeId> assignableSprintIds(@NonNull SnowflakeId teamId) {
    return sprintFacade.findAssignableSprints(teamId).stream()
        .map(sprint -> sprint.getId())
        .toList();
  }
}
