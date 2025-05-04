package undecided.erp.shared.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.YearMonth;
import org.junit.jupiter.api.Test;

class YearMonthIntervalTest {

  @Test
  void shouldCreateYearMonthIntervalWithBeginAndEnd() {
    ApplicableYearMonth beginYearMonth = ApplicableYearMonth.of(YearMonth.now());
    ApplicableYearMonth endYearMonth = ApplicableYearMonth.of(YearMonth.now());
    YearMonthInterval interval = YearMonthInterval.create(beginYearMonth, endYearMonth);
    assertNotNull(interval);
    assertEquals(beginYearMonth, interval.getBeginYearMonth());
    assertEquals(endYearMonth, interval.getEndYearMonth());
  }

  @Test
  void shouldCreateYearMonthIntervalWithOnlyBegin() {
    ApplicableYearMonth beginYearMonth = ApplicableYearMonth.of(YearMonth.now());
    YearMonthInterval interval = YearMonthInterval.create(beginYearMonth);
    assertNotNull(interval);
    assertEquals(beginYearMonth, interval.getBeginYearMonth());
    assertEquals(ApplicableYearMonth.MAX, interval.getEndYearMonth());
  }
}
