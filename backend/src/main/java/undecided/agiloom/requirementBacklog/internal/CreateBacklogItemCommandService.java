package undecided.agiloom.requirementBacklog.internal;

import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import java.time.Clock;
import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * バックログアイテムを作成するコマンドサービス。
 *
 * <p>チームメンバーとステークホルダー（チーム外の従業員）が作成でき、
 * 初期状態は必ず {@code SUGGESTED} になります（エンティティのデフォルト値）。
 * 所属チームの存在は、イベントで連携された自領域のサブセットで確認します。
 */
@Service
@Transactional
class CreateBacklogItemCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final Clock clock;

  CreateBacklogItemCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      SnowflakeIdGenerator snowflakeIdGenerator,
      Clock clock) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.snowflakeIdGenerator = snowflakeIdGenerator;
    this.clock = clock;
  }

  SnowflakeId execute(@NonNull CreateBacklogItemCommand command) {
    checkNonEmpty(command.getTitle(), () -> new IllegalArgumentException("BacklogItem title must not be empty"));
    checkNonEmpty(command.getCreatorName(), () -> new IllegalArgumentException("Creator name must not be empty"));

    scrumTeamSubsetRepository
        .findById(command.getScrumTeamId())
        .orElseThrow(
            () -> new IllegalArgumentException("ScrumTeam not found: " + command.getScrumTeamId()));

    LocalDateTime now = LocalDateTime.now(clock);
    BacklogItem backlogItem =
        BacklogItem.builder()
            .id(snowflakeIdGenerator.nextIdValue())
            .title(command.getTitle())
            .description(command.getDescription())
            .priority(command.getPriority())
            .creatorEmployeeId(command.getCreatorEmployeeId())
            .creatorName(command.getCreatorName())
            .sprintId(command.getSprintId())
            .scrumTeamId(command.getScrumTeamId())
            .createdAt(now)
            .updatedAt(now)
            .build();

    return backlogItemRepository.save(backlogItem).getId();
  }
}
