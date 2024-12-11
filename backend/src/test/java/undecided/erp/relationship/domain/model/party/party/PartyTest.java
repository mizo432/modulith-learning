package undecided.erp.relationship.domain.model.party.party;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.party.partyRelationship.PartyRelationship.PartyRelationships;

@DisplayName("パーティ")
class PartyTest {

  @Nested
  @DisplayName("toString method tests")
  class ToStringTests {

    @Test
    @DisplayName("should return a string containing the type, id and attribute of the Party")
    void shouldReturnStringWithAllAttributes() {
      // given
      PartyType type = PartyType.PERSON;

      Party party = Party.createForInsert(type, SimpleName.reconstruct("party"),
          PartyRelationships.empty());

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

  @Nested
  @DisplayName("toString method with null arguments tests")
  class ToStringWithNullArgumentsTests {

    @Test
    @DisplayName("should return a string with null SimpleName")
    void shouldReturnStringWithNullSimpleName() {
      // given
      PartyType type = PartyType.PERSON;
      Party party = new Party(SnowflakeId.newInstance(), type, null,
          PartyRelationships.reconstruct(null));

      String expectedString =
          "Party{id=" + party.getId() + ", type=" + type
              + ", simpleName=null, partyRelationships=[]}";

      // when
      String actualString = party.toString();

      // then
      assertThat(actualString)
          .isEqualTo(expectedString);
    }

    @Test
    @DisplayName("should return a string with null PartyRelationships")
    void shouldReturnStringWithNullPartyRelationships() {
      // given
      PartyType type = PartyType.PERSON;
      SimpleName name = SimpleName.reconstruct("name");
      Party party = new Party(SnowflakeId.newInstance(), type, name, null);

      String expectedString =
          "Party{id=" + party.getId() + ", type=" + type + ", simpleName=" + name
              + ", partyRelationships=null}";

      // when
      String actualString = party.toString();

      // then
      assertThat(actualString)
          .isEqualTo(expectedString);
    }
  }

}
