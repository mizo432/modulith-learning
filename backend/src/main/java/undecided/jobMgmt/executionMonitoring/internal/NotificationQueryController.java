package undecided.jobMgmt.executionMonitoring.internal;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.jobMgmt.executionMonitoring.application.query.NotificationQuery;
import undecided.jobMgmt.executionMonitoring.presentation.dto.NotificationSettingResponse;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationQueryController {

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
}
