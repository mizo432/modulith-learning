package undecided.association.relationshipMgmt.party.internal.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.spi.party.GovtAssignedId;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.SearchName;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("PersonFactoryImplの単体テスト")
class PersonFactoryImplTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  private final PersonFactoryImpl factory = new PersonFactoryImpl(snowflakeIdGenerator);

  @Nested
  @DisplayName("create メソッドのテスト")
  class CreateTest {

    @Test
    @DisplayName("Personから新しいPersonエンティティを作成し、一意のIDとPERSONタイプコードを設定すること")
    void shouldCreatePersonWithUniqueAndPartyTypeCode() {
      // Arrange
      SnowflakeId expectedId = SnowflakeId.of(1234567890L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(expectedId);

      Person input = createPerson();

      // Act
      Person result = factory.create(input);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.getPartyId()).isEqualTo(expectedId);
      assertThat(result.getPartyTypeCode()).isEqualTo(PartyTypeCode.PERSON);
    }

    @Test
    @DisplayName("入力されたPersonの属性が正しくコピーされること")
    void shouldCopyAllAttributesFromInputPerson() {
      // Arrange
      SnowflakeId expectedId = SnowflakeId.of(9876543210L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(expectedId);

      LegalName legalName = LegalName.create("田中", "太郎");
      DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.of(1990, 5, 15));
      GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");
      SearchName searchName = SearchName.of("田中太郎");

      Person input =
          new Person(
              SnowflakeId.of(1L),
              PartyTypeCode.PERSON,
              "Tanaka",
              "T001",
              govtAssignedId,
              searchName,
              legalName,
              dateOfBirth,
              Collections.emptyList());

      // Act
      Person result = factory.create(input);

      // Assert
      assertThat(result.getName()).isEqualTo("Tanaka");
      assertThat(result.getCode()).isEqualTo("T001");
      assertThat(result.getGovtAssignedId()).isEqualTo(govtAssignedId);
      assertThat(result.getSearchName()).isEqualTo(searchName);
      assertThat(result.getLegalName()).isEqualTo(legalName);
      assertThat(result.getDateOfBirth()).isEqualTo(dateOfBirth);
    }

    @Test
    @DisplayName("作成されたPersonは元のPersonと独立したインスタンスであること")
    void shouldCreateIndependentInstance() {
      // Arrange
      SnowflakeId expectedId = SnowflakeId.of(1111111111L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(expectedId);

      Person input = createPerson();

      // Act
      Person result = factory.create(input);

      // Assert
      assertThat(result).isNotSameAs(input);
    }

    @Test
    @DisplayName("複数回呼び出した場合、それぞれ異なるIDが設定されること")
    void shouldAssignDifferentIdsOnMultipleCalls() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(1000000001L);
      SnowflakeId id2 = SnowflakeId.of(1000000002L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(id1, id2);

      Person input = createPerson();

      // Act
      Person result1 = factory.create(input);
      Person result2 = factory.create(input);

      // Assert
      assertThat(result1.getPartyId()).isEqualTo(id1);
      assertThat(result2.getPartyId()).isEqualTo(id2);
      assertThat(result1.getPartyId()).isNotEqualTo(result2.getPartyId());
    }

    @Test
    @DisplayName("null値を含むPersonでも正しく作成されること")
    void shouldCreatePersonWithNullAttributes() {
      // Arrange
      SnowflakeId expectedId = SnowflakeId.of(2222222222L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(expectedId);

      Person input =
          new Person(
              SnowflakeId.of(1L),
              PartyTypeCode.PERSON,
              "Smith",
              null,
              null,
              null,
              LegalName.empty(),
              DateOfBirth.empty(),
              Collections.emptyList());

      // Act
      Person result = factory.create(input);

      // Assert
      assertThat(result.getPartyId()).isEqualTo(expectedId);
      assertThat(result.getPartyTypeCode()).isEqualTo(PartyTypeCode.PERSON);
      assertThat(result.getName()).isEqualTo("Smith");
      assertThat(result.getCode()).isNull();
    }

  }

  // ヘルパーメソッド
  private Person createPerson() {
    LegalName legalName = LegalName.create("田中", "太郎");
    DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.of(1990, 5, 15));
    GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");
    SearchName searchName = SearchName.of("田中太郎");

    return new Person(
        SnowflakeId.of(1L),
        PartyTypeCode.PERSON,
        "Tanaka",
        "T001",
        govtAssignedId,
        searchName,
        legalName,
        dateOfBirth,
        Collections.emptyList());
  }
}
