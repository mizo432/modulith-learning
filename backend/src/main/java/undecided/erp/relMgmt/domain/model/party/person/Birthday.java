package undecided.erp.relMgmt.domain.model.party.person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Birthday(LocalDate value) {

  public static Birthday of(LocalDate value) {
    return new Birthday(value);
  }

  public int dateInteger() {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
    return Integer.parseInt(value.format(df));
  }
}
