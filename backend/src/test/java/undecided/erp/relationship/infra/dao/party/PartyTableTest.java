package undecided.erp.relationship.infra.dao.party;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyType;

public class PartyTableTest {

  @Test
  public void toEntityTest() {
    PartyTable partyTable = new PartyTable();
    partyTable.setId(1L);
    partyTable.setType(PartyType.PERSON);

    Party partyEntity = partyTable.toEntity();

    assertNotNull(partyEntity);
    assertEquals(partyTable.getId(), partyEntity.getId().getValue());
    assertEquals(partyTable.getType(), partyEntity.getType());
  }

  @Test
  public void toTableRecsTest() {
    List<Party> partyEntities = List.of(Party.createForInsert(PartyType.PERSON),
        Party.createForInsert(PartyType.ORGANIZATION));

    List<PartyTable> partyTables = PartyTable.toTableRecs(partyEntities);

    assertNotNull(partyTables);
    assertEquals(partyEntities.size(), partyTables.size());
    for (int i = 0; i < partyEntities.size(); i++) {
      assertEquals(partyEntities.get(i).getId().getValue(), partyTables.get(i).getId());
      assertEquals(partyEntities.get(i).getType(), partyTables.get(i).getType());
    }
  }

  @Test
  public void toTableRecTest() {
    Party partyEntity = Party.createForInsert(PartyType.PERSON);

    PartyTable partyTable = PartyTable.toTableRec(partyEntity);

    assertNotNull(partyTable);
    assertEquals(partyEntity.getId().getValue(), partyTable.getId());
    assertEquals(partyEntity.getType(), partyTable.getType());
  }


  @Test
  public void toEntitiesTest() {
    List<PartyTable> partyTables = List.of(
        new PartyTable(1L, PartyType.PERSON),
        new PartyTable(2L, PartyType.ORGANIZATION)
    );

    List<Party> partyEntities = PartyTable.toEntities(partyTables);

    assertNotNull(partyEntities);
    assertEquals(partyTables.size(), partyEntities.size());
    for (int i = 0; i < partyEntities.size(); i++) {
      assertEquals(partyTables.get(i).getId(), partyEntities.get(i).getId().getValue());
      assertEquals(partyTables.get(i).getType(), partyEntities.get(i).getType());
    }
  }
}
