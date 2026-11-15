package undecided.agiloom.sprintExecution.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("EndDayOfWeekのテスト")
class EndDayOfWeekTest {

  @Nested
  @DisplayName("getDayOfWeekメソッドのテスト")
  class GetDayOfWeekTest {

    @Test
    @DisplayName("対応するDayOfWeekが返される")
    void shouldReturnCorrectDayOfWeek() {
      assertThat(EndDayOfWeek.MONDAY.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
      assertThat(EndDayOfWeek.TUESDAY.getDayOfWeek()).isEqualTo(DayOfWeek.TUESDAY);
      assertThat(EndDayOfWeek.WEDNESDAY.getDayOfWeek()).isEqualTo(DayOfWeek.WEDNESDAY);
      assertThat(EndDayOfWeek.THURSDAY.getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
      assertThat(EndDayOfWeek.FRIDAY.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    }
  }

  @Nested
  @DisplayName("fromDayOfWeekメソッドのテスト")
  class FromDayOfWeekTest {

    @Test
    @DisplayName("営業日のDayOfWeekからEndDayOfWeekが取得される")
    void shouldConvertFromWeekday() {
      assertThat(EndDayOfWeek.fromDayOfWeek(DayOfWeek.MONDAY)).isEqualTo(EndDayOfWeek.MONDAY);
      assertThat(EndDayOfWeek.fromDayOfWeek(DayOfWeek.FRIDAY)).isEqualTo(EndDayOfWeek.FRIDAY);
    }

    @Test
    @DisplayName("土曜日が指定された場合例外がスローされる")
    void shouldThrowExceptionForSaturday() {
      assertThatThrownBy(() -> EndDayOfWeek.fromDayOfWeek(DayOfWeek.SATURDAY))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("日曜日が指定された場合例外がスローされる")
    void shouldThrowExceptionForSunday() {
      assertThatThrownBy(() -> EndDayOfWeek.fromDayOfWeek(DayOfWeek.SUNDAY))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }
}
