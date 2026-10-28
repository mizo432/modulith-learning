package undecided.jobMgmt.executionMonitoring.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingRequest {

  private String targetType;
  private String targetId;
  private String channelType;
  private String channelConfig;
  private boolean notifyOnSuccess;
  private boolean notifyOnFailure;
  private boolean notifyOnUnknown;
  private String subjectTemplate;
  private String bodyTemplate;
}
