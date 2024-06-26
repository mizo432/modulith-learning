package undecided.erp.relMgmt.adapters.dao.partyTable;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.relMgmt.domain.model.party.party.PartyType;

@Table(schema = "relationship", name = "parties")
class PartyRecordTest {

  @Test
  void testToEntity() {
    PartyRecord partyRecord = new PartyRecord();
    partyRecord.setId(1L);
    partyRecord.setType(PartyType.PERSON);
    partyRecord.setGovtAssignedId("123");

    Party expectedEntity = Party.reconstruct(1L, PartyType.PERSON, "123");
    Party actualEntity = partyRecord.toEntity();

    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getType(), actualEntity.getType());
    assertEquals(expectedEntity.getAttribute().getGovtAssignedId(),
        actualEntity.getAttribute().getGovtAssignedId());
  }

  @Test
  void testFromEntity() {
    Party party = Party.reconstruct(1L, PartyType.PERSON, "123");

    PartyRecord expectedRecord = new PartyRecord();
    expectedRecord.setId(1L);
    expectedRecord.setType(PartyType.PERSON);
    expectedRecord.setGovtAssignedId("123");

    PartyRecord actualRecord = PartyRecord.fromEntity(party);

    assertEquals(expectedRecord.getId(), actualRecord.getId());
    assertEquals(expectedRecord.getType(), actualRecord.getType());
    assertEquals(expectedRecord.getGovtAssignedId(), actualRecord.getGovtAssignedId());
  }
}
