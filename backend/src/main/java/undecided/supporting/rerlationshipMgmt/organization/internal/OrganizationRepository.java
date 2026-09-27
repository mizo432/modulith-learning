package undecided.supporting.rerlationshipMgmt.organization.internal;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.supporting.rerlationshipMgmt.organization.spi.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, UUID> {}
