package undecided.agiloom.sprintExecution.internal.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import undecided.generic.calendarReg.spi.Holiday;
import undecided.generic.calendarReg.spi.HolidayQuery;

/**
 * 期間内の営業日数（土日・祝日を除く日数）を数えるドメインサービス。
 *
 * <p>祝日はカレンダーレジストリ（calendarReg）の HolidayQuery から取得する。
 */
@Component
public class BusinessDayCounter {

  private final HolidayQuery holidayQuery;

  public BusinessDayCounter(HolidayQuery holidayQuery) {
    this.holidayQuery = holidayQuery;
  }

  /**
   * 開始日から終了日まで（両端を含む）の営業日数を返す。
   *
   * @throws IllegalArgumentException 開始日が終了日より後の場合
   */
  public int countBusinessDays(@NonNull LocalDate from, @NonNull LocalDate to) {
    if (from.isAfter(to)) {
      throw new IllegalArgumentException("from must not be after to: " + from + " > " + to);
    }
    Set<LocalDate> holidayDates =
        holidayQuery.findBetween(from, to).stream().map(Holiday::getHolidayDate).collect(Collectors.toSet());
    int count = 0;
    for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
      DayOfWeek dayOfWeek = date.getDayOfWeek();
      if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
        continue;
      }
      if (holidayDates.contains(date)) {
        continue;
      }
      count++;
    }
    return count;
  }
}
