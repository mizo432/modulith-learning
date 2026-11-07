package undecided.apigateway.config;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * CORS（Cross-Origin Resource Sharing）の設定プロパティ。
 *
 * <p>設計書 5節に基づくCORS設定を環境ごとに切り替えられるようにします。
 * 開発環境では `application.yml` で、本番環境では `application-prod.yml` で
 * 異なるオリジンを設定してください。
 */
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

  /** 許可されたオリジンのリスト */
  private @NonNull List<String> allowedOrigins =
      List.of("http://localhost:3000", "https://agiloom.example.com");

  /** 許可されたHTTPメソッドのリスト */
  private @NonNull List<String> allowedMethods =
      List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");

  /** 許可されたヘッダーのリスト（`*` はすべてを許可） */
  private @NonNull List<String> allowedHeaders = List.of("*");

  /** クライアントからアクセス可能なレスポンスヘッダーのリスト */
  private @NonNull List<String> exposedHeaders = List.of("Authorization");

  /** クレデンシャル（Cookie、Authorizationヘッダー）の送信を許可するかどうか */
  private boolean allowCredentials = true;

  /** プリフライトリクエストのキャッシュ時間（秒） */
  private long maxAge = 3600L;

  @NonNull
  public List<String> getAllowedOrigins() {
    return allowedOrigins;
  }

  public void setAllowedOrigins(@NonNull List<String> allowedOrigins) {
    this.allowedOrigins = allowedOrigins;
  }

  @NonNull
  public List<String> getAllowedMethods() {
    return allowedMethods;
  }

  public void setAllowedMethods(@NonNull List<String> allowedMethods) {
    this.allowedMethods = allowedMethods;
  }

  @NonNull
  public List<String> getAllowedHeaders() {
    return allowedHeaders;
  }

  public void setAllowedHeaders(@NonNull List<String> allowedHeaders) {
    this.allowedHeaders = allowedHeaders;
  }

  @NonNull
  public List<String> getExposedHeaders() {
    return exposedHeaders;
  }

  public void setExposedHeaders(@NonNull List<String> exposedHeaders) {
    this.exposedHeaders = exposedHeaders;
  }

  public boolean isAllowCredentials() {
    return allowCredentials;
  }

  public void setAllowCredentials(boolean allowCredentials) {
    this.allowCredentials = allowCredentials;
  }

  public long getMaxAge() {
    return maxAge;
  }

  public void setMaxAge(long maxAge) {
    this.maxAge = maxAge;
  }
}
