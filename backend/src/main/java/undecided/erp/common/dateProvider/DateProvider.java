package undecided.erp.common.dateProvider;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/**
 * DateProviderクラスは現在の日付と時刻を提供する役割を担っています。
 */
public class DateProvider {

  private static final AtomicReference<DateProvider> dateProvider = new AtomicReference<>(
      new DateProvider());

  DateProvider() {

  }

  protected DateProvider(DateProvider dateProvider) {
    DateProvider.setDateProvider(dateProvider);
  }

  public static void setDateProvider(DateProvider aDateProvider) {
    DateProvider.dateProvider.set(aDateProvider);
  }

  /**
   * DateProviderオブジェクトを初期化します。
   * <p>
   * このメソッドは、新しいDateProviderのインスタンスで現在の日付プロバイダを設定します。
   */
  static void initialize() {
    DateProvider.dateProvider.set(new DateProvider());

  }

  /**
   * 現在のLocalDateTimeを返します。
   *
   * @return 現在のLocalDateTime
   */
  public static LocalDateTime currentLocalDateTime() {
    return DateProvider.dateProvider.get().now();
  }

  /**
   * Returns the current date and time.
   *
   * @return the current date and time
   * @deprecated use {@link #currentLocalDateTime()} method.
   */
  @Deprecated
  public static Date currentDate() {
    ZonedDateTime zdt = currentLocalDateTime().atZone(ZoneId.systemDefault());
    return Date.from(zdt.toInstant());

  }

  /**
   * 現在のローカル日付を返します。
   *
   * @return 現在のローカル日付
   */
  public static LocalDate currentLocalDate() {
    return LocalDate.from(currentLocalDateTime());

  }

  /**
   * 現在のローカル時間を返します。
   *
   * @return 現在のローカル時間
   */
  public static LocalTime currentLocalTime() {
    return LocalTime.from(currentLocalDateTime());

  }

  /**
   * 現在の年と月をYearMonthオブジェクトとして返します。
   *
   * @return 現在の年と月
   */
  public static YearMonth currentYearMonth() {
    LocalDateTime currentDateTime = currentLocalDateTime();
    return YearMonth.from(currentDateTime);
  }

  /**
   * エポックからの現在の時間をミリ秒で返します。
   *
   * @return 現在の時間（ミリ秒）
   */
  public static long currentTimeMillis() {
    LocalDateTime currentLocalDateTime = currentLocalDateTime();
    Instant instant = currentLocalDateTime.atZone(ZoneId.systemDefault()).toInstant();
    return instant.toEpochMilli();

  }

  protected LocalDateTime now() {
    return LocalDateTime.now();
  }

  /**
   * 現在の月と日を MonthDay オブジェクトとして取得します。
   *
   * @return 現在の月と日を表す MonthDay オブジェクト。
   */
  public static MonthDay currentMonthDay() {
    return MonthDay.from(currentLocalDateTime());
  }

}
