package undecided.erp.shared.date;

import static undecided.erp.common.primitive.Objects2.isNull;
import static undecided.erp.common.verifier.ObjectVerifiers.verifyNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Objects;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import undecided.erp.common.dateProvider.DateProvider;
import undecided.erp.common.verifier.LocalDateVerifiers;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicableDate {

  public static final LocalDate MAX_DATE = LocalDate.of(10000, 1, 1)
      .minusDays(2);

  /**
   * MAXは、アプリケーションで使用できる最大の日付を表します。 これはApplicableDateクラスのインスタンスです。
   */
  public static final ApplicableDate MAX = new ApplicableDate(MAX_DATE);

  public static final ApplicableDate EMPTY = new ApplicableDate(null);

  @JsonValue
  private LocalDate value;

  @JsonCreator
  public static ApplicableDate of(LocalDate value) {
    LocalDateVerifiers.verifyAtMost(value,
        () -> new IllegalArgumentException("Value cannot be after MAX_DATE"), MAX_DATE);
    if (isNull(value)) {
      return EMPTY;
    }

    return new ApplicableDate(value);
  }

  /**
   * 昨日の日付を表すApplicableDateインスタンスを返します。
   *
   * @return 昨日の日付を表すApplicableDateインスタンス
   */
  public static ApplicableDate yesterday() {
    return new ApplicableDate(DateProvider.currentLocalDate()
        .minusDays(1));

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicableDate that = (ApplicableDate) o;
    return Objects.equal(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  public ApplicableMonth getMonth() {
    if (isNull(value)) {
      ApplicableMonth.empty();
    }
    return ApplicableMonth.of(value.getMonth());
  }

  /**
   * 日付をyyyyMMddの形式で整数に変換して返します。
   *
   * @return 整数形式の日付
   * @throws IllegalStateException 値がnullの場合
   */
  public int dateInteger() {
    verifyNotNull(value, () -> new IllegalStateException("value is null"));
    return value.getYear() * 10000 + value.getMonthValue() * 100 + value.getDayOfMonth();

  }
}
