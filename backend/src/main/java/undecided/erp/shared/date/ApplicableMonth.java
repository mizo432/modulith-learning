package undecided.erp.shared.date;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.time.Month;
import lombok.Getter;
import lombok.NonNull;
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

  /**
   * このApplicableMonthが指定されたApplicableMonthよりも前かどうかをチェックします。
   *
   * @param month 比較するApplicableMonth
   * @return このApplicableMonthが指定されたApplicableMonthより前の場合はtrue、それ以外の場合はfalse
   * @throws IllegalArgumentException 入力またはこのApplicableMonthがnullの場合
   */
  public boolean isBefore(@NonNull ApplicableMonth month) {
    ValueObjects.checkNotEmpty(month,
        () -> new IllegalArgumentException("比較対象の月が空です"));
    ValueObjects.checkNotEmpty(this,
        () -> new IllegalArgumentException("自身の月が空です"));

    return value.getValue() < month.getValue().getValue();
  }

  @Override
  public boolean isEmpty() {
    return isNull(value);
  }
}
