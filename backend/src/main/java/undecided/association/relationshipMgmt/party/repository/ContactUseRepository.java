package undecided.association.relationshipMgmt.party.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface ContactUseRepository extends JpaRepository<ContactUse, SnowflakeId> {

  List<ContactUse> findByPartyId(SnowflakeId partyId);
}
