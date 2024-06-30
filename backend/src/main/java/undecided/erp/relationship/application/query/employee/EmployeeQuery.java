package undecided.erp.relationship.application.query.employee;

import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

public interface EmployeeQuery {

  Employee findById(SnowflakeId<Party> id);

}
