package undecided.agiloom.productDiscovery.internal.presentation;

import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.productDiscovery.internal.command.CreateScrumTeamCommand;
import undecided.agiloom.productDiscovery.internal.command.CreateScrumTeamCommandService;
import undecided.agiloom.productDiscovery.internal.command.UpdateScrumTeamCommand;
import undecided.agiloom.productDiscovery.internal.command.UpdateScrumTeamCommandService;
import undecided.agiloom.productDiscovery.internal.query.ScrumTeamQueryService;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/products/{productId}/scrum-teams")
public class ScrumTeamCommandController {

  private final CreateScrumTeamCommandService createScrumTeamCommandService;
  private final UpdateScrumTeamCommandService updateScrumTeamCommandService;
  private final ScrumTeamQueryService scrumTeamQueryService;
  private final ScrumTeamResponseConverter scrumTeamResponseConverter;
  private final ApplicationEventPublisher eventPublisher;

  public ScrumTeamCommandController(
      CreateScrumTeamCommandService createScrumTeamCommandService,
      UpdateScrumTeamCommandService updateScrumTeamCommandService,
      ScrumTeamQueryService scrumTeamQueryService,
      ScrumTeamResponseConverter scrumTeamResponseConverter,
      ApplicationEventPublisher eventPublisher) {
    this.createScrumTeamCommandService = createScrumTeamCommandService;
    this.updateScrumTeamCommandService = updateScrumTeamCommandService;
    this.scrumTeamQueryService = scrumTeamQueryService;
    this.scrumTeamResponseConverter = scrumTeamResponseConverter;
    this.eventPublisher = eventPublisher;
  }

  @PostMapping
  public ResponseEntity<ScrumTeamResponse> createScrumTeam(
      @PathVariable String productId, @Valid @RequestBody CreateScrumTeamRequest request) {
    SnowflakeId productSnowflakeId = SnowflakeId.of(Long.parseLong(productId));
    CreateScrumTeamCommand command =
        CreateScrumTeamCommand.builder()
            .productId(productSnowflakeId)
            .name(request.getName())
            .businessDomain(request.getBusinessDomain())
            .poEmployeeId(request.getPoEmployeeId())
            .poName(request.getPoName())
            .smEmployeeId(request.getSmEmployeeId())
            .smName(request.getSmName())
            .build();

    SnowflakeId id = createScrumTeamCommandService.execute(command);
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(id).orElseThrow();
    // 他事業領域がサブセットを保持できるよう、変更イベントを発行する
    eventPublisher.publishEvent(ScrumTeamChanged.of(scrumTeam));
    return ResponseEntity.ok(scrumTeamResponseConverter.toScrumTeamResponse(scrumTeam));
  }

  @PatchMapping("/{scrumTeamId}")
  public ResponseEntity<ScrumTeamResponse> updateScrumTeam(
      @PathVariable String productId,
      @PathVariable String scrumTeamId,
      @Valid @RequestBody UpdateScrumTeamRequest request) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    updateScrumTeamCommandService.execute(
        UpdateScrumTeamCommand
            .builder()
            .id(snowflakeId)
            .name(request.getName())
            .businessDomain(request.getBusinessDomain())
            .poEmployeeId(request.getPoEmployeeId())
            .poName(request.getPoName())
            .smEmployeeId(request.getSmEmployeeId())
            .smName(request.getSmName())
            .build());
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    eventPublisher.publishEvent(ScrumTeamChanged.of(scrumTeam));
    return ResponseEntity.ok(scrumTeamResponseConverter.toScrumTeamResponse(scrumTeam));
  }

  @DeleteMapping("/{scrumTeamId}")
  public ResponseEntity<Void> deleteScrumTeam(
      @PathVariable String productId, @PathVariable String scrumTeamId) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    ScrumTeam scrumTeam =
        scrumTeamQueryService
            .findById(snowflakeId)
            .orElseThrow(() -> new IllegalArgumentException("ScrumTeam not found: " + scrumTeamId));
    updateScrumTeamCommandService.delete(snowflakeId);
    eventPublisher.publishEvent(ScrumTeamChanged.removed(scrumTeam));
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{scrumTeamId}/po")
  public ResponseEntity<ScrumTeamResponse> assignPo(
      @PathVariable String productId,
      @PathVariable String scrumTeamId,
      @Valid @RequestBody AssignPoRequest request) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    updateScrumTeamCommandService.assignPo(
        snowflakeId, request.getPoEmployeeId(), request.getPoName());
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    eventPublisher.publishEvent(ScrumTeamChanged.of(scrumTeam));
    return ResponseEntity.ok(scrumTeamResponseConverter.toScrumTeamResponse(scrumTeam));
  }

  @PatchMapping("/{scrumTeamId}/po/unassign")
  public ResponseEntity<ScrumTeamResponse> unassignPo(
      @PathVariable String productId, @PathVariable String scrumTeamId) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    updateScrumTeamCommandService.unassignPo(snowflakeId);
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    eventPublisher.publishEvent(ScrumTeamChanged.of(scrumTeam));
    return ResponseEntity.ok(scrumTeamResponseConverter.toScrumTeamResponse(scrumTeam));
  }

  @PatchMapping("/{scrumTeamId}/sm")
  public ResponseEntity<ScrumTeamResponse> assignSm(
      @PathVariable String productId,
      @PathVariable String scrumTeamId,
      @Valid @RequestBody AssignSmRequest request) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    updateScrumTeamCommandService.assignSm(
        snowflakeId, request.getSmEmployeeId(), request.getSmName());
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    eventPublisher.publishEvent(ScrumTeamChanged.of(scrumTeam));
    return ResponseEntity.ok(scrumTeamResponseConverter.toScrumTeamResponse(scrumTeam));
  }

  @PatchMapping("/{scrumTeamId}/sm/unassign")
  public ResponseEntity<ScrumTeamResponse> unassignSm(
      @PathVariable String productId, @PathVariable String scrumTeamId) {
    SnowflakeId snowflakeId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    updateScrumTeamCommandService.unassignSm(snowflakeId);
    ScrumTeam scrumTeam = scrumTeamQueryService.findById(snowflakeId).orElseThrow();
    eventPublisher.publishEvent(ScrumTeamChanged.of(scrumTeam));
    return ResponseEntity.ok(scrumTeamResponseConverter.toScrumTeamResponse(scrumTeam));
  }
}
