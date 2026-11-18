package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * バックログアイテムの見積もり値を設定するコマンドサービス。
 *
 * <p>見積もり値（StoryPoint）の設定と、ESTIMATED 状態での再評価（値変更）を行います。
 * 権限判定はチームメンバーのみが実行可能（{@link ScrumTeamSubset#isTeamMember} で判定）。
 * 見積もり値が設定されると {@link BacklogItemEstimated} イベントを発行します。
 */
@Service
@Transactional
class EstimateBacklogItemCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final ApplicationEventPublisher eventPublisher;

  EstimateBacklogItemCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      ApplicationEventPublisher eventPublisher) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.eventPublisher = eventPublisher;
  }

  void execute(@NonNull EstimateBacklogItemCommand command) {
    throw new UnsupportedOperationException("EstimateBacklogItemCommandService is not yet implemented");
  }
}
