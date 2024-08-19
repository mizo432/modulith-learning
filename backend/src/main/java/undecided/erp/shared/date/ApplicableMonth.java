package undecided.erp.shared.date;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.time.Month;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.verifier.ObjectVerifiers;
import undecided.erp.shared.entity.SingleValue;

@Getter
public class ApplicableMonth implements SingleValue<Month> {

  public static final ApplicableMonth MAX_MONTH = new ApplicableMonth(Month.DECEMBER);
  public static final ApplicableMonth EMPTY = new ApplicableMonth();

  private final Month value;

  public ApplicableMonth(Month month) {
    this.value = month;
  }

  public ApplicableMonth() {
    this.value = null;
  }

  public static ApplicableMonth of(Month month) {
    return new ApplicableMonth(month);

  }

  public static ApplicableMonth empty() {
    return EMPTY;

  }

  @Override
  public String toString() {
    if (isNull(value)) {
      return null;
    }
    return value.toString();
  }

  public boolean isBefore(@NonNull ApplicableMonth month) {
    ObjectVerifiers.verifyNotNull(value, () -> new IllegalArgumentException("値が空です"));
    ValueObjects.checkNotEmpty(this,
        () -> new IllegalArgumentException("比較対象の月が空です"));

    return value.getValue() < month.getValue().getValue();
  }

  @Override
  public boolean isEmpty() {
    return isNull(value);
  }
}
