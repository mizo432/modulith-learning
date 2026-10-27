package undecided.config.presentation.web.idempotency;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * 冪等性キーを処理するServlet Filterです。
 *
 * <p>POSTおよびPATCHメソッドのリクエストで`Idempotency-Key`ヘッダーを検出し、 重複リクエストの場合はキャッシュされたレスポンスを返却します。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IdempotencyKeyFilter implements Filter {

  private static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";
  private static final String IDEMPOTENCY_KEY_ATTRIBUTE = "IDEMPOTENCY_KEY_ATTRIBUTE";
  private static final Set<HttpMethod> IDEMPOTENT_METHODS =
      Set.of(HttpMethod.POST, HttpMethod.PATCH);

  private final IdempotencyKeyStore idempotencyKeyStore;

  public IdempotencyKeyFilter(@NonNull IdempotencyKeyStore idempotencyKeyStore) {
    this.idempotencyKeyStore = idempotencyKeyStore;
  }

  @Override
  public void doFilter(
      @NonNull ServletRequest request,
      @NonNull ServletResponse response,
      @NonNull FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String key = httpRequest.getHeader(IDEMPOTENCY_KEY_HEADER);
    HttpMethod method = HttpMethod.valueOf(httpRequest.getMethod());

    // 冪等性キーが存在しない場合、または対象外のメソッドの場合はフィルタをスキップ
    if (key == null || key.isBlank() || !IDEMPOTENT_METHODS.contains(method)) {
      filterChain.doFilter(request, response);
      return;
    }

    // 既に処理済みのキーの場合は、キャッシュされた結果を返す
    IdempotencyKeyStore.IdempotencyResult cachedResult = idempotencyKeyStore.getIfPresent(key);
    if (cachedResult != null) {
      httpResponse.setStatus(cachedResult.status());
      if (cachedResult.body() != null) {
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.getWriter().write(String.valueOf(cachedResult.body()));
      }
      return;
    }

    // 新しいキーの場合は、リクエスト属性にキーを保存して処理を続行
    httpRequest.setAttribute(IDEMPOTENCY_KEY_ATTRIBUTE, key);

    // チェーンを続行
    filterChain.doFilter(request, response);

    // レスポンス結果をキャッシュに保存
    if (idempotencyKeyStore.isKeyNew(key)) {
      idempotencyKeyStore.put(key, httpResponse.getStatus(), null);
    }
  }
}
