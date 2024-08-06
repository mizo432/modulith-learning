package undecided.erp.relationship.domain.model.party.person;

import static undecided.erp.common.primitive.Objects2.isNull;
import static undecided.erp.common.primitive.Strings2.isEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.verifier.StringVerifiers;

@EqualsAndHashCode
@Getter
public class FirstName {

  public static final FirstName EMPTY = new FirstName();
  @JsonValue
  private final String value;

  private FirstName(final String value) {
    this.value = value;
  }

  public FirstName() {
    value = null;
  }

  @JsonCreator
  public static FirstName of(@NonNull @NotEmpty final String value) {
    StringVerifiers.verifyNonEmpty(value, () -> new IllegalArgumentException("value is not null"));
    return new FirstName(value);
  }

  public static FirstName reconstruct(final String value) {
    if (isNull(value)) {
      return EMPTY;
    }

    if (isEmpty(value)) {
      return EMPTY;
    }
    return new FirstName(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }

}
