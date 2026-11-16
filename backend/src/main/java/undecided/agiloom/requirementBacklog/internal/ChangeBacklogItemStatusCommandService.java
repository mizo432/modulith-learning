package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatus;
import undecided.agiloom.requirementBacklog.spi.BacklogItemStatusChanged;

/**
 * バックログアイテムの状態変更を行うコマンドサービス。
 *
 * <p>状態変更は、そのチームのSMとPOのみが行えます（Issue #486／#487）。
 * 権限判定は、自領域のサブセット（{@link ScrumTeamSubset}）が保持する PO／SM と操作者を照合します。
 * 権限のない従業員が試みた場合は {@link BacklogItemPermissionDeniedException} をスローします。
 * 遷移ルール自体（SUGGESTED からのみ遷移可能）はエンティティが検証します。
 *
 * <p>遷移が成功した場合は {@link BacklogItemStatusChanged} イベントを発行し、
 * ロングタームイベントとして履歴を残します（Issue #572 の決定）。
 */
@Service
@Transactional
class ChangeBacklogItemStatusCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final ApplicationEventPublisher eventPublisher;

  ChangeBacklogItemStatusCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      ApplicationEventPublisher eventPublisher) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.eventPublisher = eventPublisher;
  }

  void execute(@NonNull ChangeBacklogItemStatusCommand command) {
    BacklogItem backlogItem =
        backlogItemRepository
            .findById(command.getBacklogItemId())
            .orElseThrow(
                () -> new IllegalArgumentException("BacklogItem not found: " + command.getBacklogItemId()));

    ScrumTeamSubset scrumTeam =
        scrumTeamSubsetRepository
            .findById(backlogItem.getScrumTeamId())
            .orElseThrow(
                () -> new IllegalArgumentException("ScrumTeam not found: " + backlogItem.getScrumTeamId()));

    if (!scrumTeam.canChangeStatusBy(command.getActorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException(
          "Only the Scrum Master or Product Owner of the team can change the status of a backlog item");
    }

    BacklogItemStatus fromStatus = backlogItem.getStatus();
    backlogItem.changeStatus(command.getTargetStatus());
    backlogItemRepository.save(backlogItem);

    eventPublisher.publishEvent(
        new BacklogItemStatusChanged(
            backlogItem.getId(),
            backlogItem.getScrumTeamId(),
            fromStatus,
            command.getTargetStatus(),
            command.getActorEmployeeId(),
            LocalDateTime.now()));
  }
}
