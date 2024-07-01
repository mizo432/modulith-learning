package undecided.erp.relationship.domain.model.party.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.erp.relationship.domain.model.party.organizationUnit.OrganizationUnitName;

@Getter
@AllArgsConstructor
public class OrganizationAttribute {

  private OrganizationUnitName name;
  /**
   * 組織の産業部門。
   */
  private IndustrySector industrySector;

}
