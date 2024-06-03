package undecided.erp.relMgmt.model.party.person;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.shared.value.NonEmptyString;
import undecided.erp.shared.value.NonNullObject;

@Getter
public class LastName {

  private final String value;

  public LastName(@NonNull @NotEmpty final String value) {
    NonNullObject.of(value);
    NonEmptyString.of(value);
    this.value = value;
  }

  public static LastName of(@NonNull @NotEmpty final String value) {
    NonNullObject.of(value);
    NonEmptyString.of(value);
    return new LastName(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
