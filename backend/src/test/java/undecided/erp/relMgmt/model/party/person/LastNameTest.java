package undecided.erp.relMgmt.model.party.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import undecided.erp.relMgmt.domain.model.party.person.LastName;

public class LastNameTest {

  @Test
  public void testOfWithNonNullAndNonEmptyValueReturnsLastName() {
    String validValue = "Doe";
    LastName lastName = LastName.of(validValue);

    assertEquals(validValue, lastName.getValue());
  }

  @Test
  public void testOfWithNonNullAndEmptyValueThrowsException() {
    String invalidValue = "";

    assertThrows(IllegalArgumentException.class, () -> LastName.of(invalidValue));
  }

  @Test
  public void testOfWithNullValueThrowsException() {
    assertThrows(NullPointerException.class, () -> LastName.of(null));
  }
}