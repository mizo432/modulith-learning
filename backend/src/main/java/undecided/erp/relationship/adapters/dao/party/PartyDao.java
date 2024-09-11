package undecided.erp.relationship.adapters.dao.party;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import undecided.erp.relationship.domain.model.party.party.PartyType;

public interface PartyDao extends CrudRepository<PartyTable, Long> {

  List<PartyTable> findByType(PartyType type);
}
