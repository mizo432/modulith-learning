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
   * <p>セキュリティ上の理由から、X-Forwarded-ForやX-Real-IPなどのプロキシヘッダーは信頼せず、
   * 直接接続元のIPアドレス（getRemoteAddr）のみを使用します。
   *
   * <p>クライアントはこれらのヘッダーを任意の値に設定できるため、信頼するとレートリミットを
   * 回避する攻撃が可能になります。プロキシ環境では、プロキシサーバーでIPアドレスを適切に
   * 転送することを前提とし、ゲートウェイが直接接続する相手のIPアドレスを使用します。
   *
   * @param request Servletリクエスト
   * @return クライアントIPアドレス
   */
  private static String getClientIp(jakarta.servlet.http.HttpServletRequest request) {
    // セキュリティ上の理由から、クライアントが制御可能なヘッダーは信頼しない
    // プロキシ環境では、プロキシのIPアドレスが返されるが、これは信頼できる値
    return request.getRemoteAddr();
  }
}
