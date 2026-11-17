package undecided.association.relationshipMgmt.partyRole.internal.role;

import org.springframework.data.repository.CrudRepository;
import undecided.association.relationshipMgmt.partyRole.spi.role.Role;
import undecided.supporting.snowflake.SnowflakeId;

public interface RoleRepository extends CrudRepository<Role, SnowflakeId> {}
