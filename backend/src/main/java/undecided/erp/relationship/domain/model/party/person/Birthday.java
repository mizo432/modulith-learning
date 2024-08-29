package undecided.erp.relationship.domain.model.party.person;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.time.LocalDate;
import undecided.erp.shared.date.ApplicableDate;
import undecided.erp.shared.entity.SingleValue;

public class Birthday implements SingleValue<LocalDate> {

  public static final Birthday EMPTY = new Birthday(null);
  private final ApplicableDate value;

  public Birthday(LocalDate value) {
    this.value = ApplicableDate.of(value);

  }

  public static Birthday of(LocalDate value) {
    return new Birthday(value);

  }

  public static Birthday reconstruct(LocalDate value) {
    if (isNull(value)) {
      return EMPTY;
    }
    return new Birthday(value);

  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }

  @Override
  public LocalDate getValue() {
    return value.getValue();
  }

  @Override
  public boolean isEmpty() {
    return isNull(value.getValue());

  }

  public int dateInteger() {
    return value.dateInteger();
  }
}
