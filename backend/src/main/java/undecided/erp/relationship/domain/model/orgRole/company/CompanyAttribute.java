package undecided.erp.relationship.domain.model.orgRole.company;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class CompanyAttribute {

  private CompanyCode code;

  private CompanyName name;

  public static CompanyAttribute of(@NonNull CompanyCode code, @NonNull CompanyName name) {
    return new CompanyAttribute(code, name);

  }


}
