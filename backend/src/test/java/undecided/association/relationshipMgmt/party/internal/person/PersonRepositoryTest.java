package undecided.association.relationshipMgmt.party.internal.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.association.relationshipMgmt.spi.party.GovtAssignedId;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.SearchName;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code PersonRepositoryTest} は、PersonRepository の統合テストクラスです。
 *
 * <p>規約要件: 1. @Tag("medium") を付与 2. @ActiveProfiles("test")
 * および @Import(TestcontainersConfiguration.class) で PostgreSQL コンテナを利用 3.
 * クラス・メソッドともに日本語 @DisplayName を付与 4. 対象メソッドごとに @Nested クラスでグループ化 5. メソッド名は should
 * で開始し、アンダースコアを使用しない 6. package-private 可視性
 */
@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("PersonRepositoryの統合テスト")
class PersonRepositoryTest {
  private static final Logger log = LoggerFactory.getLogger(PersonRepositoryTest.class);

  @Autowired PersonRepository personRepository;

  @AfterEach
  void cleanup() {
    personRepository.deleteAll();
  }

  @Nested
  @DisplayName("findBySearchNameメソッドのテスト")
  class FindBySearchNameTest {

    @Test
    @DisplayName("存在しないSearchNameで検索すると空のOptionalが返ること")
    void shouldReturnEmptyWhenNoPersonFound() {
      // Act
      Optional<Person> actual = personRepository.findBySearchName(SearchName.of("ジョン・ドウ"));

      log.debug("findBySearchNameメソッドのテストが実行されました。actual={}", actual);

      // Assert
      assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("存在するSearchNameで検索するとPersonが返ること")
    void shouldReturnPersonWhenSearchNameMatches() {
      // Arrange
      Person person =
          new Person(
              SnowflakeId.of(1L),
              PartyTypeCode.PERSON,
              "テスト個人",
              "P001",
              GovtAssignedId.of("1234567890123"),
              SearchName.of("ジョン・ドウ"),
              LegalName.create("山田", "太郎"),
              DateOfBirth.of(LocalDate.of(1990, 1, 1)),
              java.util.Collections.emptyList());
      personRepository.save(person);
      log.info("findBySearchNameメソッドのテストが実行されました。person={}", person);

      // Act
      Optional<Person> actual = personRepository.findBySearchName(SearchName.of("ジョン・ドウ"));

      // Assert
      assertThat(actual).isPresent();
      assertThat(actual.get().getSearchName()).isEqualTo(SearchName.of("ジョン・ドウ"));
    }
  }
}
