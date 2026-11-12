package undecided.association.relationshipMgmt.party.internal.partyRelationship;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.spi.party.partyRelationship.PartyRelationship;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface PartyRelationshipRepository extends JpaRepository<PartyRelationship, SnowflakeId> {

  List<PartyRelationship> findByPartyPartyId(SnowflakeId partyId);

  List<PartyRelationship> findByRelatedPartyPartyId(SnowflakeId partyId);
}
