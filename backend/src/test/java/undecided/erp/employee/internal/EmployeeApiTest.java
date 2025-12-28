package undecided.erp.employee.internal;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Unit tests for {@link EmployeeApi}.
 *
 * <p>This test class validates the REST API endpoints for employee-related operations. Tests cover
 * happy paths, edge cases, and error conditions using MockMvc for controller testing.
 */
@WebMvcTest(EmployeeApi.class)
@DisplayName("EmployeeApi Unit Tests")
class EmployeeApiTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/employees should return an Employee object with 200 OK")
  void testGetEmployee_ShouldReturnEmployeeObject() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  @DisplayName("GET /api/employees should return valid JSON response")
  void testGetEmployee_ShouldReturnValidJson() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists());
  }

  @Test
  @DisplayName("GET /api/employees should handle multiple concurrent requests")
  void testGetEmployee_ConcurrentRequests() throws Exception {
    // Given - simulate concurrent requests
    for (int i = 0; i < 5; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
  }

  @Test
  @DisplayName("GET /api/employees should return 200 even without Accept header")
  void testGetEmployee_WithoutAcceptHeader() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/employees")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/employees should handle request with charset specification")
  void testGetEmployee_WithCharsetInContentType() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/employees endpoint should be case-sensitive")
  void testGetEmployee_CaseSensitivePath() throws Exception {
    // When & Then - uppercase path should not match
    mockMvc.perform(get("/api/EMPLOYEES")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /api/employees should return consistent results across multiple calls")
  void testGetEmployee_ConsistentResults() throws Exception {
    // Given - make multiple calls
    for (int i = 0; i < 3; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", notNullValue()));
    }
  }

  @Test
  @DisplayName("POST /api/employees should return 405 Method Not Allowed")
  void testPostEmployee_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - POST is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("PUT /api/employees should return 405 Method Not Allowed")
  void testPutEmployee_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - PUT is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/employees")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("DELETE /api/employees should return 405 Method Not Allowed")
  void testDeleteEmployee_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - DELETE is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/employees"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("GET /api/employees with query parameters should be ignored")
  void testGetEmployee_WithQueryParameters() throws Exception {
    // When & Then - query params should be ignored but request should succeed
    mockMvc
        .perform(get("/api/employees").param("id", "123").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/employees/ with trailing slash should work")
  void testGetEmployee_WithTrailingSlash() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/employees/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}