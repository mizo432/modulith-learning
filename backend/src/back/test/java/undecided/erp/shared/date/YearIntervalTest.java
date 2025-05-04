package undecided.erp.shared.date;

import java.time.Year;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class YearIntervalTest {

  @Test
  public void testCreate_withTwoParameters() {
    ApplicableYear beginYear = ApplicableYear.of(Year.of(2000));
    ApplicableYear endYear = ApplicableYear.of(Year.of(2025));
    YearInterval interval = YearInterval.create(beginYear, endYear);
    Assertions.assertEquals(beginYear, interval.getBeginYear());
    Assertions.assertEquals(endYear, interval.getEndYear());
  }

  @Test
  public void testCreate_withOneParameters() {
    ApplicableYear beginYear = ApplicableYear.of(Year.of(2000));
    YearInterval interval = YearInterval.create(beginYear);
    Assertions.assertEquals(beginYear, interval.getBeginYear());
    Assertions.assertEquals(ApplicableYear.MAX, interval.getEndYear());
  }
}
