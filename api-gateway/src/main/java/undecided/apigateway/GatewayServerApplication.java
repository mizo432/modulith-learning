package undecided.apigateway;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * GatewayServerApplicationクラスは、Spring Bootベースのゲートウェイサーバーアプリケーションを表します。
 *
 * <p>このクラスはアプリケーションのエントリーポイントであり、ルーティング機能を提供します。
 *
 * <p>また、サービスディスカバリクライアントの有効化を行います。
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServerApplication {

  /**
   * メインメソッドはSpring Bootアプリケーションのエントリーポイントとして機能します。
   *
   * <p>これはGatewayServerApplicationを初期化し、実行します。
   *
   * @param args アプリケーションに渡されるコマンドライン引数。
   */
  public static void main(String[] args) {
    SpringApplication.run(GatewayServerApplication.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> customRoutes() {
    // @formatter:off
    return route("path_route")
        .GET("/get", http())
        .before(uri("https://httpbin.org"))
        .build()
        .and(
            route("host_route")
                .route(host("*.myhost.org"), http())
                .before(uri("https://httpbin.org"))
                .build()
                .and(
                    route("rewrite_route")
                        .route(host("*.rewrite.org"), http())
                        .before(uri("https://httpbin.org"))
                        .before(rewritePath("/foo/(?<segment>.*)", "/${segment}"))
                        .build()
                        .and(
                            route("circuitbreaker_route")
                                .route(host("*.circuitbreaker.org"), http())
                                .before(uri("https://httpbin.org"))
                                .filter(circuitBreaker("slowcmd"))
                                .build()
                                .and(
                                    route("circuitbreaker_fallback_route")
                                        .route(host("*.circuitbreakerfallback.org"), http())
                                        .before(uri("https://httpbin.org"))
                                        .filter(
                                            circuitBreaker(
                                                c ->
                                                    c.setId("slowcmd")
                                                        .setFallbackUri("forward:/fallback")))
                                        .build()))));
    // @formatter:on
  }
}
