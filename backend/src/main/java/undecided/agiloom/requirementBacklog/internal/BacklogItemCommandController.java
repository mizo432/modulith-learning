package undecided.agiloom.requirementBacklog.internal;

import jakarta.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
class BacklogItemCommandController {

  private final CreateBacklogItemCommandService createBacklogItemCommandService;
  private final UpdateBacklogItemCommandService updateBacklogItemCommandService;
  private final ChangeBacklogItemStatusCommandService changeBacklogItemStatusCommandService;
  private final EstimateBacklogItemCommandService estimateBacklogItemCommandService;
  private final SplitBacklogItemCommandService splitBacklogItemCommandService;
  private final AddBacklogItemCommentCommandService addBacklogItemCommentCommandService;
  private final DeleteBacklogItemCommentCommandService deleteBacklogItemCommentCommandService;
  private final UpdateBacklogItemCommentCommandService updateBacklogItemCommentCommandService;
  private final BacklogItemQueryService backlogItemQueryService;
  private final BacklogItemResponseConverter backlogItemResponseConverter;
  private final AttachDocumentCommandService attachDocumentCommandService;
  private final DetachDocumentCommandService detachDocumentCommandService;

  BacklogItemCommandController(
      CreateBacklogItemCommandService createBacklogItemCommandService,
      UpdateBacklogItemCommandService updateBacklogItemCommandService,
      ChangeBacklogItemStatusCommandService changeBacklogItemStatusCommandService,
      EstimateBacklogItemCommandService estimateBacklogItemCommandService,
      SplitBacklogItemCommandService splitBacklogItemCommandService,
      AddBacklogItemCommentCommandService addBacklogItemCommentCommandService,
      DeleteBacklogItemCommentCommandService deleteBacklogItemCommentCommandService,
      UpdateBacklogItemCommentCommandService updateBacklogItemCommentCommandService,
      BacklogItemQueryService backlogItemQueryService,
      BacklogItemResponseConverter backlogItemResponseConverter,
      AttachDocumentCommandService attachDocumentCommandService,
      DetachDocumentCommandService detachDocumentCommandService) {
    this.createBacklogItemCommandService = createBacklogItemCommandService;
    this.updateBacklogItemCommandService = updateBacklogItemCommandService;
    this.changeBacklogItemStatusCommandService = changeBacklogItemStatusCommandService;
    this.estimateBacklogItemCommandService = estimateBacklogItemCommandService;
    this.splitBacklogItemCommandService = splitBacklogItemCommandService;
    this.addBacklogItemCommentCommandService = addBacklogItemCommentCommandService;
    this.deleteBacklogItemCommentCommandService = deleteBacklogItemCommentCommandService;
    this.updateBacklogItemCommentCommandService = updateBacklogItemCommentCommandService;
    this.backlogItemQueryService = backlogItemQueryService;
    this.backlogItemResponseConverter = backlogItemResponseConverter;
    this.attachDocumentCommandService = attachDocumentCommandService;
    this.detachDocumentCommandService = detachDocumentCommandService;
  }

