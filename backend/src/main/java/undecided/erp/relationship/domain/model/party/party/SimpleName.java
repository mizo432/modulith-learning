package undecided.erp.relationship.domain.model.party.party;

import static undecided.erp.common.primitive.Strings2.isEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.verifier.StringVerifiers;

@Getter
public class SimpleName {

  public static final SimpleName EMPTY = new SimpleName();
  @JsonValue
  private final String value;

  private SimpleName() {
    value = null;
  }

  private SimpleName(@NonNull @NotEmpty final String value) {
    this.value = value;
  }

  @JsonCreator
  public static SimpleName of(@NonNull @NotEmpty final String value) {
    StringVerifiers.verifyNonEmpty(value,
        () -> new IllegalArgumentException("value can't be empty."));
    return new SimpleName(value);
  }

  public static SimpleName reconstruct(String value) {
    if (isEmpty(value)) {
      return EMPTY;
    }

    return new SimpleName(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);

  }

}
