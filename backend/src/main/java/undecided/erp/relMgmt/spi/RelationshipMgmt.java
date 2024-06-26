package undecided.erp.relMgmt.spi;

import lombok.NonNull;
import undecided.erp.relMgmt.domain.model.party.party.Party;
import undecided.erp.relMgmt.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

public interface RelationshipMgmt {

  Employee findById(@NonNull SnowflakeId<Party> id);
}
