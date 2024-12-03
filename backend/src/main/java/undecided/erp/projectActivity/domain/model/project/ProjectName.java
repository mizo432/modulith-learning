package undecided.erp.projectActivity.domain.model.project;

import static undecided.erp.common.precondition.StringPrecondition.checkNonEmpty;
import static undecided.erp.common.primitive.Objects2.isNull;

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
public class ProjectName implements SingleValue<String> {

  public static final ProjectName EMPTY = new ProjectName();
  private final String value;

  ProjectName() {
    this(null);
  }

  public static ProjectName of(@NonNull String value) {
    checkNonEmpty(value,
        () -> new IllegalArgumentException("projectName may not be empty."));
    return new ProjectName(value);
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
