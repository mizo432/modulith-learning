package undecided.apigateway.config.handlers;

import java.io.IOException;
import java.time.Instant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 認証失敗(401)時のレスポンスを返すAuthenticationEntryPoint。
 *
 * <p>設計書 4.1節で定義されたカスタムレスポンス形式でJSONを返します。
 * 未認証のリクエストが保護されたリソースにアクセスした際に呼び出されます。
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AuthenticationException authException)
      throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    String json =
        "{\"error\":{\"status\":401,\"code\":\"UNAUTHORIZED\",\"message\":\"認証が必要です\",\"timestamp\":\""
            + Instant.now()
            + "\",\"path\":\""
            + request.getRequestURI()
            + "\"}}";
    response.getWriter().write(json);
  }
}
