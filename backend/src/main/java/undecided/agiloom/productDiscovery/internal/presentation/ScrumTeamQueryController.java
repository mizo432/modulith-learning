package undecided.agiloom.productDiscovery.internal.presentation;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.productDiscovery.internal.query.ScrumTeamQueryService;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/products/{productId}/scrum-teams")
public class ScrumTeamQueryController {

  private final ScrumTeamQueryService scrumTeamQueryService;

  public ScrumTeamQueryController(ScrumTeamQueryService scrumTeamQueryService) {
    this.scrumTeamQueryService = scrumTeamQueryService;
  }

  @GetMapping
  public List<ScrumTeamResponse> getScrumTeams(
      @PathVariable String productId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    SnowflakeId productSnowflakeId = SnowflakeId.of(Long.parseLong(productId));
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return scrumTeamQueryService
        .findByProductId(productSnowflakeId)
        .stream()
        .map(this::toResponse)
        .toList();
  }

  @GetMapping("/{scrumTeamId}")
  public ScrumTeamResponse getScrumTeam(
      @PathVariable String productId, @PathVariable @NotNull String scrumTeamId) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    ScrumTeam scrumTeam =
        scrumTeamQueryService
            .findById(snowflakeId)
            .orElseThrow(
                () -> new IllegalArgumentException("ScrumTeam not found: " + scrumTeamId));
    return toResponse(scrumTeam);
  }

  ScrumTeamResponse toResponse(ScrumTeam scrumTeam) {
    return ScrumTeamResponse.builder()
        .id(scrumTeam.getId())
        .name(scrumTeam.getName())
        .businessDomain(scrumTeam.getBusinessDomain())
        .poEmployeeId(scrumTeam.getPoEmployeeId())
        .poName(scrumTeam.getPoName())
        .productId(
            scrumTeam.getProduct() != null ? String.valueOf(scrumTeam.getProduct().getId()) : null)
        .createdAt(scrumTeam.getCreatedAt())
        .updatedAt(scrumTeam.getUpdatedAt())
        .build();
  }
}
