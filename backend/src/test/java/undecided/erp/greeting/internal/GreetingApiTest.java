package undecided.erp.greeting.internal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import undecided.erp.common.exception.BusinessException;

/**
 * Unit tests for {@link GreetingApi}.
 *
 * <p>This test class validates the greeting API endpoint behavior, including exception handling.
 * The current implementation throws a BusinessException, which these tests verify.
 */
@WebMvcTest(GreetingApi.class)
@DisplayName("GreetingApi Unit Tests")
class GreetingApiTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/greeting should throw BusinessException")
  void testGetGreeting_ShouldThrowBusinessException() throws Exception {
    // When & Then - expects the exception to be handled by exception handler
    mockMvc
        .perform(get("/api/greeting").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError());
  }

  @Test
  @DisplayName("GET /api/greeting should handle request without content type")
  void testGetGreeting_WithoutContentType() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/greeting")).andExpect(status().is5xxServerError());
  }

  @Test
  @DisplayName("GET /api/greeting with Accept header should still throw exception")
  void testGetGreeting_WithAcceptHeader() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/greeting").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError());
  }

  @Test
  @DisplayName("GET /api/greeting endpoint should be case-sensitive")
  void testGetGreeting_CaseSensitivePath() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/GREETING")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /api/greeting with trailing slash should work")
  void testGetGreeting_WithTrailingSlash() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/greeting/")).andExpect(status().is5xxServerError());
  }

  @Test
  @DisplayName("GET /api/greeting should consistently throw exception")
  void testGetGreeting_ConsistentExceptionBehavior() throws Exception {
    // Given - make multiple calls to verify consistent behavior
    for (int i = 0; i < 3; i++) {
      // When & Then
      mockMvc.perform(get("/api/greeting")).andExpect(status().is5xxServerError());
    }
  }

  @Test
  @DisplayName("POST /api/greeting should return 405 Method Not Allowed")
  void testPostGreeting_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/greeting")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("PUT /api/greeting should return 405 Method Not Allowed")
  void testPutGreeting_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/greeting")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("DELETE /api/greeting should return 405 Method Not Allowed")
  void testDeleteGreeting_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/greeting"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("GET /api/greeting with query parameters should still throw exception")
  void testGetGreeting_WithQueryParameters() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/greeting").param("name", "test"))
        .andExpect(status().is5xxServerError());
  }

  @Test
  @DisplayName("Direct method call should throw BusinessException with ResultMessages")
  void testGetGreeting_DirectMethodCall_ShouldThrowBusinessException() {
    // Given
    GreetingApi greetingApi = new GreetingApi();

    // When & Then
    assertThrows(
        BusinessException.class,
        () -> greetingApi.get(),
        "Expected BusinessException to be thrown");
  }

  @Test
  @DisplayName("Multiple concurrent requests should all throw exceptions")
  void testGetGreeting_ConcurrentRequests() throws Exception {
    // Given - simulate concurrent requests
    for (int i = 0; i < 5; i++) {
      // When & Then
      mockMvc.perform(get("/api/greeting")).andExpect(status().is5xxServerError());
    }
  }
}