package undecided.erp.shared.value;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FixedLengthStringTest {

  @Test
  public void testOfWithCorrectLength() {
    String value = "test";
    int length = value.length();

    FixedLengthString lengthCode = FixedLengthString.of(value, length);

    Assertions.assertNotNull(lengthCode);
    Assertions.assertEquals(value, lengthCode.value());
  }

  @Test
  public void testOfWithIncorrectLength() {
    String value = "test";
    int incorrectLength = value.length() + 1;

    Assertions.assertThrows(IllegalArgumentException.class,
        () -> FixedLengthString.of(value, incorrectLength));
  }
}