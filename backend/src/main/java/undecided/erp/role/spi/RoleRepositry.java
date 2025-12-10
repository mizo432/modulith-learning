package undecided.erp.role.spi;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepositry extends CrudRepository<Role, UUID> {}
