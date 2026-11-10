package undecided.apigateway.config.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(JwtAccessDeniedHandler.class);
  private static final String CODE = "FORBIDDEN";
  private static final String MESSAGE = "Access is denied";

  private final @NonNull ObjectMapper objectMapper;

  public JwtAccessDeniedHandler(@NonNull ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull AccessDeniedException accessDeniedException)
      throws IOException {
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");

    SecurityErrorResponse errorResponse =
        new SecurityErrorResponse(
            HttpStatus.FORBIDDEN.value(), CODE, MESSAGE, request.getRequestURI());

    try {
      String json = objectMapper.writeValueAsString(errorResponse);
      response.getWriter().write(json);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize access denied error response", e);
      response.getWriter().write(fallbackJson(request.getRequestURI()));
    }
  }

  private static String fallbackJson(String path) {
    return "{\"error\":{\"status\":403,\"code\":\"FORBIDDEN\",\"message\":\"Access is denied\",\"path\":\""
        + escapeJsonString(path) + "\"}}";
  }

  private static String escapeJsonString(String value) {
    return value.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t");
  }
}
