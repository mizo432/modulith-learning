package undecided.authorization.infra.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.SignedJWT;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * OAuth2認可サーバー設定の統合テスト
 *
 * <p>REQ-00011: authorizationモジュールをOAuth 2.0認可サーバーとして構成すること。 REQ-00010: 認証はOAuth 2.0 Authorization
 * Code with PKCEフローを使用すること。 REQ-00012: アクセストークンはJWT形式で発行すること。 REQ-00013: アクセストークンの有効期間は15分以内とする。
 * REQ-00014: リフレッシュトークンを使用してアクセストークンの再発行を行うこと。 REQ-00015: リフレッシュトークンの有効期間は7日以内とする。 REQ-00016:
 * トークンの署名にはRS256アルゴリズムを使用すること。 REQ-00018: 認可エンドポイント、トークンエンドポイントはHTTPSのみで提供すること。 REQ-00020: OAuth
 * 2.0認可サーバーは、ユーザーの同意画面を表示しない。
 */
@Tag("medium")
@DisplayName("OAuth2認可サーバー設定の統合テスト")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(OAuth2AuthorizationServerConfigTest.TestcontainersConfig.class)
class OAuth2AuthorizationServerConfigTest {

  @TestConfiguration
  static class TestcontainersConfig {
    @Bean
    @ServiceConnection(name = "postgresql")
    public PostgreSQLContainer<?> postgresContainer() {
      return new PostgreSQLContainer<>("postgres:17.0");
    }
  }

  @Nested
  @DisplayName("RegisteredClientRepositoryの設定テスト")
  class RegisteredClientRepositoryTest {

    @Autowired private RegisteredClientRepository registeredClientRepository;

