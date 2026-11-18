package undecided.agiloom.requirementBacklog.internal;

import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemComment;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/** バックログアイテムへコメントを追加するコマンドサービス。 */
@Service
@Transactional
class AddBacklogItemCommentCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final BacklogItemCommentRepository backlogItemCommentRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  AddBacklogItemCommentCommandService(
      BacklogItemRepository backlogItemRepository,
      BacklogItemCommentRepository backlogItemCommentRepository,
      SnowflakeIdGenerator snowflakeIdGenerator) {
    this.backlogItemRepository = backlogItemRepository;
    this.backlogItemCommentRepository = backlogItemCommentRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
  }

  SnowflakeId execute(@NonNull AddBacklogItemCommentCommand command) {
    checkNonEmpty(command.getAuthorName(), () -> new IllegalArgumentException("Author name must not be empty"));
    checkNonEmpty(command.getBody(), () -> new IllegalArgumentException("Comment body must not be empty"));

    // 親コメントの検証（backlogItem検証より前に実行）
    BacklogItemComment parentComment = null;
    if (command.getParentCommentId() != null) {
      parentComment =
          backlogItemCommentRepository
              .findById(command.getParentCommentId())
              .orElseThrow(
                  () -> new IllegalArgumentException(
                      "Parent comment not found: " + command.getParentCommentId()));

      // 親コメントが同じbacklogItemに属するか検証
      if (!parentComment.getBacklogItem().getId().equals(command.getBacklogItemId())) {
        throw new IllegalArgumentException(
            "Parent comment belongs to a different backlog item");
      }
    }

    BacklogItem backlogItem =
        backlogItemRepository
            .findById(command.getBacklogItemId())
            .orElseThrow(
                () -> new IllegalArgumentException("BacklogItem not found: " + command.getBacklogItemId()));

    SnowflakeId newId = snowflakeIdGenerator.nextIdValue();

    // 自身を親とするチェック（採番されたIDが親コメントIDと一致する場合）
    if (parentComment != null && newId.equals(parentComment.getId())) {
      throw new IllegalArgumentException("コメントを自身の子返信として追加することはできません");
    }

    BacklogItemComment comment =
        BacklogItemComment.builder()
            .id(newId)
            .backlogItem(backlogItem)
            .authorEmployeeId(command.getAuthorEmployeeId())
            .authorName(command.getAuthorName())
            .body(command.getBody())
            .parentComment(parentComment)
            .build();

    return backlogItemCommentRepository.save(comment).getId();
  }
}
