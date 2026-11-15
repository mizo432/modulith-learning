package undecided.agiloom.sprintExecution.spi;

import java.time.DayOfWeek;
import org.jspecify.annotations.NonNull;

/**
 * スプリント終了曜日を表す列挙型。
 *
 * <p>月曜日〜金曜日の営業日のみを設定可能です。
 */
public enum EndDayOfWeek {

  /** 月曜日 */
  MONDAY(DayOfWeek.MONDAY),

  /** 火曜日 */
  TUESDAY(DayOfWeek.TUESDAY),

  /** 水曜日 */
  WEDNESDAY(DayOfWeek.WEDNESDAY),

  /** 木曜日 */
  THURSDAY(DayOfWeek.THURSDAY),

  /** 金曜日 */
  FRIDAY(DayOfWeek.FRIDAY);

  private final DayOfWeek dayOfWeek;

  EndDayOfWeek(DayOfWeek dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }

  /**
   * 対応する {@link DayOfWeek} を取得します。
   *
   * @return DayOfWeek
   */
  @NonNull
  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  /**
   * {@link DayOfWeek} から {@link EndDayOfWeek} を取得します。
   *
   * @param dayOfWeek 変換対象の曜日
   * @return 対応する EndDayOfWeek
   * @throws IllegalArgumentException 土曜日または日曜日が指定された場合
   */
  @NonNull
  public static EndDayOfWeek fromDayOfWeek(@NonNull DayOfWeek dayOfWeek) {
    for (EndDayOfWeek endDay : values()) {
      if (endDay.dayOfWeek == dayOfWeek) {
        return endDay;
      }
    }
    throw new IllegalArgumentException(
        "End day of week must be a weekday (Monday-Friday), but was: " + dayOfWeek);
  }
}
