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

  private static final AtomicReference<LocalDateTime> startDateTime = new AtomicReference<>();
  private static final AtomicReference<LocalDateTime> localDateTime = new AtomicReference<>();

  public ShiftDateTimeProvider(LocalDateTime localDateTime) {
    super();
    ShiftDateTimeProvider.localDateTime.set(localDateTime);
    ShiftDateTimeProvider.startDateTime.set(LocalDateTime.now());
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


  @Override
  protected LocalDateTime now() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    return ShiftDateTimeProvider
        .localDateTime.get()
        .plusNanos(
            Duration.between(ShiftDateTimeProvider.startDateTime.get(), currentDateTime).toNanos());
  }

  /**
   * DateProviderを初期化することでクリアします。
   */
  public static void clear() {
    DateProvider.initialize();

  }

}
