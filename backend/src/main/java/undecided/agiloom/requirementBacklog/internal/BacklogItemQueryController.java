package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
class BacklogItemQueryController {

  private final BacklogItemQueryService backlogItemQueryService;
  private final BacklogItemResponseConverter backlogItemResponseConverter;
  private final BacklogItemDependencyQueryService backlogItemDependencyQueryService;

  BacklogItemQueryController(
      BacklogItemQueryService backlogItemQueryService,
      BacklogItemResponseConverter backlogItemResponseConverter,
      BacklogItemDependencyQueryService backlogItemDependencyQueryService) {
    this.backlogItemQueryService = backlogItemQueryService;
    this.backlogItemResponseConverter = backlogItemResponseConverter;
    this.backlogItemDependencyQueryService = backlogItemDependencyQueryService;
  }

  @GetMapping("/api/v1/scrum-teams/{scrumTeamId}/backlog-items")
  @Transactional(readOnly = true)
  List<BacklogItemResponse> getBacklogItems(
      @PathVariable String scrumTeamId,
      @RequestParam(required = false) BacklogItemStatus status) {
    SnowflakeId teamId = SnowflakeId.of(Long.parseLong(scrumTeamId));
    return backlogItemQueryService.findByScrumTeamId(teamId, status).stream()
        .map(backlogItemResponseConverter::toBacklogItemResponse)
        .toList();
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}")
  @Transactional(readOnly = true)
  BacklogItemResponse getBacklogItem(@PathVariable String backlogItemId) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    BacklogItem backlogItem =
        backlogItemQueryService
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("BacklogItem not found: " + backlogItemId));
    BacklogItemResponse response = backlogItemResponseConverter.toBacklogItemResponse(backlogItem);
    response.setAttachments(backlogItemQueryService.findAttachments(id));
    return response;
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}/attachments")
  @Transactional(readOnly = true)
  List<BacklogItemAttachmentResponse> getAttachments(
      @PathVariable String backlogItemId, @RequestParam("actorEmployeeId") String actorEmployeeId) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    return backlogItemQueryService.findAttachments(
        id, SnowflakeId.of(Long.parseLong(actorEmployeeId)));
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}/attachments/{documentId}/download")
  @Transactional(readOnly = true)
  ResponseEntity<InputStreamResource> downloadAttachment(
      @PathVariable String backlogItemId,
      @PathVariable String documentId,
      @RequestParam("actorEmployeeId") String actorEmployeeId) {
    undecided.generic.documentMgmt.spi.DocumentContent content =
        backlogItemQueryService.downloadAttachment(
            SnowflakeId.of(Long.parseLong(backlogItemId)),
            SnowflakeId.of(Long.parseLong(documentId)),
            SnowflakeId.of(Long.parseLong(actorEmployeeId)));
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(content.contentType()));
    headers.setContentDisposition(
        ContentDisposition.attachment().filename(content.fileName()).build());
    headers.setContentLength(content.fileSize());
    return ResponseEntity.ok().headers(headers).body(new InputStreamResource(content.content()));
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}/attachments/{documentId}/versions")
  @Transactional(readOnly = true)
  List<BacklogItemAttachmentResponse> getAttachmentVersions(
      @PathVariable String backlogItemId,
      @PathVariable String documentId,
      @RequestParam("actorEmployeeId") String actorEmployeeId) {
    return backlogItemQueryService.findAttachmentVersions(
        SnowflakeId.of(Long.parseLong(backlogItemId)),
        SnowflakeId.of(Long.parseLong(documentId)),
        SnowflakeId.of(Long.parseLong(actorEmployeeId)));
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}/assignable-sprints")
  @Transactional(readOnly = true)
  List<AssignableSprintResponse> getAssignableSprints(
      @PathVariable String backlogItemId, @RequestParam String actorEmployeeId) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    return backlogItemQueryService.findAssignableSprints(
        id, SnowflakeId.of(Long.parseLong(actorEmployeeId)));
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}/comments")
  @Transactional(readOnly = true)
  List<BacklogItemCommentResponse> getComments(@PathVariable String backlogItemId) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    return backlogItemQueryService.findComments(id).stream()
        .map(backlogItemResponseConverter::toCommentResponse)
        .toList();
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}/dependencies")
  @Transactional(readOnly = true)
  BacklogItemDependenciesResponse getDependencies(@PathVariable String backlogItemId) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    return backlogItemDependencyQueryService.getDependencies(id);
  }
}
