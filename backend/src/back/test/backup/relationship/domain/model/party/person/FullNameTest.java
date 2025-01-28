package undecided.erp.relationship.domain.model.party.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FullNameTest {

  @Test
  public void testCreateFullName() {
    FirstName firstName = FirstName.of("First");
    LastName lastName = LastName.of("Last");
    FullName fullName = FullName.create(firstName, lastName);
    assertEquals("Last First", fullName.toString());
  }

  @Test
  public void throwNullPointerExceptionOnNullFirstName() {
    LastName lastName = LastName.of("Last");
    assertThrows(NullPointerException.class, () -> {
      FullName fullName = FullName.create(null, lastName);
    });
  }

  @Test
  public void throwNullPointerExceptionOnNullLastName() {
    FirstName firstName = FirstName.of("First");
    assertThrows(NullPointerException.class, () -> {
      FullName fullName = FullName.create(firstName, null);
    });
  }

  @Test
  public void throwNullPointerExceptionOnNullFirstAndLastName() {
    assertThrows(NullPointerException.class, () -> {
      FullName fullName = FullName.create(null, null);
    });
  }


}
