package undecided.erp.relationship.spi;

import java.util.Optional;
import lombok.NonNull;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

public interface RelationshipMgmt {

  Optional<Employee> findById(@NonNull SnowflakeId id);
}
