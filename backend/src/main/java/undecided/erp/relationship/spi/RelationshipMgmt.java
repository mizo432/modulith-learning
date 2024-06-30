package undecided.erp.relationship.spi;

import lombok.NonNull;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;
import undecided.erp.shared.entity.SnowflakeId;

public interface RelationshipMgmt {

  Employee findById(@NonNull SnowflakeId<Party> id);
}
