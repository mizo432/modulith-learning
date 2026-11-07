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

@Tag("small")
@DisplayName("JwtAuthenticationEntryPointのテスト")
class JwtAuthenticationEntryPointTest {

  private JwtAuthenticationEntryPoint entryPoint;

  @BeforeEach
  void setUp() {
    entryPoint = new JwtAuthenticationEntryPoint();
  }

  @Nested
  @DisplayName("commenceメソッドのテスト")
  class CommenceTest {

    @Test
    @DisplayName("401ステータスコードが設定されること")
    void shouldSet401Status() throws Exception {
      // Arrange
      var request = mock(HttpServletRequest.class);
      var response = mock(HttpServletResponse.class);
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

      // Act
      entryPoint.commence(request, response, null);

      // Assert
      verify(response).setStatus(401);
    }

    @Test
    @DisplayName("Content-Typeがapplication/jsonに設定されること")
    void shouldSetContentTypeToJson() throws Exception {
      // Arrange
      var request = mock(HttpServletRequest.class);
      var response = mock(HttpServletResponse.class);
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

      // Act
      entryPoint.commence(request, response, null);

      // Assert
      verify(response).setContentType("application/json");
    }

    @Test
    @DisplayName("CharacterEncodingがUTF-8に設定されること")
    void shouldSetCharacterEncodingToUtf8() throws Exception {
      // Arrange
      var request = mock(HttpServletRequest.class);
      var response = mock(HttpServletResponse.class);
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

      // Act
      entryPoint.commence(request, response, null);

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
      when(request.getRequestURI()).thenReturn("/api/test");
      when(response.getWriter()).thenReturn(new PrintWriter(writer));

      // Act
      entryPoint.commence(request, response, null);

      // Assert
      String body = writer.toString();
      assertThat(body)
          .as("レスポンスにstatusフィールドが含まれること")
          .contains("\"status\":401");
      assertThat(body)
          .as("レスポンスにcodeフィールドが含まれること")
          .contains("\"code\":\"UNAUTHORIZED\"");
      assertThat(body)
          .as("レスポンスにmessageフィールドが含まれること")
          .contains("\"message\":\"認証が必要です\"");
      assertThat(body)
          .as("レスポンスにtimestampフィールドが含まれること")
          .contains("\"timestamp\":\"");
      assertThat(body)
          .as("レスポンスにpathフィールドが含まれること")
          .contains("\"path\":\"/api/test\"");
    }
  }
}
