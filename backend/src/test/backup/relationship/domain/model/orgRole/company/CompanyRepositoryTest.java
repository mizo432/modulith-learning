package undecided.erp.relationship.domain.model.orgRole.company;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

@DataJpaTest
@Disabled
@DisplayName("会社リポジトリテスト")
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CompanyRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CompanyRepository companyRepository;

  private final String testCompanyNameValue = "TestName";
  private CompanyCode testCompanyCode;
  private Company testCompany;

  @BeforeEach
  void setUp() {
    String testCompanyCodeValue = "TestCode";
    testCompanyCode = CompanyCode.of(testCompanyCodeValue);
    CompanyAttribute testCompanyAttribute = CompanyAttribute.of(
        testCompanyCode, CompanyName.of(testCompanyNameValue)
    );
    SnowflakeId<Party> id = SnowflakeId.of((long) 1);
    testCompany = Company.create(id, testCompanyAttribute);
    entityManager.persist(testCompany);
    entityManager.flush();
  }

  @Nested
  @DisplayName("属性の名称で検索")
  @Tag("medium")
  class FindByAttribute_Name_ValueContainingTest {

    @Test
    @DisplayName("テスト会社名で見つける")
    void shouldFindCompanyByAttributeName() {
      // Given

      // When
      List<Company> found = companyRepository.findByAttribute_Name_ValueContaining(
          testCompanyNameValue);

      // Then
      assertThat(found.size())
          .isEqualTo(1);
      assertThat(found.stream()
          .allMatch(
              company -> testCompanyNameValue.equals(
                  company.getAttribute().getName().getValue()))).isTrue();
    }

    @Test
    @DisplayName("存在しない属性の会社名で会社を検索すれば、結果は空になる")
    void shouldReturnEmptyWhenFindCompanyByNonexistentAttributeName() {
      // given
      String nonexistentCompanyName = "NonexistentName";

      // when
      List<Company> notFound = companyRepository.findByAttribute_Name_ValueContaining(
          nonexistentCompanyName);

      // then
      assertThat(notFound).isEmpty();

    }
  }


  @Nested
  @DisplayName("属性のコード検索")
  @Tag("medium")
  class FindByAttribute_CodeTest {

    @Test
    @DisplayName("属性のコードで会社を検索する")
    void shouldFindCompanyByAttributeCode() {
      // Given

      // When
      List<Company> found = companyRepository.findByAttribute_Code(
          testCompany.getAttribute().getCode());

      // Then
      assertThat(found.size())
          .isEqualTo(1);
      assertThat(found.stream()
          .allMatch(
              company -> testCompanyCode.equals(
                  company.getAttribute().getCode()))).isTrue();
    }

    @Test
    @DisplayName("存在しない属性のコードで会社を検索すれば、結果は空になる")
    void shouldReturnEmptyWhenFindCompanyByNonexistentAttributeCode() {
      // given
      String nonexistentCompanyCode = "NonexistentCode";

      // when
      List<Company> notFound = companyRepository.findByAttribute_Code(
          CompanyCode.of(nonexistentCompanyCode));

      // then
      assertThat(notFound).isEmpty();
    }
  }
}