package undecided.erp.organization.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import undecided.erp.organization.spi.Organization;

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
    testOrganization = createTestOrganization(testUuid);
  }

  private Organization createTestOrganization(UUID id) {
    Organization org = new Organization();
    org.setOrganizationId(id);
    org.setOrganizationCode("0001010203004");
    org.setFullName("Test Organization");
    org.setLevel0Code("0001");
    org.setLevel1Code("01");
    org.setLevel2Code("02");
    org.setLevel3Code("03");
    org.setLevel4Code("004");
    org.setLevel0Name("Level 0");
    org.setLevel1Name("Level 1");
    org.setLevel2Name("Level 2");
    org.setLevel3Name("Level 3");
    org.setLevel4Name("Level 4");
    org.setValidFrom(LocalDateTime.now().minusDays(10));
    org.setValidTo(null);
    return org;
  }

  // ========== findAll() Tests ==========

  @Test
  @DisplayName("findAll should return all organizations from repository")
  void testFindAll_ShouldReturnAllOrganizations() {
    // Given
    List<Organization> organizations = new ArrayList<>();
    organizations.add(testOrganization);
    organizations.add(createTestOrganization(UUID.randomUUID()));
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
      organizations.add(createTestOrganization(UUID.randomUUID()));
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
    result.add(createTestOrganization(UUID.randomUUID()));
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
    assertThat(result.get(0).getFullName()).isEqualTo("Test Organization");
    assertThat(result.get(0).getLevel0Code()).isEqualTo("0001");
    assertThat(result.get(0).getLevel4Code()).isEqualTo("004");
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
    assertThat(result.get().getOrganizationId()).isEqualTo(testUuid);
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
    assertThat(org.getFullName()).isEqualTo("Test Organization");
    assertThat(org.getLevel0Name()).isEqualTo("Level 0");
    assertThat(org.getLevel1Name()).isEqualTo("Level 1");
    assertThat(org.getLevel2Name()).isEqualTo("Level 2");
    assertThat(org.getLevel3Name()).isEqualTo("Level 3");
    assertThat(org.getLevel4Name()).isEqualTo("Level 4");
    assertThat(org.getValidFrom()).isNotNull();
  }

  @Test
  @DisplayName("findById should return organization with null validTo")
  void testFindById_OrganizationWithNullValidTo_ShouldSucceed() {
    // Given
    testOrganization.setValidTo(null);
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
    testOrganization.setValidTo(validTo);
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

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
    when(organizationRepository.findById(id3)).thenReturn(Optional.of(createTestOrganization(id3)));

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