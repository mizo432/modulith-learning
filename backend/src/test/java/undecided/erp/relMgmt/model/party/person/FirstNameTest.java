package undecided.erp.relMgmt.model.party.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FirstNameTest {

  private String firstNameValue = "John";

  @Test
  void shouldCreateFirstNameInstanceWhenOfMethodIsCalledWithValidName() {
    FirstName firstName = FirstName.of(firstNameValue);
    assertNotNull(firstName);
    assertEquals(firstNameValue, firstName.getValue());
  }

  @Test
  void shouldThrowExceptionWhenOfMethodIsCalledWithNullValue() {
    assertThrows(NullPointerException.class, () -> FirstName.of(null));
  }

  @Test
  void shouldThrowExceptionWhenOfMethodIsCalledWithEmptyValue() {
    assertThrows(IllegalArgumentException.class, () -> FirstName.of(""));
  }
}