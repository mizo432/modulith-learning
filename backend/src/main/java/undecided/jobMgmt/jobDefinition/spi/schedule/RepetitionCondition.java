package undecided.jobMgmt.jobDefinition.spi.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Cron式による繰り返し条件を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepetitionCondition {

  @Column(name = "cron_expression", nullable = false, length = 100)
  private String cronExpression;

  @Column(name = "timezone", nullable = false, length = 50)
  private String timezone;

  public RepetitionCondition(String cronExpression, String timezone) {
    if (cronExpression == null || cronExpression.isBlank()) {
      throw new IllegalArgumentException("Cron expression must not be null or blank");
    }
    if (timezone == null || timezone.isBlank()) {
      throw new IllegalArgumentException("Timezone must not be null or blank");
    }
    this.cronExpression = cronExpression;
    this.timezone = timezone;
  }
}
