package undecided.apigateway.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

  /** 許可されたオリジンのリスト。本番環境では必ず明示的に設定すること */
  private @NonNull List<String> allowedOrigins =
      List.of("http://localhost:3000");

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
}
