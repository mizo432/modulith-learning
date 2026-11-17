package undecided.apigateway.config.handlers;

import java.io.IOException;
import java.time.Instant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 認可失敗(403)時のレスポンスを返すAccessDeniedHandler。
 *
 * <p>設計書 4.1節で定義されたカスタムレスポンス形式でJSONを返します。
 * 認証済みだが権限不足のリクエストが保護されたリソースにアクセスした際に呼び出されます。
 */
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AccessDeniedException accessDeniedException)
      throws IOException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    String json =
        "{\"error\":{\"status\":403,\"code\":\"FORBIDDEN\",\"message\":\"アクセスが拒否されました\",\"timestamp\":\""
            + Instant.now()
            + "\",\"path\":\""
            + request.getRequestURI()
            + "\"}}";
    response.getWriter().write(json);
  }
}
