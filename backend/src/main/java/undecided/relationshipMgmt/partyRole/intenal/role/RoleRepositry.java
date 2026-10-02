package undecided.relationshipMgmt.partyRole.intenal.role;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import undecided.relationshipMgmt.partyRole.spi.role.Role;

public interface RoleRepositry extends CrudRepository<Role, UUID> {}
