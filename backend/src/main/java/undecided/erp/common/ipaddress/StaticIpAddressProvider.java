package undecided.erp.common.ipaddress;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ネットワーク接続のための静的IPアドレスを提供するクラス。
 *
 * <p>このクラスは、ホストシステムのIPアドレスを取得する責任を持つ {@link IpAddressProvider} クラスを拡張します。 このクラスによって提供される IP
 * アドレスは静的であり、ランタイム中に変更されません。</p>
 *
 * <p>IPアドレスは、一貫性を保証し、IPアドレスへのアクセスや更新時に競合状態を防ぐために、{@link AtomicReference}
 * を使用してスレッドセーフな方法で保存されます。</p>
 *
 * <p>このクラスのインスタンスは、希望の静的IPアドレスを使用してコンストラクタで作成されます。 作成されると、{@link #ipHostAddress()}
 * メソッドを通じてIPアドレスにアクセスできます。</p>
 *
 * <p>静的IPアドレスをネットワーク操作で使用可能にするためには、{@link #initialize(String)} メソッドを呼び出す必要があります。 このメソッドは
 * {@link StaticIpAddressProvider} クラスの新しいインスタンスを作成し、静的IPアドレスを渡します。 この新しいインスタンスは、既存の
 * {@link IpAddressProvider} インスタンスを初期化するために使用されます。</p>
 *
 * <p>このクラスはネットワークスタックの初期化やシャットダウンの管理を担当するわけではなく、ネットワーク接続のための静的IPアドレスだけを提供します。</p>
 */
public class StaticIpAddressProvider extends IpAddressProvider {

  private static final AtomicReference<String> ipAddress = new AtomicReference<>();

  public StaticIpAddressProvider(String ipAddress) {
    super();
    StaticIpAddressProvider.ipAddress.set(ipAddress);
  }

  /**
   * 与えられたIPアドレスでStaticIpAddressProviderを初期化します。
   * <p>
   * このメソッドは、与えられたIPアドレスでStaticIpAddressProviderクラスの新しいインスタンスを作成し、
   * それをIpAddressProviderクラスの新しいインスタンスに渡して初期化します。
   *
   * @param ipAddress StaticIpAddressProviderを初期化するためのIPアドレス
   */
  public static void initialize(String ipAddress) {
    StaticIpAddressProvider instance = new StaticIpAddressProvider(ipAddress);
    new IpAddressProvider(instance);
  }

  @Override
  protected String ipHostAddress() {
    return StaticIpAddressProvider.ipAddress.get();
  }

  /**
   * 現在のIPアドレスプロバイダーをクリア(消去)する。
   */
  public static void clear() {
    IpAddressProvider.clear();

  }
}
