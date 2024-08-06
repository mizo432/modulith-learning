package undecided.erp.relationship.domain.model.party.person;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Birthday(LocalDate value) {

  public static final Birthday EMPTY = new Birthday(null);

  public static Birthday of(LocalDate value) {
    return new Birthday(value);

  }

  public static Birthday reconstruct(LocalDate value) {
    if (isNull(value)) {
      return EMPTY;
    }
    return new Birthday(value);

  }

  public Integer dateInteger() {
    if (isNull(value)) {
      return null;
    }

    DateTimeFormatter df = DateTimeFormatter.ofPattern("uuuuMMdd");
    return Integer.parseInt(value.format(df));
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }
}
