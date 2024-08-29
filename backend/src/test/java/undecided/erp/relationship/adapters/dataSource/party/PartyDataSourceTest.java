package undecided.erp.relationship.adapters.dataSource.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.adapters.dao.party.PartyDao;
import undecided.erp.relationship.adapters.dao.party.PartyTable;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyType;

@Tag("medium")
class PartyDataSourceTest {

  private final PartyDao partyDaoMock = mock(PartyDao.class);
  private final PartyDataSource partyDataSource = new PartyDataSource(partyDaoMock);

  @Test
  public void testFindByType() {
    PartyType type = PartyType.PERSON;
    Party party1 = Party.reconstruct(1L, type, "party1");
    Party party2 = Party.reconstruct(2L, type, "party2");
    List<Party> expected = Arrays.asList(party1, party2);

    when(partyDaoMock.findByType(type)).thenReturn(PartyTable.toTableRecs(expected));

    List<Party> result = partyDataSource.findByType(type);

    assertThat(result)
        .containsExactly(party1, party2);
  }

}
