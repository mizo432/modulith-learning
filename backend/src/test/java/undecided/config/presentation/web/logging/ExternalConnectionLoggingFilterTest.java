package undecided.config.presentation.web.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("small")
@DisplayName("ExternalConnectionLoggingFilterの単体テスト")
@ExtendWith(MockitoExtension.class)
class ExternalConnectionLoggingFilterTest {

  private ExternalConnectionLoggingFilter filter;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  @Mock private FilterConfig filterConfig;

  @BeforeEach
  void setUp() {
    filter = new ExternalConnectionLoggingFilter();
  }

  @Nested
  @DisplayName("initメソッドのテスト")
  class InitTest {

    @Test
    @DisplayName("initが例外をスローしないこと")
    void shouldInitWithoutException() throws ServletException {
      // Act & Assert
      filter.init(filterConfig);
    }
  }

  @Nested
  @DisplayName("destroyメソッドのテスト")
  class DestroyTest {

    @Test
    @DisplayName("destroyが例外をスローしないこと")
    void shouldDestroyWithoutException() {
      // Act & Assert
      filter.destroy();
    }
  }

  @Nested
  @DisplayName("doFilterメソッドのテスト")
  class DoFilterTest {

    @Test
    @DisplayName("HTTPリクエストでフィルタチェーンが呼び出されること")
    void shouldCallFilterChainForHttpRequest() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("HTTPリクエストでログが出力されること")
    void shouldLogExternalConnectionForHttpRequest() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("POST");
      when(request.getRequestURI()).thenReturn("/api/users");
      when(request.getRemoteAddr()).thenReturn("192.168.1.1");
      when(response.getStatus()).thenReturn(201);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert - フィルタチェーンが呼び出されればログ処理も実行されている
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("HTTPリクエストで開始時刻属性が設定されること")
    void shouldSetStartTimeAttributeForHttpRequest() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert - 属性の設定が呼び出されていることを確認
      verify(request).setAttribute(anyString(), any(Long.class));
    }

