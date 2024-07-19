package undecided.erp.relationship.application.facade;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.erp.relationship.application.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.relationship.spi.RelationshipMgmt;
import undecided.erp.shared.entity.SnowflakeId;

@Service
@RequiredArgsConstructor
public class RelationshipFacade implements RelationshipMgmt {

  private final EmployeeQuery employeeQuery;

  @Override
  public Optional<Employee> findById(@NonNull SnowflakeId<Party> id) {
    return employeeQuery.findById(id);
  }

}