  @PostMapping("/api/v1/backlog-items/{backlogItemId}/attachments")
  ResponseEntity<BacklogItemAttachmentResponse> attachDocument(
      @PathVariable String backlogItemId,
      @RequestParam("file") MultipartFile file,
      @RequestParam(value = "uploaderEmployeeId", required = false) String uploaderEmployeeId,
      @RequestParam(value = "uploaderName", required = false) String uploaderName,
      @RequestParam(value = "seriesId", required = false) String seriesId) {
    SnowflakeId itemId = SnowflakeId.of(Long.parseLong(backlogItemId));
    SnowflakeId documentId =
        attachDocumentCommandService.execute(
            AttachDocumentCommand.builder()
                .backlogItemId(itemId)
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .content(inputStreamOf(file))
                .uploaderEmployeeId(
                    uploaderEmployeeId == null || uploaderEmployeeId.isEmpty()
                        ? null
                        : SnowflakeId.of(Long.parseLong(uploaderEmployeeId)))
                .uploaderName(uploaderName)
                .seriesId(
                    seriesId == null || seriesId.isEmpty()
                        ? null
                        : SnowflakeId.of(Long.parseLong(seriesId)))
                .build());
    BacklogItemAttachmentResponse response =
        backlogItemQueryService.findAttachments(itemId).stream()
            .filter(attachment -> attachment.getDocumentId().equals(documentId))
            .findFirst()
            .orElseThrow();
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/api/v1/backlog-items/{backlogItemId}/attachments/{documentId}")
  ResponseEntity<Void> detachDocument(
      @PathVariable String backlogItemId,
      @PathVariable String documentId,
      @RequestParam("actorEmployeeId") String actorEmployeeId) {
    detachDocumentCommandService.execute(
        DetachDocumentCommand.builder()
            .backlogItemId(SnowflakeId.of(Long.parseLong(backlogItemId)))
            .documentId(SnowflakeId.of(Long.parseLong(documentId)))
            .actorEmployeeId(SnowflakeId.of(Long.parseLong(actorEmployeeId)))
            .build());
    return ResponseEntity.noContent().build();
  }

  private static InputStream inputStreamOf(MultipartFile file) {
    try {
      return file.getInputStream();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @PostMapping("/api/v1/scrum-teams/{scrumTeamId}/backlog-items")
  ResponseEntity<BacklogItemResponse> createBacklogItem(
      @PathVariable String scrumTeamId, @Valid @RequestBody CreateBacklogItemRequest request) {
    CreateBacklogItemCommand command =
        CreateBacklogItemCommand.builder()
            .scrumTeamId(SnowflakeId.of(Long.parseLong(scrumTeamId)))
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(request.getPriority())
            .creatorEmployeeId(request.getCreatorEmployeeId())
            .creatorName(request.getCreatorName())
            .sprintId(
                request.getSprintId() != null
                    ? SnowflakeId.of(Long.parseLong(request.getSprintId()))
                    : null)
            .build();

    SnowflakeId id = createBacklogItemCommandService.execute(command);
    BacklogItem backlogItem = backlogItemQueryService.findById(id).orElseThrow();
    return ResponseEntity.ok(backlogItemResponseConverter.toBacklogItemResponse(backlogItem));
  }

  @PatchMapping("/api/v1/backlog-items/{backlogItemId}")
  ResponseEntity<BacklogItemResponse> updateBacklogItem(
      @PathVariable String backlogItemId, @Valid @RequestBody UpdateBacklogItemRequest request) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    updateBacklogItemCommandService.execute(
        UpdateBacklogItemCommand.builder()
            .backlogItemId(id)
            .title(request.getTitle())
            .description(request.getDescription())
            .priority(request.getPriority())
            .sprintId(
                request.getSprintId() != null
                    ? SnowflakeId.of(Long.parseLong(request.getSprintId()))
                    : null)
            .value(request.getValue())
            .build());
    BacklogItem backlogItem = backlogItemQueryService.findById(id).orElseThrow();
    return ResponseEntity.ok(backlogItemResponseConverter.toBacklogItemResponse(backlogItem));
  }

  @DeleteMapping("/api/v1/backlog-items/{backlogItemId}")
  ResponseEntity<Void> deleteBacklogItem(@PathVariable String backlogItemId) {
    updateBacklogItemCommandService.delete(SnowflakeId.of(Long.parseLong(backlogItemId)));
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/api/v1/backlog-items/{backlogItemId}/status")
  ResponseEntity<BacklogItemResponse> changeBacklogItemStatus(
      @PathVariable String backlogItemId,
      @Valid @RequestBody ChangeBacklogItemStatusRequest request) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    changeBacklogItemStatusCommandService.execute(
        ChangeBacklogItemStatusCommand.builder()
            .backlogItemId(id)
            .targetStatus(request.getStatus())
            .actorEmployeeId(request.getActorEmployeeId())
            .build());
    BacklogItem backlogItem = backlogItemQueryService.findById(id).orElseThrow();
    return ResponseEntity.ok(backlogItemResponseConverter.toBacklogItemResponse(backlogItem));
  }

  @PatchMapping("/api/v1/backlog-items/{backlogItemId}/estimate")
  ResponseEntity<BacklogItemResponse> estimateBacklogItem(
      @PathVariable String backlogItemId,
      @Valid @RequestBody EstimateBacklogItemRequest request) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    estimateBacklogItemCommandService.execute(
        EstimateBacklogItemCommand.builder()
            .backlogItemId(id)
            .storyPoint(request.getStoryPoint())
            .actorEmployeeId(request.getActorEmployeeId())
            .build());
    BacklogItem backlogItem = backlogItemQueryService.findById(id).orElseThrow();
    return ResponseEntity.ok(backlogItemResponseConverter.toBacklogItemResponse(backlogItem));
  }

  @PostMapping("/api/v1/backlog-items/{backlogItemId}/split")
  ResponseEntity<SplitBacklogItemResponse> splitBacklogItem(
      @PathVariable String backlogItemId, @Valid @RequestBody SplitBacklogItemRequest request) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    List<SplitBacklogItemCommand.Child> children =
        request.children() != null
            ? request.children().stream()
                .map(
                    child ->
                        child != null
                            ? new SplitBacklogItemCommand.Child(child.title(), child.storyPoint())
                            : null)
                .toList()
            : null;
    splitBacklogItemCommandService.execute(
        new SplitBacklogItemCommand(id, request.actorEmployeeId(), children));
    BacklogItem parent = backlogItemQueryService.findById(id).orElseThrow();
    List<BacklogItemResponse> childResponses =
        backlogItemQueryService.findByParentItemId(id).stream()
            .map(backlogItemResponseConverter::toBacklogItemResponse)
            .toList();
    return ResponseEntity.ok(
        new SplitBacklogItemResponse(
            backlogItemResponseConverter.toBacklogItemResponse(parent), childResponses));
  }

