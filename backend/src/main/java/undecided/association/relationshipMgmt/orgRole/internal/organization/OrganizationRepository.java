package undecided.association.relationshipMgmt.orgRole.internal.organization;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.association.relationshipMgmt.orgRole.spi.Organization;
import undecided.supporting.snowflake.SnowflakeId;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, SnowflakeId> {}
