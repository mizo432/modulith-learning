package undecided.serviceregistory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * これはEureka Serverアプリケーションのメインクラスです。
 * <p>
 * このアプリケーションはNetflix Eurekaを使用してサービスレジストリとして機能します。
 *
 * @SpringBootApplicationアノテーションはこのクラスをSpring Bootアプリケーションとしてマークします。
 * @EnableEurekaServerアノテーションはアプリケーションにEurekaサーバーを有効にします。
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

  /**
   * Eureka サーバーアプリケーションのエントリーポイント。
   * <p>
   * このメソッドは Spring Boot の SpringApplication.run を呼び出して、 アプリケーションを起動します。
   *
   * @param args 起動時にアプリケーションに渡されるコマンドライン引数
   */
  public static void main(String[] args) {
    SpringApplication.run(EurekaServerApplication.class, args);
  }

}
