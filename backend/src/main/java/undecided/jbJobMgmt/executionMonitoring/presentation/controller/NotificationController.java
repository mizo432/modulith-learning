package undecided.jbJobMgmt.executionMonitoring.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jbJobMgmt.executionMonitoring.application.command.NotificationCommand;
import undecided.jbJobMgmt.executionMonitoring.application.query.NotificationQuery;
import undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting.NotificationChannel;
import undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting.NotificationCondition;
import undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting.NotificationSetting;
import undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting.NotificationTarget;
import undecided.jbJobMgmt.executionMonitoring.domain.notificationsetting.NotificationTemplate;
import undecided.jbJobMgmt.executionMonitoring.presentation.dto.NotificationSettingRequest;
import undecided.jbJobMgmt.executionMonitoring.presentation.dto.NotificationSettingResponse;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

  private final NotificationCommand notificationCommand;
  private final NotificationQuery notificationQuery;

  @GetMapping
  public List<NotificationSettingResponse> findAll() {
    return notificationQuery.findAllNotificationSettings().stream()
        .map(NotificationSettingResponse::from)
        .toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<NotificationSettingResponse> findById(@PathVariable String id) {
    return notificationQuery.findNotificationSettingById(SnowflakeId.of(Long.parseLong(id)))
        .map(ns -> ResponseEntity.ok(NotificationSettingResponse.from(ns)))
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public NotificationSettingResponse register(@RequestBody NotificationSettingRequest request) {
    NotificationTarget target = new NotificationTarget(request.getTargetType(), request.getTargetId());
    NotificationChannel channel = new NotificationChannel(request.getChannelType(), request.getChannelConfig());
    NotificationCondition condition = new NotificationCondition(
        request.isNotifyOnSuccess(), request.isNotifyOnFailure(), request.isNotifyOnUnknown());
    NotificationTemplate template = new NotificationTemplate(request.getSubjectTemplate(), request.getBodyTemplate());

    NotificationSetting ns = notificationCommand.registerNotificationSetting(target, channel, condition, template);
    return NotificationSettingResponse.from(ns);
  }

  @PutMapping("/{id}/channel")
  public NotificationSettingResponse changeChannel(@PathVariable String id,
      @RequestBody NotificationSettingRequest request) {
    NotificationChannel channel = new NotificationChannel(request.getChannelType(), request.getChannelConfig());
    NotificationSetting ns = notificationCommand.changeChannel(SnowflakeId.of(Long.parseLong(id)), channel);
    return NotificationSettingResponse.from(ns);
  }

  @PutMapping("/{id}/condition")
  public NotificationSettingResponse changeCondition(@PathVariable String id,
      @RequestBody NotificationSettingRequest request) {
    NotificationCondition condition = new NotificationCondition(
        request.isNotifyOnSuccess(), request.isNotifyOnFailure(), request.isNotifyOnUnknown());
    NotificationSetting ns = notificationCommand.changeCondition(SnowflakeId.of(Long.parseLong(id)), condition);
    return NotificationSettingResponse.from(ns);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    notificationCommand.deleteNotificationSetting(SnowflakeId.of(Long.parseLong(id)));
    return ResponseEntity.noContent().build();
  }
}
