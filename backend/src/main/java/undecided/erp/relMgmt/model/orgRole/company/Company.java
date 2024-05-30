package undecided.erp.relMgmt.model.orgRole.company;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import undecided.erp.relMgmt.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

@RequiredArgsConstructor
@Getter
public class Company {

  private final SnowflakeId<Party> id;

  private final CompanyAttribute attribute;


  public static Company create(SnowflakeId<Party> id, CompanyAttribute attribute) {
    return new Company(id, attribute);
  }
}
