package undecided.erp.relMgmt.spi;

import undecided.erp.relMgmt.EmployeeDto;
import undecided.erp.relMgmt.internal.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

public interface EmployeeQuery {

  EmployeeDto findById(SnowflakeId<Party> id);

}