  @PostMapping("/api/v1/backlog-items/{backlogItemId}/comments")
  ResponseEntity<BacklogItemCommentResponse> addComment(
      @PathVariable String backlogItemId, @Valid @RequestBody AddBacklogItemCommentRequest request) {
    SnowflakeId commentId =
        addBacklogItemCommentCommandService.execute(
            AddBacklogItemCommentCommand.builder()
                .backlogItemId(SnowflakeId.of(Long.parseLong(backlogItemId)))
                .authorEmployeeId(request.getAuthorEmployeeId())
                .authorName(request.getAuthorName())
                .body(request.getBody())
                .parentCommentId(request.getParentCommentId())
                .build());
    BacklogItemComment comment =
        backlogItemQueryService
            .findComments(SnowflakeId.of(Long.parseLong(backlogItemId)))
            .stream()
            .filter(c -> commentId.equals(c.getId()))
            .findFirst()
            .orElseThrow();
    return ResponseEntity.ok(backlogItemResponseConverter.toCommentResponse(comment));
  }

  @DeleteMapping("/api/v1/backlog-items/{backlogItemId}/comments/{commentId}")
  ResponseEntity<Void> deleteComment(
      @PathVariable String backlogItemId,
      @PathVariable String commentId,
      @RequestBody DeleteBacklogItemCommentRequest request) {
    deleteBacklogItemCommentCommandService.execute(
        DeleteBacklogItemCommentCommand.builder()
            .backlogItemId(SnowflakeId.of(Long.parseLong(backlogItemId)))
            .commentId(SnowflakeId.of(Long.parseLong(commentId)))
            .actorEmployeeId(request.getActorEmployeeId())
            .build());
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/api/v1/backlog-items/{backlogItemId}/comments/{commentId}")
  ResponseEntity<BacklogItemCommentResponse> updateComment(
      @PathVariable String backlogItemId,
      @PathVariable String commentId,
      @Valid @RequestBody UpdateBacklogItemCommentRequest request) {
    updateBacklogItemCommentCommandService.execute(
        UpdateBacklogItemCommentCommand.builder()
            .commentId(SnowflakeId.of(Long.parseLong(commentId)))
            .body(request.getBody())
            .actorEmployeeId(request.getActorEmployeeId())
            .build());
    BacklogItemComment comment =
        backlogItemQueryService
            .findComments(SnowflakeId.of(Long.parseLong(backlogItemId)))
            .stream()
            .filter(c -> SnowflakeId.of(Long.parseLong(commentId)).equals(c.getId()))
            .findFirst()
            .orElseThrow();
    return ResponseEntity.ok(backlogItemResponseConverter.toCommentResponse(comment));
  }
}
