package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
class BacklogItemQueryController {

  private final BacklogItemQueryService backlogItemQueryService;
  private final BacklogItemResponseConverter backlogItemResponseConverter;

  BacklogItemQueryController(
      BacklogItemQueryService backlogItemQueryService,
      BacklogItemResponseConverter backlogItemResponseConverter) {
    this.backlogItemQueryService = backlogItemQueryService;
    this.backlogItemResponseConverter = backlogItemResponseConverter;
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
    return backlogItemResponseConverter.toBacklogItemResponse(backlogItem);
  }

  @GetMapping("/api/v1/backlog-items/{backlogItemId}/comments")
  @Transactional(readOnly = true)
  List<BacklogItemCommentResponse> getComments(@PathVariable String backlogItemId) {
    SnowflakeId id = SnowflakeId.of(Long.parseLong(backlogItemId));
    return backlogItemQueryService.findComments(id).stream()
        .map(backlogItemResponseConverter::toCommentResponse)
        .toList();
  }
}
