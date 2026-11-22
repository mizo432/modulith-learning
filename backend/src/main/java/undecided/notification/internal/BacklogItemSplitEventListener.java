package undecided.notification.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSplit;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/** バックログアイテム分割イベントの通知リスナー。 */
@RequiredArgsConstructor
class BacklogItemSplitEventListener {

  private final NotificationEntityRepository notificationRepository;
  private final NotificationScrumTeamMemberRepository scrumTeamMemberRepository;
  private final EmailNotificationService emailNotificationService;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  @ApplicationModuleListener
  public void onBacklogItemSplit(BacklogItemSplit event) {
    throw new UnsupportedOperationException();
  }
}