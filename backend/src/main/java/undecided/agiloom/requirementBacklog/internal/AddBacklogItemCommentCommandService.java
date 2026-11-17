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

    BacklogItem backlogItem =
        backlogItemRepository
            .findById(command.getBacklogItemId())
            .orElseThrow(
                () -> new IllegalArgumentException("BacklogItem not found: " + command.getBacklogItemId()));

    BacklogItemComment comment =
        BacklogItemComment.builder()
            .id(snowflakeIdGenerator.nextIdValue())
            .backlogItem(backlogItem)
            .authorEmployeeId(command.getAuthorEmployeeId())
            .authorName(command.getAuthorName())
            .body(command.getBody())
            .build();

    return backlogItemCommentRepository.save(comment).getId();
  }
}
