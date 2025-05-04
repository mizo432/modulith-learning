package undecided.erp.relationship.domain.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FullNameTest {

  @Test
  void testCreateMethod() {
    FirstName firstName = FirstName.of("John");
    LastName lastName = LastName.of("Doe");

    FullName fullName = FullName.create(firstName, lastName);

    assertThat(fullName.toString())
        .isEqualTo("Doe John");
  }

  @Test
  void testReconstructMethod() {
    FirstName firstName = FirstName.of("Jane");
    LastName lastName = LastName.of("Smith");

    FullName fullName = FullName.reconstruct(firstName, lastName);

    assertThat(fullName.toString())
        .isEqualTo("Smith Jane");
  }
}
