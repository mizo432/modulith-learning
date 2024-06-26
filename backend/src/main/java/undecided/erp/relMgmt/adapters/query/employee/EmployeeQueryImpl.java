package undecided.erp.relMgmt.adapters.query.employee;

import org.springframework.stereotype.Service;
import undecided.erp.relMgmt.application.query.employee.EmployeeQuery;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.relMgmt.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

@Service
public class EmployeeQueryImpl implements EmployeeQuery {

  public Employee findById(SnowflakeId<Party> id) {
    return null;
  }

}
