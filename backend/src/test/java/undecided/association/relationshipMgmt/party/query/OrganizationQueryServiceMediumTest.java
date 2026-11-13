package undecided.association.relationshipMgmt.party.query;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.association.relationshipMgmt.party.repository.OrgRepository;
import undecided.association.relationshipMgmt.spi.party.OrgQuery;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.association.relationshipMgmt.spi.party.org.OrgCode;
import undecided.association.relationshipMgmt.spi.party.org.OrgName;
import undecided.association.relationshipMgmt.spi.party.org.OrgStatus;
import undecided.association.relationshipMgmt.spi.party.org.OrgTypeCode;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("OrganizationQueryServiceの統合テスト")
class OrganizationQueryServiceMediumTest {

  @Autowired private OrgQuery orgQuery;

  @Autowired private OrgRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Nested
  @DisplayName("findAllメソッドのテスト")
  class FindAllTest {

    @Test
    @DisplayName("データが存在しない場合、空のリストを返すこと")
    void shouldReturnEmptyListWhenNoData() {
      // Act
      List<Org> result = orgQuery.findAll();

      // Assert
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("データが存在する場合、全てのOrgを返すこと")
    void shouldReturnAllOrgsWhenDataExists() {
      // Arrange
      Org org1 = createAndSaveOrg("ORG001", "組織1");
      Org org2 = createAndSaveOrg("ORG002", "組織2");

      // Act
      List<Org> result = orgQuery.findAll();

      // Assert
      assertThat(result).hasSize(2);
      assertThat(result).extracting(Org::getOrgCode)
          .containsExactlyInAnyOrder(OrgCode.of("ORG001"), OrgCode.of("ORG002"));
    }
  }

  @Nested
  @DisplayName("findByIdメソッドのテスト")
  class FindByIdTest {

    @Test
    @DisplayName("存在するIDが指定された場合、Orgを返すこと")
    void shouldReturnOrgWhenIdExists() {
      // Arrange
      Org savedOrg = createAndSaveOrg("ORG001", "テスト組織");

      // Act
      Optional<Org> result = orgQuery.findById(savedOrg.getPartyId());

      // Assert
      assertThat(result).isPresent();
      Org foundOrg = result.get();
      assertThat(foundOrg.getPartyId().value()).isEqualTo(savedOrg.getPartyId().value());
      assertThat(foundOrg.getOrgName()).isEqualTo(OrgName.of("テスト組織"));
      assertThat(foundOrg.getOrgCode()).isEqualTo(OrgCode.of("ORG001"));
      assertThat(foundOrg.getOrgTypeCode()).isEqualTo(OrgTypeCode.COMPANY);
      assertThat(foundOrg.getStatus()).isEqualTo(OrgStatus.ACTIVE);
    }

    @Test
    @DisplayName("存在しないIDが指定された場合、Optional.emptyを返すこと")
    void shouldReturnEmptyWhenIdDoesNotExist() {
      // Act
      Optional<Org> result = orgQuery.findById(SnowflakeId.of(9999999999L));

      // Assert
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("複数のOrg中存在するIDが指定された場合、正しいOrgを返すこと")
    void shouldReturnCorrectOrgWhenMultipleOrgsExist() {
      // Arrange
      Org org1 = createAndSaveOrg("ORG001", "組織1");
      createAndSaveOrg("ORG002", "組織2");

      // Act
      Optional<Org> result = orgQuery.findById(org1.getPartyId());

      // Assert
      assertThat(result).isPresent();
      assertThat(result.get().getOrgCode()).isEqualTo(OrgCode.of("ORG001"));
    }
  }

  /**
   * テスト用のOrgを作成して保存します。
   *
   * @param orgCode 組織コード
   * @param orgName 組織名
   * @return 保存されたOrgエンティティ
   */
  private Org createAndSaveOrg(String orgCode, String orgName) {
    Org org = Org.create(
        SnowflakeId.of(Long.parseLong(orgCode.replace("ORG", "1"))),
        orgCode,
        java.time.LocalDateTime.now(),
        OrgName.of(orgName),
        OrgCode.of(orgCode),
        OrgTypeCode.COMPANY,
        null,
        OrgStatus.ACTIVE
    );
    return repository.save(org);
  }
}
