package undecided.erp.shared.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NonEmptyStringTest {

  @Test
  public void whenValueIsNull_thenShouldReturnObjectWithNull() {
    assertEquals(new NonEmptyString(null).value(), NonEmptyString.of(null).value());
  }

  @Test
  public void whenValueIsEmpty_thenShouldThrowException() {
    assertThrows(IllegalArgumentException.class, () -> NonEmptyString.of(""));
  }

  @Test
  public void whenValueIsNotEmpty_thenShouldReturnObjectWithSameValue() {
    assertEquals(new NonEmptyString("test").value(), NonEmptyString.of("test").value());
  }
}
