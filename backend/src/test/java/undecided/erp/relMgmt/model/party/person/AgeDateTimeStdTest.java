package undecided.erp.relMgmt.model.party.person;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.shared.date.Today;

class AgeDateTimeStdTest {

  @Test
  void checkAgeWhenBornToday() {
    Birthday today = Birthday.of(LocalDate.now());
    Today currentDate = Today.of(LocalDate.now());
    AgeDateTimeStd result = AgeDateTimeStd.calculateFrom(today, currentDate);
    assertEquals(0, result.value());
  }

  @Test
  void checkAgeWhenBornTenYearsAgo() {
    Birthday tenYearsAgo = Birthday.of(LocalDate.now().minusYears(10));
    Today currentDate = Today.of(LocalDate.now());
    AgeDateTimeStd result = AgeDateTimeStd.calculateFrom(tenYearsAgo, currentDate);
    assertEquals(10, result.value());
  }

}
