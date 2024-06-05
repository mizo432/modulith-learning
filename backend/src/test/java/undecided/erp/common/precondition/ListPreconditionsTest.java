package undecided.erp.common.precondition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListPreconditionsTest {

  @Test
  void testCheckNotEmptyWithEmptyList() {
    List<String> list = Collections.emptyList();
    assertThrows(IllegalArgumentException.class, () -> ListPreconditions.checkNotEmpty(list));
  }

  @Test
  void testCheckNotEmptyWithNonEmptyList() {
    List<String> list = Arrays.asList("Test1", "Test2");
    List<String> result = ListPreconditions.checkNotEmpty(list);
    assertEquals(result, list);
  }

  @Test
  void testCheckNotEmptyWithNullList() {
    List<String> list = null;
    List<String> result = ListPreconditions.checkNotEmpty(list);
    assertNull(result);
  }

  @Test
  void testCheckAnyElementNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListPreconditions.checkAnyElementNotNull(list,
        () -> new IllegalArgumentException("List must not contain null element"));
    assertNull(result);
  }

  @Test
  void testCheckAnyElementNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", null);
    List<String> result = ListPreconditions.checkAnyElementNotNull(list,
        () -> new IllegalArgumentException("List must not contain null element"));
    assertEquals(result, list);
  }

  @Test
  void testCheckAnyElementNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(IllegalArgumentException.class,
        () -> ListPreconditions.checkAnyElementNotNull(list,
            () -> new IllegalArgumentException("List must not contain null element")));
  }

  @Test
  void testCheckAllElementsNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", "Test2");
    List<String> result = ListPreconditions.checkAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx));
    assertEquals(list, result);
  }

  @Test
  void testCheckAllElementsNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListPreconditions.checkAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx));
    assertNull(result);
  }

  @Test
  void testCheckAllElementNotNullWithSomeNullInList() {
    List<String> list = Arrays.asList("Test1", null, "Test2");
    assertThrows(IndexedRuntimeException.class, () -> ListPreconditions.checkAllElementNotNull(list,
        idx -> new IndexedRuntimeException("List must not contain null element at index: " + idx,
            idx)));
  }

  @Test
  void testCheckOneElementNotNullWithNullList() {
    List<String> list = null;
    List<String> result = ListPreconditions.checkOneElementNotNull(list,
        () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertNull(result);
  }

  @Test
  void testCheckOneElementNotNullWithNonNullListOneNotNull() {
    List<String> list = Arrays.asList(null, "Test1", null);
    List<String> result = ListPreconditions.checkOneElementNotNull(list,
        () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertEquals(result, list);
  }

  @Test
  void testCheckOneElementNotNullWithNonNullListMoreThanOneNotNull() {
    List<String> list = Arrays.asList("Test1", "Test2");
    assertThrows(IllegalArgumentException.class,
        () -> ListPreconditions.checkOneElementNotNull(list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element")));
  }

  @Test
  void testCheckOneElementNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(IllegalArgumentException.class,
        () -> ListPreconditions.checkOneElementNotNull(list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element")));
  }
}
