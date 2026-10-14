package undecided.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * APIゲートウェイのセキュリティ設定
 *
 * <p>OAuth 2.0リソースサーバーとして動作し、JWTアクセストークンの検証を行います。 REQ-00017: api-gatewayはOAuth
 * 2.0リソースサーバーとして動作し、アクセストークンの検証を行うこと。 REQ-00021: リソースサーバーはJWTの署名を検証し、署名が無効なトークンは拒否すること。 REQ-00022:
 * リソースサーバーはJWTの有効期限 (expクレーム)を検証し、期限切れのトークンは拒否すること。 REQ-00023: リソースサーバーはJWTの発行者
 * (issクレーム)を検証し、信頼された認可サーバーから発行されたトークンのみを許可すること。 REQ-00025: リソースサーバーはJWTのオーディエンス
 * (audクレーム)を検証し、自身が対象のリソースサーバーであることを確認すること。
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * セキュリティフィルターチェーンを設定します。
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
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
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
   * @return JwtAuthenticationConverter
   */
  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    return new JwtAuthenticationConverter();
  }
}
