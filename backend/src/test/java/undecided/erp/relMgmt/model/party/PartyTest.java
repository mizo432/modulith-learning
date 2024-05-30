package undecided.erp.relMgmt.model.party;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import undecided.erp.common.dateProvider.StaticDateTimeProvider;
import undecided.erp.relMgmt.model.party.party.GovtAssignedId;
import undecided.erp.relMgmt.model.party.party.Party;
import undecided.erp.relMgmt.model.party.party.PartyAttribute;
import undecided.erp.relMgmt.model.party.party.PartyType;

class PartyTest {

  @BeforeEach
  void setUp() {
    StaticDateTimeProvider.initialize(LocalDateTime.of(2023, 5, 28, 9, 58, 1, 2000000));

  }

  @AfterEach
  void tearDown() {
    StaticDateTimeProvider.clear();
  }

  @Test
  public void testCreateForInsert() {
    PartyAttribute attribute = PartyAttribute.create(GovtAssignedId.of("123456"));
    Party party = Party.createForInsert(PartyType.PERSON, attribute);
    System.out.println(party);
    assertNotNull(party.getId());
    assertEquals(PartyType.PERSON, party.getType());
    assertEquals(attribute, party.getAttribute());
    assertTrue(party.getAllAddressUses().isEmpty());
    assertTrue(party.getAllPartyRelationships().isEmpty());
  }

  @Test
  public void testToString() {
    PartyAttribute attribute = PartyAttribute.create(GovtAssignedId.of("123456"));
    Party party = Party.createForInsert(PartyType.PERSON, attribute);
    System.out.println(party);
    String expected = "Party{" +
        "id=" + party.getId() +
        ", type=" + PartyType.PERSON +
        ", attribute=" + attribute +
        ", allAddressUses=" + party.getAllAddressUses() +
        ", allPartyRelationships=" + party.getAllPartyRelationships() +
        '}';
    assertEquals(expected, party.toString());
  }
}
