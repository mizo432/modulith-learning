package undecided.erp.relMgmt.model.party;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PartyTest {

  /**
   * Method under test: {@link Party#toString()}
   */
  @Test
  void testPartyToString() {
    Party party = Party.create(1L, PartyType.PERSON, "1234567");
    // Act
    String actualToStringResult = party.toString();
    // Assert
    assertThat(actualToStringResult).isEqualTo(
        "Party{id=1, type=PERSON, govtAssignedId=1234567, currentAddressUses=[], allAddressUses=[], currentPartyRelationships=[], allPartyRelationships=[]}");
  }

}
