package undecided.erp.relationship.model.party;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import undecided.erp.common.dateProvider.StaticDateTimeProvider;
import undecided.erp.relationship.domain.model.party.party.GovtAssignedId;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyAttribute;
import undecided.erp.relationship.domain.model.party.party.PartyType;

class PartyTest {

  @BeforeEach
  void setUp() {
    StaticDateTimeProvider.initialize(LocalDateTime.now());

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

}