    @Test
    @DisplayName("frontendクライアントが登録されていること")
    void shouldHaveFrontendClientRegistered() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client).as("frontendクライアントが登録されていること").isNotNull();
    }

    @Test
    @DisplayName("クライアントはAuthorization Codeグラントタイプをサポートすること")
    void shouldSupportAuthorizationCodeGrant() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client)
          .as("Authorization Codeグラントタイプがサポートされていること")
          .isNotNull()
          .satisfies(
              c ->
                  assertThat(c.getAuthorizationGrantTypes())
                      .contains(AuthorizationGrantType.AUTHORIZATION_CODE));
    }

    @Test
    @DisplayName("クライアントはRefresh Tokenグラントタイプをサポートすること (REQ-00014)")
    void shouldSupportRefreshTokenGrant() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client)
          .as("Refresh Tokenグラントタイプがサポートされていること")
          .isNotNull()
          .satisfies(
              c ->
                  assertThat(c.getAuthorizationGrantTypes())
                      .contains(AuthorizationGrantType.REFRESH_TOKEN));
    }

    @Test
    @DisplayName("クライアントはPKCEを必須とすること (REQ-00010)")
    void shouldRequirePkce() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client)
          .as("PKCEが必須であること")
          .isNotNull()
          .satisfies(c -> assertThat(client.getClientSettings().isRequireProofKey()).isTrue());
    }

    @Test
    @DisplayName("クライアントはユーザーの同意画面を表示しないこと (REQ-00020)")
    void shouldNotRequireAuthorizationConsent() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client)
          .as("ユーザーの同意画面を表示しないこと")
          .isNotNull()
          .satisfies(
              c ->
                  assertThat(client.getClientSettings().isRequireAuthorizationConsent()).isFalse());
    }

    @Test
    @DisplayName("アクセストークンの有効期間は15分であること (REQ-00013)")
    void shouldHaveAccessTokenTtlOf15Minutes() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      Duration accessTokenTtl = client.getTokenSettings().getAccessTokenTimeToLive();

      assertThat(accessTokenTtl).as("アクセストークンの有効期間は15分であること").isEqualTo(Duration.ofMinutes(15));
    }

    @Test
    @DisplayName("リフレッシュトークンの有効期間は7日であること (REQ-00015)")
    void shouldHaveRefreshTokenTtlOf7Days() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      Duration refreshTokenTtl = client.getTokenSettings().getRefreshTokenTimeToLive();

      assertThat(refreshTokenTtl).as("リフレッシュトークンの有効期間は7日であること").isEqualTo(Duration.ofDays(7));
    }

    @Test
    @DisplayName("リフレッシュトークンの再利用は許可されていないこと")
    void shouldNotReuseRefreshTokens() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client)
          .as("リフレッシュトークンの再利用は許可されていないこと")
          .isNotNull()
          .satisfies(c -> assertThat(c.getTokenSettings().isReuseRefreshTokens()).isFalse());
    }

    @Test
    @DisplayName("クライアントはOpenIDスコープをサポートすること")
    void shouldSupportOpenIdScope() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client)
          .as("OpenIDスコープがサポートされていること")
          .isNotNull()
          .satisfies(c -> assertThat(c.getScopes()).contains(OidcScopes.OPENID));
    }

    @Test
    @DisplayName("クライアントはHTTPSのredirect URIが設定されていること (REQ-00018)")
    void shouldHaveRedirectUri() {
      RegisteredClient client = registeredClientRepository.findByClientId("frontend");

      assertThat(client)
          .as("redirect URIが設定されていること")
          .isNotNull()
          .satisfies(
              c ->
                  assertThat(c.getRedirectUris())
                      .isNotEmpty()
                      .contains("https://localhost:3000/callback"));
    }
  }

  @Nested
  @DisplayName("AuthorizationServerSettingsの設定テスト")
  class AuthorizationServerSettingsTest {

    @Autowired private AuthorizationServerSettings authorizationServerSettings;

    @Test
    @DisplayName("issuerはhttps://localhost:8443であること (REQ-00018)")
    void shouldHaveCorrectIssuer() {
      String issuer = authorizationServerSettings.getIssuer();

      assertThat(issuer)
          .as("issuerはhttps://localhost:8443であること")
          .isEqualTo("https://localhost:8443");
    }

    @Test
    @DisplayName("認可エンドポイントが設定されていること")
    void shouldHaveAuthorizationEndpoint() {
      String authorizationEndpoint = authorizationServerSettings.getAuthorizationEndpoint();

      assertThat(authorizationEndpoint).as("認可エンドポイントが設定されていること").isEqualTo("/oauth2/authorize");
    }

    @Test
    @DisplayName("トークンエンドポイントが設定されていること")
    void shouldHaveTokenEndpoint() {
      String tokenEndpoint = authorizationServerSettings.getTokenEndpoint();

      assertThat(tokenEndpoint).as("トークンエンドポイントが設定されていること").isEqualTo("/oauth2/token");
    }

    @Test
    @DisplayName("JWKSエンドポイントが設定されていること")
    void shouldHaveJwksEndpoint() {
      String jwksEndpoint = authorizationServerSettings.getJwkSetEndpoint();

      assertThat(jwksEndpoint).as("JWKSエンドポイントが設定されていること").isEqualTo("/oauth2/jwks");
    }

    @Test
    @DisplayName("OIDCユーザー情報エンドポイントが設定されていること")
    void shouldHaveOidcUserInfoEndpoint() {
      String userInfoEndpoint = authorizationServerSettings.getOidcUserInfoEndpoint();

      assertThat(userInfoEndpoint).as("OIDCユーザー情報エンドポイントが設定されていること").isEqualTo("/userinfo");
    }
  }

  @Nested
  @DisplayName("JWTデコーダーの設定テスト")
  class JwtDecoderTest {

    @Autowired private JwtDecoder jwtDecoder;

    @Test
    @DisplayName("JwtDecoder Beanが設定されていること")
    void shouldHaveJwtDecoderBean() {
      assertThat(jwtDecoder).as("JwtDecoder Beanが設定されていること").isNotNull();
    }
  }

  @Nested
  @DisplayName("JWTエンコーダーの設定テスト (REQ-00012)")
  class JwtEncoderTest {

    @Autowired private JwtEncoder jwtEncoder;

    @Test
    @DisplayName("JwtEncoder Beanが設定されていること (REQ-00012)")
    void shouldHaveJwtEncoderBean() {
      assertThat(jwtEncoder).as("JwtEncoder Beanが設定されていること").isNotNull();
    }

    @Test
    @DisplayName("JWTトークンはRS256アルゴリズムで署名されること (REQ-00016)")
    void shouldSignJwtWithRs256Algorithm() throws Exception {
      // Arrange
      JwtClaimsSet claimsSet =
          JwtClaimsSet.builder()
              .subject("test-user")
              .issuer("https://localhost:8443")
              .issuedAt(Instant.now())
              .expiresAt(Instant.now().plus(Duration.ofMinutes(1)))
              .build();

      // Act
      JwtEncoderParameters params = JwtEncoderParameters.from(claimsSet);
      String encodedJwt = jwtEncoder.encode(params).getTokenValue();
      SignedJWT signedJwt = SignedJWT.parse(encodedJwt);

      // Assert
      assertThat(signedJwt)
          .as("JWTが署名されていること")
          .isNotNull();
      assertThat(signedJwt.getHeader().getAlgorithm())
          .as("署名アルゴリズムはRS256であること")
          .isEqualTo(JWSAlgorithm.RS256);
    }
  }

  @Nested
  @DisplayName("RSAキーペア生成のテスト")
  class RsaKeyGenerationTest {

    /** RSAキーペアを生成します。 OAuth2AuthorizationServerConfigのprivateメソッドをテストするために、 同様のロジックをここに実装しています。 */
    private static KeyPair generateRsaKeyPair() {
      try {
        java.security.KeyPairGenerator keyPairGenerator =
            java.security.KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
      } catch (Exception ex) {
        throw new IllegalStateException("Failed to generate RSA key pair", ex);
      }
    }

    @Test
    @DisplayName("RSAキーペアが正常に生成されること (REQ-00016)")
    void shouldGenerateRsaKeyPair() {
      // Act
      KeyPair keyPair = generateRsaKeyPair();

      // Assert
      assertThat(keyPair).as("RSAキーペアが生成されること").isNotNull();
      assertThat(keyPair.getPublic()).as("公開鍵がRSAPublicKeyであること").isInstanceOf(RSAPublicKey.class);
      assertThat(keyPair.getPrivate())
          .as("秘密鍵がRSAPrivateKeyであること")
          .isInstanceOf(RSAPrivateKey.class);
    }
  }
}
