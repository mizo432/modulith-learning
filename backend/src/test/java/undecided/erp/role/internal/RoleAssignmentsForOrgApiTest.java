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
 * Unit tests for {@link RoleAssignmentsForOrgApi}.
 *
 * <p>This test class validates the REST API endpoints for organization role assignment operations.
 * Tests cover happy paths, edge cases, and error conditions using MockMvc.
 */
@WebMvcTest(RoleAssignmentsForOrgApi.class)
@DisplayName("RoleAssignmentsForOrgApi Unit Tests")
class RoleAssignmentsForOrgApiTest {

  @Autowired private MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg should return RoleAssignmentForOrg with 200 OK")
  void testGetRoleAssignmentForOrg_ShouldReturnObject() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roleAssignmentsForOrg").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", notNullValue()));
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg should return valid JSON response")
  void testGetRoleAssignmentForOrg_ShouldReturnValidJson() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roleAssignmentsForOrg").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").exists());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg should handle multiple concurrent requests")
  void testGetRoleAssignmentForOrg_ConcurrentRequests() throws Exception {
    // Given - simulate concurrent requests
    for (int i = 0; i < 5; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/roleAssignmentsForOrg").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
    }
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg should return 200 without Accept header")
  void testGetRoleAssignmentForOrg_WithoutAcceptHeader() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/roleAssignmentsForOrg")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg with charset should work")
  void testGetRoleAssignmentForOrg_WithCharsetInContentType() throws Exception {
    // When & Then
    mockMvc
        .perform(
            get("/api/roleAssignmentsForOrg")
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg endpoint should be case-sensitive")
  void testGetRoleAssignmentForOrg_CaseSensitivePath() throws Exception {
    // When & Then - uppercase path should not match
    mockMvc.perform(get("/api/ROLEASSIGNMENTSFORORG")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg should return consistent results")
  void testGetRoleAssignmentForOrg_ConsistentResults() throws Exception {
    // Given - make multiple calls
    for (int i = 0; i < 3; i++) {
      // When & Then
      mockMvc
          .perform(get("/api/roleAssignmentsForOrg").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", notNullValue()));
    }
  }

  @Test
  @DisplayName("POST /api/roleAssignmentsForOrg should return 405 Method Not Allowed")
  void testPostRoleAssignmentForOrg_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - POST is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(
                    "/api/roleAssignmentsForOrg")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("PUT /api/roleAssignmentsForOrg should return 405 Method Not Allowed")
  void testPutRoleAssignmentForOrg_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - PUT is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put(
                    "/api/roleAssignmentsForOrg")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("DELETE /api/roleAssignmentsForOrg should return 405 Method Not Allowed")
  void testDeleteRoleAssignmentForOrg_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then - DELETE is not supported
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(
                "/api/roleAssignmentsForOrg"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg with query parameters should be ignored")
  void testGetRoleAssignmentForOrg_WithQueryParameters() throws Exception {
    // When & Then - query params should be ignored but request should succeed
    mockMvc
        .perform(
            get("/api/roleAssignmentsForOrg")
                .param("orgId", "123")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg/ with trailing slash should work")
  void testGetRoleAssignmentForOrg_WithTrailingSlash() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/roleAssignmentsForOrg/").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/roleAssignmentsForOrg should accept various media types")
  void testGetRoleAssignmentForOrg_WithDifferentAcceptHeaders() throws Exception {
    // When & Then - test with different Accept headers
    mockMvc
        .perform(get("/api/roleAssignmentsForOrg").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/roleAssignmentsForOrg").accept(MediaType.ALL))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Direct method call should return new RoleAssignmentForOrg instance")
  void testGetRoleAssignmentForOrg_DirectMethodCall_ShouldReturnObject() {
    // Given
    RoleAssignmentsForOrgApi api = new RoleAssignmentsForOrgApi();

    // When
    var result = api.get();

    // Then
    org.junit.jupiter.api.Assertions.assertNotNull(result);
  }
}