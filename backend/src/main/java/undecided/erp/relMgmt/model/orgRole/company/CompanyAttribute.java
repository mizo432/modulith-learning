package undecided.erp.relMgmt.model.orgRole.company;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
@Getter
public class CompanyAttribute {

  private final CompanyCode code;

  private final CompanyName name;

  public static CompanyAttribute of(@NonNull CompanyCode code, @NonNull CompanyName name) {
    return new CompanyAttribute(code, name);

  }


}
