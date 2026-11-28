package undecided.agiloom.requirementBacklog.internal;

import java.time.Clock;
import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムの内容更新と削除を行うコマンドサービス。 */
@Service
@Transactional
class UpdateBacklogItemCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final Clock clock;

  UpdateBacklogItemCommandService(BacklogItemRepository backlogItemRepository, Clock clock) {
    this.backlogItemRepository = backlogItemRepository;
    this.clock = clock;
  }

  void execute(@NonNull UpdateBacklogItemCommand command) {
    BacklogItem backlogItem = findOrThrow(command.getBacklogItemId());
    backlogItem.update(
        command.getTitle(), command.getDescription(), command.getPriority(), command.getSprintId(), command.getValue(), LocalDateTime.now(clock));
    backlogItemRepository.save(backlogItem);
  }

  void delete(@NonNull SnowflakeId backlogItemId) {
    BacklogItem backlogItem = findOrThrow(backlogItemId);
    backlogItemRepository.delete(backlogItem);
  }

  private BacklogItem findOrThrow(SnowflakeId backlogItemId) {
    return backlogItemRepository
        .findById(backlogItemId)
        .orElseThrow(() -> new IllegalArgumentException("BacklogItem not found: " + backlogItemId));
  }
}
