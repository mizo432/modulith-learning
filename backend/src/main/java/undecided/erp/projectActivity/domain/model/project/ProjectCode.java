package undecided.erp.projectActivity.domain.model.project;

import static undecided.erp.common.primitive.Objects2.isNull;
import static undecided.erp.common.verifier.StringVerifiers.verifyHalfWidthFixedLength;
import static undecided.erp.common.verifier.StringVerifiers.verifyNonEmpty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.ValueObject;
import undecided.erp.shared.entity.SingleValue;

@ValueObject
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class ProjectCode implements SingleValue<String> {

  public static final ProjectCode EMPTY = new ProjectCode();
  private static final int LENGTH = 3;

  private final String value;

  ProjectCode() {
    this(null);
  }

  public static ProjectCode of(@NonNull String value) {
    verifyNonEmpty(value,
        () -> new IllegalArgumentException(
            "ProjectCode cannot be empty. Please provide a valid code."));
    verifyHalfWidthFixedLength(value,
        () -> new IllegalArgumentException("ProjectCode must be exactly " + LENGTH
            + " characters long and use half-width characters."), LENGTH);
    return new ProjectCode(value);
  }

  @Override
  public boolean isEmpty() {
    return isNull(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
