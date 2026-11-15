package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandService;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandService;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintSettingsCommand;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintSettingsCommandService;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryService;
import undecided.agiloom.sprintExecution.internal.query.SprintSettingsQueryService;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintCommandController {

    private final CreateSprintCommandService createSprintCommandService;
    private final UpdateSprintCommandService updateSprintCommandService;
    private final UpdateSprintSettingsCommandService updateSprintSettingsCommandService;
    private final SprintQueryService sprintQueryService;
    private final SprintSettingsQueryService sprintSettingsQueryService;
    private final SprintQueryController sprintQueryController;

    public SprintCommandController(CreateSprintCommandService createSprintCommandService,
                            UpdateSprintCommandService updateSprintCommandService,
                            UpdateSprintSettingsCommandService updateSprintSettingsCommandService,
                            SprintQueryService sprintQueryService,
                            SprintSettingsQueryService sprintSettingsQueryService,
                            SprintQueryController sprintQueryController) {
        this.createSprintCommandService = createSprintCommandService;
        this.updateSprintCommandService = updateSprintCommandService;
        this.updateSprintSettingsCommandService = updateSprintSettingsCommandService;
        this.sprintQueryService = sprintQueryService;
        this.sprintSettingsQueryService = sprintSettingsQueryService;
        this.sprintQueryController = sprintQueryController;
    }

    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(@Valid @RequestBody CreateSprintRequest request) {
        CreateSprintCommand command = CreateSprintCommand.builder()
                .productId(request.getProductId())
                .sprintNumber(request.getSprintNumber())
                .title(request.getTitle())
                .goal(request.getGoal())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .teamId(request.getTeamId())
                .teamName(request.getTeamName())
                .build();

        SnowflakeId id = createSprintCommandService.execute(command);
        Sprint sprint = sprintQueryService.findById(id).orElseThrow();
        return ResponseEntity.ok(sprintQueryController.toResponse(sprint));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SprintResponse> changeStatus(@PathVariable @NotNull String id,
                                       @RequestBody @NotNull SprintStatus status) {
        SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
        updateSprintCommandService.changeStatus(snowflakeId, status);
        Sprint sprint = sprintQueryService.findById(snowflakeId).orElseThrow();
        return ResponseEntity.ok(sprintQueryController.toResponse(sprint));
    }

    /**
     * スプリント設定を更新します。
     * スプリント0中にのみ設定を変更できます。
     */
    @PutMapping("/settings")
    public ResponseEntity<SprintSettingsResponse> updateSprintSettings(
            @Valid @RequestBody UpdateSprintSettingsRequest request) {
        UpdateSprintSettingsCommand command = UpdateSprintSettingsCommand.builder()
                .productId(request.getProductId())
                .businessDays(request.getBusinessDays())
                .endDayOfWeek(request.getEndDayOfWeek())
                .build();

        updateSprintSettingsCommandService.execute(command);
        SprintSettings settings = sprintSettingsQueryService.findByProductId(request.getProductId()).orElseThrow();
        return ResponseEntity.ok(toSprintSettingsResponse(settings));
    }

    private SprintSettingsResponse toSprintSettingsResponse(SprintSettings settings) {
        return SprintSettingsResponse.builder()
                .productId(settings.getProductId())
                .businessDays(settings.getBusinessDays())
                .endDayOfWeek(settings.getEndDayOfWeek())
                .build();
    }
}
