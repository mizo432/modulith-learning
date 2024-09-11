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
public class LastName {

  public static final LastName EMPTY = new LastName();

  @JsonValue
  private final String value;

  private LastName(final String value) {
    this.value = value;
  }

  private LastName() {
    this.value = null;
  }

  @JsonCreator
  public static LastName of(@NonNull @NotEmpty final String value) {
    StringVerifiers.verifyNonEmpty(value, () -> new IllegalArgumentException("value is not empty"));
    return new LastName(value);
  }

  public static LastName reconstruct(String value) {
    if (isNull(value)) {
      return EMPTY;
    }

    if (isEmpty(value)) {
      return EMPTY;
    }
    return new LastName(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }
}
