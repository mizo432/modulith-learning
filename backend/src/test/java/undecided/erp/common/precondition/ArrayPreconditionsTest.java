package undecided.erp.common.precondition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ArrayPreconditionsTest {

  @Test
  void testCheckNotEmptyNull() {
    assertNull(ArrayPreconditions.checkNotEmpty(null));

  }

  @Test
  void testCheckNotEmptyEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> ArrayPreconditions.checkNotEmpty(new Integer[]{}));
  }

  @Test
  void testCheckNotEmptyNotEmpty() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertEquals(array, ArrayPreconditions.checkNotEmpty(array));
  }

  @Test
  void testCheckAllElementNotNullNullArray() {
    assertNull(ArrayPreconditions.checkAllElementNotNull(null,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testCheckAllElementNotNullEmptyArray() {
    Integer[] array = new Integer[]{};
    assertEquals(array, ArrayPreconditions.checkAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testCheckAllElementNotNullWithNullException() {
    Integer[] array = new Integer[]{1, 2, 3, null};
    assertThrows(IndexedRuntimeException.class,
        () -> ArrayPreconditions.checkAllElementNotNull(array,
            i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testCheckAllElementNotNullWithoutNullException() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertEquals(array, ArrayPreconditions.checkAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i)));
  }
}
