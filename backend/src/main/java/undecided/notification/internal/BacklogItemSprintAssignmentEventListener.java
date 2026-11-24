package undecided.notification.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.BacklogItemSprintAssignmentChanged;
import undecided.notification.spi.NotificationEntity;
import undecided.notification.spi.NotificationType;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/** バックログアイテムのスプリント割り当て変更イベントの通知リスナー（Issue #525）。 */
@Component
@RequiredArgsConstructor
class BacklogItemSprintAssignmentEventListener {

  private final NotificationEntityRepository notificationRepository;
  private final NotificationScrumTeamMemberRepository scrumTeamMemberRepository;
  private final EmailNotificationService emailNotificationService;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  @ApplicationModuleListener
  public void onBacklogItemSprintAssignmentChanged(BacklogItemSprintAssignmentChanged event) {
    Map<SnowflakeId, NotificationScrumTeamMember> recipients = new LinkedHashMap<>();
    for (NotificationScrumTeamMember member :
        scrumTeamMemberRepository.findByScrumTeamId(event.scrumTeamId())) {
      if (!member.employeeId().equals(event.actorEmployeeId())) {
        recipients.merge(
            member.employeeId(),
            member,
            (existing, candidate) ->
                existing.email() == null || existing.email().isEmpty() ? candidate : existing);
      }
    }

    String title = "バックログアイテムのスプリント割り当てが変更されました";
    String content = "バックログアイテムのスプリント割り当てが変更されました";
    for (NotificationScrumTeamMember recipient : recipients.values()) {
      NotificationEntity notification =
          NotificationEntity.builder()
              .id(snowflakeIdGenerator.nextIdValue())
              .recipientId(recipient.employeeId())
              .backlogItemId(event.backlogItemId())
              .title(title)
              .content(content)
              .type(NotificationType.BACKLOG_ITEM_SPRINT_ASSIGNMENT_CHANGED)
              .build();
      notificationRepository.save(notification);

      if (recipient.email() != null && !recipient.email().isEmpty()) {
        emailNotificationService.sendAsync(recipient.email(), title, content);
      }
    }
  }
}
