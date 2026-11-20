package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;

/**
 * フィーチャー削除サービス。
 *
 * <p>フィーチャーとバックログアイテムの関連（紐付け）のみ削除し、バックログアイテム自体は残す（#661）。
 */
class DeleteFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final FeatureBacklogItemRepository featureBacklogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  DeleteFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull FeatureBacklogItemRepository featureBacklogItemRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.featureRepository = featureRepository;
    this.featureBacklogItemRepository = featureBacklogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  /**
   * フィーチャーを削除します。
   *
   * @param command 削除コマンド
   */
  public void execute(@NonNull DeleteFeatureCommand command) {
  }
}
