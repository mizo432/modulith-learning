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
import undecided.association.relationshipMgmt.orgRole.internal.organization.OrganizationQueryService;
import undecided.association.relationshipMgmt.orgRole.internal.organization.OrganizationRepository;
import undecided.association.relationshipMgmt.orgRole.spi.Organization;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@link OrganizationQueryService} のユニットテストクラス。
 *
 * <p>組織関連のクエリ実装を検証します。リポジトリへの適切な委譲とデータ変換の正確性をテストします。
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrganizationQueryService のテスト")
class OrganizationQueryServiceTest {

  @Mock private OrganizationRepository organizationRepository;

  @InjectMocks private OrganizationQueryService organizationQueryService;

  private Organization testOrganization;
  private UUID testUuid;

  @BeforeEach
  void setUp() {
    testUuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
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
  @DisplayName("findAll メソッドのテスト")
  class FindAllTest {

    @Test
    @DisplayName("リポジトリからすべての組織を返すこと")
    void shouldReturnAllOrganizations() {
      // Given
      List<Organization> organizations = new ArrayList<>();
      organizations.add(testOrganization);
      organizations.add(createTestOrganization());
      when(organizationRepository.findAll()).thenReturn(organizations);

      // When
      List<Organization> result = organizationQueryService.findAll();

      // Then
      assertThat(result).isNotNull();
      assertThat(result).hasSize(2);
      assertThat(result.get(0).getOrganizationId()).isEqualTo(testOrganization.getOrganizationId());
      verify(organizationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("リポジトリが空の場合、空のリストを返すこと")
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {
      // Given
      when(organizationRepository.findAll()).thenReturn(new ArrayList<>());

      // When
      List<Organization> result = organizationQueryService.findAll();

      // Then
      assertThat(result).isNotNull();
      assertThat(result).isEmpty();
      verify(organizationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("大量の組織データを処理できること")
    void shouldReturnAllItemsWithLargeDataset() {
      // Given
      List<Organization> organizations = new ArrayList<>();
      for (int i = 0; i < 1000; i++) {
        organizations.add(createTestOrganization());
      }
      when(organizationRepository.findAll()).thenReturn(organizations);

      // When
      List<Organization> result = organizationQueryService.findAll();

      // Then
      assertThat(result).isNotNull();
      assertThat(result).hasSize(1000);
      verify(organizationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("変更可能なリストを返すこと")
    void shouldReturnModifiableList() {
      // Given
      List<Organization> organizations = new ArrayList<>();
      organizations.add(testOrganization);
      when(organizationRepository.findAll()).thenReturn(organizations);

      // When
      List<Organization> result = organizationQueryService.findAll();

      // Then
      assertThat(result).isNotNull();
      result.add(createTestOrganization());
      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("リポジトリを1回だけ呼び出すこと")
    void shouldCallRepositoryOnce() {
      // Given
      when(organizationRepository.findAll()).thenReturn(new ArrayList<>());

      // When
      organizationQueryService.findAll();

      // Then
      verify(organizationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("組織データの整合性を保持すること")
    void shouldPreserveDataIntegrity() {
      // Given
      List<Organization> organizations = new ArrayList<>();
      organizations.add(testOrganization);
      when(organizationRepository.findAll()).thenReturn(organizations);

      // When
      List<Organization> result = organizationQueryService.findAll();

      // Then
      assertThat(result.get(0).getOrganizationCode()).isEqualTo("0001010203004");
      assertThat(result.get(0).getName()).isEqualTo("Test Organization");
      assertThat(result.get(0).getOrganizationTypeCode()).isEqualTo("DEPARTMENT");
    }
  }

  @Nested
  @DisplayName("findById メソッドのテスト")
  class FindByIdTest {

    @Test
    @DisplayName("組織が見つかった場合、それを返すこと")
    void shouldReturnOrganizationWhenFound() {
      // Given
      when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

      // When
      Optional<Organization> result = organizationQueryService.findById(testUuid);

      // Then
      assertThat(result).isPresent();
      assertThat(result.get().getOrganizationId()).isEqualTo(testOrganization.getOrganizationId());
      assertThat(result.get().getOrganizationCode()).isEqualTo("0001010203004");
      verify(organizationRepository, times(1)).findById(testUuid);
    }

    @Test
    @DisplayName("組織が見つからない場合、空のOptionalを返すこと")
    void shouldReturnEmptyWhenNotFound() {
      // Given
      UUID nonExistingId = UUID.fromString("22222222-2222-2222-2222-222222222222");
      when(organizationRepository.findById(nonExistingId)).thenReturn(Optional.empty());

      // When
      Optional<Organization> result = organizationQueryService.findById(nonExistingId);

      // Then
      assertThat(result).isEmpty();
      verify(organizationRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("null UUIDをリポジトリに委譲すること")
    void shouldDelegateNullIdToRepository() {
      // Given
      when(organizationRepository.findById(null)).thenReturn(Optional.empty());

      // When
      Optional<Organization> result = organizationQueryService.findById(null);

      // Then
      assertThat(result).isEmpty();
      verify(organizationRepository, times(1)).findById(null);
    }

    @Test
    @DisplayName("リポジトリを1回だけ呼び出すこと")
    void shouldCallRepositoryOnce() {
      // Given
      when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

      // When
      organizationQueryService.findById(testUuid);

      // Then
      verify(organizationRepository, times(1)).findById(testUuid);
    }

    @Test
    @DisplayName("組織データの整合性を保持すること")
    void shouldPreserveDataIntegrity() {
      // Given
      when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

      // When
      Optional<Organization> result = organizationQueryService.findById(testUuid);

      // Then
      assertThat(result).isPresent();
      Organization org = result.get();
      assertThat(org.getName()).isEqualTo("Test Organization");
      assertThat(org.getOrganizationTypeCode()).isEqualTo("DEPARTMENT");
      assertThat(org.getStatus()).isEqualTo("ACTIVE");
      assertThat(org.getValidFrom()).isNotNull();
    }

    @Test
    @DisplayName("validToがnullの組織を返すこと")
    void shouldReturnOrganizationWithNullValidTo() {
      // Given
      when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

      // When
      Optional<Organization> result = organizationQueryService.findById(testUuid);

      // Then
      assertThat(result).isPresent();
      assertThat(result.get().getValidTo()).isNull();
    }

    @Test
    @DisplayName("validToが設定された組織を返すこと")
    void shouldReturnOrganizationWithValidTo() {
      // Given
      LocalDateTime validTo = LocalDateTime.now().plusDays(30);
      Organization orgWithValidTo = createTestOrganizationWithValidTo(validTo);
      when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(orgWithValidTo));

      // When
      Optional<Organization> result = organizationQueryService.findById(testUuid);

      // Then
      assertThat(result).isPresent();
      assertThat(result.get().getValidTo()).isEqualTo(validTo);
    }

    @Test
    @DisplayName("複数の呼び出しをそれぞれリポジトリに委譲すること")
    void shouldDelegateMultipleCalls() {
      // Given
      when(organizationRepository.findById(testUuid)).thenReturn(Optional.of(testOrganization));

      // When
      organizationQueryService.findById(testUuid);
      organizationQueryService.findById(testUuid);
      organizationQueryService.findById(testUuid);

      // Then
      verify(organizationRepository, times(3)).findById(testUuid);
    }

    @Test
    @DisplayName("異なるUUIDでそれぞれリポジトリを呼び出すこと")
    void shouldCallRepositoryForEachDifferentId() {
      // Given
      UUID id1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
      UUID id2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
      UUID id3 = UUID.fromString("33333333-3333-3333-3333-333333333333");
      when(organizationRepository.findById(id1)).thenReturn(Optional.of(testOrganization));
      when(organizationRepository.findById(id2)).thenReturn(Optional.empty());
      when(organizationRepository.findById(id3)).thenReturn(Optional.of(createTestOrganization()));

      // When
      organizationQueryService.findById(id1);
      organizationQueryService.findById(id2);
      organizationQueryService.findById(id3);

      // Then
      verify(organizationRepository, times(1)).findById(id1);
      verify(organizationRepository, times(1)).findById(id2);
      verify(organizationRepository, times(1)).findById(id3);
    }
  }
}
