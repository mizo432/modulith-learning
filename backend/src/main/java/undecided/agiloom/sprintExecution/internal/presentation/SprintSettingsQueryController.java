package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.agiloom.sprintExecution.internal.query.SprintSettingsQueryService;
import undecided.agiloom.sprintExecution.spi.SprintSettings;

import java.util.UUID;

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
      @PathVariable @NotNull UUID productId) {
    SprintSettings settings = sprintSettingsQueryService.getSettingsOrDefault(productId);
    return toResponse(settings);
  }

  private SprintSettingsResponse toResponse(SprintSettings settings) {
    return SprintSettingsResponse.builder()
        .productId(settings.getProductId())
        .businessDays(settings.getBusinessDays())
        .endDayOfWeek(settings.getEndDayOfWeek())
        .build();
  }
}
