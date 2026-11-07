package undecided.apigateway.config.ratelimit;

import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * レートリミッティングのキー解決設定
 *
 * <p>リクエストの識別キーを解決し、レートリミッターが各クライアントごとに
 * リクエスト制限を適用できるようにします。
 *
 * <p>この実装では、クライアントのIPアドレスをキーとして使用します。
 * 認証済みのリクエストでは、Principal名を優先して使用します。
 */
@Configuration
public class KeyResolverConfig {

  /**
   * IPアドレスベースのキー解決器を提供します。
   *
   * <p>認証済みのユーザーの場合はPrincipal名を、未認証の場合はクライアントIPアドレスを
   * レートリミットのキーとして使用します。これにより、認証済みユーザーは个人账户で、
   未認証ユーザーはIPアドレスごとに制限されます。
   *
   * @return ServerRequestからキー文字列を解決するFunction
   */
  @Bean
  public java.util.function.Function<ServerRequest, String> ipKeyResolver() {
    return request -> {
      // 認証済みユーザーの場合はPrincipal名を使用
      var principal = request.servletRequest().getUserPrincipal();
      if (principal != null) {
        return principal.getName();
      }
      // 未認証の場合はIPアドレスを使用
      return getClientIp(request.servletRequest());
    };
  }

  /**
   * リクエストからクライアントのIPアドレスを取得します。
   *
   * <p>プロキシ経由の場合、X-Forwarded-Forヘッダーから元のクライアントIPを取得します。
   *
   * @param request Servletリクエスト
   * @return クライアントIPアドレス
   */
  private static String getClientIp(jakarta.servlet.http.HttpServletRequest request) {
    String forwardedFor = request.getHeader("X-Forwarded-For");
    if (forwardedFor != null && !forwardedFor.isEmpty()) {
      // X-Forwarded-Forは "client, proxy1, proxy2" の形式なので、最初のIPがクライアント
      return forwardedFor.split(",")[0].trim();
    }
    String realIp = request.getHeader("X-Real-IP");
    if (realIp != null && !realIp.isEmpty()) {
      return realIp;
    }
    return request.getRemoteAddr();
  }
}
