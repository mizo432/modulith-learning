package undecided.erp.addressMgmt.domain.model.machiAza;

import java.time.LocalDate;
import undecided.erp.shared.date.ApplicableDate;

/**
 * 効力発生日を表現するクラスです。
 *
 * <p>EffectiveDateは、特定の目的で有効と考えられる特定の日付を表す不変のクラスです。
 *
 * <p>EffectiveDateのインスタンスは、{@link #of(LocalDate)} と {@link #of(ApplicableDate)}
 * メソッドを通じて取得できます。EffectiveDateオブジェクトの値は {@link ApplicableDate} のインスタンスです。
 *
 * <p>許される最大のEffectiveDateは、{@link #MAX} という定数によって表され、これは許される最大の {@link ApplicableDate} に等しい。
 */
public class MachiAzaEffectiveDate {

  public static final MachiAzaEffectiveDate MAX = new MachiAzaEffectiveDate(ApplicableDate.MAX);
  public static final MachiAzaEffectiveDate EMPTY = new MachiAzaEffectiveDate(ApplicableDate.EMPTY);
  private final ApplicableDate value;

  private MachiAzaEffectiveDate(ApplicableDate value) {
    this.value = value;
  }

  public LocalDate getValue() {
    if (value == null) {
      return null;
    }
    if (value.getValue() == null) {
      return null;
    }
    return value.getValue();
  }

  public static MachiAzaEffectiveDate of(ApplicableDate value) {
    if (value == null) {
      return EMPTY;
    }
    if (value == ApplicableDate.EMPTY) {
      return EMPTY;
    }
    if (value.isAfter(ApplicableDate.MAX)) {
      throw new IllegalArgumentException("Value cannot be after MAX");
    }

    return new MachiAzaEffectiveDate(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value.toString());

  }
}
