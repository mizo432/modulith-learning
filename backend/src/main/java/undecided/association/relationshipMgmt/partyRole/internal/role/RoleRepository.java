package undecided.association.relationshipMgmt.partyRole.internal.role;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import undecided.association.relationshipMgmt.partyRole.spi.role.Role;

public interface RoleRepository extends CrudRepository<Role, UUID> {}
