package undecided.relationshipMgmt.orgRole.internal.organization;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.relationshipMgmt.orgRole.spi.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, UUID> {}
