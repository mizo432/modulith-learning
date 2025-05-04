package undecided.erp.shared.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Month;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("適用月を操作するApplicableMonthクラスのテスト")
public class ApplicableMonthTest {

  @Nested
  @DisplayName("isBeforeメソッドテスト")
  class IsBeforeMethodTest {

    @Test
    @DisplayName("指定された月が後の月の場合はtrueが返される")
    public void shouldReturnTrueWhenSpecifiedMonthIsAfter() {
      ApplicableMonth january = ApplicableMonth.of(Month.JANUARY);
      ApplicableMonth march = ApplicableMonth.of(Month.MARCH);

      assertThat(january.isBefore(march)).isTrue();
    }

    @Test
    @DisplayName("指定された月が同じ月の場合はfalseが返される")
    public void shouldReturnFalseWhenSpecifiedMonthIsSame() {
      ApplicableMonth january = ApplicableMonth.of(Month.JANUARY);
      ApplicableMonth anotherJanuary = ApplicableMonth.of(Month.JANUARY);

      assertThat(january.isBefore(anotherJanuary)).isFalse();
    }

    @Test
    @DisplayName("指定された月が前の月の場合はfalseが返される")
    public void shouldReturnFalseWhenSpecifiedMonthIsBefore() {
      ApplicableMonth march = ApplicableMonth.of(Month.MARCH);
      ApplicableMonth january = ApplicableMonth.of(Month.JANUARY);

      assertThat(march.isBefore(january)).isFalse();
    }

    @Test
    @DisplayName("引数がnullの場合は、IllegalArgumentExceptionを送出する")
    public void shouldThrowIllegalArgumentExceptionWhenParameterIsNull() {
      ApplicableMonth january = ApplicableMonth.of(Month.JANUARY);

      assertThatThrownBy(() -> january.isBefore(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("month is marked non-null but is null");
    }
  }
}
