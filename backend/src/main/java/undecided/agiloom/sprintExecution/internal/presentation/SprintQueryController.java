package undecided.agiloom.sprintExecution.internal.presentation;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryService;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintQueryController {

    private final SprintQueryService sprintQueryService;

    public SprintQueryController(SprintQueryService sprintQueryService) {
        this.sprintQueryService = sprintQueryService;
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

    SprintResponse toResponse(Sprint sprint) {
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
