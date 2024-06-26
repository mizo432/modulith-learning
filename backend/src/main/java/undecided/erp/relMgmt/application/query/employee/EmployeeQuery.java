package undecided.erp.relMgmt.application.query.employee;

import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.relMgmt.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

public interface EmployeeQuery {

  Employee findById(SnowflakeId<Party> id);

}
