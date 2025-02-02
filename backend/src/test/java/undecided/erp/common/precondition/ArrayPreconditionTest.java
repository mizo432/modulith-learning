package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

public class ArrayPreconditionTest {

  @Test
  void testVerifyNotEmptyNull() {
    assertThat((Object[]) ArrayPrecondition.verifyNotEmpty(null)).isNull();

  }

  @Test
  void testVerifyNotEmptyEmpty() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> ArrayPrecondition.verifyNotEmpty(new Integer[]{}));
  }

  @Test
  void testVerifyNotEmptyNotEmpty() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThat(ArrayPrecondition.verifyNotEmpty(array)).isEqualTo(array);
  }

  @Test
  void testVerifyAllElementNotNullNullArray() {
    assertThat((Object[]) ArrayPrecondition.verifyAllElementNotNull(null,
        i -> new IndexedRuntimeException("Array element is null.", i))).isNull();
  }

  @Test
  void testVerifyAllElementNotNullEmptyArray() {
    Integer[] array = new Integer[]{};
    assertThat(ArrayPrecondition.verifyAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i))).isEqualTo(array);
  }

  @Test
  void testVerifyAllElementNotNullWithNullException() {
    Integer[] array = new Integer[]{1, 2, 3, null};
    assertThatExceptionOfType(IndexedRuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.verifyAllElementNotNull(array,
            i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testVerifyAllElementNotNullWithoutNullException() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThat(ArrayPrecondition.verifyAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i))).isEqualTo(array);
  }

  @Test
  void testVerifyOneElementNotNullWithNullArray() {
    assertThat((Object[]) ArrayPrecondition.verifyOneElementNotNull(null,
        () -> new RuntimeException(
            "All the array elements are null or not exactly one element is not null."))).isNull();
  }

  @Test
  void testVerifyOneElementNotNullWithAllNulls() {
    Integer[] array = new Integer[]{null, null, null};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.verifyOneElementNotNull(array,
            () -> new RuntimeException(
                "All the array elements are null or not exactly one element is not null.")));
  }

  @Test
  void testVerifyOneElementNotNullWithOneNotNull() {
    Integer[] array = new Integer[]{1, null, null};
    assertThat(ArrayPrecondition.verifyOneElementNotNull(array,
        () -> new RuntimeException(
            "All the array elements are null or not exactly one element is not null."))).isEqualTo(
        array);
  }

  @Test
  void testVerifyOneElementNotNullWithMoreThanOneNotNull() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.verifyOneElementNotNull(array,
            () -> new RuntimeException(
                "All the array elements are null or not exactly one element is not null.")));
  }

  @Test
  void testVerifyAnyElementNotNullWithNullArray() {
    assertThat((Object[]) ArrayPrecondition.verifyAnyElementNotNull(null,
        () -> new RuntimeException("All elements in the array are null."))).isNull();
  }

  @Test
  void testVerifyAnyElementNotNullWithAllNulls() {
    Integer[] array = new Integer[]{null, null, null};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.verifyAnyElementNotNull(array,
            () -> new RuntimeException("All elements in the array are null.")));
  }

  @Test
  void testVerifyAnyElementNotNullWithNotNull() {
    Integer[] array = new Integer[]{1, null, null};
    assertThat(ArrayPrecondition.verifyAnyElementNotNull(array,
        () -> new RuntimeException("All elements in the array are null."))).isEqualTo(array);
  }
}
