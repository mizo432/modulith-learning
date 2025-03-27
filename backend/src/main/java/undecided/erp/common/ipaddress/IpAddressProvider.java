package undecided.erp.common.ipaddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * IpAddressProviderクラスは、現在のホストのIPアドレスを提供する役割を果たします。
 * <p>
 * IPアドレスの取得には、java.netパッケージのInetAddressクラスを使用しています。
 * <p>
 * IpAddressProviderを使用するには、静的メソッド `ipAddress()` を呼び出すことで、IPアドレスを文字列として返します。
 * <p>
 * さらに高度な機能として、静的メソッド `setIpAddressProvider()`
 * を提供しており、これによってIpAddressProviderクラスのカスタム実装を設定することができます。デフォルトのIPアドレス取得メカニズムを異なる実装に置き換える場合に便利です。
 * <p>
 * IPアドレスプロバイダーをデフォルトの実装にリセットしたい場合は、静的メソッド `clear()` を呼び出します。
 * <p>
 * 注意: IpAddressProviderクラスは、スレッドセーフを保証するためにAtomicReferenceを使用したシングルトンとして実装されています。
 */
public class IpAddressProvider {

  /**
   * IpAddressProviderクラスのインスタンスを管理するスレッドセーフなAtomicReference。
   * <p>
   * デフォルトのIpAddressProviderのインスタンスを保持し、必要に応じて別のインスタンスに置き換えることができます。
   * この変数は、現在のIPアドレスプロバイダーの管理や切り替えに使用されます。
   * <p>
   * 注意: この変数はスレッドセーフな操作のためにAtomicReferenceを使用しています。
   */
  private final static AtomicReference<IpAddressProvider> ipAddressProvider =
      new AtomicReference<>(new IpAddressProvider());

  /**
   * IpAddressProviderクラスのデフォルトコンストラクタ。
   * <p>
   * ホストマシンの現在のIPアドレスを提供するためのデフォルトインスタンスを生成します。 通常、このコンストラクタは直接使用されることはなく、
   * スタティックメソッドやサブクラスを通して利用されます。
   * <p>
   * 注意: このクラスはスレッドセーフに設計されており、AtomicReferenceを使用して デフォルトインスタンスを管理します。
   */
  IpAddressProvider() {

  }


  /**
   * IpAddressProviderクラスの新しいインスタンスを初期化します。
   * <p>
   * このコンストラクタにより、新しいIpAddressProviderインスタンスで 現在のIPアドレスプロバイダーを設定します。
   *
   * @param ipAddressProvider 新しいIPアドレスプロバイダーとして設定するIpAddressProviderインスタンス
   */
  protected IpAddressProvider(IpAddressProvider ipAddressProvider) {
    IpAddressProvider.setIpAddressProvider(ipAddressProvider);
  }

  /**
   * 現在のホストのIPアドレスを取得します。
   *
   * @return 現在のホストのIPアドレス
   * @throws UnknownHostException ホストのIPアドレスを決定できない場合
   */
  public static String ipAddress() throws UnknownHostException {
    return IpAddressProvider
        .ipAddressProvider
        .get()
        .ipHostAddress();
  }

  /**
   * 現在のIPアドレスプロバイダーを設定します。
   * <p>
   * このメソッドを使用して、カスタムのIpAddressProviderを設定することができます。 設定後、{@link #ipAddress()}
   * メソッド呼び出し時にカスタム実装が使用されます。
   *
   * @param ipAddressProvider 新しいIPアドレスプロバイダーとして設定するIpAddressProviderインスタンス
   */
  public static void setIpAddressProvider(IpAddressProvider ipAddressProvider) {
    IpAddressProvider.ipAddressProvider.set(ipAddressProvider);
  }

  /**
   * DateProviderを初期化する
   */
  public static void clear() {
    IpAddressProvider.ipAddressProvider.set(new IpAddressProvider());

  }

  /**
   * 現在日時をLocalDateTime型で取得します
   *
   * @return 現在日時
   */
  protected String ipHostAddress() throws UnknownHostException {
    return InetAddress.getLocalHost().getHostAddress();

  }

}