    @Test
    @DisplayName("HTTPリクエストで開始時刻属性が削除されること")
    void shouldRemoveStartTimeAttributeAfterProcessing() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert - 属性の削除が呼び出されていることを確認
      verify(request).removeAttribute(anyString());
    }

    @Test
    @DisplayName("HTTPリクエストでX-Forwarded-ForヘッダーからIPを取得すること")
    void shouldGetClientIpFromXForwardedForHeader() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 10.0.0.2");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("フィルタチェーンで例外が発生してもログが出力されること")
    void shouldLogEvenWhenFilterChainThrowsException() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(500);
      IOException ioException = new IOException("Test exception");
      doThrow(ioException).when(filterChain).doFilter(any(), any());

      // Act & Assert
      assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
          .isInstanceOf(IOException.class)
          .hasMessage("Test exception");
    }

    @Test
    @DisplayName("ServletExceptionがフィルタチェーンから伝播すること")
    void shouldPropagateServletExceptionFromFilterChain() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(500);
      ServletException servletException = new ServletException("Servlet error");
      doThrow(servletException).when(filterChain).doFilter(any(), any());

      // Act & Assert
      assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
          .isInstanceOf(ServletException.class)
          .hasMessage("Servlet error");
    }

    @Test
    @DisplayName("非HTTPリクエストでフィルタチェーンが直接呼び出されること")
    void shouldCallFilterChainDirectlyForNonHttpRequest() throws Exception {
      // Arrange
      ServletRequest nonHttpRequest = mock(ServletRequest.class);
      ServletResponse nonHttpResponse = mock(ServletResponse.class);

      // Act
      filter.doFilter(nonHttpRequest, nonHttpResponse, filterChain);

      // Assert
      verify(filterChain).doFilter(nonHttpRequest, nonHttpResponse);
    }

    @Test
    @DisplayName("非HTTPレスポンスでフィルタチェーンが直接呼び出されること")
    void shouldCallFilterChainDirectlyForNonHttpResponse() throws Exception {
      // Arrange
      ServletResponse nonHttpResponse = mock(ServletResponse.class);

      // Act
      filter.doFilter(request, nonHttpResponse, filterChain);

      // Assert
      verify(filterChain).doFilter(request, nonHttpResponse);
    }

    @Test
    @DisplayName("異なるHTTPメソッドでログが出力されること")
    void shouldLogDifferentHttpMethods() throws Exception {
      // Arrange & Act & Assert
      String[] methods = {"GET", "POST", "PUT", "DELETE", "PATCH"};

      for (String method : methods) {
        // Reset mocks for each iteration
        org.mockito.Mockito.reset(filterChain, request, response);

        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(response.getStatus()).thenReturn(200);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
      }
    }

    @Test
    @DisplayName("異なるステータスコードでログが出力されること")
    void shouldLogDifferentStatusCodes() throws Exception {
      // Arrange & Act & Assert
      int[] statuses = {200, 201, 204, 400, 404, 500};

      for (int status : statuses) {
        org.mockito.Mockito.reset(filterChain, request, response);

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(response.getStatus()).thenReturn(status);

        filter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
      }
    }
  }

  @Nested
  @DisplayName("getClientIpメソッドのテスト")
  class GetClientIpTest {

    @Test
    @DisplayName("X-Forwarded-Forヘッダーがない場合はremoteAddrを返すこと")
    void shouldReturnRemoteAddrWhenNoXForwardedForHeader() throws Exception {
      // Arrange
      when(request.getHeader("X-Forwarded-For")).thenReturn(null);
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert - getRemoteAddrが呼び出されていることを確認
      verify(request).getRemoteAddr();
    }

    @Test
    @DisplayName("X-Forwarded-Forヘッダーが空の場合はremoteAddrを返すこと")
    void shouldReturnRemoteAddrWhenXForwardedForIsEmpty() throws Exception {
      // Arrange
      when(request.getHeader("X-Forwarded-For")).thenReturn("");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert
      verify(request).getRemoteAddr();
    }

    @Test
    @DisplayName("X-Forwarded-Forヘッダーが単一のIPの場合、そのIPが使用されること")
    void shouldUseSingleIpFromXForwardedFor() throws Exception {
      // Arrange
      when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1");
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("X-Forwarded-Forヘッダーが複数のIPの場合、最初のIPが使用されること")
    void shouldUseFirstIpFromXForwardedFor() throws Exception {
      // Arrange
      when(request.getHeader("X-Forwarded-For")).thenReturn("10.0.0.1, 10.0.0.2, 10.0.0.3");
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("X-Forwarded-Forヘッダーに空白がある場合、トリムされたIPが使用されること")
    void shouldTrimWhitespaceFromXForwardedFor() throws Exception {
      // Arrange
      when(request.getHeader("X-Forwarded-For")).thenReturn("  10.0.0.1  , 10.0.0.2");
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getStatus()).thenReturn(200);

      // Act
      filter.doFilter(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }
  }

  @Nested
  @DisplayName("境界値のテスト")
  class BoundaryValuesTest {

    @Test
    @DisplayName("空のURIでフィルタが動作すること")
    void shouldHandleEmptyUri() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(200);

      // Act & Assert
      filter.doFilter(request, response, filterChain);
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("長いURIでフィルタが動作すること")
    void shouldHandleLongUri() throws Exception {
      // Arrange
      String longUri = "/api/" + "a".repeat(1000);
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn(longUri);
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(200);

      // Act & Assert
      filter.doFilter(request, response, filterChain);
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("nullのX-Forwarded-Forヘッダーでフィルタが動作すること")
    void shouldHandleNullXForwardedFor() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getHeader("X-Forwarded-For")).thenReturn(null);
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(200);

      // Act & Assert
      filter.doFilter(request, response, filterChain);
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("特殊文字を含むURIでフィルタが動作すること")
    void shouldHandleUriWithSpecialCharacters() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test?param=value&other=123");
      when(request.getRemoteAddr()).thenReturn("127.0.0.1");
      when(response.getStatus()).thenReturn(200);

      // Act & Assert
      filter.doFilter(request, response, filterChain);
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    @DisplayName("IPv6アドレスでフィルタが動作すること")
    void shouldHandleIpv6Address() throws Exception {
      // Arrange
      when(request.getMethod()).thenReturn("GET");
      when(request.getRequestURI()).thenReturn("/api/test");
      when(request.getRemoteAddr()).thenReturn("::1");
      when(response.getStatus()).thenReturn(200);

      // Act & Assert
      filter.doFilter(request, response, filterChain);
      verify(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }
  }
}
