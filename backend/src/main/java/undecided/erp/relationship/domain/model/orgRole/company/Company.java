package undecided.erp.relationship.domain.model.orgRole.company;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.entity.SnowflakeId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Company {

  private SnowflakeId<Party> id;

  private CompanyAttribute attribute;


  public static Company create(SnowflakeId<Party> id, CompanyAttribute attribute) {
    return new Company(id, attribute);
  }

  public static Company create(CompanyAttribute attribute) {
    return new Company(SnowflakeId.newInstance(), attribute);
  }
}
