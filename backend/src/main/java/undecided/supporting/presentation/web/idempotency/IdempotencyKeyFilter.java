package undecided.supporting.presentation.web.idempotency;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 冪等性キーを処理するServlet Filterです。
 *
 * <p>POSTおよびPATCHメソッドのリクエストで`Idempotency-Key`ヘッダーを検出し、 重複リクエストの場合はキャッシュされたレスポンスを返却します。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IdempotencyKeyFilter extends OncePerRequestFilter {

  private static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";
  private static final String IDEMPOTENCY_KEY_ATTRIBUTE = "IDEMPOTENCY_KEY_ATTRIBUTE";
  private static final Set<HttpMethod> IDEMPOTENT_METHODS =
      Set.of(HttpMethod.POST, HttpMethod.PATCH);

  private final IdempotencyKeyStore idempotencyKeyStore;

  public IdempotencyKeyFilter(@NonNull IdempotencyKeyStore idempotencyKeyStore) {
    this.idempotencyKeyStore = idempotencyKeyStore;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String key = request.getHeader(IDEMPOTENCY_KEY_HEADER);
    HttpMethod method = HttpMethod.valueOf(request.getMethod());

    // 冪等性キーが存在しない場合、または対象外のメソッドの場合はフィルタをスキップ
    return key == null || key.isBlank() || !IDEMPOTENT_METHODS.contains(method);
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String key = request.getHeader(IDEMPOTENCY_KEY_HEADER);

    // 既に処理済みのキーの場合は、キャッシュされた結果を返す
    IdempotencyKeyStore.IdempotencyResult cachedResult = idempotencyKeyStore.getIfPresent(key);
    if (cachedResult != null) {
      response.setStatus(cachedResult.status());
      if (cachedResult.body() != null) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(String.valueOf(cachedResult.body()));
      }
      return;
    }

    // 新しいキーの場合は、リクエスト属性にキーを保存して処理を続行
    request.setAttribute(IDEMPOTENCY_KEY_ATTRIBUTE, key);

    // チェーンを続行
    filterChain.doFilter(request, response);

    // レスポンス結果をキャッシュに保存
    if (idempotencyKeyStore.isKeyNew(key)) {
      idempotencyKeyStore.put(key, response.getStatus(), null);
    }
  }
}
