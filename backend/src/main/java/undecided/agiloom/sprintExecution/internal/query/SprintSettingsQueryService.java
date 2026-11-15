package undecided.agiloom.sprintExecution.internal.query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintSettingsRepository;
import undecided.agiloom.sprintExecution.internal.domain.SprintSettingsCalculator;
import undecided.agiloom.sprintExecution.spi.SprintSettings;

/**
 * スプリント設定クエリサービス。
 */
@Service
@Transactional(readOnly = true)
public class SprintSettingsQueryService {

  private final SprintSettingsRepository sprintSettingsRepository;
  private final SprintSettingsCalculator sprintSettingsCalculator;

  public SprintSettingsQueryService(
      SprintSettingsRepository sprintSettingsRepository,
      SprintSettingsCalculator sprintSettingsCalculator) {
    this.sprintSettingsRepository = sprintSettingsRepository;
    this.sprintSettingsCalculator = sprintSettingsCalculator;
  }

  /**
   * プロダクトIDでスプリント設定を検索します。
   *
   * @param productId プロダクトのID
   * @return 該当するスプリント設定のOptional
   */
  public Optional<SprintSettings> findByProductId(@NonNull UUID productId) {
    return sprintSettingsRepository.findByProductId(productId);
  }

  /**
   * 開始日からスプリントの終了日を計算します。
   *
   * @param productId プロダクトのID
   * @param startDate スプリントの開始日
   * @return 計算された終了日
   * @throws IllegalArgumentException 設定が見つからない場合
   */
  @NonNull
  public LocalDate calculateEndDate(@NonNull UUID productId, @NonNull LocalDate startDate) {
    SprintSettings settings = sprintSettingsRepository
        .findByProductId(productId)
        .orElseThrow(() -> new IllegalArgumentException(
            "Sprint settings not found for product: " + productId));
    return sprintSettingsCalculator.calculateEndDate(settings, startDate);
  }
}
