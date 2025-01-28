package undecided.erp.addressMgmt.model.machiAza;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.machiAza.MachiAzaAbolitionDate;
import undecided.erp.shared.date.ApplicableDate;

class MachiAzaAbolitionDateTest {

  @Test
  void testOf_withNullValue() {
    MachiAzaAbolitionDate result = MachiAzaAbolitionDate.of(null);
    assertNotNull(result, "Expected non-null MachiAzaAbolitionDate object");
  }

  @Test
  void testOf_withValidDate() {
    LocalDate testDate = LocalDate.of(2020, 1, 15);
    MachiAzaAbolitionDate result = MachiAzaAbolitionDate.of(testDate);
    assertNotNull(result, "Expected non-null MachiAzaAbolitionDate object");
    assertEquals(testDate, result.getValue().getValue(), "Expected date to match input date");
  }

  @Test
  void testOf_withDateAfterMaxDate() {
    LocalDate maxDate = ApplicableDate.MAX_DATE;
    LocalDate dateAfterMaxDate = maxDate.plusDays(1);
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> MachiAzaAbolitionDate.of(dateAfterMaxDate),
        "Expected IllegalArgumentException when date is after MAX_DATE");
  }
}