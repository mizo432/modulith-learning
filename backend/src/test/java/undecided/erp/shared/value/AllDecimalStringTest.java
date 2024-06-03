package undecided.erp.shared.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AllDecimalStringTest {

  @Test
  void testOfWithNullValue() {
    AllDecimalString ads = AllDecimalString.of(null);
    assertNull(ads.value());
  }

  @Test
  void testOfWithEmptyString() {
    AllDecimalString ads = AllDecimalString.of("");
    assertEquals("", ads.value());
  }

  @Test
  void testOfWithValidDecimalString() {
    AllDecimalString ads = AllDecimalString.of("123");
    assertEquals("123", ads.value());
  }

  @Test
  void testOfWithInvalidDecimalString() {
    assertThrows(IllegalArgumentException.class, () -> AllDecimalString.of("abc"));
  }
}
