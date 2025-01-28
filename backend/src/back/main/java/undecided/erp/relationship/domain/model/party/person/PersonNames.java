package undecided.erp.relationship.domain.model.party.person;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode
@Getter
public class PersonNames {

  public static final PersonNames EMPTY = new PersonNames();
  private final FirstName firstName;
  private final LastName lastName;

  private PersonNames() {
    firstName = FirstName.EMPTY;
    lastName = LastName.EMPTY;

  }

  PersonNames(FirstName firstName, LastName lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public static PersonNames create(@NonNull FirstName firstName, @NonNull LastName lastName) {
    return new PersonNames(firstName, lastName);
  }

  public static PersonNames reconstruct(String firstName, String lastName) {
    return create(FirstName.reconstruct(firstName), LastName.reconstruct(lastName));
  }

  @Override
  public String toString() {
    return "PersonNames{" +
        "firstName=" + firstName +
        ", lastName=" + lastName +
        '}';
  }

  public FullName fullName() {
    return FullName.reconstruct(firstName, lastName);

  }


}
