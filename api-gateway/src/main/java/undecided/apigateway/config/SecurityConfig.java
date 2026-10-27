package undecided.apigateway.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

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

  /**
   * JWTのauthoritiesクレームからGrantedAuthorityリストを抽出します。
   *
   * @param jwt JWTトークン
   * @return GrantedAuthorityのコレクション
   */
  private static Collection<? extends GrantedAuthority> extractAuthorities(Jwt jwt) {
    Object authoritiesObj = jwt.getClaim("authorities");
    if (authoritiesObj instanceof List<?> list) {
      return list.stream()
          .filter(obj -> obj instanceof String)
          .map(obj -> (String) obj)
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
    }
    return List.of();
  }

  /**
   * セキュリティフィルターチェーンを設定します。
   *
   * <p>REQ-00003: APIゲートウェイで認証・認可を行うために、ロールベースのアクセス制御を構成します。
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception 例外
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    // Swagger UI は公開
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    // 管理者用APIはROLE_ADMINのみアクセス可能
                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")
                    // 一般APIは認証済みユーザーならアクセス可能
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

    return http.build();
  }

  /**
   * JWT認証コンバーターを設定します。
   *
   * <p>REQ-00003: JWTの「authorities」クレームからロール情報を抽出し、 Spring SecurityのGrantedAuthorityとして設定します。
   * これにより、APIゲートウェイでロールベースの認可が可能になります。
   *
   * @return Converter<Jwt, ? extends AbstractAuthenticationToken>
   */
  @Bean
  public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
    return jwt -> {
      Collection<? extends GrantedAuthority> authorities = extractAuthorities(jwt);
      return new JwtAuthenticationToken(jwt, authorities);
    };
  }
}
