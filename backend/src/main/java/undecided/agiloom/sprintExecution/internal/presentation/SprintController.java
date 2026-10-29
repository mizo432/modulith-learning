package undecided.agiloom.sprintExecution.internal.presentation;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandService;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandService;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryService;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintController {

    private final CreateSprintCommandService createSprintCommandService;
    private final UpdateSprintCommandService updateSprintCommandService;
    private final SprintQueryService sprintQueryService;

    public SprintController(CreateSprintCommandService createSprintCommandService,
                            UpdateSprintCommandService updateSprintCommandService,
                            SprintQueryService sprintQueryService) {
        this.createSprintCommandService = createSprintCommandService;
        this.updateSprintCommandService = updateSprintCommandService;
        this.sprintQueryService = sprintQueryService;
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
        return ResponseEntity.ok(toResponse(sprint));
    }

    @GetMapping
    public List<SprintResponse> getSprints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return sprintQueryService.findAll(pageable).stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public SprintResponse getSprint(@PathVariable @NotNull String id) {
        SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
        Sprint sprint = sprintQueryService.findById(snowflakeId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + id));
        return toResponse(sprint);
    }

    @PatchMapping("/{id}/status")
    public SprintResponse changeStatus(@PathVariable @NotNull String id,
                                       @RequestBody @NotNull SprintStatus status) {
        SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
        updateSprintCommandService.changeStatus(snowflakeId, status);
        Sprint sprint = sprintQueryService.findById(snowflakeId).orElseThrow();
        return toResponse(sprint);
    }

    private SprintResponse toResponse(Sprint sprint) {
        return SprintResponse.builder()
                .id(sprint.getId())
                .productId(sprint.getProductId())
                .sprintNumber(sprint.getSprintNumber())
                .title(sprint.getTitle())
                .goal(sprint.getGoal())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .status(sprint.getStatus())
                .teamId(sprint.getTeamId())
                .teamName(sprint.getTeamName())
                .velocity(sprint.getVelocity())
                .notes(sprint.getNotes())
                .createdAt(sprint.getCreatedAt())
                .updatedAt(sprint.getUpdatedAt())
                .build();
    }
}
