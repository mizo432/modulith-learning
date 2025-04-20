package undecided.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;

/**
 * 認証・認可アプリケーションのメインクラス。
 * <p>
 * このクラスはSpring Bootアプリケーションの初期化と起動を行います。また、 アプリケーションコンテキストが開始されたイベントを処理し、
 * アプリケーション名やサーバーポートなど、アプリケーション固有の情報を初期化します。
 * <p>
 * このアプリケーションは、ユーザー認証と認可の機能を提供します。
 * Spring Securityを使用してセキュリティを実装し、JWTトークンベースの認証を行います。
 * また、ロールベースのアクセス制御（RBAC）を実装しています。
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AuthorizationApplication {

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
   * 認証・認可アプリケーションを開始します。
   *
   * @param args コマンドライン引数
   */
  public static void main(String[] args) {
    SpringApplication.run(AuthorizationApplication.class, args);
  }

  /**
   * ContextStartedEventを処理し、アプリケーション名やサーバーポートなどの アプリケーション固有の情報を初期化します。
   *
   * @param ctxStartEvt コンテキストの開始を表すイベント
   */
  @EventListener
  public void handleContextRefreshEvent(ContextStartedEvent ctxStartEvt) {
    // アプリケーション情報の初期化処理
    System.out.println("Authorization Application started on port " + serverPort);
  }
}
