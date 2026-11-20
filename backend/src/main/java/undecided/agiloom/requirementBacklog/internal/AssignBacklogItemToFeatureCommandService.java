package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;

/**
 * バックログアイテムをフィーチャーに割り当てるサービス。
 *
 * <p>同じフィーチャーへの再割り当ては冪等に成功し、別のフィーチャーへの割り当ては409エラー（決定4）。
 */
class AssignBacklogItemToFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final BacklogItemRepository backlogItemRepository;
  private final FeatureBacklogItemRepository featureBacklogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  AssignBacklogItemToFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull BacklogItemRepository backlogItemRepository,
      @NonNull FeatureBacklogItemRepository featureBacklogItemRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.featureRepository = featureRepository;
    this.backlogItemRepository = backlogItemRepository;
    this.featureBacklogItemRepository = featureBacklogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  /**
   * バックログアイテムをフィーチャーに割り当てます。
   *
   * @param command 割り当てコマンド
   */
  public void execute(@NonNull AssignBacklogItemToFeatureCommand command) {
  }
}
