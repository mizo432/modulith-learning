package undecided.erp.common.dateProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 現在のLocalDateTimeをシフトするためのカスタムDateTimeProvider。
 * <p>
 * このクラスはDateProviderクラスを拡張します。
 */
public class ShiftDateTimeProvider extends DateProvider {

  /**
   * A thread-safe reference to the LocalDateTime that marks the start time when the
   * ShiftDateTimeProvider was initialized.
   * <p>
   * This variable is used to calculate the time shift applied to the current LocalDateTime within
   * the ShiftDateTimeProvider class.
   * <p>
   * The START_DATE_TIME is initially set to the current LocalDateTime at the moment an instance of
   * ShiftDateTimeProvider is created. Subsequent calculations for accessing shifted current time
   * rely on the difference between this start time and the actual system current time.
   * <p>
   * Use this variable to track and compute time shifts in scenarios where the apparent local time
   * needs to be advanced or delayed systematically for testing or scheduling purposes.
   */
  private static final AtomicReference<LocalDateTime> START_DATE_TIME = new AtomicReference<>();

  /**
   * A thread-safe reference to the LocalDateTime used as the shifted time within the
   * ShiftDateTimeProvider class.
   * <p>
   * This variable holds the base LocalDateTime value from which any time shifts are calculated,
   * allowing the class to simulate different current times based on the intended time manipulation
   * logic.
   * <p>
   * LOCAL_DATE_TIME is initially set during the construction of a ShiftDateTimeProvider instance.
   * It is updated to represent the "current" LocalDateTime as determined by the internal logic when
   * calculating the shifted time in correlation with the real system time.
   */
  private static final AtomicReference<LocalDateTime> LOCAL_DATE_TIME = new AtomicReference<>();

  public ShiftDateTimeProvider(LocalDateTime localDateTime) {
    super();
    LOCAL_DATE_TIME.set(localDateTime);
    START_DATE_TIME.set(LocalDateTime.now());
  }

  /**
   * 与えられたLocalDateTimeを用いて、ShiftDateTimeProvider及びDateProviderオブジェクトを初期化します。
   *
   * @param localDateTime ShiftDateTimeProviderとDateProviderオブジェクトを初期化するために使用するLocalDateTime
   */
  public static void initialize(LocalDateTime localDateTime) {
    ShiftDateTimeProvider instance = new ShiftDateTimeProvider(localDateTime);
    new DateProvider(instance);
  }

  /**
   * DateProviderを初期化することでクリアします。
   */
  public static void clear() {
    DateProvider.clear();

  }

  @Override
  protected LocalDateTime now() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    return LOCAL_DATE_TIME.get()
        .plusNanos(Duration.between(START_DATE_TIME.get(), currentDateTime).toNanos());
  }

}
