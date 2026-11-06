package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryServiceInterface;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintCommandController {

    private final CreateSprintCommandServiceInterface createSprintCommandService;
    private final UpdateSprintCommandServiceInterface updateSprintCommandService;
    private final SprintQueryServiceInterface sprintQueryService;
    private final SprintQueryController sprintQueryController;

    public SprintCommandController(CreateSprintCommandServiceInterface createSprintCommandService,
                            UpdateSprintCommandServiceInterface updateSprintCommandService,
                            SprintQueryServiceInterface sprintQueryService,
                            SprintQueryController sprintQueryController) {
        this.createSprintCommandService = createSprintCommandService;
        this.updateSprintCommandService = updateSprintCommandService;
        this.sprintQueryService = sprintQueryService;
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
        SnowflakeId snowflakeId = parseSnowflakeId(id);
        updateSprintCommandService.changeStatus(snowflakeId, status);
        Sprint sprint = sprintQueryService.findById(snowflakeId).orElseThrow();
        return ResponseEntity.ok(sprintQueryController.toResponse(sprint));
    }

    // IDをSnowflakeIdに変換し、不正な形式の場合はIllegalArgumentExceptionをスローする
    private SnowflakeId parseSnowflakeId(String id) {
        try {
            long value = Long.parseLong(id);
            return SnowflakeId.of(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Sprint IDは数値である必要があります: " + id, e);
        }
    }
}
