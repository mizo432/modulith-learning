package undecided.erp.shared.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.lang.NonNull;
import undecided.erp.common.dateProvider.DateProvider;

public record Today(LocalDate value) {

  public LocalDate addOneMonth() {
    return value.plusMonths(1);
  }

  public static Today of(@NonNull LocalDate value) {
    return new Today(value);

  }

  public static Today newInstance() {
    return new Today(DateProvider.currentLocalDate());

  }

  public int nextMonthInteger() {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
    return Integer.parseInt(value.format(df));

  }
}
