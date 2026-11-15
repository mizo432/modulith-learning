package undecided.agiloom.scrumTeamManagement.internal.presentation;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.scrumTeamManagement.internal.query.ScrumTeamQueryService;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/scrum-teams")
public class ScrumTeamQueryController {

  private final ScrumTeamQueryService scrumTeamQueryService;

  public ScrumTeamQueryController(ScrumTeamQueryService scrumTeamQueryService) {
    this.scrumTeamQueryService = scrumTeamQueryService;
  }

  @GetMapping
  public List<ScrumTeamResponse> getScrumTeams(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return scrumTeamQueryService.findAll(pageable).stream().map(this::toResponse).toList();
  }

  @GetMapping("/{id}")
  public ScrumTeamResponse getScrumTeam(@PathVariable @NotNull String id) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    ScrumTeam scrumTeam =
        scrumTeamQueryService
            .findById(snowflakeId)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + id));
    return toResponse(scrumTeam);
  }

  @GetMapping("/product/{productId}")
  public List<ScrumTeamResponse> getScrumTeamsByProductId(
      @PathVariable @NotNull UUID productId) {
    return scrumTeamQueryService.findByProductId(productId).stream()
        .map(this::toResponse)
        .toList();
  }

  ScrumTeamResponse toResponse(ScrumTeam scrumTeam) {
    return ScrumTeamResponse.builder()
        .id(scrumTeam.getId())
        .productId(scrumTeam.getProductId())
        .teamName(scrumTeam.getTeamName())
        .businessDomain(scrumTeam.getBusinessDomain())
        .description(scrumTeam.getDescription())
        .poEmployeeId(scrumTeam.getPoEmployeeId())
        .status(scrumTeam.getStatus())
        .createdAt(scrumTeam.getCreatedAt())
        .updatedAt(scrumTeam.getUpdatedAt())
        .build();
  }
}
