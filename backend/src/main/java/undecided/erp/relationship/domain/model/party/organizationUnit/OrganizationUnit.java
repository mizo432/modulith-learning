package undecided.erp.relationship.domain.model.party.organizationUnit;

import java.util.List;
import undecided.erp.relationship.domain.model.party.addressUse.AddressUse;
import undecided.erp.relationship.domain.model.party.organization.Organization;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * OrganizationUnitクラスは組織内の単位を表現します。
 */
public class OrganizationUnit {

  private SnowflakeId<OrganizationUnit> id;
  private Organization organization;
  private List<OrganizationUnit> larger;
  private List<OrganizationUnit> smaller;
  private List<AddressUse> addressUses;
}
