package undecided.agiloom.sprintExecution.internal.presentation;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryService;
import undecided.agiloom.sprintExecution.internal.query.SprintSettingsQueryService;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintQueryController {

    private final SprintQueryService sprintQueryService;
    private final SprintSettingsQueryService sprintSettingsQueryService;

    public SprintQueryController(SprintQueryService sprintQueryService,
                                 SprintSettingsQueryService sprintSettingsQueryService) {
        this.sprintQueryService = sprintQueryService;
        this.sprintSettingsQueryService = sprintSettingsQueryService;
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

    /**
     * プロダクトのスプリント設定を取得します。
     */
    @GetMapping("/settings")
    public SprintSettingsResponse getSprintSettings(
            @RequestParam @NotNull String productId) {
        SnowflakeId productIdId = SnowflakeId.of(Long.parseLong(productId));
        SprintSettings settings = sprintSettingsQueryService.findByProductId(productIdId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Sprint settings not found for product: " + productId));
        return toSprintSettingsResponse(settings);
    }

    SprintResponse toResponse(Sprint sprint) {
        return SprintResponse.builder()
                .id(sprint.getId())
                .productId(sprint.getProductId().value().toString())
                .sprintNumber(sprint.getSprintNumber())
                .title(sprint.getTitle())
                .goal(sprint.getGoal())
                .startDate(sprint.getStartDate())
                .endDate(sprint.getEndDate())
                .status(sprint.getStatus())
                .teamId(sprint.getTeamId().value().toString())
                .teamName(sprint.getTeamName())
                .velocity(sprint.getVelocity())
                .notes(sprint.getNotes())
                .isSprintZero(sprint.isSprintZero())
                .createdAt(sprint.getCreatedAt())
                .updatedAt(sprint.getUpdatedAt())
                .build();
    }

    private SprintSettingsResponse toSprintSettingsResponse(SprintSettings settings) {
        return SprintSettingsResponse.builder()
                .productId(settings.getProductId().value().toString())
                .businessDays(settings.getBusinessDays())
                .endDayOfWeek(settings.getEndDayOfWeek())
                .build();
    }
}
