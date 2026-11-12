package undecided.config.presentation.web.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.github.mizo432.shared.logging.ExceptionLogger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
@DisplayName("ExceptionLoggingFilterの単体テスト")
@ExtendWith(MockitoExtension.class)
class ExceptionLoggingFilterTest {

  private ExceptionLoggingFilter filter;

  @Mock private ExceptionLogger exceptionLogger;

  @Mock private ServletRequest request;

  @Mock private ServletResponse response;

  @Mock private FilterChain filterChain;

  @Mock private FilterConfig filterConfig;

  @BeforeEach
  void setUp() {
    filter = new ExceptionLoggingFilter(exceptionLogger);
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
    @DisplayName("例外が発生しない場合、フィルタチェーンが呼び出されること")
    void shouldCallFilterChainWhenNoException() throws Exception {
      // Act
      filter.doFilter(request, response, filterChain);

      // Assert
      verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("IOExceptionが発生した場合、ログ記録後再スローすること")
    void shouldLogAndRethrowIOException() throws Exception {
      // Arrange
      IOException ioException = new IOException("IO error");
      doThrow(ioException).when(filterChain).doFilter(any(), any());

      // Act & Assert
      assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
          .isInstanceOf(IOException.class)
          .hasMessage("IO error");

      verify(exceptionLogger).error(ioException);
    }

    @Test
    @DisplayName("ServletExceptionが発生した場合、ログ記録後再スローすること")
    void shouldLogAndRethrowServletException() throws Exception {
      // Arrange
      ServletException servletException = new ServletException("Servlet error");
      doThrow(servletException).when(filterChain).doFilter(any(), any());

      // Act & Assert
      assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
          .isInstanceOf(ServletException.class)
          .hasMessage("Servlet error");

      verify(exceptionLogger).error(servletException);
    }

    @Test
    @DisplayName("RuntimeExceptionが発生した場合、ログ記録後再スローすること")
    void shouldLogAndRethrowRuntimeException() throws Exception {
      // Arrange
      RuntimeException runtimeException = new RuntimeException("Runtime error");
      doThrow(runtimeException).when(filterChain).doFilter(any(), any());

      // Act & Assert
      assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
          .isInstanceOf(RuntimeException.class)
          .hasMessage("Runtime error");

      verify(exceptionLogger).error(runtimeException);
    }

    @Test
    @DisplayName("NullPointerExceptionが発生した場合、ログ記録後再スローすること")
    void shouldLogAndRethrowNullPointerException() throws Exception {
      // Arrange
      NullPointerException nullPointerException = new NullPointerException("Null error");
      doThrow(nullPointerException).when(filterChain).doFilter(any(), any());

      // Act & Assert
      assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("Null error");

      verify(exceptionLogger).error(nullPointerException);
    }

    @Test
    @DisplayName("IllegalArgumentExceptionが発生した場合、ログ記録後再スローすること")
    void shouldLogAndRethrowIllegalArgumentException() throws Exception {
      // Arrange
      IllegalArgumentException illegalArgumentException =
          new IllegalArgumentException("Illegal argument");
      doThrow(illegalArgumentException).when(filterChain).doFilter(any(), any());

      // Act & Assert
      assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Illegal argument");

      verify(exceptionLogger).error(illegalArgumentException);
    }
  }

  @Nested
  @DisplayName("getExceptionLoggerメソッドのテスト")
  class GetExceptionLoggerTest {

    @Test
    @DisplayName("設定されたExceptionLoggerを返すこと")
    void shouldReturnExceptionLogger() {
      // Act
      ExceptionLogger actual = filter.getExceptionLogger();

      // Assert
      assertThat(actual).isSameAs(exceptionLogger);
    }
  }

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("ExceptionLoggerで初期化されたフィルタが正常に作成されること")
    void shouldCreateFilterWithExceptionLogger() {
      // Assert
      assertThat(filter.getExceptionLogger()).isSameAs(exceptionLogger);
    }
  }
}
