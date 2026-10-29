package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandService;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandService;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryService;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintCommandController {

    private final CreateSprintCommandService createSprintCommandService;
    private final UpdateSprintCommandService updateSprintCommandService;
    private final SprintQueryService sprintQueryService;
    private final SprintQueryController sprintQueryController;

    public SprintCommandController(CreateSprintCommandService createSprintCommandService,
                            UpdateSprintCommandService updateSprintCommandService,
                            SprintQueryService sprintQueryService,
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
        SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
        updateSprintCommandService.changeStatus(snowflakeId, status);
        Sprint sprint = sprintQueryService.findById(snowflakeId).orElseThrow();
        return ResponseEntity.ok(sprintQueryController.toResponse(sprint));
    }
}
