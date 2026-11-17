package undecided.apigateway.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import undecided.apigateway.config.handlers.JwtAccessDeniedHandler;
import undecided.apigateway.config.handlers.JwtAuthenticationEntryPoint;

/**
 * APIゲートウェイのセキュリティ設定
 *
 * <p>OAuth 2.0リソースサーバーとして動作し、JWTアクセストークンの検証と認可を行います。 REQ-00003:
 * APIゲートウェイで認証・認可配置を行うこと。JWTからロール情報を抽出し、アクセス制御に使用すること。 REQ-00017: api-gatewayはOAuth
 * 2.0リソースサーバーとして動作し、アクセストークンの検証を行うこと。 REQ-00021: リソースサーバーはJWTの署名を検証し、署名が無効なトークンは拒否すること。 REQ-00022:
 * リソースサーバーはJWTの有効期限 (expクレーム)を検証し、期限切れのトークンは拒否すること。 REQ-00023: リソースサーバーはJWTの発行者
 * (issクレーム)を検証し、信頼された認可サーバーから発行されたトークンのみを許可すること。 REQ-00025: リソースサーバーはJWTのオーディエンス
 * (audクレーム)を検証し、自身が対象のリソースサーバーであることを確認すること。 REQ-00030: nbfクレーム (Not
 * Before)が設定されている場合、リソースサーバーはその時刻以前のトークン使用を拒否すること。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final @NonNull CorsProperties corsProperties;

  public SecurityConfig(@NonNull CorsProperties corsProperties) {
    this.corsProperties = corsProperties;
  }

  /**
   * JWTのrolesクレームからGrantedAuthorityリストを抽出します。
   *
   * <p>設計書 2.3節で定義された`roles`クレームを使用し、各ロールに`ROLE_`プレフィックスを追加します。
   * これにより、Spring Securityの`hasRole()`メソッドが正しく動作します。
   *
   * <p>rolesクレームがnull、またはList以外の型（文字列、数値など）の場合、
   * 安全にフォールバックして空リストを返します。これは意図的な動作であり、
   * 不正な形式のクレームを持つトークンを拒否するのではなく、
   * 権限なしで認証済みとして扱います。
   *
   * @param jwt JWTトークン
   * @return GrantedAuthorityのコレクション
   */
  private static @NonNull Collection<? extends GrantedAuthority> extractAuthorities(@NonNull Jwt jwt) {
    Object rolesObj = jwt.getClaim("roles");
    if (rolesObj instanceof List<?> list) {
      return list.stream()
          .filter(obj -> obj instanceof String)
          .map(obj -> (String) obj)
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
          .collect(Collectors.toList());
    }
    // rolesクレームがnull、またはList以外の型（文字列、数値など）の場合、
    // 安全にフォールバックして空リストを返す。これは意図的な動作であり、
    // 不正な形式のクレームを持つトークンを拒否するのではなく、
    // 権限なしで認証済みとして扱う。
    return List.of();
  }

  /**
   * セキュリティフィルターチェーンを設定します。
   *
   * <p>REQ-00003: APIゲートウェイで認証・認可を行うために、ロールベースのアクセス制御を構成します。
   *
   * <p>設計書 5節に基づくCORS設定を適用し、設計書 6節に基づくセキュリティヘッダーを追加します。
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception 例外
   */
  @Bean
  public @NonNull SecurityFilterChain securityFilterChain(@NonNull HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(
            cors ->
                cors.configurationSource(
                    request -> {
                      var config = new CorsConfiguration();
                      config.setAllowedOrigins(corsProperties.getAllowedOrigins());
                      config.setAllowedMethods(corsProperties.getAllowedMethods());
                      config.setAllowedHeaders(corsProperties.getAllowedHeaders());
                      config.setExposedHeaders(corsProperties.getExposedHeaders());
                      config.setAllowCredentials(corsProperties.isAllowCredentials());
                      config.setMaxAge(corsProperties.getMaxAge());
                      return config;
                    }))
        .headers(
            headers ->
                headers
                    // contentTypeOptionsはデフォルトでnosniffが有効
                    .frameOptions(frame -> frame.deny())
                    .httpStrictTransportSecurity(
                        hsts ->
                            hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
                    .xssProtection(
                        xss ->
                            xss.headerValue(
                                XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                    .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                    .cacheControl(cache -> cache.disable()))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    // Swagger UI は公開
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    // 認証関連エンドポイントは公開（ログイン/トークン更新など）
                    .requestMatchers("/api/auth/**")
                    .permitAll()
                    // 管理者用APIはROLE_PRODUCT_OWNERのみアクセス可能
                    // 設計書 3.2節にADMINロールが定義されていないため、PRODUCT_OWNERを使用
                    .requestMatchers("/api/admin/**")
                    .hasRole("PRODUCT_OWNER")
                    // ヘルスチェックエンドポイントは公開
                    .requestMatchers("/actuator/health")
                    .permitAll()
                    // 明示的に許可されていないルートはすべて拒否
                    .anyRequest()
                    .denyAll())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
        .exceptionHandling(
            ex -> ex
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler()));

    return http.build();
  }

  /**
   * JWT認証コンバーターを設定します。
   *
   * <p>REQ-00003: JWTの「roles」クレームからロール情報を抽出し、各ロールに「ROLE_」プレフィックスを
   * 追加してSpring SecurityのGrantedAuthorityとして設定します。これにより、APIゲートウェイで
   * ロールベースの認可が可能になります。
   *
   * @return Converter<Jwt, ? extends AbstractAuthenticationToken>
   */
  @Bean
  public @NonNull Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
    return jwt -> {
      Collection<? extends GrantedAuthority> authorities = extractAuthorities(jwt);
      return new JwtAuthenticationToken(jwt, authorities);
    };
  }
}
