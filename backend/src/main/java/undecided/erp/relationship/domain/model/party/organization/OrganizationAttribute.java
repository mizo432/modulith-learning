package undecided.erp.relationship.domain.model.party.organization;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrganizationAttribute {

  private final OrganizationName name;
  /**
   * 組織の産業部門。
   */
  private final IndustrySector industrySector;

}
