package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

public class ArrayPreconditionTest {

  @Test
  void testCheckNotEmptyNull() {
    assertThat((Object[]) ArrayPrecondition.checkNotEmpty(null)).isNull();

  }

  @Test
  void testCheckNotEmptyEmpty() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> ArrayPrecondition.checkNotEmpty(new Integer[]{}));
  }

  @Test
  void testCheckNotEmptyNotEmpty() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThat(ArrayPrecondition.checkNotEmpty(array)).isEqualTo(array);
  }

  @Test
  void testCheckAllElementNotNullNullArray() {
    assertThat((Object[]) ArrayPrecondition.checkAllElementNotNull(null,
        i -> new IndexedRuntimeException("Array element is null.", i))).isNull();
  }

  @Test
  void testCheckAllElementNotNullEmptyArray() {
    Integer[] array = new Integer[]{};
    assertThat(ArrayPrecondition.checkAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i))).isEqualTo(array);
  }

  @Test
  void testCheckAllElementNotNullWithNullException() {
    Integer[] array = new Integer[]{1, 2, 3, null};
    assertThatExceptionOfType(IndexedRuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.checkAllElementNotNull(array,
            i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testCheckAllElementNotNullWithoutNullException() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThat(ArrayPrecondition.checkAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i))).isEqualTo(array);
  }

  @Test
  void testCheckOneElementNotNullWithNullArray() {
    assertThat((Object[]) ArrayPrecondition.checkOneElementNotNull(null,
        () -> new RuntimeException(
            "All the array elements are null or not exactly one element is not null."))).isNull();
  }

  @Test
  void testCheckOneElementNotNullWithAllNulls() {
    Integer[] array = new Integer[]{null, null, null};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.checkOneElementNotNull(array,
            () -> new RuntimeException(
                "All the array elements are null or not exactly one element is not null.")));
  }

  @Test
  void testCheckOneElementNotNullWithOneNotNull() {
    Integer[] array = new Integer[]{1, null, null};
    assertThat(ArrayPrecondition.checkOneElementNotNull(array,
        () -> new RuntimeException(
            "All the array elements are null or not exactly one element is not null."))).isEqualTo(
        array);
  }

  @Test
  void testCheckOneElementNotNullWithMoreThanOneNotNull() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.checkOneElementNotNull(array,
            () -> new RuntimeException(
                "All the array elements are null or not exactly one element is not null.")));
  }

  @Test
  void testCheckAnyElementNotNullWithNullArray() {
    assertThat((Object[]) ArrayPrecondition.checkAnyElementNotNull(null,
        () -> new RuntimeException("All elements in the array are null."))).isNull();
  }

  @Test
  void testCheckAnyElementNotNullWithAllNulls() {
    Integer[] array = new Integer[]{null, null, null};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayPrecondition.checkAnyElementNotNull(array,
            () -> new RuntimeException("All elements in the array are null.")));
  }

  @Test
  void testCheckAnyElementNotNullWithNotNull() {
    Integer[] array = new Integer[]{1, null, null};
    assertThat(ArrayPrecondition.checkAnyElementNotNull(array,
        () -> new RuntimeException("All elements in the array are null."))).isEqualTo(array);
  }
}
