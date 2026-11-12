package com.github.mizo432.shared.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ListPreconditions のテスト")
class ListPreconditionsTest {

  @Nested
  @DisplayName("checkNotEmpty メソッドのテスト")
  class CheckNotEmptyTest {

    @Test
    @DisplayName("リストが空の場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenListIsEmpty() {
      List<String> list = Collections.emptyList();
      assertThatThrownBy(() -> ListPreconditions.checkNotEmpty(list))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("リストが空でない場合、リストを返すこと")
    void shouldReturnListWhenNotEmpty() {
      List<String> list = Arrays.asList("Test1", "Test2");
      List<String> result = ListPreconditions.checkNotEmpty(list);
      assertThat(result).isEqualTo(list);
    }

    @Test
    @DisplayName("リストがnullの場合、nullを返すこと")
    void shouldReturnNullWhenListIsNull() {
      List<String> list = null;
      List<String> result = ListPreconditions.checkNotEmpty(list);
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("checkAnyItemNotNull メソッドのテスト")
  class CheckAnyItemNotNullTest {

    @Test
    @DisplayName("リストがnullの場合、nullを返すこと")
    void shouldReturnNullWhenListIsNull() {
      List<String> list = null;
      List<String> result =
          ListPreconditions.checkAnyItemNotNull(
              list, () -> new IllegalArgumentException("List must not contain null element"));
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("nullでない要素が1つ以上ある場合、リストを返すこと")
    void shouldReturnListWhenAnyItemNotNull() {
      List<String> list = Arrays.asList("Test1", null);
      List<String> result =
          ListPreconditions.checkAnyItemNotNull(
              list, () -> new IllegalArgumentException("List must not contain null element"));
      assertThat(result).isEqualTo(list);
    }

    @Test
    @DisplayName("すべての要素がnullの場合、例外をスローすること")
    void shouldThrowExceptionWhenAllNull() {
      List<String> list = Arrays.asList(null, null);
      assertThatThrownBy(
              () ->
                  ListPreconditions.checkAnyItemNotNull(
                      list, () -> new IllegalArgumentException("List must not contain null element")))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("checkAllItemNotNull メソッドのテスト")
  class CheckAllItemNotNullTest {

    @Test
    @DisplayName("null要素を含まない場合、リストを返すこと")
    void shouldReturnListWhenAllNotNull() {
      List<String> list = Arrays.asList("Test1", "Test2");
      List<String> result =
          ListPreconditions.checkAllItemNotNull(
              list,
              idx ->
                  new IndexedRuntimeException(
                      "List must not contain null element at index: " + idx, idx));
      assertThat(result).isEqualTo(list);
    }

    @Test
    @DisplayName("リストがnullの場合、nullを返すこと")
    void shouldReturnNullWhenListIsNull() {
      List<String> list = null;
      List<String> result =
          ListPreconditions.checkAllItemNotNull(
              list,
              idx ->
                  new IndexedRuntimeException(
                      "List must not contain null element at index: " + idx, idx));
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("null要素を含む場合、例外をスローすること")
    void shouldThrowExceptionWhenContainsNull() {
      List<String> list = Arrays.asList("Test1", null, "Test2");
      assertThatThrownBy(
              () ->
                  ListPreconditions.checkAllItemNotNull(
                      list,
                      idx ->
                          new IndexedRuntimeException(
                              "List must not contain null element at index: " + idx, idx)))
          .isInstanceOf(IndexedRuntimeException.class);
    }
  }

  @Nested
  @DisplayName("checkOneItemNotNull メソッドのテスト")
  class CheckOneItemNotNullTest {

    @Test
    @DisplayName("リストがnullの場合、nullを返すこと")
    void shouldReturnNullWhenListIsNull() {
      List<String> list = null;
      List<String> result =
          ListPreconditions.checkOneItemNotNull(
              list,
              () -> new IllegalArgumentException("List must contain exactly one non-null element"));
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("1つだけnullでない要素がある場合、リストを返すこと")
    void shouldReturnListWhenOneNotNull() {
      List<String> list = Arrays.asList(null, "Test1", null);
      List<String> result =
          ListPreconditions.checkOneItemNotNull(
              list,
              () -> new IllegalArgumentException("List must contain exactly one non-null element"));
      assertThat(result).isEqualTo(list);
    }

    @Test
    @DisplayName("nullでない要素が複数ある場合、例外をスローすること")
    void shouldThrowExceptionWhenMoreThanOneNotNull() {
      List<String> list = Arrays.asList("Test1", "Test2");
      assertThatThrownBy(
              () ->
                  ListPreconditions.checkOneItemNotNull(
                      list,
                      () ->
                          new IllegalArgumentException(
                              "List must contain exactly one non-null element")))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("すべての要素がnullの場合、例外をスローすること")
    void shouldThrowExceptionWhenAllNull() {
      List<String> list = Arrays.asList(null, null);
      assertThatThrownBy(
              () ->
                  ListPreconditions.checkOneItemNotNull(
                      list,
                      () ->
                          new IllegalArgumentException(
                              "List must contain exactly one non-null element")))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("checkSize メソッドのテスト")
  class CheckSizeTest {

    @Test
    @DisplayName("リストがnullの場合、nullを返すこと")
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
    @DisplayName("リストのサイズが期待値と一致する場合、リストを返すこと")
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
    @DisplayName("リストが空でサイズ0を期待する場合、空のリストを返すこと")
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
