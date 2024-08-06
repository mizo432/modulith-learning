package undecided.erp.relationship.domain.model.party.party;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("パーティ")
class PartyTest {

  @Nested
  @DisplayName("createForInsert method tests")
  class CreateForInsertTests {

    @Test
    @DisplayName("should create an instance with assigned PartyAttribute")
    void shouldCreateWithAssignedPartyAttribute() {
      // given
      PartyType type = PartyType.PERSON;

      // when
      Party party = Party.createForInsert(type, SimpleName.reconstruct("party"));

      // then
      assertThat(party.getType())
          .isEqualTo(type);

    }

    @Test
    @DisplayName("should create an instance with assigned PartyType")
    void shouldCreateWithAssignedPartyType() {
      // given
      PartyType type = PartyType.PERSON;

      // when
      Party party = Party.createForInsert(type, SimpleName.reconstruct("party"));

      // then
      assertThat(party.getType())
          .isEqualTo(type);
    }

    @Test
    @DisplayName("should create an instance with unique SnowflakeId")
    void shouldCreateWithUniqueSnowflakeId() {
      // given
      PartyType type = PartyType.PERSON;

      // when
      Party party1 = Party.createForInsert(type, SimpleName.reconstruct("party1"));
      Party party2 = Party.createForInsert(type, SimpleName.reconstruct("party2"));

      // then
      System.out.println(party1);
      System.out.println(party2);
      assertThat(party1.getId())
          .isNotEqualTo(party2.getId());
    }
  }

  @Nested
  @DisplayName("toString method tests")
  class ToStringTests {

    @Test
    @DisplayName("should return a string containing the type, id and attribute of the Party")
    void shouldReturnStringWithAllAttributes() {
      // given
      PartyType type = PartyType.PERSON;

      Party party = Party.createForInsert(type, SimpleName.reconstruct("party"));

      String id = party.getId().toString();
      String expectedString =
          "Party{id=" + id + ", type=" + type + '}';

      // when
      String actualString = party.toString();

      // then
      assertThat(actualString)
          .isEqualTo(expectedString);
    }
  }

}
