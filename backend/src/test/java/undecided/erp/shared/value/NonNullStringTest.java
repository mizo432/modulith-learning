package undecided.erp.shared.value;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NonNullStringTest {

  @Test
  void of_CreateNonNullCodeWithNonNullString_NotNull() {
    String input = "test";
    NonNullString nonNullString = NonNullString.of(input);
    assertNotNull(nonNullString);
  }

  @Test
  void of_CreateNonNullCodeWithNullString_ThrowsIllegalArgumentException() {
    String input = null;
    assertThrows(IllegalArgumentException.class, () -> {
      NonNullString.of(input);
    });
  }
}
