package undecided.erp.common.verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListVerifiersTest {

  @Test
  void testVerifyNotEmptyWithEmptyList() {
    List<String> list = Collections.emptyList();
    assertThrows(IllegalArgumentException.class, () -> ListVerifiers.verifyNotEmpty(list));
  }

  @Test
  void testVerifyNotEmptyWithNonEmptyList() {
    List<String> list = Arrays.asList("Test1", "Test2");
    List<String> result = ListVerifiers.verifyNotEmpty(list);
    assertEquals(result, list);
  }

  @Test
  void testVerifyNotEmptyWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.verifyNotEmpty(list);
    assertNull(result);
  }

  @Test
  void testVerifyAnyElementNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.verifyAnyElementNotNull(list,
        () -> new IllegalArgumentException("List must not contain null element"));
    assertNull(result);
  }

  @Test
  void testVerifyAnyElementNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", null);
    List<String> result = ListVerifiers.verifyAnyElementNotNull(list,
        () -> new IllegalArgumentException("List must not contain null element"));
    assertEquals(result, list);
  }

  @Test
  void testVerifyAnyElementNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(IllegalArgumentException.class,
        () -> ListVerifiers.verifyAnyElementNotNull(list,
            () -> new IllegalArgumentException("List must not contain null element")));
  }

  @Test
  void testCheckAllElementsNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", "Test2");
    List<String> result = ListVerifiers.verifyAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx));
    assertEquals(list, result);
  }

  @Test
  void testCheckAllElementsNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.verifyAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx));
    assertNull(result);
  }

  @Test
  void testVerifyAllElementNotNullWithSomeNullInList() {
    List<String> list = Arrays.asList("Test1", null, "Test2");
    assertThrows(IndexedRuntimeException.class, () -> ListVerifiers.verifyAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx)));
  }

  @Test
  void testVerifyOneElementNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.verifyOneElementNotNull(list,
        () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertNull(result);
  }

  @Test
  void testVerifyOneElementNotNullWithNonNullListOneNotNull() {
    List<String> list = Arrays.asList(null, "Test1", null);
    List<String> result = ListVerifiers.verifyOneElementNotNull(list,
        () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertEquals(result, list);
  }

  @Test
  void testVerifyOneElementNotNullWithNonNullListMoreThanOneNotNull() {
    List<String> list = Arrays.asList("Test1", "Test2");
    assertThrows(IllegalArgumentException.class,
        () -> ListVerifiers.verifyOneElementNotNull(list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element")));
  }

  @Test
  void testVerifyOneElementNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(IllegalArgumentException.class,
        () -> ListVerifiers.verifyOneElementNotNull(list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element")));
  }
}
