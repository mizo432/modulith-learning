package undecided.jbJobMgmt.executionMonitoring.spi.notificationsetting;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 通知テンプレートを表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationTemplate {

  @Column(name = "subject_template", length = 200)
  private String subjectTemplate;

  @Column(name = "body_template", columnDefinition = "text")
  private String bodyTemplate;

  public NotificationTemplate(String subjectTemplate, String bodyTemplate) {
    if (subjectTemplate == null || subjectTemplate.isBlank()) {
      throw new IllegalArgumentException("Subject template must not be null or blank");
    }
    this.subjectTemplate = subjectTemplate;
    this.bodyTemplate = bodyTemplate;
  }

  public static NotificationTemplate defaultJobNotification() {
    return new NotificationTemplate(
        "[Job] {{jobName}} - {{status}}",
        "ジョブ「{{jobName}}」の実行結果：{{status}}\n開始時刻：{{startTime}}\n終了時刻：{{endTime}}\nメッセージ：{{message}}"
    );
  }
}
