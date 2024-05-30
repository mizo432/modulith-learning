package undecided.erp.relMgmt.model.personRole.employee;

import org.springframework.lang.NonNull;
import undecided.erp.shared.value.AllDecimalString;
import undecided.erp.shared.value.FixedLengthString;
import undecided.erp.shared.value.NonNullString;

public record EmployeeNo(String value) {


  private static final int LENGTH = 8;

  public static EmployeeNo of(@NonNull String value) {
    NonNullString.of(value);
    AllDecimalString.of(value);
    FixedLengthString.of(value, LENGTH);
    return new EmployeeNo(value);

  }

}
