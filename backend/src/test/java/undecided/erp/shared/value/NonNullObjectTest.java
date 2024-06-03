package undecided.erp.shared.value;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class NonNullObjectTest {

  @Test
  void of_CreateNonNullCodeWithNonNullString_NotNull() {
    String input = "test";
    NonNullObject nonNullObject = NonNullObject.of(input);
  }

  @Test
  void of_CreateNonNullCodeWithNonNullObject_NotNull() {
    Object input = new Object();
    NonNullObject nonNullObject = NonNullObject.of(input);
    assertNotNull(nonNullObject);
  }

  @Test
  void of_CreateNonNullCodeWithNullString_ThrowsIllegalArgumentException() {
    String input = null;
    assertThrows(IllegalArgumentException.class, () -> {
      NonNullObject.of(input);
    });
  }

  @Test
  void of_CreateNonNullCodeWithNonNullObject_ThrowsIllegalArgumentException() {
    Object input = null;
    assertThrows(IllegalArgumentException.class, () -> {
      NonNullObject.of(input);
    });
  }
}

