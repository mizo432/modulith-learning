package undecided.generic.relationshipMgmt.internal;

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
import org.junit.jupiter.api.Nested;
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
 * {@link OrganizationQueryImpl} のユニットテストクラスです。
 * リポジトリへの委譲とデータ変換の正確性を検証します。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrganizationQueryImplのテスト")
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

  @Nested
  @DisplayName("findAllメソッドのテスト")
  class FindAllTest {

    @Test
    @DisplayName("リポジトリからすべての組織を返す")
    void shouldReturnAllOrganizations() {
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
    @DisplayName("リポジトリが空の場合は空のリストを返す")
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {
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
    @DisplayName("大量の組織データでもすべて返す")
    void shouldReturnAllItemsWithLargeDataset() {
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
    @DisplayName("変更可能なリストを返す")
    void shouldReturnModifiableList() {
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
    @DisplayName("リポジトリを1回だけ呼び出す")
    void shouldCallRepositoryOnce() {
    // Given
    when(organizationRepository.findAll()).thenReturn(new ArrayList<>());

    // When
    organizationQueryImpl.findAll();

    // Then
    verify(organizationRepository, times(1)).findAll();
  }

    @Test
    @DisplayName("組織データの整合性を保持する")
    void shouldPreserveDataIntegrity() {
      List<Organization> organizations = new ArrayList<>();
      organizations.add(testOrganization);
      when(organizationRepository.findAll()).thenReturn(organizations);

      List<Organization> result = organizationQueryImpl.findAll();

      assertThat(result.get(0).getOrganizationCode()).isEqualTo("0001010203004");
      assertThat(result.get(0).getName()).isEqualTo("Test Organization");
      assertThat(result.get(0).getOrganizationTypeCode()).isEqualTo("DEPARTMENT");
    }
  }

  @Nested
  @DisplayName("findByIdメソッドのテスト")
  class FindByIdTest {

    @Test
    @DisplayName("存在するIDで組織を返す")
    void shouldReturnOrganizationWhenFound() {
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
    @DisplayName("存在しないIDの場合は空のOptionalを返す")
    void shouldReturnEmptyWhenNotFound() {
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
    @DisplayName("null IDの場合はリポジトリに委譲する")
    void shouldDelegateToRepositoryWhenIdIsNull() {
    // Given
    when(organizationRepository.findById(null)).thenReturn(Optional.empty());

    // When
    Optional<Organization> result = organizationQueryImpl.findById(null);

    // Then
    assertThat(result).isEmpty();
    verify(organizationRepository, times(1)).findById(null);
  }

    @Test
    @DisplayName("リポジトリを1回だけ呼び出す")
    void shouldCallRepositoryOnce() {
    // Given
    when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

    // When
    organizationQueryImpl.findById(testUuid);

    // Then
    verify(organizationRepository, times(1)).findById(testUuid);
  }

    @Test
    @DisplayName("組織データの整合性を保持する")
    void shouldPreserveDataIntegrity() {
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
    @DisplayName("validToがnullの組織を返す")
    void shouldReturnOrganizationWithNullValidTo() {
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
    @DisplayName("validToが設定された組織を返す")
    void shouldReturnOrganizationWithValidTo() {
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
    @DisplayName("複数の呼び出しでそれぞれリポジトリに委譲する")
    void shouldDelegateToRepositoryOnMultipleCalls() {
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
    @DisplayName("異なるIDでそれぞれリポジトリを呼び出す")
    void shouldCallRepositoryForEachDifferentId() {
      UUID id1 = UUID.randomUUID();
      UUID id2 = UUID.randomUUID();
      UUID id3 = UUID.randomUUID();
      when(organizationRepository.findById(id1)).thenReturn(Optional.of(testOrganization));
      when(organizationRepository.findById(id2)).thenReturn(Optional.empty());
      when(organizationRepository.findById(id3)).thenReturn(Optional.of(createTestOrganization()));

      organizationQueryImpl.findById(id1);
      organizationQueryImpl.findById(id2);
      organizationQueryImpl.findById(id3);

      verify(organizationRepository, times(1)).findById(id1);
      verify(organizationRepository, times(1)).findById(id2);
      verify(organizationRepository, times(1)).findById(id3);
    }
  }
}
