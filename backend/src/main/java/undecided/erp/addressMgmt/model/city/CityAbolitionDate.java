package undecided.erp.addressMgmt.model.city;

import java.time.LocalDate;
import lombok.Getter;
import undecided.erp.shared.date.ApplicableDate;

/**
 * AbolitionDateクラスは、何かが廃止された日付を表します。
 * <p>
 * これは、アプリケーションで適用可能な日付を表すApplicableDateクラスのラッパークラスです。
 * AbolitionDateクラスは、廃止日を作成し、その有効性をチェックするメソッドを提供します。
 */
@Getter
public class CityAbolitionDate {

  public static final CityAbolitionDate MAX = new CityAbolitionDate(ApplicableDate.MAX);
  /**
   * AbolitionDateの値。これはアプリケーションで適用可能な日付を表しています。 これはApplicableDateクラスのラッパークラスです。
   */
  private final ApplicableDate value;

  /**
   * 与えられたApplicableDateの値でAbolitionDateを設定します。
   *
   * @param value 設定するApplicableDateの値
   */
  private CityAbolitionDate(ApplicableDate value) {
    this.value = value;
  }


  /**
   * 与えられたLocalDate値でAbolitionDateオブジェクトを作成します。
   * <p>
   * 値は MAX_DATE 以降であってはならず、そうでない場合は IllegalArgumentException がスローされます。 このメソッドは AbolitionDate
   * オブジェクトを作成するために ApplicableDate.of メソッドをラッピングします。
   *
   * @param value AbolitionDateに設定するLocalDate値
   * @return 与えられた値でAbolitionDateオブジェクト
   * @throws IllegalArgumentException 値がMAX_DATE以降の場合
   */
  public static CityAbolitionDate of(LocalDate value) {
    if (value.isAfter(ApplicableDate.MAX_DATE)) {
      throw new IllegalArgumentException("Value cannot be after MAX_DATE");
    }

    return new CityAbolitionDate(ApplicableDate.of(value));
  }

  /**
   * 昨日の日付を表すPrefectureAbolitionDateオブジェクトを返します。
   *
   * @return 昨日の日付を表すPrefectureAbolitionDateオブジェクト
   */
  public static CityAbolitionDate yesterday() {
    return new CityAbolitionDate(ApplicableDate.yesterday());

  }

  public static CityAbolitionDate now() {
    return new CityAbolitionDate(ApplicableDate.now());

  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
