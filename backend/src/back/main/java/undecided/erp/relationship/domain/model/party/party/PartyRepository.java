package undecided.erp.relationship.domain.model.party.party;

import java.util.List;

public interface PartyRepository {

  List<Party> findByType(PartyType type);

  void insert(Party party);

  void update(Party party);
}
