package undecided.erp.relationship.adapters.query.employee;

import org.springframework.stereotype.Service;
import undecided.erp.relationship.application.query.employee.EmployeeQuery;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

@Service
public class EmployeeQueryImpl implements EmployeeQuery {

  public Employee findById(SnowflakeId<Party> id) {
    return null;
  }

}
