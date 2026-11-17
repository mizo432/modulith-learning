package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintSettingsCommand;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintSettingsCommandService;
import undecided.agiloom.sprintExecution.internal.query.SprintSettingsQueryService;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.supporting.snowflake.SnowflakeId;

/** スプリント設定更新APIコントローラー。 */
@RestController
@RequestMapping("/api/v1/products")
public class SprintSettingsCommandController {

  private final UpdateSprintSettingsCommandService updateSprintSettingsCommandService;
  private final SprintSettingsQueryService sprintSettingsQueryService;

  public SprintSettingsCommandController(
      UpdateSprintSettingsCommandService updateSprintSettingsCommandService,
      SprintSettingsQueryService sprintSettingsQueryService) {
    this.updateSprintSettingsCommandService = updateSprintSettingsCommandService;
    this.sprintSettingsQueryService = sprintSettingsQueryService;
  }

  /**
   * プロダクトのスプリント設定を更新します。
   */
  @PatchMapping("/{productId}/sprint-settings")
  public ResponseEntity<SprintSettingsResponse> updateSprintSettings(
      @PathVariable @NotNull String productId,
      @Valid @RequestBody UpdateSprintSettingsRequest request) {
    SnowflakeId productIdId = SnowflakeId.of(Long.parseLong(productId));
    UpdateSprintSettingsCommand command = UpdateSprintSettingsCommand.builder()
        .productId(productIdId)
        .businessDays(request.getBusinessDays())
        .endDayOfWeek(request.getEndDayOfWeek())
        .build();

    updateSprintSettingsCommandService.execute(command);
    SprintSettings settings = sprintSettingsQueryService.getSettingsOrDefault(productIdId);
    return ResponseEntity.ok(toResponse(settings));
  }

  private SprintSettingsResponse toResponse(SprintSettings settings) {
    return SprintSettingsResponse.builder()
        .productId(settings.getProductId().value().toString())
        .businessDays(settings.getBusinessDays())
        .endDayOfWeek(settings.getEndDayOfWeek())
        .build();
  }
}
