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
 * バックログアイテムの見積もり値を設定するコマンドサービス。
 *
 * <p>見積もり値（StoryPoint）の設定と、値の設定に伴う状態遷移を一体化します（設計決定1）。
 * <ul>
 *   <li>READY 状態の場合：値の設定後に ESTIMATED へ遷移し、{@link BacklogItemStatusChanged} と
 *       {@link BacklogItemEstimated} を各1回発行する</li>
 *   <li>ESTIMATED 状態の場合：値の変更のみを行い、{@link BacklogItemEstimated} を1回発行する</li>
 *   <li>それ以外の状態の場合：{@link IllegalStateException} をスローする</li>
 * </ul>
 * 権限判定はチームメンバーのみが実行可能（{@link ScrumTeamSubset#isTeamMember} で判定）。
 */
@Service
@Transactional
class EstimateBacklogItemCommandService {

  private final BacklogItemRepository backlogItemRepository;
  private final ScrumTeamSubsetRepository scrumTeamSubsetRepository;
  private final ApplicationEventPublisher eventPublisher;

  EstimateBacklogItemCommandService(
      BacklogItemRepository backlogItemRepository,
      ScrumTeamSubsetRepository scrumTeamSubsetRepository,
      ApplicationEventPublisher eventPublisher) {
    this.backlogItemRepository = backlogItemRepository;
    this.scrumTeamSubsetRepository = scrumTeamSubsetRepository;
    this.eventPublisher = eventPublisher;
  }

  void execute(@NonNull EstimateBacklogItemCommand command) {
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

    // 権限判定：チームメンバーのみが許可
    if (!scrumTeam.isTeamMember(command.getActorEmployeeId())) {
      throw new BacklogItemPermissionDeniedException(
          "Only team members can estimate a backlog item");
    }

    BacklogItemStatus currentStatus = backlogItem.getStatus();

    // 見積もり値の設定（StoryPoint値の検証を含む）
    backlogItem.setStoryPoint(command.getStoryPoint());

    LocalDateTime now = LocalDateTime.now();

    if (currentStatus == BacklogItemStatus.READY) {
      // READY → ESTIMATED へ遷移（値の設定と状態遷移の一体化）
      backlogItem.changeStatus(BacklogItemStatus.ESTIMATED);
      backlogItemRepository.save(backlogItem);

      // BacklogItemStatusChanged イベントを発行
      eventPublisher.publishEvent(
          new BacklogItemStatusChanged(
              backlogItem.getId(),
              backlogItem.getScrumTeamId(),
              BacklogItemStatus.READY,
              BacklogItemStatus.ESTIMATED,
              command.getActorEmployeeId(),
              now));

      // BacklogItemEstimated イベントを発行
      eventPublisher.publishEvent(
          new BacklogItemEstimated(
              backlogItem.getId(),
              backlogItem.getScrumTeamId(),
              command.getStoryPoint(),
              command.getActorEmployeeId(),
              now));
    } else if (currentStatus == BacklogItemStatus.ESTIMATED) {
      // ESTIMATED 状態での再評価（値の変更のみ）
      backlogItemRepository.save(backlogItem);

      // BacklogItemEstimated イベントを発行
      eventPublisher.publishEvent(
          new BacklogItemEstimated(
              backlogItem.getId(),
              backlogItem.getScrumTeamId(),
              command.getStoryPoint(),
              command.getActorEmployeeId(),
              now));
    } else {
      // READY および ESTIMATED 以外の状態は見積もり値の設定を許可しない
      throw new IllegalStateException(
          "Cannot estimate a backlog item in status " + currentStatus
              + "; only READY or ESTIMATED status is allowed");
    }
  }
}
