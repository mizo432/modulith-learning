package undecided.erp.addressMgmt.domain.model.city;

import java.time.LocalDate;
import lombok.Getter;
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
 *
 * <p>使用例:
 * <pre>{@code
 * EffectiveDate date1 = EffectiveDate.of(LocalDate.of(2022, 6, 30));
 * EffectiveDate date2 = EffectiveDate.of(ApplicableDate.of(LocalDate.of(2022, 6, 30)));
 * }</pre>
 */
@Getter
public class CityEffectiveDate {

  public static final CityEffectiveDate MAX = new CityEffectiveDate(ApplicableDate.MAX);
  private final ApplicableDate value;

  private CityEffectiveDate(ApplicableDate value) {
    this.value = value;
  }


  public static CityEffectiveDate of(LocalDate value) {
    if (value.isAfter(ApplicableDate.MAX_DATE)) {
      throw new IllegalArgumentException("Value cannot be after MAX_DATE");
    }

    return of(ApplicableDate.of(value));
  }

  public static CityEffectiveDate of(ApplicableDate value) {
    if (value.isAfter(ApplicableDate.MAX)) {
      throw new IllegalArgumentException("Value cannot be after MAX");
    }

    return new CityEffectiveDate(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value.toString());

  }
}
