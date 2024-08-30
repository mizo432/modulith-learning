package undecided.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * GatewayServerApplicationは、Spring Bootアプリケーションのエントリーポイントです。
 * <p>
 * このアプリケーションはゲートウェイサーバーとして機能し、提供された設定に基づいて 入ってくるリクエストを適切なサービスにルーティングします。
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServerApplication {

  /**
   * メインメソッドはSpring Bootアプリケーションのエントリーポイントとして機能します。
   * <p>
   * これはGatewayServerApplicationを初期化し、実行します。
   *
   * @param args アプリケーションに渡されるコマンドライン引数。
   */
  public static void main(String[] args) {
    SpringApplication.run(GatewayServerApplication.class, args);
  }

  /**
   * ゲートウェイサーバーでリクエストをルーティングするためのカスタムなRouteLocatorを返します。
   *
   * @param builder RouteLocatorを構築するために使用されるRouteLocatorBuilder
   * @return カスタムなルートが設定されたRouteLocator
   */
  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
        .route("backbone", r -> r.path("/backbone/**")
//            .filters(f -> f.stripPrefix( 1 ))
            .uri("lb://BACKBONE"))
/*
        .route("service1", r -> r.path("/service1/**")
            .filters(f -> f.stripPrefix(1))
            .uri("lb://SERVICE-NAME1"))
        .route("service2", r -> r.path("/service2/**")
            .filters(f -> f.stripPrefix(1))
            .uri("lb://SERVICE-NAME2"))

 */
        .build();
  }
}
