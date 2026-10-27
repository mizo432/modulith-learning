package undecided.authorization.infra.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * OAuth2認可サーバー設定
 *
 * <p>OAuth 2.0 / OpenID Connect 認可サーバーの設定を行います。 REQ-00011: authorizationモジュールをOAuth
 * 2.0認可サーバーとして構成すること。 REQ-00010: 認証はOAuth 2.0 Authorization Code with PKCEフローを使用すること。 REQ-00012:
 * アクセストークンはJWT (JSON Web Token) 形式で発行すること。 REQ-00013: アクセストークンの有効期間は15分以内とする。 REQ-00014:
 * リフレッシュトークンを使用してアクセストークンの再発行を行うこと。 REQ-00015: リフレッシュトークンの有効期間は7日以内とする。 REQ-00016: トークンの署名にはRS256
 * (RSA with SHA-256) アルゴリズムを使用すること。 REQ-00018: 認可エンドポイント、トークンエンドポイントはHTTPSのみで提供すること。 REQ-00020:
 * OAuth 2.0認可サーバーは、ユーザーの同意画面を表示しない。
 */
@Configuration
public class OAuth2AuthorizationServerConfig {

  private static final Duration ACCESS_TOKEN_TIME_TO_LIVE = Duration.ofMinutes(15);
  private static final Duration REFRESH_TOKEN_TIME_TO_LIVE = Duration.ofDays(7);

  // REQ-00019: クライアントIDとクライアントシークレットは環境変数から読み込む
  @Value("${oauth2.client.id:frontend}")
  private String clientId;

  @Value("${oauth2.client.secret:}")
  private String clientSecret;

  @Value("${oauth2.client.redirect-uris:https://localhost:3000/callback,https://localhost:3000/login/oauth2/code}")
  private List<String> redirectUris;

  @Value("${oauth2.client.post-logout-redirect-uris:https://localhost:3000/}")
  private List<String> postLogoutRedirectUris;

  /**
   * RSAキーペアを生成します。
   *
   * @return KeyPair
   */
  private static KeyPair generateRsaKey() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      return keyPairGenerator.generateKeyPair();
    } catch (Exception ex) {
      throw new IllegalStateException("Failed to generate RSA key pair", ex);
    }
  }

  /**
   * OAuth2認可サーバーのセキュリティフィルターチェーンを設定します。
   *
   * <p>OAuth 2.0 Authorization Code with PKCEフローをサポートするために、認可サーバーエンドポイントの
   * セキュリティを設定します。ログインページへのリダイレクトとOpenID Connect 1.0を有効にします。
   *
   * @param http HttpSecurity
   * @return SecurityFilterChain
   * @throws Exception 例外
   */
  @Bean
  @Order(1)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
      throws Exception {

    // OAuth2認可サーバーエンドポイントのみを対象とする
    http.securityMatcher("/oauth2/**")
        // OAuth2認可サーバーの設定を適用
        .oauth2AuthorizationServer(
            authorizationServer -> authorizationServer.oidc(Customizer.withDefaults()))
        // 例外ハンドリングを設定：認証が必要の場合はログインページにリダイレクト
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling.defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint("/login"),
                    new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

    return http.build();
  }

  /**
   * 登録済みクライアントリポジトリを設定します。
   *
   * <p>REQ-00010: OAuth 2.0 Authorization Code with PKCEフローを使用するため、 クライアントは公開クライアント（public
   * client）として設定し、クライアントシークレットを不要とします。 PKCE（Proof Key for Code
   * Exchange）により、クライアントシークレットなしでも安全な認証フローを実現します。
   *
   * @return RegisteredClientRepository
   */
  @Bean
  public RegisteredClientRepository registeredClientRepository() {
    // REQ-00019: クライアントIDとクライアントシークレットは環境変数から読み込み、平文でコードに記述しない
    RegisteredClient frontendClient =
        RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(clientId)
            // PKCEフローではクライアントシークレット不要（公開クライアント）
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUris(uris -> uris.addAll(redirectUris))
            .postLogoutRedirectUris(uris -> uris.addAll(postLogoutRedirectUris))
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE)
            .scope(OidcScopes.EMAIL)
            .tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(ACCESS_TOKEN_TIME_TO_LIVE)
                    .refreshTokenTimeToLive(REFRESH_TOKEN_TIME_TO_LIVE)
                    .reuseRefreshTokens(false)
                    .build())
            .clientSettings(
                ClientSettings.builder()
                    // PKCEを必須とする
                    .requireProofKey(true)
                    // ユーザーの同意画面を表示しない（REQ-00020）
                    .requireAuthorizationConsent(false)
                    .build())
            .build();

    return new InMemoryRegisteredClientRepository(frontendClient);
  }

  /**
   * JWKソースを設定します。
   *
   * @return JWKSource<SecurityContext>
   */
  @Bean
  public JWKSource<SecurityContext> jwkSource() {
    KeyPair keyPair = generateRsaKey();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

    // REQ-00016: RS256 (RSA with SHA-256) アルゴリズムを明示的に設定
    RSAKey rsaKey =
        new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .algorithm(JWSAlgorithm.RS256)
            .keyID(UUID.randomUUID().toString())
            .build();

    JWKSet jwkSet = new JWKSet(rsaKey);
    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
  }

  /**
   * JWTデコーダーを設定します。
   *
   * @return JwtDecoder
   */
  @Bean
  public JwtDecoder jwtDecoder() {
    return org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withJwkSetUri(
            "https://localhost:8443/oauth2/jwks")
        .build();
  }

  /**
   * JWTエンコーダーを設定します。
   *
   * <p>REQ-00012: アクセストークンはJWT (JSON Web Token) 形式で発行すること。 REQ-00016: トークンの署名にはRS256 (RSA with
   * SHA-256) アルゴリズムを使用すること。
   *
   * @return JwtEncoder
   */
  @Bean
  public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
    return new NimbusJwtEncoder(jwkSource);
  }

  /**
   * 認可サーバー設定を設定します。
   *
   * @return AuthorizationServerSettings
   */
  @Bean
  public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().issuer("https://localhost:8443").build();
  }

  /**
   * OAuth2トークンカスタマイザーを設定します。 JWTにユーザー情報、ロール、スコープを追加します。
   *
   * <p>REQ-00003: APIゲートウェイで認証・認可を行うために、JWTにロール情報を含めること。 REQ-00026:
   * JWTのサブジェクト(subクレーム)は認証されたユーザーの一意の識別子を含むこと。
   *
   * @return OAuth2TokenCustomizer<JwtEncodingContext>
   */
  @Bean
  public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
    return context -> {
      if (context.getTokenType().getValue().equals("access_token")) {
        // ユーザー名を追加
        context.getClaims().claim("user_name", context.getPrincipal().getName());

        // ロール情報を authorities クレームに追加 (REQ-00003)
        List<String> roles =
            context.getPrincipal().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        context.getClaims().claim("authorities", roles);

        // スコープ情報を scopes クレームに追加
        if (context.getAuthorizedScopes() != null && !context.getAuthorizedScopes().isEmpty()) {
          context.getClaims().claim("scopes", context.getAuthorizedScopes().toArray(new String[0]));
        }
      }
    };
  }
}
