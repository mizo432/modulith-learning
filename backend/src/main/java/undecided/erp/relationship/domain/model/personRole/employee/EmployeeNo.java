package undecided.erp.relationship.domain.model.personRole.employee;

import static undecided.erp.common.primitive.Objects2.isNull;
import static undecided.erp.common.verifier.StringVerifiers.verifyAllDecimal;
import static undecided.erp.common.verifier.StringVerifiers.verifyHalfWidthFixedLength;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.NonNull;
import undecided.erp.common.primitive.Strings2;

public record EmployeeNo(String value) {


  public static final EmployeeNo EMPTY = new EmployeeNo(null);
  private static final int LENGTH = 8;

  @JsonCreator
  public static EmployeeNo of(@NonNull String value) {
    verifyAllDecimal(value,
        () -> new IllegalArgumentException("value must be decimal."));
    verifyHalfWidthFixedLength(value,
        () -> new IllegalArgumentException("value length"), LENGTH);
    return new EmployeeNo(value);

  }

  /**
   * Reconstructs an EmployeeNo object from the given value.
   *
   * @param value the value used to reconstruct the EmployeeNo object
   * @return an EmployeeNo object representing the given value, or EMPTY if the value is null or
   * empty
   */
  public static EmployeeNo reconstruct(String value) {
    return isNull(Strings2.nullIfEmpty(value)) ? EMPTY : new EmployeeNo(value);
  }

}
