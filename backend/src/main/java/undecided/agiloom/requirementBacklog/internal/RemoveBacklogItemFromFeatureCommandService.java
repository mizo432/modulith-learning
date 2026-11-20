package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;

/**
 * バックログアイテムをフィーチャーから外すサービス。
 */
class RemoveBacklogItemFromFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final FeatureBacklogItemRepository featureBacklogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  RemoveBacklogItemFromFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull FeatureBacklogItemRepository featureBacklogItemRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.featureRepository = featureRepository;
    this.featureBacklogItemRepository = featureBacklogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  /**
   * バックログアイテムをフィーチャーから外します。
   *
   * @param command 外しコマンド
   */
  public void execute(@NonNull RemoveBacklogItemFromFeatureCommand command) {
  }
}
