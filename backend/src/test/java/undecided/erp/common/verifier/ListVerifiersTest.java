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
  void testCheckNotEmptyWithEmptyList() {
    List<String> list = Collections.emptyList();
    assertThrows(IllegalArgumentException.class, () -> ListVerifiers.checkNotEmpty(list));
  }

  @Test
  void testCheckNotEmptyWithNonEmptyList() {
    List<String> list = Arrays.asList("Test1", "Test2");
    List<String> result = ListVerifiers.checkNotEmpty(list);
    assertEquals(result, list);
  }

  @Test
  void testCheckNotEmptyWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.checkNotEmpty(list);
    assertNull(result);
  }

  @Test
  void testCheckAnyElementNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.checkAnyElementNotNull(list,
        () -> new IllegalArgumentException("List must not contain null element"));
    assertNull(result);
  }

  @Test
  void testCheckAnyElementNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", null);
    List<String> result = ListVerifiers.checkAnyElementNotNull(list,
        () -> new IllegalArgumentException("List must not contain null element"));
    assertEquals(result, list);
  }

  @Test
  void testCheckAnyElementNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(IllegalArgumentException.class,
        () -> ListVerifiers.checkAnyElementNotNull(list,
            () -> new IllegalArgumentException("List must not contain null element")));
  }

  @Test
  void testCheckAllElementsNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", "Test2");
    List<String> result = ListVerifiers.checkAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx));
    assertEquals(list, result);
  }

  @Test
  void testCheckAllElementsNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.checkAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx));
    assertNull(result);
  }

  @Test
  void testCheckAllElementNotNullWithSomeNullInList() {
    List<String> list = Arrays.asList("Test1", null, "Test2");
    assertThrows(IndexedRuntimeException.class, () -> ListVerifiers.checkAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx)));
  }

  @Test
  void testCheckOneElementNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListVerifiers.checkOneElementNotNull(list,
        () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertNull(result);
  }

  @Test
  void testCheckOneElementNotNullWithNonNullListOneNotNull() {
    List<String> list = Arrays.asList(null, "Test1", null);
    List<String> result = ListVerifiers.checkOneElementNotNull(list,
        () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertEquals(result, list);
  }

  @Test
  void testCheckOneElementNotNullWithNonNullListMoreThanOneNotNull() {
    List<String> list = Arrays.asList("Test1", "Test2");
    assertThrows(IllegalArgumentException.class,
        () -> ListVerifiers.checkOneElementNotNull(list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element")));
  }

  @Test
  void testCheckOneElementNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(IllegalArgumentException.class,
        () -> ListVerifiers.checkOneElementNotNull(list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element")));
  }
}
