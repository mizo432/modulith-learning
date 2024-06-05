package undecided.erp.common.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ArrayVerifiersTest {

  @Test
  void testVerifyNotEmptyNull() {
    assertNull(ArrayVerifiers.verifyNotEmpty(null));

  }

  @Test
  void testVerifyNotEmptyEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> ArrayVerifiers.verifyNotEmpty(new Integer[]{}));
  }

  @Test
  void testVerifyNotEmptyNotEmpty() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertEquals(array, ArrayVerifiers.verifyNotEmpty(array));
  }

  @Test
  void testVerifyAllElementNotNullNullArray() {
    assertNull(ArrayVerifiers.verifyAllElementNotNull(null,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testVerifyAllElementNotNullEmptyArray() {
    Integer[] array = new Integer[]{};
    assertEquals(array, ArrayVerifiers.verifyAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testVerifyAllElementNotNullWithNullException() {
    Integer[] array = new Integer[]{1, 2, 3, null};
    assertThrows(IndexedRuntimeException.class,
        () -> ArrayVerifiers.verifyAllElementNotNull(array,
            i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testVerifyAllElementNotNullWithoutNullException() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertEquals(array, ArrayVerifiers.verifyAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }
}
