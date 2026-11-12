package undecided.association.relationshipMgmt.party.internal.partyRelationship;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import undecided.association.relationshipMgmt.party.internal.person.PersonRepository;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.partyRelationship.PartyRelationship;
import undecided.association.relationshipMgmt.spi.party.partyRelationship.RelationshipType;
import undecided.association.relationshipMgmt.spi.party.partyRelationship.WhenAssociated;
import undecided.association.relationshipMgmt.spi.party.partyRelationship.WhenDisassociated;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code PartyRelationshipRepositoryTest} は、PartyRelationshipRepository の統合テストクラスです。
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
@DisplayName("PartyRelationshipRepositoryの統合テスト")
class PartyRelationshipRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(PartyRelationshipRepositoryTest.class);
  @Autowired PersonRepository personRepository;

  @Autowired PartyRelationshipRepository partyRelationshipRepository;

  @AfterEach
  void cleanup() {
    partyRelationshipRepository.deleteAll();
    personRepository.deleteAll();
  }

  private Person createAndSavePerson(SnowflakeId id, String name) {
    Person person =
        new Person(
            id,
            undecided.association.relationshipMgmt.spi.party.PartyTypeCode.PERSON,
            "テスト",
            "CODE" + id.value(),
            undecided.association.relationshipMgmt.spi.party.GovtAssignedId.of(
                String.valueOf(id.value())),
            undecided.association.relationshipMgmt.spi.party.SearchName.of("テスト"),
            LegalName.create("山田", "太郎"),
            DateOfBirth.of(LocalDate.of(1990, 1, 1)),
            java.util.Collections.emptyList());
    return personRepository.save(person);
  }

  @Nested
  @DisplayName("findByPartyPartyIdメソッドのテスト")
  class FindByPartyPartyIdTest {

    @Test
    @DisplayName("存在しないpartyIdで検索すると空リストが返ること")
    void shouldReturnEmptyListWhenPartyIdNotFound() {
      // Act
      List<PartyRelationship> actual =
          partyRelationshipRepository.findByPartyPartyId(SnowflakeId.of(999999L));

      // Assert
      assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("存在するpartyIdで検索すると該当する関係が返ること")
    void shouldReturnRelationshipsWhenPartyIdMatches() {
      // Arrange
      Party party1 = createAndSavePerson(SnowflakeId.of(100L), "Party1");
      Party party2 = createAndSavePerson(SnowflakeId.of(200L), "Party2");

      PartyRelationship relationship =
          new PartyRelationship(
              SnowflakeId.of(1L),
              party1,
              party2,
              RelationshipType.EMPLOYED_BY,
              WhenAssociated.of(LocalDateTime.of(2024, 1, 1, 0, 0)),
              WhenDisassociated.empty());
      partyRelationshipRepository.save(relationship);

      // Act
      List<PartyRelationship> actual =
          partyRelationshipRepository.findByPartyPartyId(SnowflakeId.of(100L));
      log.info("actual: {}", actual);
      // Assert
      assertThat(actual).hasSize(1);
      assertThat(actual.get(0).getPartyId().value()).isEqualTo(SnowflakeId.of(100L).value());
    }

    @Test
    @DisplayName("複数の関係が登録されているとすべて返ること")
    void shouldReturnAllRelationshipsWhenMultipleExist() {
      // Arrange
      Party party1 = createAndSavePerson(SnowflakeId.of(300L), "Party1");
      Party party2 = createAndSavePerson(SnowflakeId.of(400L), "Party2");
      Party party3 = createAndSavePerson(SnowflakeId.of(500L), "Party3");

      PartyRelationship relationship1 =
          new PartyRelationship(
              SnowflakeId.of(10L),
              party1,
              party2,
              RelationshipType.EMPLOYED_BY,
              WhenAssociated.of(LocalDateTime.of(2024, 1, 1, 0, 0)),
              WhenDisassociated.empty());
      PartyRelationship relationship2 =
          new PartyRelationship(
              SnowflakeId.of(11L),
              party1,
              party3,
              RelationshipType.UNKNOWN,
              WhenAssociated.of(LocalDateTime.of(2024, 6, 1, 0, 0)),
              WhenDisassociated.empty());
      partyRelationshipRepository.save(relationship1);
      partyRelationshipRepository.save(relationship2);

      // Act
      List<PartyRelationship> actual =
          partyRelationshipRepository.findByPartyPartyId(SnowflakeId.of(300L));
      log.info("actual: {}", actual);

      // Assert
      assertThat(actual).hasSize(2);
      assertThat(actual)
          .extracting(r -> r.getRelatedPartyId().value())
          .containsExactlyInAnyOrder(SnowflakeId.of(400L).value(), SnowflakeId.of(500L).value());
    }
  }

  @Nested
  @DisplayName("findByRelatedPartyPartyIdメソッドのテスト")
  class FindByRelatedPartyPartyIdTest {

    @Test
    @DisplayName("存在しないrelatedPartyIdで検索すると空リストが返ること")
    void shouldReturnEmptyListWhenRelatedPartyIdNotFound() {
      // Act
      List<PartyRelationship> actual =
          partyRelationshipRepository.findByRelatedPartyPartyId(SnowflakeId.of(999999L));

      // Assert
      assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("存在するrelatedPartyIdで検索すると該当する関係が返ること")
    void shouldReturnRelationshipsWhenRelatedPartyIdMatches() {
      // Arrange
      Party party1 = createAndSavePerson(SnowflakeId.of(100L), "Party1");
      Party party2 = createAndSavePerson(SnowflakeId.of(200L), "Party2");

      PartyRelationship relationship =
          new PartyRelationship(
              SnowflakeId.of(2L),
              party1,
              party2,
              RelationshipType.EMPLOYED_BY,
              WhenAssociated.of(LocalDateTime.of(2024, 1, 1, 0, 0)),
              WhenDisassociated.empty());
      partyRelationshipRepository.save(relationship);

      // Act
      List<PartyRelationship> actual =
          partyRelationshipRepository.findByRelatedPartyPartyId(SnowflakeId.of(200L));

      // Assert
      assertThat(actual).hasSize(1);
      assertThat(actual.get(0).getRelatedPartyId().value()).isEqualTo(SnowflakeId.of(200L).value());
    }
  }

  @Nested
  @DisplayName("JpaRepository継承メソッドのテスト")
  class JpaRepositoryInheritedMethodsTest {

    @Test
    @DisplayName("saveとfindByIdで保存・取得できること")
    void shouldSaveAndFindById() {
      // Arrange
      Party party1 = createAndSavePerson(SnowflakeId.of(100L), "Party1");
      Party party2 = createAndSavePerson(SnowflakeId.of(200L), "Party2");

      PartyRelationship relationship =
          new PartyRelationship(
              SnowflakeId.of(3L),
              party1,
              party2,
              RelationshipType.EMPLOYED_BY,
              WhenAssociated.of(LocalDateTime.of(2024, 1, 1, 0, 0)),
              WhenDisassociated.empty());

      // Act
      PartyRelationship saved = partyRelationshipRepository.save(relationship);
      Optional<PartyRelationship> found =
          partyRelationshipRepository.findById(saved.getRelationshipId());

      // Assert
      assertThat(found).isPresent();
      assertThat(found.get().getPartyId().value()).isEqualTo(SnowflakeId.of(100L).value());
      assertThat(found.get().getRelatedPartyId().value()).isEqualTo(SnowflakeId.of(200L).value());
      assertThat(found.get().getRelationshipType()).isEqualTo(RelationshipType.EMPLOYED_BY);
    }

    @Test
    @DisplayName("存在しないIDでfindByIdすると空Optionalが返ること")
    void shouldReturnEmptyWhenFindByIdNotFound() {
      // Act
      Optional<PartyRelationship> found =
          partyRelationshipRepository.findById(SnowflakeId.of(999999L));

      // Assert
      assertThat(found).isEmpty();
    }
  }
}
