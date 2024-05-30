package undecided.erp.shared.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.YearMonth;
import org.junit.jupiter.api.Test;

class ApplicableYearMonthTest {

  @Test
  void shouldReturnApplicableYearMonth_whenOfYearMonthBeforeMaxYearMonth() {
    YearMonth yearMonth = YearMonth.of(2024, 12);
    ApplicableYearMonth applicableYearMonth = ApplicableYearMonth.of(yearMonth);
    assertEquals("2024-12", applicableYearMonth.toString());
  }

  @Test
  void shouldThrowException_whenOfYearMonthEqualsMaxYearMonth() {
    YearMonth yearMonth = YearMonth.of(10000, 1);
    assertThrows(IllegalArgumentException.class, () -> ApplicableYearMonth.of(yearMonth));
  }
}
