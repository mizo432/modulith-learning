package undecided.agiloom.requirementBacklog.internal;

import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.requirementBacklog.spi.BacklogItemEstimated;
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

    // 権限判定：遷移種別ごとに異なる権限を適用
    BacklogItemStatus currentStatus = backlogItem.getStatus();
    BacklogItemStatus targetStatus = command.getTargetStatus();

    if (currentStatus == BacklogItemStatus.FROZEN && targetStatus == BacklogItemStatus.SUGGESTED) {
      // FROZEN → SUGGESTED（復帰）は PO のみ許可
      if (!scrumTeam.isPo(command.getActorEmployeeId())) {
        throw new BacklogItemPermissionDeniedException(
            "Only the Product Owner can unfreeze a backlog item");
      }
    } else if (currentStatus == BacklogItemStatus.ACCEPTED && targetStatus == BacklogItemStatus.READY) {
      // ACCEPTED → READY はチームメンバー全員が許可
      if (!scrumTeam.isTeamMember(command.getActorEmployeeId())) {
        throw new BacklogItemPermissionDeniedException(
            "Only team members can mark a backlog item as ready");
      }
    } else if (currentStatus == BacklogItemStatus.READY && targetStatus == BacklogItemStatus.SUGGESTED) {
      // READY → SUGGESTED はチームメンバー全員が許可
      if (!scrumTeam.isTeamMember(command.getActorEmployeeId())) {
        throw new BacklogItemPermissionDeniedException(
            "Only team members can revert a backlog item from ready to suggested");
      }
    } else if (currentStatus == BacklogItemStatus.READY && targetStatus == BacklogItemStatus.ACCEPTED) {
      // READY → ACCEPTED（逆遷移）はチームメンバー全員が許可（Issue #717 の決定）
      if (!scrumTeam.isTeamMember(command.getActorEmployeeId())) {
        throw new BacklogItemPermissionDeniedException(
            "Only team members can revert a backlog item from ready to accepted");
      }
    } else if (currentStatus == BacklogItemStatus.READY && targetStatus == BacklogItemStatus.ESTIMATED) {
      // READY → ESTIMATED はチームメンバー全員が許可
      if (!scrumTeam.isTeamMember(command.getActorEmployeeId())) {
        throw new BacklogItemPermissionDeniedException(
            "Only team members can estimate a backlog item");
      }
    } else if (currentStatus == BacklogItemStatus.ESTIMATED && targetStatus == BacklogItemStatus.READY) {
      // ESTIMATED → READY はチームメンバー全員が許可
      if (!scrumTeam.isTeamMember(command.getActorEmployeeId())) {
        throw new BacklogItemPermissionDeniedException(
            "Only team members can revert a backlog item from estimated to ready");
      }
    } else {
      // SUGGESTED → ACCEPTED, SUGGESTED → FROZEN は PO / SM のみが許可
      if (!scrumTeam.canChangeStatusBy(command.getActorEmployeeId())) {
        throw new BacklogItemPermissionDeniedException(
            "Only the Scrum Master or Product Owner of the team can change the status of a backlog item");
      }
    }

    BacklogItemStatus fromStatus = backlogItem.getStatus();
    backlogItem.changeStatus(command.getTargetStatus());
    backlogItemRepository.save(backlogItem);

    LocalDateTime now = LocalDateTime.now();
    eventPublisher.publishEvent(
        new BacklogItemStatusChanged(
            backlogItem.getId(),
            backlogItem.getScrumTeamId(),
            fromStatus,
            command.getTargetStatus(),
            command.getActorEmployeeId(),
            now));

    // READY → ESTIMATED 遷移時に BacklogItemEstimated イベントを発行
    if (fromStatus == BacklogItemStatus.READY && command.getTargetStatus() == BacklogItemStatus.ESTIMATED) {
      eventPublisher.publishEvent(
          new BacklogItemEstimated(
              backlogItem.getId(),
              backlogItem.getScrumTeamId(),
              backlogItem.getStoryPoint(),
              command.getActorEmployeeId(),
              now));
    }
  }
}
