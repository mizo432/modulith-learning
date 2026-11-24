package undecided.notification.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSprintAssignmentChanged;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/** バックログアイテムのスプリント割り当て変更イベントの通知リスナー（Issue #525 段階3）。 */
@Component
@RequiredArgsConstructor
class BacklogItemSprintAssignmentEventListener {

  private final NotificationEntityRepository notificationRepository;
  private final NotificationScrumTeamMemberRepository scrumTeamMemberRepository;
  private final EmailNotificationService emailNotificationService;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  @ApplicationModuleListener
  public void onBacklogItemSprintAssignmentChanged(BacklogItemSprintAssignmentChanged event) {
    throw new UnsupportedOperationException("not implemented yet");
  }
}
