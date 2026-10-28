package undecided.jobMgmt.executionMonitoring.presentation.dto;

import undecided.jobMgmt.executionMonitoring.spi.notificationsetting.NotificationSetting;

public class NotificationSettingResponse {

  private Long id;
  private String targetType;
  private String targetId;
  private String channelType;
  private String channelConfig;
  private boolean notifyOnSuccess;
  private boolean notifyOnFailure;
  private boolean notifyOnUnknown;
  private String subjectTemplate;
  private String bodyTemplate;
  private boolean enabled;

  public Long getId() { return id; }
  public String getTargetType() { return targetType; }
  public String getTargetId() { return targetId; }
  public String getChannelType() { return channelType; }
  public String getChannelConfig() { return channelConfig; }
  public boolean isNotifyOnSuccess() { return notifyOnSuccess; }
  public boolean isNotifyOnFailure() { return notifyOnFailure; }
  public boolean isNotifyOnUnknown() { return notifyOnUnknown; }
  public String getSubjectTemplate() { return subjectTemplate; }
  public String getBodyTemplate() { return bodyTemplate; }
  public boolean isEnabled() { return enabled; }

  public static NotificationSettingResponse from(NotificationSetting ns) {
    NotificationSettingResponse response = new NotificationSettingResponse();
    response.id = ns.getId().value();
    response.targetType = ns.getTarget().getTargetType();
    response.targetId = ns.getTarget().getTargetId();
    response.channelType = ns.getChannel().getType();
    response.channelConfig = ns.getChannel().getConfig();
    response.notifyOnSuccess = ns.getCondition().isNotifyOnSuccess();
    response.notifyOnFailure = ns.getCondition().isNotifyOnFailure();
    response.notifyOnUnknown = ns.getCondition().isNotifyOnUnknown();
    response.subjectTemplate = ns.getTemplate().getSubjectTemplate();
    response.bodyTemplate = ns.getTemplate().getBodyTemplate();
    response.enabled = ns.isEnabled();
    return response;
  }
}
