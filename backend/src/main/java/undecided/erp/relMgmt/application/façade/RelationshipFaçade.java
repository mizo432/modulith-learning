package undecided.erp.relMgmt.application.façade;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.erp.relMgmt.application.query.employee.EmployeeQuery;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.relMgmt.domain.model.personRole.employee.Employee;
import undecided.erp.relMgmt.spi.RelationshipMgmt;
import undecided.erp.shared.entity.SnowflakeId;

@Service
@RequiredArgsConstructor
public class RelationshipFaçade implements RelationshipMgmt {

  private final EmployeeQuery employeeQuery;

  @Override
  public Employee findById(@NonNull SnowflakeId<Party> id) {
    return employeeQuery.findById(id);
  }
}
