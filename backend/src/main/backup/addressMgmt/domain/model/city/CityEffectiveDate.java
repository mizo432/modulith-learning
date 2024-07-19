package undecided.erp.addressMgmt.domain.model.city;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Embeddable
public class CityEffectiveDate {

  public static final CityEffectiveDate EMPTY = new CityEffectiveDate();
  public static final CityEffectiveDate MAX = new CityEffectiveDate(ApplicableDate.MAX);
  @Embedded
  private ApplicableDate value = ApplicableDate.EMPTY;

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

  public static CityEffectiveDate reconstruct(LocalDate effectiveDate) {
    return new CityEffectiveDate(ApplicableDate.of(effectiveDate));
  }

  @Override
  public String toString() {
    return String.valueOf(value.toString());

  }
}
