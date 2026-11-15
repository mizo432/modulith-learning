package undecided.agiloom.scrumTeamManagement.internal.presentation;

import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.scrumTeamManagement.internal.command.CreateScrumTeamCommand;
import undecided.agiloom.scrumTeamManagement.internal.command.CreateScrumTeamCommandService;
import undecided.agiloom.scrumTeamManagement.internal.command.UpdateScrumTeamCommand;
import undecided.agiloom.scrumTeamManagement.internal.command.UpdateScrumTeamCommandService;
import undecided.agiloom.scrumTeamManagement.internal.query.ScrumTeamQueryService;
import undecided.agiloom.scrumTeamManagement.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/scrum-teams")
public class ScrumTeamCommandController {

  private final CreateScrumTeamCommandService createScrumTeamCommandService;
  private final UpdateScrumTeamCommandService updateScrumTeamCommandService;
  private final ScrumTeamQueryService scrumTeamQueryService;
  private final ScrumTeamQueryController scrumTeamQueryController;

  public ScrumTeamCommandController(
      CreateScrumTeamCommandService createScrumTeamCommandService,
      UpdateScrumTeamCommandService updateScrumTeamCommandService,
      ScrumTeamQueryService scrumTeamQueryService,
      ScrumTeamQueryController scrumTeamQueryController) {
    this.createScrumTeamCommandService = createScrumTeamCommandService;
    this.updateScrumTeamCommandService = updateScrumTeamCommandService;
    this.scrumTeamQueryService = scrumTeamQueryService;
    this.scrumTeamQueryController = scrumTeamQueryController;
  }

  @PostMapping
  public ResponseEntity<ScrumTeamResponse> createScrumTeam(
      @Valid @RequestBody CreateScrumTeamRequest request) {
    CreateScrumTeamCommand command =
        CreateScrumTeamCommand.builder()
            .productId(request.getProductId())
            .teamName(request.getTeamName())
            .businessDomain(request.getBusinessDomain())
            .description(request.getDescription())
            .poEmployeeId(request.getPoEmployeeId())
            .build();

    SnowflakeId id = createScrumTeamCommandService.execute(command);
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(id).orElseThrow();
    return ResponseEntity.ok(scrumTeamQueryController.toResponse(scrumTeam));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ScrumTeamResponse> updateScrumTeam(
      @PathVariable String id, @Valid @RequestBody UpdateScrumTeamRequest request) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateScrumTeamCommandService.execute(
        UpdateScrumTeamCommand.builder()
            .id(snowflakeId)
            .teamName(request.getTeamName())
            .businessDomain(request.getBusinessDomain())
            .description(request.getDescription())
            .build());
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    return ResponseEntity.ok(scrumTeamQueryController.toResponse(scrumTeam));
  }

  @PostMapping("/{id}/assign-po")
  public ResponseEntity<ScrumTeamResponse> assignPo(
      @PathVariable String id, @Valid @RequestBody AssignPoRequest request) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateScrumTeamCommandService.assignPo(snowflakeId, request.getPoEmployeeId());
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    return ResponseEntity.ok(scrumTeamQueryController.toResponse(scrumTeam));
  }

  @DeleteMapping("/{id}/assign-po")
  public ResponseEntity<ScrumTeamResponse> unassignPo(@PathVariable String id) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateScrumTeamCommandService.unassignPo(snowflakeId);
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    return ResponseEntity.ok(scrumTeamQueryController.toResponse(scrumTeam));
  }

  @PatchMapping("/{id}/archive")
  public ResponseEntity<ScrumTeamResponse> archiveScrumTeam(@PathVariable String id) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateScrumTeamCommandService.archive(snowflakeId);
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    return ResponseEntity.ok(scrumTeamQueryController.toResponse(scrumTeam));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteScrumTeam(@PathVariable String id) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(id));
    updateScrumTeamCommandService.delete(snowflakeId);
    return ResponseEntity.noContent().build();
  }
}
