package undecided.association.relationshipMgmt.spi.party.person;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.supporting.snowflake.SnowflakeId;
import java.time.LocalDateTime;

/**
 * {@code PersonTest} は、Personエンティティの単体テストクラスです。
 * <p>
 * M1修正で追加された{@code create()}ファクトリメソッドの動作と、
 * {@code @AllArgsConstructor(access = AccessLevel.PROTECTED)}の修正を検証します。
 */
@Tag("small")
@DisplayName("Personエンティティの単体テスト")
class PersonTest {

  @Nested
  @DisplayName("createメソッドのテスト")
  class CreateMethodTest {

    @Test
    @DisplayName("有効な引数でPersonインスタンスが作成されること")
    void shouldCreatePersonWithValidArguments() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(1000000001L);
      String code = "PERSON001";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      LegalName legalName = LegalName.of("田中", "太郎");
      DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.of(1990, 5, 15));
      DateOfDie dateOfDie = DateOfDie.empty();

      // Act
      Person person = Person.create(
          partyId,
          PartyTypeCode.PERSON,
          code,
          validFrom,
          legalName,
          dateOfBirth,
          dateOfDie);

      // Assert
      assertThat(person).as("Personインスタンスが作成されること").isNotNull();
      assertThat(person.getPartyId()).isEqualTo(partyId);
      assertThat(person.getPartyTypeCode()).isEqualTo(PartyTypeCode.PERSON);
      assertThat(person.getCode()).isEqualTo(code);
      assertThat(person.getValidFrom()).isEqualTo(validFrom);
      assertThat(person.getLegalName()).isEqualTo(legalName);
      assertThat(person.getDateOfBirth()).isEqualTo(dateOfBirth);
      assertThat(person.getDateOfDie()).isEqualTo(dateOfDie);
    }

    @Test
    @DisplayName("死亡日を設定して作成できること")
    void shouldCreatePersonWithDateOfDie() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(1000000002L);
      String code = "PERSON002";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      LegalName legalName = LegalName.of("佐藤", "花子");
      DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.of(1950, 3, 10));
      DateOfDie dateOfDie = DateOfDie.of(LocalDate.of(2020, 11, 20));

      // Act
      Person person = Person.create(
          partyId,
          PartyTypeCode.PERSON,
          code,
          validFrom,
          legalName,
          dateOfBirth,
          dateOfDie);

      // Assert
      assertThat(person.getDateOfDie().value()).isEqualTo(LocalDate.of(2020, 11, 20));
    }

    @Test
    @DisplayName("日本語の名前で正常に作成されること")
    void shouldCreatePersonWithJapaneseName() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(1000000003L);
      String code = "PERSON003";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      LegalName legalName = LegalName.of("山田", "次郎");
      DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.of(1985, 8, 20));
      DateOfDie dateOfDie = DateOfDie.empty();

      // Act
      Person person = Person.create(
          partyId,
          PartyTypeCode.PERSON,
          code,
          validFrom,
          legalName,
          dateOfBirth,
          dateOfDie);

      // Assert
      assertThat(person.getLegalName().lastName().value()).isEqualTo("山田");
      assertThat(person.getLegalName().firstName().value()).isEqualTo("次郎");
    }

    @Test
    @DisplayName("異なるIDで複数のPersonが作成できること")
    void shouldCreateMultiplePersonsWithDifferentIds() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(1000000001L);
      SnowflakeId id2 = SnowflakeId.of(1000000002L);
      String code1 = "PERSON001";
      String code2 = "PERSON002";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      LegalName legalName1 = LegalName.of("田中", "太郎");
      LegalName legalName2 = LegalName.of("佐藤", "花子");
      DateOfBirth dateOfBirth1 = DateOfBirth.of(LocalDate.of(1990, 5, 15));
      DateOfBirth dateOfBirth2 = DateOfBirth.of(LocalDate.of(1995, 10, 25));
      DateOfDie dateOfDie = DateOfDie.empty();

      // Act
      Person person1 = Person.create(id1, PartyTypeCode.PERSON, code1, validFrom, legalName1, dateOfBirth1, dateOfDie);
      Person person2 = Person.create(id2, PartyTypeCode.PERSON, code2, validFrom, legalName2, dateOfBirth2, dateOfDie);

      // Assert
      assertThat(person1.getPartyId()).isNotEqualTo(person2.getPartyId());
      assertThat(person1.getCode()).isNotEqualTo(person2.getCode());
      assertThat(person1.getLegalName()).isNotEqualTo(person2.getLegalName());
    }
  }

  @Nested
  @DisplayName("エンティティアノテーションのテスト")
  class EntityAnnotationTest {

    @Test
    @DisplayName("Personクラスに@Entityアノテーションが付与されていること")
    void shouldHaveEntityAnnotation() {
      // Assert
      assertThat(Person.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
    }

    @Test
    @DisplayName("PersonクラスはPartyを継承していること")
    void shouldExtendParty() {
      // Assert
      assertThat(Person.class.getSuperclass()).isEqualTo(Party.class);
    }

  }
}
