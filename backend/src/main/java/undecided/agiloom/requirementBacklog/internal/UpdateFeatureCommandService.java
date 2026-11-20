package undecided.agiloom.requirementBacklog.internal;

import org.jspecify.annotations.NonNull;

/**
 * フィーチャー更新サービス。
 */
class UpdateFeatureCommandService {

  private final FeatureRepository featureRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;

  UpdateFeatureCommandService(
      @NonNull FeatureRepository featureRepository,
      @NonNull ScrumTeamSubsetRepository scrumTeamSubsetRepository) {
    this.featureRepository = featureRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
  }

  /**
   * フィーチャーを更新します。
   *
   * @param command 更新コマンド
   */
  public void execute(@NonNull UpdateFeatureCommand command) {
  }
}
