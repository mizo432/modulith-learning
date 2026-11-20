package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャークエリサービス（進捗計算含む）。
 */
class FeatureQueryService {

  private final FeatureRepository featureRepository;
  private final FeatureBacklogItemRepository featureBacklogItemRepository;
  private final BacklogItemRepository backlogItemRepository;

  FeatureQueryService(
      @NonNull FeatureRepository featureRepository,
      @NonNull FeatureBacklogItemRepository featureBacklogItemRepository,
      @NonNull BacklogItemRepository backlogItemRepository) {
    this.featureRepository = featureRepository;
    this.featureBacklogItemRepository = featureBacklogItemRepository;
    this.backlogItemRepository = backlogItemRepository;
  }

  /**
   * チームのフィーチャー一覧を取得します。
   *
   * @param scrumTeamId スクラムチームID
   * @return フィーチャー一覧
   */
  public List<Feature> findByScrumTeamId(@NonNull SnowflakeId scrumTeamId) {
    return List.of();
  }

  /**
   * フィーチャーの詳細を取得します。
   *
   * @param featureId フィーチャーID
   * @return フィーチャー詳細（存在する場合）
   */
  public Optional<Feature> findById(@NonNull SnowflakeId featureId) {
    return Optional.empty();
  }

  /**
   * フィーチャーの進捗情報を計算します。
   *
   * @param featureId フィーチャーID
   * @return 進捗情報（完了SP, 総SP, 進捗率）
   */
  public FeatureProgress calculateProgress(@NonNull SnowflakeId featureId) {
    return null;
  }

  /**
   * フィーチャーの進捗情報を表すレコード。
   */
  public record FeatureProgress(
      int completedStoryPoints,
      int totalStoryPoints,
      double progressPercentage) {
  }
}
