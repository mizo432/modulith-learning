package undecided.config.presentation.web.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 外部接続ログを出力するためのサーブレットフィルタです。
 *
 * <p>すべてのHTTPリクエストとレスポンスの接続情報をログに記録します。
 * 記録される情報には、HTTPメソッド、URI、クライアントIPアドレス、レスポンスステータスコード、
 * および処理時間が含まれます。
 *
 * <p>このフィルタは、システムへの外部からの接続を監視・記録するために使用され、
 * 非機能要件の「ログ取得・監査証跡（E-05）」に対応します。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExternalConnectionLoggingFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(ExternalConnectionLoggingFilter.class);

  private static final String START_TIME_ATTR =
      ExternalConnectionLoggingFilter.class.getName() + ".startTime";

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // 初期化処理なし
  }

  @Override
  public void destroy() {
    // クリーンアップ処理なし
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    if (!(servletRequest instanceof HttpServletRequest request)
        || !(servletResponse instanceof HttpServletResponse response)) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }

    long startTime = System.nanoTime();
    request.setAttribute(START_TIME_ATTR, startTime);

    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      long handlingTime = System.nanoTime() - startTime;
      logExternalConnection(request, response, handlingTime);
      request.removeAttribute(START_TIME_ATTR);
    }
  }

  /**
   * 外部接続情報をログに記録します。
   *
   * @param request HTTPリクエスト
   * @param response HTTPレスポンス
   * @param handlingTimeNanos 処理時間（ナノ秒）
   */
  private void logExternalConnection(HttpServletRequest request, HttpServletResponse response, long handlingTimeNanos) {
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String clientIp = getClientIp(request);
    int status = response.getStatus();

    if (logger.isInfoEnabled()) {
      logger.info(
          "EXTERNAL_CONNECTION method={} uri={} clientIp={} status={} handlingTime={}ns",
          method, uri, clientIp, status, handlingTimeNanos);
    }
  }

  /**
   * クライアントのIPアドレスを取得します。
   * プロキシ環境を考慮し、X-Forwarded-Forヘッダーが存在する場合はその値を優先します。
   *
   * @param request HTTPリクエスト
   * @return クライアントのIPアドレス
   */
  private String getClientIp(HttpServletRequest request) {
    String forwardedFor = request.getHeader("X-Forwarded-For");
    if (forwardedFor != null && !forwardedFor.isEmpty()) {
      // X-Forwarded-Forは "client, proxy1, proxy2" の形式なので、最初の値がクライアントIP
      return forwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
