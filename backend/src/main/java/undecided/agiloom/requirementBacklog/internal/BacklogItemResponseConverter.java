package undecided.agiloom.requirementBacklog.internal;

import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;

/** BacklogItem／BacklogItemComment エンティティを Response DTO に変換するコンバーターです。 */
@Component
class BacklogItemResponseConverter {

  BacklogItemResponse toBacklogItemResponse(BacklogItem backlogItem) {
    return BacklogItemResponse.builder()
        .id(backlogItem.getId())
        .title(backlogItem.getTitle())
        .description(backlogItem.getDescription())
        .status(backlogItem.getStatus())
        .statusLabel(backlogItem.getStatus().label())
        .priority(backlogItem.getPriority())
        .creatorEmployeeId(backlogItem.getCreatorEmployeeId())
        .creatorName(backlogItem.getCreatorName())
        .sprintId(backlogItem.getSprintId() != null ? String.valueOf(backlogItem.getSprintId().value()) : null)
        .scrumTeamId(
            backlogItem.getScrumTeamId() != null ? String.valueOf(backlogItem.getScrumTeamId().value()) : null)
        .parentItemId(
            backlogItem.getParentItemId() != null && !backlogItem.getParentItemId().isEmpty()
                ? String.valueOf(backlogItem.getParentItemId().value())
                : null)
        .value(backlogItem.getValue())
        .qualityCategory(backlogItem.getQualityCategory())
        .qualityCategoryLabel(
            backlogItem.getQualityCategory() != null
                ? backlogItem.getQualityCategory().label()
                : null)
        .storyPoint(backlogItem.getStoryPoint())
        .createdAt(backlogItem.getCreatedAt())
        .updatedAt(backlogItem.getUpdatedAt())
        .build();
  }

  BacklogItemCommentResponse toCommentResponse(BacklogItemComment comment) {
    return BacklogItemCommentResponse.builder()
        .id(comment.getId())
        .backlogItemId(
            comment.getBacklogItem() != null ? String.valueOf(comment.getBacklogItem().getId().value()) : null)
        .authorEmployeeId(comment.getAuthorEmployeeId())
        .authorName(comment.getAuthorName())
        .body(comment.getBody())
        .createdAt(comment.getCreatedAt())
        .parentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
        .hasReplies(comment.isHasReplies())
        .build();
  }
}
