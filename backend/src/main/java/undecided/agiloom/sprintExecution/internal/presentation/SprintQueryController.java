package undecided.agiloom.sprintExecution.internal.presentation;

import java.util.List;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryServiceInterface;
import undecided.supporting.snowflake.SnowflakeId;

// @Validatedを付与することで、@RequestParamへの@Min/@Max制約がSpring MVC層で有効になる
@Validated
@RestController
@RequestMapping("/api/v1/sprints")
public class SprintQueryController {

    // ページネーションの最大サイズ制限（DoS対策）。@Max制約で拒否されるが、防御の纵深として保持
    private static final int MAX_PAGE_SIZE = 100;

    private final SprintQueryServiceInterface sprintQueryService;
    private final SprintResponseAssembler sprintResponseAssembler;

    public SprintQueryController(SprintQueryServiceInterface sprintQueryService,
                                  SprintResponseAssembler sprintResponseAssembler) {
        this.sprintQueryService = sprintQueryService;
        this.sprintResponseAssembler = sprintResponseAssembler;
    }

    @GetMapping
    public List<SprintResponse> getSprints(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(MAX_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return sprintQueryService.findAll(pageable).stream()
                .map(sprintResponseAssembler::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public SprintResponse getSprint(@PathVariable @NotNull String id) {
        SnowflakeId snowflakeId = SprintIdUtils.parseSnowflakeId(id);
        SprintDTO sprint = sprintQueryService.findById(snowflakeId)
                .orElseThrow(() -> new IllegalArgumentException("Sprint not found: " + id));
        return sprintResponseAssembler.toResponse(sprint);
    }
}
