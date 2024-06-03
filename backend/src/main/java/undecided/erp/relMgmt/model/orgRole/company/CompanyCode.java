package undecided.erp.relMgmt.model.orgRole.company;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import undecided.erp.shared.value.AllDecimalString;
import undecided.erp.shared.value.FixedLengthString;
import undecided.erp.shared.value.NonNullObject;

@RequiredArgsConstructor
@Getter
public class CompanyCode {

  private static final int LENGTH = 3;
  private final String value;

  public static CompanyCode of(@NonNull String value) {
    NonNullObject.of(value);
    AllDecimalString.of(value);
    FixedLengthString.of(value, LENGTH);
    return new CompanyCode(value);

  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
