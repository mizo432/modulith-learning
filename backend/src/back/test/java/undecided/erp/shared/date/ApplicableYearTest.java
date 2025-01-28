package undecided.erp.shared.date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Year;
import org.junit.jupiter.api.Test;

class ApplicableYearTest {

  @Test
  void shouldReturnApplicableYearWhenYearIsBeforeMaxYear() {
    Year validYear = Year.of(9998);
    ApplicableYear applicableYear = ApplicableYear.of(validYear);
    assertNotNull(applicableYear);
    assertEquals(validYear, applicableYear.getValue());
  }

  @Test
  void shouldThrowExceptionWhenYearIsAfterMaxYear() {
    Year invalidYear = Year.of(10000);
    assertThrows(IllegalArgumentException.class, () -> ApplicableYear.of(invalidYear));
  }
}
