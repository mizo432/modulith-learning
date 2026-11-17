package undecided.apigateway.config.handlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

@Tag("small")
@DisplayName("JwtAccessDeniedHandlerのテスト")
class JwtAccessDeniedHandlerTest {

  private JwtAccessDeniedHandler handler;

  @BeforeEach
  void setUp() {
    handler = new JwtAccessDeniedHandler();
  }

  @Nested
  @DisplayName("handleメソッドのテスト")
  class HandleTest {

    @Test
    @DisplayName("403ステータスコードが設定されること")
    void shouldSet403Status() throws Exception {
      // Arrange
      var request = mock(HttpServletRequest.class);
      var response = mock(HttpServletResponse.class);
      when(request.getRequestURI()).thenReturn("/api/admin/test");
      when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
      var accessDeniedException = mock(AccessDeniedException.class);

      // Act
      handler.handle(request, response, accessDeniedException);

      // Assert
      verify(response).setStatus(403);
    }

    @Test
    @DisplayName("Content-Typeがapplication/jsonに設定されること")
    void shouldSetContentTypeToJson() throws Exception {
      // Arrange
      var request = mock(HttpServletRequest.class);
      var response = mock(HttpServletResponse.class);
      when(request.getRequestURI()).thenReturn("/api/admin/test");
      when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
      var accessDeniedException = mock(AccessDeniedException.class);

      // Act
      handler.handle(request, response, accessDeniedException);

      // Assert
      verify(response).setContentType("application/json");
    }

    @Test
    @DisplayName("CharacterEncodingがUTF-8に設定されること")
    void shouldSetCharacterEncodingToUtf8() throws Exception {
      // Arrange
      var request = mock(HttpServletRequest.class);
      var response = mock(HttpServletResponse.class);
      when(request.getRequestURI()).thenReturn("/api/admin/test");
      when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
      var accessDeniedException = mock(AccessDeniedException.class);

      // Act
      handler.handle(request, response, accessDeniedException);

      // Assert
      verify(response).setCharacterEncoding("UTF-8");
    }

    @Test
    @DisplayName("JSONレスポンスに必須フィールドが含まれること")
    void shouldContainRequiredFields() throws Exception {
      // Arrange
      var request = mock(HttpServletRequest.class);
      var writer = new StringWriter();
      var response = mock(HttpServletResponse.class);
      when(request.getRequestURI()).thenReturn("/api/admin/test");
      when(response.getWriter()).thenReturn(new PrintWriter(writer));
      var accessDeniedException = mock(AccessDeniedException.class);

      // Act
      handler.handle(request, response, accessDeniedException);

      // Assert
      String body = writer.toString();
      assertThat(body)
          .as("レスポンスにstatusフィールドが含まれること")
          .contains("\"status\":403");
      assertThat(body)
          .as("レスポンスにcodeフィールドが含まれること")
          .contains("\"code\":\"FORBIDDEN\"");
      assertThat(body)
          .as("レスポンスにmessageフィールドが含まれること")
          .contains("\"message\":\"アクセスが拒否されました\"");
      assertThat(body)
          .as("レスポンスにtimestampフィールドが含まれること")
          .contains("\"timestamp\":\"");
      assertThat(body)
          .as("レスポンスにpathフィールドが含まれること")
          .contains("\"path\":\"/api/admin/test\"");
    }
  }
}
