package undecided.erp.common.dateProvider;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * StaticDateTimeProvider クラスは、DateProvider 抽象クラスの具体的な実装です。
 * <p>
 * これにより、now() メソッドを呼び出すときに戻される固定の LocalDateTime を設定することができます。
 */
public class StaticDateTimeProvider extends DateProvider {

  private static final AtomicReference<LocalDateTime> localDateTime = new AtomicReference<>();

  private StaticDateTimeProvider() {

  }

  /**
   * 与えられたLocalDateTimeの値でStaticDateTimeProviderを初期化します。
   *
   * @param localDateTime StaticDateTimeProviderに設定するLocalDateTimeの値
   */
  public static void initialize(LocalDateTime localDateTime) {
    StaticDateTimeProvider instance = new StaticDateTimeProvider();
    instance.setLocalDateTime(localDateTime);
    setDateProvider(instance);


  }

  private void setLocalDateTime(LocalDateTime aLocalDateTime) {
    StaticDateTimeProvider.localDateTime.set(aLocalDateTime);

  }

  @Override
  protected LocalDateTime now() {
    return localDateTime.get();

  }

  /**
   * DateProviderを初期化することでクリアします。
   */
  public static void clear() {
    DateProvider.initialize();

  }

}
