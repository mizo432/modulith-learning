package undecided.shared.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
  void testCheckAnyItemNotNullWithNullList() {
    List<String> list = null;
    List<String> result =
        ListPreconditions.checkAnyItemNotNull(
            list, () -> new IllegalArgumentException("List must not contain null element"));
    assertNull(result);
  }

  @Test
  void testCheckAnyItemNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", null);
    List<String> result =
        ListPreconditions.checkAnyItemNotNull(
            list, () -> new IllegalArgumentException("List must not contain null element"));
    assertEquals(result, list);
  }

  @Test
  void testCheckAnyItemNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            ListPreconditions.checkAnyItemNotNull(
                list, () -> new IllegalArgumentException("List must not contain null element")));
  }

  @Test
  void testCheckAllElementsNotNullWithNonNullList() {
    List<String> list = Arrays.asList("Test1", "Test2");
    List<String> result =
        ListPreconditions.checkAllItemNotNull(
            list,
            idx ->
                new IndexedRuntimeException(
                    "List must not contain null element at index: " + idx, idx));
    assertEquals(list, result);
  }

  @Test
  void testCheckAllElementsNotNullWithNullList() {
    List<String> list = null;
    List<String> result =
        ListPreconditions.checkAllItemNotNull(
            list,
            idx ->
                new IndexedRuntimeException(
                    "List must not contain null element at index: " + idx, idx));
    assertNull(result);
  }

  @Test
  void testCheckAllItemNotNullWithSomeNullInList() {
    List<String> list = Arrays.asList("Test1", null, "Test2");
    assertThrows(
        IndexedRuntimeException.class,
        () ->
            ListPreconditions.checkAllItemNotNull(
                list,
                idx ->
                    new IndexedRuntimeException(
                        "List must not contain null element at index: " + idx, idx)));
  }

  @Test
  void testCheckOneItemNotNullWithNullList() {
    List<String> list = null;
    List<String> result =
        ListPreconditions.checkOneItemNotNull(
            list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertNull(result);
  }

  @Test
  void testCheckOneElementNotNullWithNonNullListOneNotNull() {
    List<String> list = Arrays.asList(null, "Test1", null);
    List<String> result =
        ListPreconditions.checkOneItemNotNull(
            list,
            () -> new IllegalArgumentException("List must contain exactly one non-null element"));
    assertEquals(result, list);
  }

  @Test
  void testCheckOneElementNotNullWithNonNullListMoreThanOneNotNull() {
    List<String> list = Arrays.asList("Test1", "Test2");
    assertThrows(
        IllegalArgumentException.class,
        () ->
            ListPreconditions.checkOneItemNotNull(
                list,
                () ->
                    new IllegalArgumentException(
                        "List must contain exactly one non-null element")));
  }

  @Test
  void testCheckOneItemNotNullWithAllNullList() {
    List<String> list = Arrays.asList(null, null);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            ListPreconditions.checkOneItemNotNull(
                list,
                () ->
                    new IllegalArgumentException(
                        "List must contain exactly one non-null element")));
  }

  @Nested
  @DisplayName("ListPreconditionsのcheckSizeのテスト")
  class CheckSizeTest {

    @Test
    @DisplayName("リストがnullの場合、nullを返す")
    void shouldReturnNullWhenListIsNull() {
      // Arrange
      List<String> list = null;

      // Act
      List<String> result =
          ListPreconditions.checkSize(list, () -> new IllegalArgumentException("Invalid size"), 2);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("リストのサイズが期待値と異なる場合、例外を投げる")
    void shouldThrowExceptionWhenSizeIsNotExpected() {
      // Arrange
      List<String> list = Arrays.asList("A", "B");

      // Act & Assert
      assertThatThrownBy(
              () ->
                  ListPreconditions.checkSize(
                      list, () -> new IllegalArgumentException("Invalid size"), 3))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Invalid size");
    }

    @Test
    @DisplayName("リストのサイズが期待値と一致する場合、リストを返す")
    void shouldReturnListWhenSizeMatchesExpected() {
      // Arrange
      List<String> list = Arrays.asList("A", "B", "C");

      // Act
      List<String> result =
          ListPreconditions.checkSize(list, () -> new IllegalArgumentException("Invalid size"), 3);

      // Assert
      assertThat(result).isEqualTo(list);
    }

    @Test
    @DisplayName("リストが空の場合、サイズ0であるかを確認")
    void shouldPassForEmptyListWhenSizeIsZero() {
      // Arrange
      List<String> list = Collections.emptyList();

      // Act
      List<String> result =
          ListPreconditions.checkSize(list, () -> new IllegalArgumentException("Invalid size"), 0);

      // Assert
      assertThat(result).isEmpty();
    }
  }
}
