package undecided.generic.rerlationshipMgmt.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.association.relationshipMgmt.orgRole.internal.organization.OrganizationQueryImpl;
import undecided.association.relationshipMgmt.orgRole.internal.organization.OrganizationRepository;
import undecided.association.relationshipMgmt.orgRole.spi.Organization;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Unit tests for {@link OrganizationQueryImpl}.
 *
 * <p>This test class validates the query implementation for organization operations. Tests verify
 * proper delegation to the repository and correct data transformation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrganizationQueryImpl Unit Tests")
class OrganizationQueryImplTest {

  @Mock private OrganizationRepository organizationRepository;

  @InjectMocks private OrganizationQueryImpl organizationQueryImpl;

  private Organization testOrganization;
  private UUID testUuid;

  @BeforeEach
  void setUp() {
    testUuid = UUID.randomUUID();
    testOrganization = createTestOrganization();
  }

  private Organization createTestOrganization() {
    return new Organization(
        SnowflakeId.of(1L),
        SnowflakeId.of(2L),
        "0001010203004",
        "Test Organization",
        "DEPARTMENT",
        null,
        "ACTIVE",
        LocalDateTime.now().minusDays(10));
  }

  private Organization createTestOrganizationWithValidTo(LocalDateTime validTo) {
    Organization org = createTestOrganization();
    setField(org, "validTo", validTo);
    return org;
  }

  private void setField(Object target, String fieldName, Object value) {
    try {
      Field field = target.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(target, value);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Failed to set field " + fieldName, e);
    }
  }

  // ========== findAll() Tests ==========

