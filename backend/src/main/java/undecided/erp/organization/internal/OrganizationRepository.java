package undecided.erp.organization.internal;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.organization.spi.Organization;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, UUID> {}
