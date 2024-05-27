package undecided.erp.relMgmt.internal.personRole.employee;

import org.springframework.stereotype.Service;
import undecided.erp.relMgmt.EmployeeDto;
import undecided.erp.relMgmt.internal.party.Party;
import undecided.erp.relMgmt.spi.EmployeeQuery;
import undecided.erp.shared.entity.SnowflakeId;

@Service
public class EmployeeQueryImpl implements EmployeeQuery {

  public EmployeeDto findById(SnowflakeId<Party> id) {
    return null;
  }

}
