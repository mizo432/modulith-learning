package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

public class ArrayVerifiersTest {

  @Test
  void testVerifyNotEmptyNull() {
    assertThat((Object[]) ArrayVerifiers.verifyNotEmpty(null)).isNull();

  }

  @Test
  void testVerifyNotEmptyEmpty() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> ArrayVerifiers.verifyNotEmpty(new Integer[]{}));
  }

  @Test
  void testVerifyNotEmptyNotEmpty() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThat(ArrayVerifiers.verifyNotEmpty(array)).isEqualTo(array);
  }

  @Test
  void testVerifyAllElementNotNullNullArray() {
    assertThat((Object[]) ArrayVerifiers.verifyAllElementNotNull(null,
        i -> new IndexedRuntimeException("Array element is null.", i))).isNull();
  }

  @Test
  void testVerifyAllElementNotNullEmptyArray() {
    Integer[] array = new Integer[]{};
    assertThat(ArrayVerifiers.verifyAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i))).isEqualTo(array);
  }

  @Test
  void testVerifyAllElementNotNullWithNullException() {
    Integer[] array = new Integer[]{1, 2, 3, null};
    assertThatExceptionOfType(IndexedRuntimeException.class).isThrownBy(
        () -> ArrayVerifiers.verifyAllElementNotNull(array,
            i -> new IndexedRuntimeException("Array element is null.", i)));
  }

  @Test
  void testVerifyAllElementNotNullWithoutNullException() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThat(ArrayVerifiers.verifyAllElementNotNull(array,
        i -> new IndexedRuntimeException("Array element is null.", i))).isEqualTo(array);
  }

  @Test
  void testVerifyOneElementNotNullWithNullArray() {
    assertThat((Object[]) ArrayVerifiers.verifyOneElementNotNull(null,
        () -> new RuntimeException(
            "All the array elements are null or not exactly one element is not null."))).isNull();
  }

  @Test
  void testVerifyOneElementNotNullWithAllNulls() {
    Integer[] array = new Integer[]{null, null, null};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayVerifiers.verifyOneElementNotNull(array,
            () -> new RuntimeException(
                "All the array elements are null or not exactly one element is not null.")));
  }

  @Test
  void testVerifyOneElementNotNullWithOneNotNull() {
    Integer[] array = new Integer[]{1, null, null};
    assertThat(ArrayVerifiers.verifyOneElementNotNull(array,
        () -> new RuntimeException(
            "All the array elements are null or not exactly one element is not null."))).isEqualTo(
        array);
  }

  @Test
  void testVerifyOneElementNotNullWithMoreThanOneNotNull() {
    Integer[] array = new Integer[]{1, 2, 3};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayVerifiers.verifyOneElementNotNull(array,
            () -> new RuntimeException(
                "All the array elements are null or not exactly one element is not null.")));
  }

  @Test
  void testVerifyAnyElementNotNullWithNullArray() {
    assertThat((Object[]) ArrayVerifiers.verifyAnyElementNotNull(null,
        () -> new RuntimeException("All elements in the array are null."))).isNull();
  }

  @Test
  void testVerifyAnyElementNotNullWithAllNulls() {
    Integer[] array = new Integer[]{null, null, null};
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(
        () -> ArrayVerifiers.verifyAnyElementNotNull(array,
            () -> new RuntimeException("All elements in the array are null.")));
  }

  @Test
  void testVerifyAnyElementNotNullWithNotNull() {
    Integer[] array = new Integer[]{1, null, null};
    assertThat(ArrayVerifiers.verifyAnyElementNotNull(array,
        () -> new RuntimeException("All elements in the array are null."))).isEqualTo(array);
  }
}
