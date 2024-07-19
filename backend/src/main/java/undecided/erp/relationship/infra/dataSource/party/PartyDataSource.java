package undecided.erp.relationship.infra.dataSource.party;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.party.party.PartyRepository;
import undecided.erp.relationship.domain.model.party.party.PartyType;
import undecided.erp.relationship.infra.dao.party.PartyDao;
import undecided.erp.relationship.infra.dao.party.PartyTable;

@RequiredArgsConstructor
@Component
public class PartyDataSource implements PartyRepository {

  private final PartyDao partyDao;

  @Override
  public List<Party> findByType(PartyType type) {
    return PartyTable.toEntities(partyDao.findByType(type));
  }
}
