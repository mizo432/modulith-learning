package undecided.notification.internal;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.agiloom.requirementBacklog.spi.BacklogItemCommentAdded;

/** チーム構成とコメント投稿者のメールアドレスを通知領域に同期する。 */
@Component
class NotificationScrumTeamSyncListener {

  private final NotificationScrumTeamSubsetRepository scrumTeamRepository;
  private final NotificationEmployeeEmailSubsetRepository emailRepository;

  NotificationScrumTeamSyncListener(
      NotificationScrumTeamSubsetRepository scrumTeamRepository,
      NotificationEmployeeEmailSubsetRepository emailRepository) {
    this.scrumTeamRepository = scrumTeamRepository;
    this.emailRepository = emailRepository;
  }

  @ApplicationModuleListener
  void onScrumTeamChanged(ScrumTeamChanged event) {
    if (event.removed()) {
      scrumTeamRepository.deleteById(event.scrumTeamId());
      return;
    }
    scrumTeamRepository.findById(event.scrumTeamId())
        .ifPresentOrElse(
            subset -> subset.apply(event),
            () -> scrumTeamRepository.save(NotificationScrumTeamSubset.from(event)));
  }

  @ApplicationModuleListener
  void onCommentAdded(BacklogItemCommentAdded event) {
    if (event.authorEmployeeId() == null || event.authorEmployeeId().isEmpty()
        || event.occurredAt() == null || event.commentId() == null || event.commentId().isEmpty()) {
      return;
    }
    emailRepository.findById(event.authorEmployeeId())
        .ifPresentOrElse(
            subset -> subset.apply(event),
            () -> emailRepository.save(NotificationEmployeeEmailSubset.from(event)));
  }
}