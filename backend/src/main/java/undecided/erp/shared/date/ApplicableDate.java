package undecided.erp.shared.date;

import static undecided.erp.common.primitive.Objects2.isNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Objects;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import undecided.erp.common.dateProvider.DateProvider;
import undecided.erp.common.verifier.LocalDateVerifiers;

@Getter
@Embeddable
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
}
