package undecided.erp.shared.date;

import java.time.LocalDate;

public class ApplicableDate {

  public static final LocalDate MAX_DATE = LocalDate.of(10000, 1, 1)
      .minusDays(2);

  public static final ApplicableDate MAX = new ApplicableDate(MAX_DATE);
  private final LocalDate value;

  private ApplicableDate(LocalDate value) {
    this.value = value;
  }


  public static ApplicableDate of(LocalDate value) {
    if (value.isAfter(MAX_DATE)) {
      throw new IllegalArgumentException("Value cannot be after MAX_DATE");
    }

    return new ApplicableDate(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
