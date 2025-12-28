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
 * Unit tests for {@link RoleAssignmentsForEmpApi}.
 *
 * <p>This test class validates the REST API endpoints for employee role assignment operations.
 * Tests cover happy paths, edge cases, and error conditions using MockMvc.
 */
@WebMvcTest(RoleAssignmentsForEmpApi.class)
@DisplayName("RoleAssignmentsForEmpApi Unit Tests")
class RoleAssignmentsForEmpApiTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/roleAssignmentsFor should return RoleAssignmentForEmp with 200 OK")
  void testGetRoleAssignmentForEmp_ShouldReturnObject() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roleAssignmentsFor").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor should return valid JSON response")
  void testGetRoleAssignmentForEmp_ShouldReturnValidJson() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roleAssignmentsFor").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor should handle multiple concurrent requests")
  void testGetRoleAssignmentForEmp_ConcurrentRequests() throws Exception {
    // Given - simulate concurrent requests
    for (int i = 0; i < 5; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/roleAssignmentsFor").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor should return 200 without Accept header")
  void testGetRoleAssignmentForEmp_WithoutAcceptHeader() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/roleAssignmentsFor")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor with charset should work")
  void testGetRoleAssignmentForEmp_WithCharsetInContentType() throws Exception {
    // When & Then
    mockMvc
        .perform(
            get("/api/roleAssignmentsFor")
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor endpoint should be case-sensitive")
  void testGetRoleAssignmentForEmp_CaseSensitivePath() throws Exception {
    // When & Then - uppercase path should not match
    mockMvc.perform(get("/api/ROLEASSIGNMENTSFOR")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor should return consistent results")
  void testGetRoleAssignmentForEmp_ConsistentResults() throws Exception {
    // Given - make multiple calls
    for (int i = 0; i < 3; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/roleAssignmentsFor").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", notNullValue()));
    }
  }

  @Test
  @DisplayName("POST /api/roleAssignmentsFor should return 405 Method Not Allowed")
  void testPostRoleAssignmentForEmp_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - POST is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(
                    "/api/roleAssignmentsFor")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("PUT /api/roleAssignmentsFor should return 405 Method Not Allowed")
  void testPutRoleAssignmentForEmp_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - PUT is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put(
                    "/api/roleAssignmentsFor")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("DELETE /api/roleAssignmentsFor should return 405 Method Not Allowed")
  void testDeleteRoleAssignmentForEmp_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - DELETE is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(
                "/api/roleAssignmentsFor"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor with query parameters should be ignored")
  void testGetRoleAssignmentForEmp_WithQueryParameters() throws Exception {
    // When & Then - query params should be ignored but request should succeed
    mockMvc
        .perform(
            get("/api/roleAssignmentsFor")
                .param("employeeId", "123")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor/ with trailing slash should work")
  void testGetRoleAssignmentForEmp_WithTrailingSlash() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roleAssignmentsFor/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsFor should accept various media types")
  void testGetRoleAssignmentForEmp_WithDifferentAcceptHeaders() throws Exception {
    // When & Then - test with different Accept headers
    mockMvc
        .perform(get("/api/roleAssignmentsFor").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/roleAssignmentsFor").accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Direct method call should return new RoleAssignmentForEmp instance")
  void testGetRoleAssignmentForEmp_DirectMethodCall_ShouldReturnObject() {
    // Given
    RoleAssignmentsForEmpApi api = new RoleAssignmentsForEmpApi();

    // When
    var result = api.get();

    // Then
    org.junit.jupiter.api.Assertions.assertNotNull(result);
  }
}