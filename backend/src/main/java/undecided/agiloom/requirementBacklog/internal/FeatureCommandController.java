package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.requirementBacklog.spi.Feature;
import undecided.agiloom.requirementBacklog.spi.FeatureStatus;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
class FeatureCommandController {

  private final CreateFeatureCommandService createFeatureCommandService;
  private final UpdateFeatureCommandService updateFeatureCommandService;
  private final DeleteFeatureCommandService deleteFeatureCommandService;
  private final AssignBacklogItemToFeatureCommandService assignBacklogItemToFeatureCommandService;
  private final RemoveBacklogItemFromFeatureCommandService removeBacklogItemFromFeatureCommandService;
  private final FeatureQueryService featureQueryService;
  private final FeatureResponseConverter featureResponseConverter;

  FeatureCommandController(
      CreateFeatureCommandService createFeatureCommandService,
      UpdateFeatureCommandService updateFeatureCommandService,
      DeleteFeatureCommandService deleteFeatureCommandService,
      AssignBacklogItemToFeatureCommandService assignBacklogItemToFeatureCommandService,
      RemoveBacklogItemFromFeatureCommandService removeBacklogItemFromFeatureCommandService,
      FeatureQueryService featureQueryService,
      FeatureResponseConverter featureResponseConverter) {
    this.createFeatureCommandService = createFeatureCommandService;
    this.updateFeatureCommandService = updateFeatureCommandService;
    this.deleteFeatureCommandService = deleteFeatureCommandService;
    this.assignBacklogItemToFeatureCommandService = assignBacklogItemToFeatureCommandService;
    this.removeBacklogItemFromFeatureCommandService = removeBacklogItemFromFeatureCommandService;
    this.featureQueryService = featureQueryService;
    this.featureResponseConverter = featureResponseConverter;
  }

  @PostMapping("/api/v1/features")
  ResponseEntity<FeatureResponse> createFeature(@Valid @RequestBody CreateFeatureRequest request) {
    CreateFeatureCommand command =
        CreateFeatureCommand.builder()
            .scrumTeamId(SnowflakeId.of(Long.parseLong(request.getScrumTeamId())))
            .name(request.getName())
            .description(request.getDescription())
            .actorEmployeeId(SnowflakeId.of(Long.parseLong(request.getActorEmployeeId())))
            .build();

    SnowflakeId id = createFeatureCommandService.execute(command);
    Feature feature = featureQueryService.findById(id).orElseThrow();
    return ResponseEntity.status(201).body(featureResponseConverter.toFeatureResponse(feature));
  }

  @PutMapping("/api/v1/features/{id}")
  ResponseEntity<FeatureResponse> updateFeature(
      @PathVariable String id, @Valid @RequestBody UpdateFeatureRequest request) {
    UpdateFeatureCommand command =
        UpdateFeatureCommand.builder()
            .featureId(SnowflakeId.of(Long.parseLong(id)))
            .scrumTeamId(SnowflakeId.of(Long.parseLong(request.getScrumTeamId())))
            .name(request.getName())
            .description(request.getDescription())
            .status(request.getStatus())
            .actorEmployeeId(SnowflakeId.of(Long.parseLong(request.getActorEmployeeId())))
            .build();

    updateFeatureCommandService.execute(command);
    Feature feature = featureQueryService.findById(SnowflakeId.of(Long.parseLong(id))).orElseThrow();
    return ResponseEntity.ok(featureResponseConverter.toFeatureResponse(feature));
  }

  @DeleteMapping("/api/v1/features/{id}")
  ResponseEntity<Void> deleteFeature(
      @PathVariable String id, @Valid @RequestBody DeleteFeatureRequest request) {
    DeleteFeatureCommand command =
        DeleteFeatureCommand.builder()
            .featureId(SnowflakeId.of(Long.parseLong(id)))
            .scrumTeamId(SnowflakeId.of(Long.parseLong(request.getScrumTeamId())))
            .actorEmployeeId(SnowflakeId.of(Long.parseLong(request.getActorEmployeeId())))
            .build();

    deleteFeatureCommandService.execute(command);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/api/v1/features/{featureId}/backlog-items/{backlogItemId}")
  ResponseEntity<Void> assignBacklogItem(
      @PathVariable String featureId,
      @PathVariable String backlogItemId,
      @Valid @RequestBody AssignBacklogItemToFeatureRequest request) {
    AssignBacklogItemToFeatureCommand command =
        AssignBacklogItemToFeatureCommand.builder()
            .featureId(SnowflakeId.of(Long.parseLong(featureId)))
            .backlogItemId(SnowflakeId.of(Long.parseLong(backlogItemId)))
            .scrumTeamId(SnowflakeId.of(Long.parseLong(request.getScrumTeamId())))
            .actorEmployeeId(SnowflakeId.of(Long.parseLong(request.getActorEmployeeId())))
            .build();

    assignBacklogItemToFeatureCommandService.execute(command);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/api/v1/features/{featureId}/backlog-items/{backlogItemId}")
  ResponseEntity<Void> removeBacklogItem(
      @PathVariable String featureId,
      @PathVariable String backlogItemId,
      @Valid @RequestBody RemoveBacklogItemFromFeatureRequest request) {
    RemoveBacklogItemFromFeatureCommand command =
        RemoveBacklogItemFromFeatureCommand.builder()
            .featureId(SnowflakeId.of(Long.parseLong(featureId)))
            .backlogItemId(SnowflakeId.of(Long.parseLong(backlogItemId)))
            .scrumTeamId(SnowflakeId.of(Long.parseLong(request.getScrumTeamId())))
            .actorEmployeeId(SnowflakeId.of(Long.parseLong(request.getActorEmployeeId())))
            .build();

    removeBacklogItemFromFeatureCommandService.execute(command);
    return ResponseEntity.noContent().build();
  }
}
