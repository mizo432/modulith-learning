package undecided.erp.common.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ArrayVerifiersTest {

  @Test
  void testCheckNotEmptyNull() {
    assertNull(ArrayVerifiers.checkNotEmpty(null));

  }

  @Test
  void testCheckNotEmptyEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> ArrayVerifiers.checkNotEmpty(new Integer[]{}));
  }

  @Test
  void testCheckNotEmptyNotEmpty() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertEquals(array, ArrayVerifiers.checkNotEmpty(array));
  }

  @Test
  void testCheckAllElementNotNullNullArray() {
    assertNull(ArrayVerifiers.checkAllElementNotNull(null,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testCheckAllElementNotNullEmptyArray() {
    Integer[] array = new Integer[]{};
    assertEquals(array, ArrayVerifiers.checkAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testCheckAllElementNotNullWithNullException() {
    Integer[] array = new Integer[]{1, 2, 3, null};
    assertThrows(IndexedRuntimeException.class,
        () -> ArrayVerifiers.checkAllElementNotNull(array,
            i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testCheckAllElementNotNullWithoutNullException() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertEquals(array, ArrayVerifiers.checkAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }
}
