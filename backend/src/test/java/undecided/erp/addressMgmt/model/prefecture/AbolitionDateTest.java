package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.date.ApplicableDate;

class AbolitionDateTest {

  @Test
  void shouldCreateAbolitionDateFromLocalDate() {
    LocalDate date = LocalDate.now();
    AbolitionDate abolitionDate = AbolitionDate.of(date);

    assertNotNull(abolitionDate);
    assertEquals(date, abolitionDate.getValue().getValue());
  }

  @Test
  void shouldThrowExceptionWhenProvidedDateIsAfterMaxDate() {
    LocalDate date = ApplicableDate.MAX_DATE.plusDays(1);

    assertThrows(IllegalArgumentException.class, () -> AbolitionDate.of(date));
  }

}