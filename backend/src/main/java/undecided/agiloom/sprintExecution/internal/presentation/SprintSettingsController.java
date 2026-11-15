package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintSettingsCommand;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintSettingsCommandService;
import undecided.agiloom.sprintExecution.internal.query.SprintSettingsQueryService;
import undecided.agiloom.sprintExecution.spi.SprintSettings;

import java.util.UUID;

/** スプリント設定APIコントローラー。 */
@RestController
@RequestMapping("/api/v1/products")
public class SprintSettingsController {

  private final UpdateSprintSettingsCommandService updateSprintSettingsCommandService;
  private final SprintSettingsQueryService sprintSettingsQueryService;

  public SprintSettingsController(
      UpdateSprintSettingsCommandService updateSprintSettingsCommandService,
      SprintSettingsQueryService sprintSettingsQueryService) {
    this.updateSprintSettingsCommandService = updateSprintSettingsCommandService;
    this.sprintSettingsQueryService = sprintSettingsQueryService;
  }

  /**
   * プロダクトのスプリント設定を取得します。
   * 設定が存在しない場合はデフォルト値を返します。
   */
  @GetMapping("/{productId}/sprint-settings")
  public SprintSettingsResponse getSprintSettings(
      @RequestParam @NotNull UUID productId) {
    SprintSettings settings = sprintSettingsQueryService.getSettingsOrDefault(productId);
    return toResponse(settings);
  }

  /**
   * プロダクトのスプリント設定を更新します。
   */
  @PatchMapping("/{productId}/sprint-settings")
  public ResponseEntity<SprintSettingsResponse> updateSprintSettings(
      @RequestParam @NotNull UUID productId,
      @RequestBody UpdateSprintSettingsRequest request) {
    UpdateSprintSettingsCommand command = UpdateSprintSettingsCommand.builder()
        .productId(productId)
        .businessDays(request.getBusinessDays())
        .endDayOfWeek(request.getEndDayOfWeek())
        .build();

    updateSprintSettingsCommandService.execute(command);
    SprintSettings settings = sprintSettingsQueryService.getSettingsOrDefault(productId);
    return ResponseEntity.ok(toResponse(settings));
  }

  private SprintSettingsResponse toResponse(SprintSettings settings) {
    return SprintSettingsResponse.builder()
        .productId(settings.getProductId())
        .businessDays(settings.getBusinessDays())
        .endDayOfWeek(settings.getEndDayOfWeek())
        .build();
  }
}
