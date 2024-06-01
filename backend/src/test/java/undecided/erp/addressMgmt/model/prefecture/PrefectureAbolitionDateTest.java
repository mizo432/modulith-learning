package undecided.erp.addressMgmt.model.prefecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.date.ApplicableDate;

class PrefectureAbolitionDateTest {

  @Test
  void shouldCreateAbolitionDateFromLocalDate() {
    LocalDate date = LocalDate.now();
    PrefectureAbolitionDate prefectureAbolitionDate = PrefectureAbolitionDate.of(date);

    assertNotNull(prefectureAbolitionDate);
    assertEquals(date, prefectureAbolitionDate.getValue().getValue());
  }

  @Test
  void shouldThrowExceptionWhenProvidedDateIsAfterMaxDate() {
    LocalDate date = ApplicableDate.MAX_DATE.plusDays(1);

    assertThrows(IllegalArgumentException.class, () -> PrefectureAbolitionDate.of(date));
  }

}