package undecided.apigateway;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions.rateLimit;
import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.host;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import undecided.apigateway.config.ratelimit.KeyResolverConfig;
import undecided.apigateway.config.ratelimit.RateLimitProperties;
import undecided.apigateway.config.ratelimit.RateLimitResponseFilter;

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

  private final RateLimitProperties rateLimitProperties;
  private final java.util.function.Function<ServerRequest, String> ipKeyResolver;
  private final RateLimitResponseFilter rateLimitResponseFilter;

  public GatewayServerApplication(
      RateLimitProperties rateLimitProperties,
      KeyResolverConfig keyResolverConfig,
      ObjectMapper objectMapper) {
    this.rateLimitProperties = rateLimitProperties;
    this.ipKeyResolver = keyResolverConfig.ipKeyResolver();
    this.rateLimitResponseFilter = new RateLimitResponseFilter(objectMapper);
  }

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

  /**
   * APIルートにレートリミッティングを適用したルーティングを設定します。
   *
   * <p>詳細設計書に基づく制限設定を適用します:
   * <ul>
   *   <li>認証関連エンドポイント（/api/auth/**）: 1分あたり5回</li>
   *   <li>トークン更新エンドポイント（/api/auth/refresh）: 1分あたり10回</li>
   *   <li>GETリクエスト: 1分あたり100回</li>
   *   <li>POST/PUT/PATCH/DELETEリクエスト: 1分あたり30回</li>
   * </ul>
   *
   * <p>Spring Cloud Gateway WebMVCのBucket4jフィルタでは、{@code setCapacity}がバケットの最大容量と
   * 補充トークン数の両方を兼ねており、独立したバースト容量の設定はサポートされていません。
   * そのため、{@link RateLimitProperties#burstCapacity}などのバースト容量プロパティは使用されません。
   *
   * <p>レートリミット超過時は、{@link RateLimitResponseFilter}によって詳細設計書 7.2節で定義された
   * JSON形式の429レスポンスが返されます。
   *
   * <p>ルートの優先順位: Spring Cloud Gateway WebMVCのRouterFunctionでは、{@code .and()} で結合された
   * ルートのマッチング順序は登録順序に従います。そのため、認証関連エンドポイントは一般的な/api/**ルート
   * よりも先頭に定義し、優先的にマッチするようにしています。これにより、{@code /api/auth/login} への
   * POSTリクエストが {@code api-auth-rate-limited} ルートにマッチし、一般的な {@code api-write-rate-limited}
   * ルートにマッチしないことが保証されます。
   *
   * <p>以下のルートはレートリミットの対象外です:
   * <ul>
   *   <li>/do/databases/** - データベース操作エンドポイント</li>
   *   <li>/v3/api-docs/** - OpenAPIドキュメント</li>
   *   <li>/swagger-ui/** - Swagger UI</li>
   * </ul>
   *
   * @return レートリミットが適用されたRouterFunction
   */
  @Bean
  public RouterFunction<ServerResponse> rateLimitedApiRoutes() {
    Duration period = rateLimitProperties.getPeriod();
    // @formatter:off
    return route("api-auth-rate-limited")
        // 認証関連エンドポイントを先頭に配置し、一般的な/api/**ルートよりも優先的にマッチさせる
        // Spring Cloud Gateway WebMVCでは、登録順序に従ってマッチングされるため、
        // /api/auth/** へのリクエストがここでマッチし、後続の /api/** ルートには到達しない
        .POST(path("/api/auth/**"), http())
        .before(uri("lb://backend"))
        .filter(rateLimit(config -> config
            .setCapacity(rateLimitProperties.getAuthCapacity())
            .setPeriod(period)
            .setKeyResolver(ipKeyResolver)))
        .filter(rateLimitResponseFilter)
        .build()
        .and(
            route("api-auth-refresh-rate-limited")
                .POST(path("/api/auth/refresh"), http())
                .before(uri("lb://backend"))
                .filter(rateLimit(config -> config
                    .setCapacity(rateLimitProperties.getAuthRefreshCapacity())
                    .setPeriod(period)
                    .setKeyResolver(ipKeyResolver)))
                .filter(rateLimitResponseFilter)
                .build())
        .and(
            route("api-get-rate-limited")
                .GET(path("/api/**"), http())
                .before(uri("lb://backend"))
                .filter(rateLimit(config -> config
                    .setCapacity(rateLimitProperties.getCapacity())
                    .setPeriod(period)
                    .setKeyResolver(ipKeyResolver)))
                .filter(rateLimitResponseFilter)
                .build())
        .and(
            route("api-write-rate-limited")
                .POST(path("/api/**"), http())
                .before(uri("lb://backend"))
                .filter(rateLimit(config -> config
                    .setCapacity(rateLimitProperties.getWriteCapacity())
                    .setPeriod(period)
                    .setKeyResolver(ipKeyResolver)))
                .filter(rateLimitResponseFilter)
                .build())
        .and(
            route("api-put-rate-limited")
                .PUT(path("/api/**"), http())
                .before(uri("lb://backend"))
                .filter(rateLimit(config -> config
                    .setCapacity(rateLimitProperties.getWriteCapacity())
                    .setPeriod(period)
                    .setKeyResolver(ipKeyResolver)))
                .filter(rateLimitResponseFilter)
                .build())
        .and(
            route("api-patch-rate-limited")
                .PATCH(path("/api/**"), http())
                .before(uri("lb://backend"))
                .filter(rateLimit(config -> config
                    .setCapacity(rateLimitProperties.getWriteCapacity())
                    .setPeriod(period)
                    .setKeyResolver(ipKeyResolver)))
                .filter(rateLimitResponseFilter)
                .build())
        .and(
            route("api-delete-rate-limited")
                .DELETE(path("/api/**"), http())
                .before(uri("lb://backend"))
                .filter(rateLimit(config -> config
                    .setCapacity(rateLimitProperties.getWriteCapacity())
                    .setPeriod(period)
                    .setKeyResolver(ipKeyResolver)))
                .filter(rateLimitResponseFilter)
                .build())
        .and(
            route("do-databases")
                .route(path("/do/databases/**"), http())
                .before(uri("lb://backend"))
                .build())
        .and(
            route("api-docs")
                .route(path("/v3/api-docs/**"), http())
                .before(uri("lb://backend"))
                .build())
        .and(
            route("swagger-ui")
                .route(path("/swagger-ui/**"), http())
                .before(uri("lb://backend"))
                .build());
    // @formatter:on
  }
}