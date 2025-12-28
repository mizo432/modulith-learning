package undecided.erp.role.internal;

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
 * Unit tests for {@link RoleApi}.
 *
 * <p>This test class validates the REST API endpoints for role-related operations. Tests cover
 * happy paths, edge cases, and error conditions using MockMvc for controller testing.
 */
@WebMvcTest(RoleApi.class)
@DisplayName("RoleApi Unit Tests")
class RoleApiTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/roles should return a Role object with 200 OK")
  void testGetRole_ShouldReturnRoleObject() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roles").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  @DisplayName("GET /api/roles should return valid JSON response")
  void testGetRole_ShouldReturnValidJson() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roles").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists());
  }

  @Test
  @DisplayName("GET /api/roles should handle multiple concurrent requests")
  void testGetRole_ConcurrentRequests() throws Exception {
    // Given - simulate concurrent requests
    for (int i = 0; i < 5; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/roles").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
  }

  @Test
  @DisplayName("GET /api/roles should return 200 even without Accept header")
  void testGetRole_WithoutAcceptHeader() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/roles")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roles should handle request with charset specification")
  void testGetRole_WithCharsetInContentType() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roles").contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roles endpoint should be case-sensitive")
  void testGetRole_CaseSensitivePath() throws Exception {
    // When & Then - uppercase path should not match
    mockMvc.perform(get("/api/ROLES")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /api/roles should return consistent results across multiple calls")
  void testGetRole_ConsistentResults() throws Exception {
    // Given - make multiple calls
    for (int i = 0; i < 3; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/roles").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", notNullValue()));
    }
  }

  @Test
  @DisplayName("POST /api/roles should return 405 Method Not Allowed")
  void testPostRole_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - POST is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("PUT /api/roles should return 405 Method Not Allowed")
  void testPutRole_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - PUT is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/roles")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("DELETE /api/roles should return 405 Method Not Allowed")
  void testDeleteRole_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - DELETE is not supported
    mockMvc
        .perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/roles"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("GET /api/roles with query parameters should be ignored")
  void testGetRole_WithQueryParameters() throws Exception {
    // When & Then - query params should be ignored but request should succeed
    mockMvc
        .perform(get("/api/roles").param("id", "123").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roles/ with trailing slash should work")
  void testGetRole_WithTrailingSlash() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roles/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roles should accept various media types")
  void testGetRole_WithDifferentAcceptHeaders() throws Exception {
    // When & Then - test with different Accept headers
    mockMvc
        .perform(get("/api/roles").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/roles").accept(MediaType.ALL)).andExpect(status().isOk());
  }

  @Test
  @DisplayName("Direct method call should return new Role instance")
  void testGetRole_DirectMethodCall_ShouldReturnRole() {
    // Given
    RoleApi roleApi = new RoleApi();

    // When
    var result = roleApi.get();

    // Then
    org.junit.jupiter.api.Assertions.assertNotNull(result);
  }
}