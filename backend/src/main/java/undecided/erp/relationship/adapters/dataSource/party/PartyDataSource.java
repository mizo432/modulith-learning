package undecided.erp.relationship.adapters.dataSource.party;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import undecided.erp.relationship.adapters.dao.party.PartyDao;
import undecided.erp.relationship.adapters.dao.party.PartyTable;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyRepository;
import undecided.erp.relationship.domain.model.party.party.PartyType;

@RequiredArgsConstructor
@Component
public class PartyDataSource implements PartyRepository {

  private final PartyDao partyDao;

  @Override
  public List<Party> findByType(PartyType type) {
    return PartyTable.toEntities(partyDao.findByType(type));
  }
}
