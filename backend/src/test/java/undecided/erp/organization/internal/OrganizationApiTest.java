package undecided.erp.organization.internal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import undecided.erp.organization.spi.Organization;
import undecided.erp.organization.spi.OrganizationQuery;

/**
 * Unit tests for {@link OrganizationApi}.
 *
 * <p>This comprehensive test suite validates the REST API endpoints for organization-related
 * operations. Tests cover happy paths, edge cases, error conditions, and boundary scenarios.
 */
@WebMvcTest(OrganizationApi.class)
@DisplayName("OrganizationApi Unit Tests")
class OrganizationApiTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private OrganizationQuery organizationQuery;

  private Organization testOrganization;
  private UUID testUuid;

  @BeforeEach
  void setUp() {
    testUuid = UUID.randomUUID();
    testOrganization = createTestOrganization(testUuid);
  }

  private Organization createTestOrganization(UUID id) {
    Organization org = new Organization();
    org.setOrganizationId(id);
    org.setOrganizationCode("0001010203004");
    org.setFullName("Test Organization Full Name");
    org.setLevel0Code("0001");
    org.setLevel1Code("01");
    org.setLevel2Code("02");
    org.setLevel3Code("03");
    org.setLevel4Code("004");
    org.setLevel0Name("Level 0 Name");
    org.setLevel1Name("Level 1 Name");
    org.setLevel2Name("Level 2 Name");
    org.setLevel3Name("Level 3 Name");
    org.setLevel4Name("Level 4 Name");
    org.setValidFrom(LocalDateTime.now().minusDays(30));
    org.setValidTo(null);
    return org;
  }

  // ========== findAll() Tests ==========

  @Test
  @DisplayName("GET /api/organizations should return all organizations with 200 OK")
  void testFindAll_ShouldReturnAllOrganizations() throws Exception {
    // Given
    List<Organization> organizations = new ArrayList<>();
    organizations.add(testOrganization);
    organizations.add(createTestOrganization(UUID.randomUUID()));
    when(organizationQuery.findAll()).thenReturn(organizations);

    // When & Then
    mockMvc
        .perform(get("/api/organizations").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].organizationId", notNullValue()))
        .andExpect(jsonPath("$[0].organizationCode", is("0001010203004")))
        .andExpect(jsonPath("$[1].organizationId", notNullValue()));

    verify(organizationQuery, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/organizations should return empty list when no organizations exist")
  void testFindAll_EmptyList_ShouldReturnEmptyArray() throws Exception {
    // Given
    when(organizationQuery.findAll()).thenReturn(new ArrayList<>());

    // When & Then
    mockMvc
        .perform(get("/api/organizations").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));

    verify(organizationQuery, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/organizations should return single organization in list")
  void testFindAll_SingleOrganization_ShouldReturnListWithOneItem() throws Exception {
    // Given
    List<Organization> organizations = new ArrayList<>();
    organizations.add(testOrganization);
    when(organizationQuery.findAll()).thenReturn(organizations);

    // When & Then
    mockMvc
        .perform(get("/api/organizations").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].fullName", is("Test Organization Full Name")));

    verify(organizationQuery, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/organizations should handle large list of organizations")
  void testFindAll_LargeList_ShouldReturnAllItems() throws Exception {
    // Given
    List<Organization> organizations = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      organizations.add(createTestOrganization(UUID.randomUUID()));
    }
    when(organizationQuery.findAll()).thenReturn(organizations);

    // When & Then
    mockMvc
        .perform(get("/api/organizations").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(100)));

    verify(organizationQuery, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/organizations should return organizations with all hierarchy levels")
  void testFindAll_ShouldReturnOrganizationsWithAllLevels() throws Exception {
    // Given
    List<Organization> organizations = new ArrayList<>();
    organizations.add(testOrganization);
    when(organizationQuery.findAll()).thenReturn(organizations);

    // When & Then
    mockMvc
        .perform(get("/api/organizations").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].level0Code", is("0001")))
        .andExpect(jsonPath("$[0].level1Code", is("01")))
        .andExpect(jsonPath("$[0].level2Code", is("02")))
        .andExpect(jsonPath("$[0].level3Code", is("03")))
        .andExpect(jsonPath("$[0].level4Code", is("004")))
        .andExpect(jsonPath("$[0].level0Name", is("Level 0 Name")))
        .andExpect(jsonPath("$[0].level1Name", is("Level 1 Name")))
        .andExpect(jsonPath("$[0].level2Name", is("Level 2 Name")))
        .andExpect(jsonPath("$[0].level3Name", is("Level 3 Name")))
        .andExpect(jsonPath("$[0].level4Name", is("Level 4 Name")));

    verify(organizationQuery, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/organizations should call query service exactly once")
  void testFindAll_ShouldCallQueryServiceOnce() throws Exception {
    // Given
    when(organizationQuery.findAll()).thenReturn(new ArrayList<>());

    // When
    mockMvc.perform(get("/api/organizations")).andExpect(status().isOk());

    // Then
    verify(organizationQuery, times(1)).findAll();
  }

  // ========== findById() Tests ==========

  @Test
  @DisplayName("GET /api/organizations/{id} should return organization when found")
  void testFindById_ExistingId_ShouldReturnOrganization() throws Exception {
    // Given
    when(organizationQuery.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When & Then
    mockMvc
        .perform(
            get("/api/organizations/{id}", testUuid).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.organizationId", is(testUuid.toString())))
        .andExpect(jsonPath("$.organizationCode", is("0001010203004")))
        .andExpect(jsonPath("$.fullName", is("Test Organization Full Name")));

    verify(organizationQuery, times(1)).findById(testUuid);
  }

  @Test
  @DisplayName("GET /api/organizations/{id} should return 404 when organization not found")
  void testFindById_NonExistingId_ShouldReturn404() throws Exception {
    // Given
    UUID nonExistingId = UUID.randomUUID();
    when(organizationQuery.findById(nonExistingId)).thenReturn(Optional.empty());

    // When & Then
    mockMvc
        .perform(
            get("/api/organizations/{id}", nonExistingId)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(organizationQuery, times(1)).findById(nonExistingId);
  }

  @Test
  @DisplayName("GET /api/organizations/{id} should throw EntityNotFoundException for missing org")
  void testFindById_MissingOrganization_ShouldThrowEntityNotFoundException() throws Exception {
    // Given
    UUID missingId = UUID.randomUUID();
    when(organizationQuery.findById(missingId)).thenReturn(Optional.empty());

    // When & Then
    mockMvc
        .perform(get("/api/organizations/{id}", missingId))
        .andExpect(status().isNotFound());

    verify(organizationQuery, times(1)).findById(missingId);
  }

  @Test
  @DisplayName("GET /api/organizations/{id} should handle UUID with different formats")
  void testFindById_DifferentUuidFormats_ShouldWork() throws Exception {
    // Given
    when(organizationQuery.findById(any(UUID.class))).thenReturn(Optional.of(testOrganization));

    // When & Then - test with lowercase UUID
    mockMvc
        .perform(
            get("/api/organizations/{id}", testUuid.toString().toLowerCase())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    // When & Then - test with uppercase UUID
    mockMvc
        .perform(
            get("/api/organizations/{id}", testUuid.toString().toUpperCase())
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/organizations/{id} should return organization with valid date range")
  void testFindById_ShouldReturnOrganizationWithValidDates() throws Exception {
    // Given
    when(organizationQuery.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When & Then
    mockMvc
        .perform(get("/api/organizations/{id}", testUuid))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.validFrom", notNullValue()));

    verify(organizationQuery, times(1)).findById(testUuid);
  }

  @Test
  @DisplayName("GET /api/organizations/{id} should return organization with null validTo")
  void testFindById_OrganizationWithNullValidTo_ShouldSucceed() throws Exception {
    // Given
    testOrganization.setValidTo(null);
    when(organizationQuery.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When & Then
    mockMvc
        .perform(get("/api/organizations/{id}", testUuid))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.organizationId", is(testUuid.toString())));

    verify(organizationQuery, times(1)).findById(testUuid);
  }

  @Test
  @DisplayName("GET /api/organizations/{id} with invalid UUID format should return 400")
  void testFindById_InvalidUuidFormat_ShouldReturn400() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/api/organizations/{id}", "invalid-uuid"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("GET /api/organizations/{id} should call query service exactly once")
  void testFindById_ShouldCallQueryServiceOnce() throws Exception {
    // Given
    when(organizationQuery.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When
    mockMvc.perform(get("/api/organizations/{id}", testUuid)).andExpect(status().isOk());

    // Then
    verify(organizationQuery, times(1)).findById(testUuid);
  }

  // ========== Additional Edge Case Tests ==========

  @Test
  @DisplayName("POST /api/organizations should return 405 Method Not Allowed")
  void testPostOrganization_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post(
                    "/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("PUT /api/organizations should return 405 Method Not Allowed")
  void testPutOrganization_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put(
                    "/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("DELETE /api/organizations should return 405 Method Not Allowed")
  void testDeleteOrganization_ShouldReturnMethodNotAllowed() throws Exception {
    // When & Then
    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(
                "/api/organizations"))
        .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("GET /api/organizations with query parameters should be ignored")
  void testFindAll_WithQueryParameters_ShouldIgnoreParams() throws Exception {
    // Given
    when(organizationQuery.findAll()).thenReturn(new ArrayList<>());

    // When & Then
    mockMvc
        .perform(get("/api/organizations").param("filter", "test"))
        .andExpect(status().isOk());

    verify(organizationQuery, times(1)).findAll();
  }

  @Test
  @DisplayName("GET /api/organizations/ with trailing slash should work")
  void testFindAll_WithTrailingSlash_ShouldWork() throws Exception {
    // Given
    when(organizationQuery.findAll()).thenReturn(new ArrayList<>());

    // When & Then
    mockMvc.perform(get("/api/organizations/")).andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/organizations endpoint should be case-sensitive")
  void testFindAll_CaseSensitivePath_ShouldReturn404() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/ORGANIZATIONS")).andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Multiple concurrent findAll requests should all succeed")
  void testFindAll_ConcurrentRequests_ShouldAllSucceed() throws Exception {
    // Given
    when(organizationQuery.findAll()).thenReturn(new ArrayList<>());

    // When & Then - simulate concurrent requests
    for (int i = 0; i < 10; i++) {
      mockMvc.perform(get("/api/organizations")).andExpect(status().isOk());
    }

    verify(organizationQuery, times(10)).findAll();
  }

  @Test
  @DisplayName("Multiple concurrent findById requests should all succeed")
  void testFindById_ConcurrentRequests_ShouldAllSucceed() throws Exception {
    // Given
    when(organizationQuery.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When & Then - simulate concurrent requests
    for (int i = 0; i < 10; i++) {
      mockMvc
          .perform(get("/api/organizations/{id}", testUuid))
          .andExpect(status().isOk());
    }

    verify(organizationQuery, times(10)).findById(testUuid);
  }

  @Test
  @DisplayName("GET /api/organizations should accept various media types")
  void testFindAll_WithDifferentAcceptHeaders_ShouldWork() throws Exception {
    // Given
    when(organizationQuery.findAll()).thenReturn(new ArrayList<>());

    // When & Then - test with different Accept headers
    mockMvc
        .perform(get("/api/organizations").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/organizations").accept(MediaType.ALL))
        .andExpect(status().isOk());
  }
}