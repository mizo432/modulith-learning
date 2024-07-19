package undecided.erp.relationship.adapters.rest.company;

import lombok.RequiredArgsConstructor;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyAttribute;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyCode;
import undecided.erp.relationship.domain.model.orgRole.company.CompanyName;

@RequiredArgsConstructor
public class CompanyAttributeDto {

  private final CompanyCode code;
  private final CompanyName name;

  public CompanyAttribute toModel() {
    return new CompanyAttribute(code, name);
  }
}
