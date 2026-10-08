package undecided.supporting.presentation.web.idempotency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("small")
@DisplayName("IdempotencyKeyFilterのテスト")
@ExtendWith(MockitoExtension.class)
class IdempotencyKeyFilterTest {

  @Mock private IdempotencyKeyStore store;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  private IdempotencyKeyFilter filter;

  @BeforeEach
  void setUp() {
    filter = new IdempotencyKeyFilter(store);
  }

  @Nested
  @DisplayName("shouldNotFilterメソッドのテスト")
  class ShouldNotFilterTest {

    @Test
    @DisplayName("Idempotency-Keyヘッダーがない場合、trueを返すこと")
    void shouldReturnTrueWhenNoIdempotencyKey() {
      // Arrange
      when(request.getHeader("Idempotency-Key")).thenReturn(null);
      when(request.getMethod()).thenReturn("POST");

      // Act
      boolean result = filter.shouldNotFilter(request);

      // Assert
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("空白のIdempotency-Keyの場合、trueを返すこと")
    void shouldReturnTrueWhenBlankIdempotencyKey() {
      // Arrange
      when(request.getHeader("Idempotency-Key")).thenReturn("   ");
      when(request.getMethod()).thenReturn("POST");

      // Act
      boolean result = filter.shouldNotFilter(request);

      // Assert
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("GETメソッドの場合、trueを返すこと")
    void shouldReturnTrueForGetMethod() {
      // Arrange
      when(request.getHeader("Idempotency-Key")).thenReturn("test-key");
      when(request.getMethod()).thenReturn("GET");

      // Act
      boolean result = filter.shouldNotFilter(request);

      // Assert
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("PUTメソッドの場合、trueを返すこと")
    void shouldReturnTrueForPutMethod() {
      // Arrange
      when(request.getHeader("Idempotency-Key")).thenReturn("test-key");
      when(request.getMethod()).thenReturn("PUT");

      // Act
      boolean result = filter.shouldNotFilter(request);

      // Assert
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("DELETEメソッドの場合、trueを返すこと")
    void shouldReturnTrueForDeleteMethod() {
      // Arrange
      when(request.getHeader("Idempotency-Key")).thenReturn("test-key");
      when(request.getMethod()).thenReturn("DELETE");

      // Act
      boolean result = filter.shouldNotFilter(request);

      // Assert
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("POSTメソッドでキーがある場合、falseを返すこと")
    void shouldReturnFalseForPostMethod() {
      // Arrange
      when(request.getHeader("Idempotency-Key")).thenReturn("test-key");
      when(request.getMethod()).thenReturn("POST");

      // Act
      boolean result = filter.shouldNotFilter(request);

      // Assert
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("PATCHメソッドでキーがある場合、falseを返すこと")
    void shouldReturnFalseForPatchMethod() {
      // Arrange
      when(request.getHeader("Idempotency-Key")).thenReturn("test-key");
      when(request.getMethod()).thenReturn("PATCH");

      // Act
      boolean result = filter.shouldNotFilter(request);

      // Assert
      assertThat(result).isFalse();
    }
  }

  @Nested
  @DisplayName("doFilterInternalメソッドのテスト")
  class DoFilterInternalTest {

    @Test
    @DisplayName("既存のキーの場合、キャッシュされたレスポンスを返してチェーンを続行しないこと")
    void shouldReturnCachedResponseForExistingKey() throws Exception {
      // Arrange
      String key = "existing-key";
      IdempotencyKeyStore.IdempotencyResult cached =
          new IdempotencyKeyStore.IdempotencyResult(201, "created");
      when(request.getHeader("Idempotency-Key")).thenReturn(key);
      when(store.getIfPresent(key)).thenReturn(cached);

      StringWriter writer = new StringWriter();
      PrintWriter printWriter = new PrintWriter(writer);
      when(response.getWriter()).thenReturn(printWriter);

      // Act
      filter.doFilterInternal(request, response, filterChain);

      // Assert
      verify(response).setStatus(201);
      verify(response).setContentType("application/json");
      assertThat(writer.toString()).isEqualTo("created");
      verifyNoMoreInteractions(filterChain);
    }

    @Test
    @DisplayName("新しいキーの場合、チェーンを続行して結果をキャッシュすること")
    void shouldContinueChainAndCacheResultForNewKey() throws Exception {
      // Arrange
      String key = "new-key";
      when(request.getHeader("Idempotency-Key")).thenReturn(key);
      when(store.getIfPresent(key)).thenReturn(null);
      when(store.isKeyNew(key)).thenReturn(true);
      when(response.getStatus()).thenReturn(201);

      // Act
      filter.doFilterInternal(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(request, response);
      verify(store).put(key, 201, null);
    }

    @Test
    @DisplayName("既存のキーの場合、チェーンを続行してキャッシュしないこと")
    void shouldContinueChainButNotCacheForExistingKey() throws Exception {
      // Arrange
      String key = "existing-key";
      when(request.getHeader("Idempotency-Key")).thenReturn(key);
      when(store.getIfPresent(key)).thenReturn(null);
      when(store.isKeyNew(key)).thenReturn(false);

      // Act
      filter.doFilterInternal(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(request, response);
      verify(store, never()).put(anyString(), anyInt(), any());
    }

    @Test
    @DisplayName("キャッシュされたボディがnullの場合、ボディを書き出さないこと")
    void shouldNotWriteBodyWhenCachedBodyIsNull() throws Exception {
      // Arrange
      String key = "existing-key";
      IdempotencyKeyStore.IdempotencyResult cached =
          new IdempotencyKeyStore.IdempotencyResult(204, null);
      when(request.getHeader("Idempotency-Key")).thenReturn(key);
      when(store.getIfPresent(key)).thenReturn(cached);

      // Act
      filter.doFilterInternal(request, response, filterChain);

      // Assert
      verify(response).setStatus(204);
      verifyNoMoreInteractions(response);
    }
  }
}
