package com.github.mizo432.shared.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ArrayPrecondition のテスト")
class ArrayPreconditionTest {

  @Nested
  @DisplayName("checkNotEmpty メソッドのテスト")
  class CheckNotEmptyTest {

    @Test
    @DisplayName("配列がnullの場合、nullを返すこと")
    void shouldReturnNullWhenArrayIsNull() {
      assertThat((Object[]) ArrayPrecondition.checkNotEmpty(null)).isNull();
    }

    @Test
    @DisplayName("配列が空の場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenArrayIsEmpty() {
      assertThatExceptionOfType(IllegalArgumentException.class)
          .isThrownBy(() -> ArrayPrecondition.checkNotEmpty(new Integer[] {}));
    }

    @Test
    @DisplayName("配列が空でない場合、配列を返すこと")
    void shouldReturnArrayWhenNotEmpty() {
      Integer[] array = new Integer[] {1, 2, 3};
      assertThat(ArrayPrecondition.checkNotEmpty(array)).isEqualTo(array);
    }
  }

  @Nested
  @DisplayName("checkAllElementNotNull メソッドのテスト")
  class CheckAllElementNotNullTest {

    @Test
    @DisplayName("配列がnullの場合、nullを返すこと")
    void shouldReturnNullWhenArrayIsNull() {
      assertThat(
              (Object[])
                  ArrayPrecondition.checkAllElementNotNull(
                      null, i -> new IndexedRuntimeException("Array element is null.", i)))
          .isNull();
    }

    @Test
    @DisplayName("配列が空の場合、配列を返すこと")
    void shouldReturnArrayWhenEmpty() {
      Integer[] array = new Integer[] {};
      assertThat(
              ArrayPrecondition.checkAllElementNotNull(
                  array, i -> new IndexedRuntimeException("Array element is null.", i)))
          .isEqualTo(array);
    }

    @Test
    @DisplayName("null要素を含む場合、例外をスローすること")
    void shouldThrowExceptionWhenContainsNull() {
      Integer[] array = new Integer[] {1, 2, 3, null};
      assertThatExceptionOfType(IndexedRuntimeException.class)
          .isThrownBy(
              () ->
                  ArrayPrecondition.checkAllElementNotNull(
                      array, i -> new IndexedRuntimeException("Array element is null.", i)));
    }

    @Test
    @DisplayName("null要素を含まない場合、配列を返すこと")
    void shouldReturnArrayWhenNoNullElements() {
      Integer[] array = new Integer[] {1, 2, 3};
      assertThat(
              ArrayPrecondition.checkAllElementNotNull(
                  array, i -> new IndexedRuntimeException("Array element is null.", i)))
          .isEqualTo(array);
    }
  }

  @Nested
  @DisplayName("checkOneElementNotNull メソッドのテスト")
  class CheckOneElementNotNullTest {

    @Test
    @DisplayName("配列がnullの場合、nullを返すこと")
    void shouldReturnNullWhenArrayIsNull() {
      assertThat(
              (Object[])
                  ArrayPrecondition.checkOneElementNotNull(
                      null,
                      () ->
                          new RuntimeException(
                              "All the array elements are null or not exactly one element is not null.")))
          .isNull();
    }

    @Test
    @DisplayName("すべての要素がnullの場合、例外をスローすること")
    void shouldThrowExceptionWhenAllNulls() {
      Integer[] array = new Integer[] {null, null, null};
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () ->
                  ArrayPrecondition.checkOneElementNotNull(
                      array,
                      () ->
                          new RuntimeException(
                              "All the array elements are null or not exactly one element is not null.")));
    }

    @Test
    @DisplayName("1つだけnullでない要素がある場合、配列を返すこと")
    void shouldReturnArrayWhenOneNotNull() {
      Integer[] array = new Integer[] {1, null, null};
      assertThat(
              ArrayPrecondition.checkOneElementNotNull(
                  array,
                  () ->
                      new RuntimeException(
                          "All the array elements are null or not exactly one element is not null.")))
          .isEqualTo(array);
    }

    @Test
    @DisplayName("nullでない要素が複数ある場合、例外をスローすること")
    void shouldThrowExceptionWhenMoreThanOneNotNull() {
      Integer[] array = new Integer[] {1, 2, 3};
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () ->
                  ArrayPrecondition.checkOneElementNotNull(
                      array,
                      () ->
                          new RuntimeException(
                              "All the array elements are null or not exactly one element is not null.")));
    }
  }

  @Nested
  @DisplayName("checkAnyElementNotNull メソッドのテスト")
  class CheckAnyElementNotNullTest {

    @Test
    @DisplayName("配列がnullの場合、nullを返すこと")
    void shouldReturnNullWhenArrayIsNull() {
      assertThat(
              (Object[])
                  ArrayPrecondition.checkAnyElementNotNull(
                      null, () -> new RuntimeException("All elements in the array are null.")))
          .isNull();
    }

    @Test
    @DisplayName("すべての要素がnullの場合、例外をスローすること")
    void shouldThrowExceptionWhenAllNulls() {
      Integer[] array = new Integer[] {null, null, null};
      assertThatExceptionOfType(RuntimeException.class)
          .isThrownBy(
              () ->
                  ArrayPrecondition.checkAnyElementNotNull(
                      array, () -> new RuntimeException("All elements in the array are null.")));
    }

    @Test
    @DisplayName("nullでない要素が1つ以上ある場合、配列を返すこと")
    void shouldReturnArrayWhenAnyNotNull() {
      Integer[] array = new Integer[] {1, null, null};
      assertThat(
              ArrayPrecondition.checkAnyElementNotNull(
                  array, () -> new RuntimeException("All elements in the array are null.")))
          .isEqualTo(array);
    }
  }
}
