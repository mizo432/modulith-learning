package undecided.agiloom.sprintExecution.internal.query;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintSettingsRepository;
import undecided.agiloom.sprintExecution.internal.domain.SprintSettingsCalculator;
import undecided.agiloom.sprintExecution.spi.EndDayOfWeek;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スプリント設定クエリサービス。
 */
@Service
@Transactional(readOnly = true)
public class SprintSettingsQueryService {

  private final SprintSettingsRepository sprintSettingsRepository;
  private final SprintSettingsCalculator sprintSettingsCalculator;
  private final Clock clock;

  public SprintSettingsQueryService(
      SprintSettingsRepository sprintSettingsRepository,
      SprintSettingsCalculator sprintSettingsCalculator,
      Clock clock) {
    this.sprintSettingsRepository = sprintSettingsRepository;
    this.sprintSettingsCalculator = sprintSettingsCalculator;
    this.clock = clock;
  }

  /**
   * プロダクトIDでスプリント設定を検索します。
   *
   * @param productId プロダクトのID
   * @return 該当するスプリント設定のOptional
   */
  public Optional<SprintSettings> findByProductId(@NonNull SnowflakeId productId) {
    checkNotNull(productId, () -> new IllegalArgumentException("productId must not be null"));
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
  public LocalDate calculateEndDate(@NonNull SnowflakeId productId, @NonNull LocalDate startDate) {
    SprintSettings settings = sprintSettingsRepository
        .findByProductId(productId)
        .orElseThrow(() -> new IllegalArgumentException(
            "Sprint settings not found for product: " + productId));
    return sprintSettingsCalculator.calculateEndDate(settings, startDate);
  }

  /**
   * プロダクトのスプリント設定を取得します。
   * 設定が存在しない場合はデフォルト値（10営業日、金曜日）を返します。
   *
   * @param productId プロダクトのID
   * @return スプリント設定（存在しない場合はデフォルト値）
   */
  @NonNull
  public SprintSettings getSettingsOrDefault(@NonNull SnowflakeId productId) {
    checkNotNull(productId, () -> new IllegalArgumentException("productId must not be null"));

    return sprintSettingsRepository.findByProductId(productId).orElseGet(() ->
        SprintSettings.builder()
            .productId(productId)
            .businessDays(10)
            .endDayOfWeek(EndDayOfWeek.FRIDAY)
            .createdAt(LocalDateTime.now(clock))
            .updatedAt(LocalDateTime.now(clock))
            .build()
    );
  }
}
