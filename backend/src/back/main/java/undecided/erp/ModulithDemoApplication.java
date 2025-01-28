package undecided.erp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import undecided.erp.shared.applicatoion.ApplicationInfoInitializer;

/**
 * Modulith Demoアプリケーションのメインクラス。
 * <p>
 * このクラスはSpring Bootアプリケーションの初期化と起動を行います。また、 アプリケーションコンテキストが開始されたイベントを処理し、
 * アプリケーション名やサーバーポートなど、アプリケーション固有の情報を初期化します。
 */
@SpringBootApplication
public class ModulithDemoApplication {

  /**
   * アプリケーションの名前を示す変数。この値はSpringのプロパティファイルから取得される。 具体的には、`spring.application.name`プロパティに対応する。
   * この変数は、アプリケーションの初期化と終了時に使用されるログやその他の情報に利用される。
   */
  @Value("${spring.application.name}")
  private String applicationName;
  /**
   * 変数は、サーバーがリッスンするポート番号を格納します。 この値はSpringのプロパティファイルから取得され、アプリケーションの初期化時に設定されます。
   * <p>
   * 具体的には`server.port`プロパティに対応します。サーバーが特定のポートで起動するように構成できます。
   */
  @Value("${server.port}")
  private String serverPort;

  /**
   * Modulith Demoアプリケーションを開始します。
   *
   * @param args コマンドライン引数
   */
  public static void main(String[] args) {
    SpringApplication.run(ModulithDemoApplication.class, args);
  }

  /**
   * ContextStartedEventを処理し、アプリケーション名やサーバーポートなどの アプリケーション固有の情報を初期化します。
   *
   * @param ctxStartEvt コンテキストの開始を表すイベント
   */
  @EventListener
  public void handleContextRefreshEvent(ContextStartedEvent ctxStartEvt) {
    ApplicationInfoInitializer.initialize(applicationName, Long.valueOf(serverPort));

  }

}
