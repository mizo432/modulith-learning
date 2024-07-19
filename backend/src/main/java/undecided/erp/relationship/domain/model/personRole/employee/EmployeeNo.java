package undecided.erp.relationship.domain.model.personRole.employee;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.NonNull;
import undecided.erp.common.verifier.StringVerifiers;

public record EmployeeNo(String value) {


  public static final EmployeeNo EMPTY = new EmployeeNo(null);
  private static final int LENGTH = 8;

  @JsonCreator
  public static EmployeeNo of(@NonNull String value) {
    StringVerifiers.verifyAllDecimal(value,
        () -> new IllegalArgumentException("value must be decimal."));
    StringVerifiers.verifyHalfWidthFixedLength(value,
        () -> new IllegalArgumentException("value length"), LENGTH);
    return new EmployeeNo(value);

  }

}
