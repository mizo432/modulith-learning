package undecided.jobMgmt.jobDefinition.spi.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * スケジュールの有効期間を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidPeriod {

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  public ValidPeriod(LocalDate startDate) {
    if (startDate == null) {
      throw new IllegalArgumentException("Start date must not be null");
    }
    this.startDate = startDate;
    this.endDate = null;
  }

  public ValidPeriod(LocalDate startDate, LocalDate endDate) {
    if (startDate == null) {
      throw new IllegalArgumentException("Start date must not be null");
    }
    if (endDate != null && endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("End date must not be before start date");
    }
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public boolean isValid(LocalDate date) {
    if (date == null) {
      return false;
    }
    if (date.isBefore(startDate)) {
      return false;
    }
    if (endDate != null && date.isAfter(endDate)) {
      return false;
    }
    return true;
  }
}
