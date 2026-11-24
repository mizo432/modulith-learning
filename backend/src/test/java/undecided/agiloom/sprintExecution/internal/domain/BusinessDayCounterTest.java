package undecided.agiloom.sprintExecution.internal.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.generic.calendarReg.spi.Holiday;
import undecided.generic.calendarReg.spi.HolidayQuery;

@Tag("small")
@DisplayName("BusinessDayCounterのテスト")
class BusinessDayCounterTest {

  private final HolidayQuery holidayQuery = mock(HolidayQuery.class);
  private final BusinessDayCounter counter = new BusinessDayCounter(holidayQuery);

  private Holiday holiday(LocalDate date) {
    Holiday holiday = mock(Holiday.class);
    when(holiday.getHolidayDate()).thenReturn(date);
    return holiday;
  }

  @Test
  @DisplayName("月曜から金曜までの期間は、祝日がなければ5となる")
  void countsWeekdays() {
    when(holidayQuery.findBetween(any(), any())).thenReturn(List.of());

    assertThat(counter.countBusinessDays(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 6)))
        .isEqualTo(5);
  }

  @Test
  @DisplayName("期間内の祝日は数えない")
  void excludesHolidays() {
    Holiday holiday = holiday(LocalDate.of(2026, 11, 3));
    when(holidayQuery.findBetween(any(), any())).thenReturn(List.of(holiday));

    assertThat(counter.countBusinessDays(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 6)))
        .isEqualTo(4);
  }

  @Test
  @DisplayName("土日のみの期間は0となる")
  void weekendOnlyIsZero() {
    when(holidayQuery.findBetween(any(), any())).thenReturn(List.of());

    assertThat(counter.countBusinessDays(LocalDate.of(2026, 11, 7), LocalDate.of(2026, 11, 8)))
        .isZero();
  }

  @Test
  @DisplayName("開始日と終了日が同じ平日のときは1となる")
  void singleWeekdayIsOne() {
    when(holidayQuery.findBetween(any(), any())).thenReturn(List.of());

    assertThat(counter.countBusinessDays(LocalDate.of(2026, 11, 2), LocalDate.of(2026, 11, 2)))
        .isEqualTo(1);
  }

  @Test
  @DisplayName("開始日が終了日より後のときは例外となる")
  void invertedRangeThrows() {
    assertThatThrownBy(
            () -> counter.countBusinessDays(LocalDate.of(2026, 11, 6), LocalDate.of(2026, 11, 2)))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
