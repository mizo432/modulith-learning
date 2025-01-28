package undecided.erp.shared.date;

import java.time.YearMonth;

public class ApplicableYearMonth {

  public static final YearMonth MAX_YEAR_MONTH = YearMonth.of(9999, 12);

  public static final ApplicableYearMonth MAX = new ApplicableYearMonth(MAX_YEAR_MONTH);
  private final YearMonth value;

  private ApplicableYearMonth(YearMonth value) {
    this.value = value;
  }


  public static ApplicableYearMonth of(YearMonth value) {
    if (value.isAfter(MAX_YEAR_MONTH)) {
      throw new IllegalArgumentException("Value cannot be after MAX_YEAR_MONTH");
    }

    return new ApplicableYearMonth(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
