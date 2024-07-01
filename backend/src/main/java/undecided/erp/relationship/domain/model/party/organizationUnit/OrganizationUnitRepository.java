package undecided.erp.relationship.domain.model.party.organizationUnit;

import org.springframework.data.repository.CrudRepository;
import undecided.erp.shared.entity.SnowflakeId;

public interface OrganizationUnitRepository extends
    CrudRepository<OrganizationUnit, SnowflakeId<OrganizationUnit>> {

}
