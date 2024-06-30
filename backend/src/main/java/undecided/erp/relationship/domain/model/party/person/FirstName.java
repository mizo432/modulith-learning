package undecided.erp.relationship.domain.model.party.person;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.shared.value.NonEmptyString;
import undecided.erp.shared.value.NonNullObject;

@Getter
public class FirstName {

  private final String value;

  private FirstName(@NonNull @NotEmpty final String value) {
    NonNullObject.of(value);
    NonEmptyString.of(value);
    this.value = value;
  }

  public static FirstName of(@NonNull @NotEmpty final String value) {
    NonNullObject.of(value);
    NonEmptyString.of(value);
    return new FirstName(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

}
