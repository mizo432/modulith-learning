package undecided.erp.relationship.infra.dataSource.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyType;
import undecided.erp.relationship.infra.dao.party.PartyDao;
import undecided.erp.relationship.infra.dao.party.PartyTable;

class PartyDataSourceTest {

  private final PartyDao partyDaoMock = mock(PartyDao.class);
  private final PartyDataSource partyDataSource = new PartyDataSource(partyDaoMock);

  @Test
  public void testFindByType() {
    PartyType type = PartyType.PERSON;
    Party party1 = Party.reconstruct(1L, type);
    Party party2 = Party.reconstruct(2L, type);
    List<Party> expected = Arrays.asList(party1, party2);

    when(partyDaoMock.findByType(type)).thenReturn(PartyTable.toTableRecs(expected));

    List<Party> result = partyDataSource.findByType(type);

    assertThat(result)
        .containsExactly(party1, party2);
  }

}
