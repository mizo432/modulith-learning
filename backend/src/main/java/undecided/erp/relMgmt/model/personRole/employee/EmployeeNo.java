package undecided.erp.relMgmt.model.personRole.employee;

import org.springframework.lang.NonNull;
import undecided.erp.shared.value.AllDecimalCode;
import undecided.erp.shared.value.LengthCode;
import undecided.erp.shared.value.NonNullCode;

public record EmployeeNo(String value) {


  private static final int LENGTH = 8;

  public static EmployeeNo of(@NonNull String value) {
    NonNullCode.of(value);
    AllDecimalCode.of(value);
    LengthCode.of(value, LENGTH);
    return new EmployeeNo(value);

  }

}
