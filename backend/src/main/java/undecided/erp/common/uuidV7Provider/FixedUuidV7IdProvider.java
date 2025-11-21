package undecided.erp.common.uuidV7Provider;

import java.util.UUID;
import org.jspecify.annotations.NonNull;

/**
 * 固定された値のUUIDを提供するためのUuidV7Provider実装クラス。
 *
 * <p>このクラスは、指定されたUUID値を返すカスタムプロバイダーとして機能します。
 * 特定のUUID値が必要な場合に利用されます。このクラスを利用することで一貫したUUIDの提供が可能となります。
 *
 * <p>スレッドセーフなUUID世代をサポートするベースクラスUuidV7Providerを拡張し、 固定のUUID値を返すようオーバーライドされたロジックを提供しています。
 */
public class FixedUuidV7IdProvider extends UuidV7Provider {

  /**
   * 固定されたUUID値を保持するためのインスタンス変数。
   *
   * <p>この変数は、{@link FixedUuidV7IdProvider}クラスにおいて、常に一定のUUID値を返す
   * プロバイダとして機能するために使用される。インスタンス生成時にセットされ、 変更されることはない。
   *
   * <p>また、この変数は{@link FixedUuidV7IdProvider#internalNewInstanse()}メソッド内で
   * 使用され、UUIDバージョン7のインスタンス生成時に固定値として提供される。
   *
   * <p>主な特徴: - 不変性: インスタンス生成時に初期化され、以降の変更は不可。 - カスタムUUIDの固定提供: 一貫性のあるUUID値を返却するための基盤を提供。
   */
  private final UUID value;

  /**
   * 指定された固定UUID値を使用してFixedUuidV7IdProviderのインスタンスを作成します。
   *
   * @param value 固定のUUID値。この値は、インスタンス生成時に設定され、 その後のUUID生成で一貫して返されます。
   */
  private FixedUuidV7IdProvider(@NonNull UUID value) {
    this.value = value;
  }

  /**
   * 指定されたUUID値を使用してUUIDバージョン7プロバイダーを初期化します。
   *
   * <p>このメソッドを呼び出すと、固定されたUUID値を提供する{@link FixedUuidV7IdProvider}を
   * 使用して新しいUUIDバージョン7プロバイダーインスタンスが設定されます。
   *
   * @param value 固定されたUUID値。新しいプロバイダーインスタンスで一貫して返される値を指定します。 nullを許容しない引数です。
   */
  public static void initialize(@NonNull UUID value) {
    new UuidV7Provider(new FixedUuidV7IdProvider(value));
  }

  /**
   * 固定UUIDプロバイダーをクリアし、UUIDバージョン7プロバイダーの状態をリセットします。
   *
   * <p>このメソッドは、内部的に{@link UuidV7Provider#clear()}を呼び出し、現在使用されているUUIDバージョン7
   * プロバイダーのインスタンスをデフォルトの状態に戻します。これにより、UUIDの生成において固定UUIDではなく、 デフォルトのUUID生成ロジックが使用されるようになります。
   */
  public static void clear() {
    UuidV7Provider.clear();
  }

  /**
   * 固定されたUUIDインスタンスを生成して返します。
   *
   * <p>このメソッドは、{@link FixedUuidV7IdProvider}内でオーバーライドされ、固定のUUID値を
   * 提供するために利用されます。常にクラスインスタンスに設定された値を返します。
   *
   * @return 固定されたUUID値
   */
  @Override
  protected @NonNull UUID internalNewInstanse() {
    return value;
  }
}
