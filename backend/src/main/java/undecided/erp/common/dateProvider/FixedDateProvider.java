package undecided.erp.common.dateProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicReference;

/**
 * StaticDateProviderクラスは、DateProvider抽象クラスのサブクラスです。
 * <p>
 * このクラスはDateProviderインターフェイスの静的実装を提供し、現在の日付と 時刻が固定され、アプリケーション全体でグローバルに設定できます。
 *
 * <p>
 * このクラスは、現在のローカル日付を保存するためにAtomicReferenceを使用します。
 * AtomicReferenceは、ローカル日付を更新するときの原子性とスレッドセーフティを保証します。
 * </p>
 *
 * <p>
 * また、StaticDateProviderクラスは静的メソッドinitializeを提供します。これにより、
 * アプリケーションが初期のグローバル日時を設定できます。initializeメソッドは、 StaticDateProviderクラスのインスタンスを作成し、ローカル日付を提供された値に設定し、
 * 新しいDateProviderインスタンスをStaticDateProviderの実装として初期化します。
 * </p>
 *
 * <p>
 * StaticDateProviderクラスは、DateProviderクラスからnowメソッドをオーバーライドします。
 * nowメソッドは、保存されたローカル日付と現在のシステム時間を使用して、現在の日付と 時刻をLocalDateTimeオブジェクトとして返します。
 * </p>
 *
 * <p>
 * さらに、StaticDateProviderクラスは、DateProviderインスタンスをクリアする静的メソッドclearを提供します。
 * これにより、DateProviderインスタンスを効果的にデフォルトの実装にリセットできます。
 * </p>
 *
 * @see DateProvider
 */
public class FixedDateProvider extends DateProvider {

  private static final AtomicReference<LocalDate> LOCAL_DATE = new AtomicReference<>();

  public FixedDateProvider(LocalDate aLocalDate) {
    super();
    LOCAL_DATE.set(aLocalDate);
  }

  /**
   * 提供されたLocalDateを現在の日付としてStaticDateProviderを初期化します。
   * <p>
   * このメソッドは、与えられたLocalDateを用いて新たなStaticDateProviderのインスタンスを生成し、 それを実装とする新しいDateProviderを初期化します。
   *
   * @param localDate 現在の日付として設定するLocalDate
   */
  public static void initialize(LocalDate localDate) {
    FixedDateProvider instance = new FixedDateProvider(localDate);
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
    return LocalDateTime.of(LOCAL_DATE.get(), LocalTime.now());

  }

}
