package undecided.erp.shared.date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TodayTest {

  @Test
  public void testNewInstance() {
    LocalDate today = LocalDate.now();
    Today instance = Today.newInstance();
    assertEquals(today, instance.value(),
        "The value of Today instance should match the current date");
  }

}
