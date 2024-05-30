package undecided.erp.shared.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateIntervalTest {

  @Test
  void shouldCreateWithBeginAndEndDate() {
    ApplicableDate beginDate = ApplicableDate.of(LocalDate.now());
    ApplicableDate endDate = ApplicableDate.of(LocalDate.of(3000, 1, 1));
    DateInterval interval = DateInterval.create(beginDate, endDate);
    assertNotNull(interval);
    assertEquals(beginDate, interval.getBeginDate());
    assertEquals(endDate, interval.getEndDate());
  }

  @Test
  void shouldCreateWithBeginDateOnly() {
    ApplicableDate beginDate = ApplicableDate.of(LocalDate.now());
    DateInterval interval = DateInterval.create(beginDate);
    assertNotNull(interval);
    assertEquals(beginDate, interval.getBeginDate());
    assertEquals(ApplicableDate.MAX, interval.getEndDate());
  }
}
