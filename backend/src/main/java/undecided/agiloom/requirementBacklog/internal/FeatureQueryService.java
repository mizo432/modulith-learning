package undecided.agiloom.requirementBacklog.internal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureBacklogItem;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * フィーチャークエリサービス（進捗計算含む）。
 */
@Service
@Transactional(readOnly = true)
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
    return featureRepository.findByScrumTeamId(scrumTeamId);
  }

  /**
   * フィーチャーの詳細を取得します。
   *
   * @param featureId フィーチャーID
   * @return フィーチャー詳細（存在する場合）
   */
  public Optional<Feature> findById(@NonNull SnowflakeId featureId) {
    return featureRepository.findById(featureId);
  }

  /**
   * フィーチャーの進捗情報を計算します。
   *
   * <p>完了したバックログアイテムのストーリーポイント合計 ÷ 配下全体のストーリーポイント合計（決定2,6）。
   * ストーリーポイント未設定（null）のアイテムは0として扱う（決定6）。
   *
   * @param featureId フィーチャーID
   * @return 進捗情報（完了SP, 総SP, 進捗率）
   */
  public FeatureProgress calculateProgress(@NonNull SnowflakeId featureId) {
    List<FeatureBacklogItem> associations = featureBacklogItemRepository.findByFeatureId(featureId);

    if (associations.isEmpty()) {
      return new FeatureProgress(0, 0, 0.0);
    }

    int completedStoryPoints = 0;
    int totalStoryPoints = 0;

    for (FeatureBacklogItem association : associations) {
      Optional<BacklogItem> itemOpt = backlogItemRepository.findById(association.getBacklogItemId());
      if (itemOpt.isPresent()) {
        BacklogItem item = itemOpt.get();
        int sp = item.getStoryPoint() != null ? item.getStoryPoint() : 0;
        totalStoryPoints += sp;
        if (item.getStatus() == BacklogItemStatus.DONE) {
          completedStoryPoints += sp;
        }
      }
    }

    double progressPercentage = totalStoryPoints > 0
        ? (double) completedStoryPoints / totalStoryPoints * 100.0
        : 0.0;

    return new FeatureProgress(completedStoryPoints, totalStoryPoints, progressPercentage);
  }

  /**
   * フィーチャーの進捗情報を表すレコード。
   */
  public record FeatureProgress(
      int completedStoryPoints,
      int totalStoryPoints,
      double progressPercentage) {
  }

  /**
   * フィーチャーに紐付いているバックログアイテムのID一覧を取得します。
   *
   * @param featureId フィーチャーID
   * @return バックログアイテムID一覧
   */
  public List<SnowflakeId> findBacklogItemIds(@NonNull SnowflakeId featureId) {
    List<FeatureBacklogItem> associations = featureBacklogItemRepository.findByFeatureId(featureId);
    return associations.stream()
        .map(FeatureBacklogItem::getBacklogItemId)
        .toList();
  }

}