  @Test
  @DisplayName("findAll should return all organizations from repository")
  void testFindAll_ShouldReturnAllOrganizations() {
    // Given
    List<Organization> organizations = new ArrayList<>();
    organizations.add(testOrganization);
    organizations.add(createTestOrganization());
    when(organizationRepository.findAll()).thenReturn(organizations);

    // When
    List<Organization> result = organizationQueryImpl.findAll();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getOrganizationId()).isEqualTo(testOrganization.getOrganizationId());
    verify(organizationRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("findAll should return empty list when repository is empty")
  void testFindAll_EmptyRepository_ShouldReturnEmptyList() {
    // Given
    when(organizationRepository.findAll()).thenReturn(new ArrayList<>());

    // When
    List<Organization> result = organizationQueryImpl.findAll();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).isEmpty();
    verify(organizationRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("findAll should handle large number of organizations")
  void testFindAll_LargeDataset_ShouldReturnAllItems() {
    // Given
    List<Organization> organizations = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      organizations.add(createTestOrganization());
    }
    when(organizationRepository.findAll()).thenReturn(organizations);

    // When
    List<Organization> result = organizationQueryImpl.findAll();

    // Then
    assertThat(result).isNotNull();
    assertThat(result).hasSize(1000);
    verify(organizationRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("findAll should return modifiable list")
  void testFindAll_ShouldReturnModifiableList() {
    // Given
    List<Organization> organizations = new ArrayList<>();
    organizations.add(testOrganization);
    when(organizationRepository.findAll()).thenReturn(organizations);

    // When
    List<Organization> result = organizationQueryImpl.findAll();

    // Then
    assertThat(result).isNotNull();
    // Verify list is modifiable by adding an element
    result.add(createTestOrganization());
    assertThat(result).hasSize(2);
  }

  @Test
  @DisplayName("findAll should call repository exactly once")
  void testFindAll_ShouldCallRepositoryOnce() {
    // Given
    when(organizationRepository.findAll()).thenReturn(new ArrayList<>());

    // When
    organizationQueryImpl.findAll();

    // Then
    verify(organizationRepository, times(1)).findAll();
  }

  @Test
  @DisplayName("findAll should preserve organization data integrity")
  void testFindAll_ShouldPreserveDataIntegrity() {
    // Given
    List<Organization> organizations = new ArrayList<>();
    organizations.add(testOrganization);
    when(organizationRepository.findAll()).thenReturn(organizations);

    // When
    List<Organization> result = organizationQueryImpl.findAll();

    // Then
    assertThat(result.get(0).getOrganizationCode()).isEqualTo("0001010203004");
    assertThat(result.get(0).getName()).isEqualTo("Test Organization");
    assertThat(result.get(0).getOrganizationTypeCode()).isEqualTo("DEPARTMENT");
  }

  // ========== findById() Tests ==========

  @Test
  @DisplayName("findById should return organization when found")
  void testFindById_ExistingId_ShouldReturnOrganization() {
    // Given
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When
    Optional<Organization> result = organizationQueryImpl.findById(testUuid);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getOrganizationId()).isEqualTo(testOrganization.getOrganizationId());
    assertThat(result.get().getOrganizationCode()).isEqualTo("0001010203004");
    verify(organizationRepository, times(1)).findById(testUuid);
  }

  @Test
  @DisplayName("findById should return empty optional when not found")
  void testFindById_NonExistingId_ShouldReturnEmpty() {
    // Given
    UUID nonExistingId = UUID.randomUUID();
    when(organizationRepository.findById(nonExistingId)).thenReturn(Optional.empty());

    // When
    Optional<Organization> result = organizationQueryImpl.findById(nonExistingId);

    // Then
    assertThat(result).isEmpty();
    verify(organizationRepository, times(1)).findById(nonExistingId);
  }

  @Test
  @DisplayName("findById should handle null UUID gracefully")
  void testFindById_NullId_ShouldDelegateToRepository() {
    // Given
    when(organizationRepository.findById(null)).thenReturn(Optional.empty());

    // When
    Optional<Organization> result = organizationQueryImpl.findById(null);

    // Then
    assertThat(result).isEmpty();
    verify(organizationRepository, times(1)).findById(null);
  }

  @Test
  @DisplayName("findById should call repository exactly once")
  void testFindById_ShouldCallRepositoryOnce() {
    // Given
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When
    organizationQueryImpl.findById(testUuid);

    // Then
    verify(organizationRepository, times(1)).findById(testUuid);
  }

  @Test
  @DisplayName("findById should preserve organization data integrity")
  void testFindById_ShouldPreserveDataIntegrity() {
    // Given
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When
    Optional<Organization> result = organizationQueryImpl.findById(testUuid);

    // Then
    assertThat(result).isPresent();
    Organization org = result.get();
    assertThat(org.getName()).isEqualTo("Test Organization");
    assertThat(org.getOrganizationTypeCode()).isEqualTo("DEPARTMENT");
    assertThat(org.getStatus()).isEqualTo("ACTIVE");
    assertThat(org.getValidFrom()).isNotNull();
  }

  @Test
  @DisplayName("findById should return organization with null validTo")
  void testFindById_OrganizationWithNullValidTo_ShouldSucceed() {
    // Given
    // validTo is null by default when using the constructor
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When
    Optional<Organization> result = organizationQueryImpl.findById(testUuid);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getValidTo()).isNull();
  }

  @Test
  @DisplayName("findById should return organization with validTo set")
  void testFindById_OrganizationWithValidTo_ShouldSucceed() {
    // Given
    LocalDateTime validTo = LocalDateTime.now().plusDays(30);
    Organization orgWithValidTo = createTestOrganizationWithValidTo(validTo);
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(orgWithValidTo));

    // When
    Optional<Organization> result = organizationQueryImpl.findById(testUuid);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().getValidTo()).isEqualTo(validTo);
  }

  @Test
  @DisplayName("Multiple findById calls should all delegate to repository")
  void testFindById_MultipleCalls_ShouldDelegateEachTime() {
    // Given
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When
    organizationQueryImpl.findById(testUuid);
    organizationQueryImpl.findById(testUuid);
    organizationQueryImpl.findById(testUuid);

    // Then
    verify(organizationRepository, times(3)).findById(testUuid);
  }

  @Test
  @DisplayName("findById with different UUIDs should call repository for each")
  void testFindById_DifferentIds_ShouldCallRepositoryForEach() {
    // Given
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    UUID id3 = UUID.randomUUID();
    when(organizationRepository.findById(id1)).thenReturn(Optional.of(testOrganization));
    when(organizationRepository.findById(id2)).thenReturn(Optional.empty());
    when(organizationRepository.findById(id3)).thenReturn(Optional.of(createTestOrganization()));

    // When
    organizationQueryImpl.findById(id1);
    organizationQueryImpl.findById(id2);
    organizationQueryImpl.findById(id3);

    // Then
    verify(organizationRepository, times(1)).findById(id1);
    verify(organizationRepository, times(1)).findById(id2);
    verify(organizationRepository, times(1)).findById(id3);
  }
}
