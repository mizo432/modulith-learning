package undecided.erp.shared.value;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NonNullCodeTest {

  @Test
  void of_CreateNonNullCodeWithNonNullString_NotNull() {
    String input = "test";
    NonNullCode nonNullCode = NonNullCode.of(input);
    assertNotNull(nonNullCode);
  }

  @Test
  void of_CreateNonNullCodeWithNullString_ThrowsIllegalArgumentException() {
    String input = null;
    assertThrows(IllegalArgumentException.class, () -> {
      NonNullCode.of(input);
    });
  }
}
