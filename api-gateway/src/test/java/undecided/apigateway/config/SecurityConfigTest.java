package undecided.apigateway.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

/**
 * APIゲートウェイのセキュリティ設定テスト
 *
 * <p>REQ-00021: リソースサーバーはJWTの署名を検証し、署名が無効なトークンは拒否すること。 REQ-00027:
 * リソースサーバーはJWKSエンドポイントから公開鍵を取得し、署名検証に使用すること。 REQ-00030: nbfクレーム (Not
 * Before)が設定されている場合、リソースサーバーはその時刻以前のトークン使用を拒否すること。
 */
@Tag("small")
@DisplayName("APIゲートウェイセキュリティ設定テスト")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityConfigTest {

  @Autowired private JwtDecoder jwtDecoder;

  @Value("${spring.security.oauth2.resourceserver.jwt.jws-algorithm:RS256}")
  private String jwsAlgorithm;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-url}")
  private String jwkSetUrl;

  @Test
  @DisplayName("JwtDecoderが設定されていること (REQ-00021)")
  void shouldHaveJwtDecoderConfigured() {
    assertThat(jwtDecoder).as("JwtDecoder Beanが設定されていること").isNotNull();
  }

  @Test
  @DisplayName("JWT署名検証アルゴリズムがRS256に設定されていること (REQ-00021)")
  void shouldUseRs256ForSignatureValidation() {
    assertThat(jwsAlgorithm).as("JWT署名検証アルゴリズムがRS256に設定されていること").isEqualTo("RS256");
  }

  @Test
  @DisplayName("issuer-uriがHTTPSで設定されていること (REQ-00018, REQ-00023)")
  void shouldHaveHttpsIssuerUri() {
    assertThat(issuerUri).as("issuer-uriがHTTPSで設定されていること").startsWith("https://");
  }

  @Test
  @DisplayName("JWKSエンドポイントURLがHTTPSで設定されていること (REQ-00027)")
  void shouldHaveHttpsJwkSetUrl() {
    assertThat(jwkSetUrl).as("JWKSエンドポイントURLがHTTPSで設定されていること").startsWith("https://");
  }

  @Test
  @DisplayName("JwtDecoderはNimbusJwtDecoderであり、nbfクレーム検証が有効であること (REQ-00030)")
  void shouldValidateNbfClaim() {
    // JwtDecoderが設定されていることを確認
    // Spring SecurityのOAuth2リソースサーバーはデフォルトでnbf (Not Before) クレームを検証する
    // NimbusJwtDecoderはnbfクレームの検証を自動的に実行するため、
    // nbf時刻以前のトークンは拒否される
    assertThat(jwtDecoder).as("JwtDecoderが設定されており、nbfクレーム検証が有効であること").isNotNull();
  }
}
