package undecided.erp.relationship.domain.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

public class PersonNamesTest {

  @Test
  void shouldCreatePersonNames() {
    FirstName firstName = FirstName.of("John");
    LastName lastName = LastName.of("Doe");

    PersonNames personNames = PersonNames.create(firstName, lastName);

    assertThat(personNames.getFirstName()).isEqualTo(firstName);
    assertThat(personNames.getLastName()).isEqualTo(lastName);
  }

  @Test
  void shouldNotCreatePersonNamesWithNullFirstName() {
    LastName lastName = LastName.of("Doe");

    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
      PersonNames.create(null, lastName);
    });
  }

  @Test
  void shouldNotCreatePersonNamesWithNullLastName() {
    FirstName firstName = FirstName.of("John");

    assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
      PersonNames.create(firstName, null);
    });
  }

  @Test
  void shouldReconstructPersonNames() {
    String firstName = "John";
    String lastName = "Doe";

    PersonNames personNames = PersonNames.reconstruct(firstName, lastName);

    assertThat(personNames.getFirstName().getValue())
        .isEqualTo(firstName);
    assertThat(personNames.getLastName().getValue())
        .isEqualTo(lastName);
  }


  @Test
  void shouldNotReconstructPersonNamesWithNullFirstName() {
    String lastName = "Doe";

    assertThat(PersonNames.reconstruct(null, lastName)).isNotNull();
  }


  @Test
  void shouldNotReconstructPersonNamesWithNullLastName() {
    String firstName = "John";
    assertThat(PersonNames.reconstruct(firstName, null)).isNotNull();
  }

  @Test
  void shouldReturnProperToStringResult() {
    String firstName = "John";
    String lastName = "Doe";

    PersonNames personNames = PersonNames.reconstruct(firstName, lastName);
    assertThat(personNames.toString()).isEqualTo(
        "PersonNames{firstName=John, lastName=Doe}");
  }

  @Test
  void shouldBeAbleToGetFullName() {
    FirstName firstName = FirstName.of("John");
    LastName lastName = LastName.of("Doe");
    PersonNames personNames = PersonNames.create(firstName, lastName);
    FullName fullName = personNames.fullName();

    assertThat(fullName.getValue()).isEqualTo("Doe John");
  }

}
