package undecided.erp.relationship.domain.model.party.party;

import org.springframework.data.repository.CrudRepository;
import undecided.erp.shared.entity.SnowflakeId;

public interface PartyRepository extends
    CrudRepository<Party, SnowflakeId<Party>> {

}
