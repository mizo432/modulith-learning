package undecided.erp.shared.date;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.dateProvider.DateProvider;

@Getter
public class ApplicableDate {

  public static final LocalDate MAX_DATE = LocalDate.of(10000, 1, 1)
      .minusDays(2);

  /**
   * MAXは、アプリケーションで使用できる最大の日付を表します。 これはApplicableDateクラスのインスタンスです。
   */
  public static final ApplicableDate MAX = new ApplicableDate(MAX_DATE);
  public static final ApplicableDate EMPTY = new ApplicableDate(null);
  private final LocalDate value;

  private ApplicableDate(LocalDate value) {
    this.value = value;
  }


  public static ApplicableDate of(LocalDate value) {
    if (value.isAfter(MAX_DATE)) {
      throw new IllegalArgumentException("Value cannot be after MAX_DATE");
    }

    return new ApplicableDate(value);
  }

  /**
   * 昨日の日付を表すApplicableDateインスタンスを返します。
   *
   * @return 昨日の日付を表すApplicableDateインスタンス
   */
  public static ApplicableDate yesterday() {
    return new ApplicableDate(DateProvider.currentLocalDate().minusDays(1));

  }

  /**
   * 現在の日付を表す新しいApplicableDateオブジェクトを返します。
   *
   * @return 現在の日付を表す新しいApplicableDateオブジェクト
   */
  public static ApplicableDate now() {
    return new ApplicableDate(DateProvider.currentLocalDate());

  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public boolean isAfter(@NonNull ApplicableDate other) {
    return this.value.isAfter(other.value);
  }

  public ApplicableDate plusDays(int i) {
    return new ApplicableDate(value.plusDays(i));
  }
}
