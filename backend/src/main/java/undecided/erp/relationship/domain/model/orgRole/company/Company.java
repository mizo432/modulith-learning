package undecided.erp.relationship.domain.model.orgRole.company;

import jakarta.persistence.Id;
import lombok.Data;
import undecided.erp.relationship.domain.model.orgRole.ICompany;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * Represents a company entity with an identifier and a company code.
 * <p>
 * The class implements the ICompany interface, which extends the OrgRole interface, indicating that
 * Company is a type of organizational role in the system.
 */
@Data
public class Company implements ICompany {

  @Id
  private SnowflakeId id;

  private CompanyCode code;

}
