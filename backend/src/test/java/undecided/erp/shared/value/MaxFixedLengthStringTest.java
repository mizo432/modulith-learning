package undecided.erp.shared.value;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MaxFixedLengthStringTest {

  @Test
  void testOf_WithValidInput_ReturnNewInstance() {
    MaxLengthString result = MaxLengthString.of("test", 5);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("test", result.value());
  }

  @Test
  void testOf_WithValueLengthEqualMax_ReturnNewInstance() {
    MaxLengthString result = MaxLengthString.of("test", 4);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("test", result.value());
  }

  @Test
  void testOf_WithValueLengthGreaterThanMax_ThrowsIllegalArgumentException() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> MaxLengthString.of("test", 3));
  }

  @Test
  void testOf_WithEmptyValue_ReturnNewInstance() {
    MaxLengthString result = MaxLengthString.of("", 3);
    Assertions.assertNotNull(result);
    Assertions.assertEquals("", result.value());
  }

  @Test
  void testOf_WithNullValue_ThrowsNullPointerException() {
    Assertions.assertThrows(NullPointerException.class, () -> MaxLengthString.of(null, 3));
  }
}