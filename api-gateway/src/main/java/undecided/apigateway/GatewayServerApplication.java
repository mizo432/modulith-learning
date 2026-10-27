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
   * Application entry point for the Spring Boot gateway server.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(GatewayServerApplication.class, args);
  }

  /**
   * Configure the gateway's functional routes used by the application.
   *
   * <p>Creates a RouterFunction that:
   * <ul>
   *   <li>Routes GET /get to https://httpbin.org (path_route).</li>
   *   <li>Forwards requests for hosts matching *.myhost.org to https://httpbin.org (host_route).</li>
   *   <li>For hosts matching *.rewrite.org forwards to https://httpbin.org and rewrites paths matching
   *       /foo/{segment} to /{segment} (rewrite_route).</li>
   *   <li>For hosts matching *.circuitbreaker.org forwards to https://httpbin.org and applies a
   *       circuit breaker named "slowcmd" (circuitbreaker_route).</li>
   *   <li>For hosts matching *.circuitbreakerfallback.org applies a circuit breaker with id "slowcmd"
   *       and a fallback forwarded to /fallback (circuitbreaker_fallback_route).</li>
   * </ul>
   *
   * @return the RouterFunction that implements the gateway routing rules
   */
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