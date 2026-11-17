package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import undecided.agiloom.sprintExecution.internal.query.SprintSettingsQueryService;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.supporting.snowflake.SnowflakeId;

/** スプリント設定取得APIコントローラー。 */
@RestController
@RequestMapping("/api/v1/products")
public class SprintSettingsQueryController {

  private final SprintSettingsQueryService sprintSettingsQueryService;

  public SprintSettingsQueryController(SprintSettingsQueryService sprintSettingsQueryService) {
    this.sprintSettingsQueryService = sprintSettingsQueryService;
  }

  /**
   * プロダクトのスプリント設定を取得します。
   * 設定が存在しない場合はデフォルト値を返します。
   */
  @GetMapping("/{productId}/sprint-settings")
  public SprintSettingsResponse getSprintSettings(
      @PathVariable @NotNull String productId) {
    SnowflakeId productIdId = SnowflakeId.of(Long.parseLong(productId));
    SprintSettings settings = sprintSettingsQueryService.getSettingsOrDefault(productIdId);
    return toResponse(settings);
  }

  private SprintSettingsResponse toResponse(SprintSettings settings) {
    return SprintSettingsResponse.builder()
        .productId(settings.getProductId().value().toString())
        .businessDays(settings.getBusinessDays())
        .endDayOfWeek(settings.getEndDayOfWeek())
        .build();
  }

  /**
   * スプリントの終了日を計算します。
   *
   * @param productId プロダクトID
   * @param startDate 開始日
   * @return 計算された終了日情報
   */
  @GetMapping("/{productId}/sprint-settings/calculate-end-date")
  public CalculateEndDateResponse calculateEndDate(
      @PathVariable @NotNull String productId,
      @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

    SnowflakeId productIdId = SnowflakeId.of(Long.parseLong(productId));
    SprintSettings settings = sprintSettingsQueryService.getSettingsOrDefault(productIdId);
    LocalDate endDate = sprintSettingsQueryService.calculateEndDate(productIdId, startDate);

    return CalculateEndDateResponse.builder()
        .startDate(startDate)
        .endDate(endDate)
        .businessDays(settings.getBusinessDays())
        .endDayOfWeekLabel(settings.getEndDayOfWeek().name())
        .isPastDate(endDate.isBefore(LocalDate.now()))
        .build();
  }
}
