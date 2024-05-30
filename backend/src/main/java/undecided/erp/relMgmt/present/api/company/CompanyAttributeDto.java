package undecided.erp.relMgmt.present.api.company;

import lombok.RequiredArgsConstructor;
import undecided.erp.relMgmt.model.orgRole.company.CompanyAttribute;
import undecided.erp.relMgmt.model.orgRole.company.CompanyCode;
import undecided.erp.relMgmt.model.orgRole.company.CompanyName;

@RequiredArgsConstructor
public class CompanyAttributeDto {

  private final CompanyCode code;
  private final CompanyName name;

  public CompanyAttribute toModel() {
    return new CompanyAttribute(code, name);
  }
}
