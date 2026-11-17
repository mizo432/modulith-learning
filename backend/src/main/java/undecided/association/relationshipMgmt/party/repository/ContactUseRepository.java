package undecided.association.relationshipMgmt.party.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface ContactUseRepository extends JpaRepository<ContactUse, SnowflakeId> {

  List<ContactUse> findByPartyId(SnowflakeId partyId);

  Optional<ContactUse> findByPartyIdAndType(SnowflakeId partyId, ContactUseType type);
}
