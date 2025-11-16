package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class Arrays2Test {

  @Test
  void whenAllElementsAreNotNull_thenAllElementsNotNullReturnsTrue() {
    //given
    Integer[] arrayWithAllElements = new Integer[]{1, 2, 3};

    //when
    boolean result = Arrays2.allElementsNotNull(arrayWithAllElements);

    //then
    assertTrue(result);
  }

  @Test
  void whenAnyElementIsNull_thenAllElementsNotNullReturnsFalse() {
    //given
    Integer[] arrayWithNullElement = new Integer[]{1, null, 3};

    //when
    boolean result = Arrays2.allElementsNotNull(arrayWithNullElement);

    //then
    assertFalse(result);
  }

  @Test
  void whenArrayIsEmpty_thenAllElementsNotNullReturnsTrue() {
    //given
    Integer[] emptyArray = new Integer[]{};

    //when
    boolean result = Arrays2.allElementsNotNull(emptyArray);

    //then
    assertTrue(result);
  }

  @DisplayName("allElementsNotNullメソッドのテスト")
  @Nested
  class AllElementsNotNullTest {

    @Test
    @DisplayName("すべての要素が非nullの場合はtrueを返すべき")
    void shouldReturnTrueWhenAllElementsAreNotNull() {
      // given
      Integer[] array = {1, 2, 3, 4, 5};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("1つの要素がnullの場合はfalseを返すべき")
    void shouldReturnFalseWhenOneElementIsNull() {
      // given
      Integer[] array = {1, 2, null, 4, 5};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("最初の要素がnullの場合はfalseを返すべき")
    void shouldReturnFalseWhenFirstElementIsNull() {
      // given
      Integer[] array = {null, 2, 3};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("最後の要素がnullの場合はfalseを返すべき")
    void shouldReturnFalseWhenLastElementIsNull() {
      // given
      Integer[] array = {1, 2, null};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("すべての要素がnullの場合はfalseを返すべき")
    void shouldReturnFalseWhenAllElementsAreNull() {
      // given
      Integer[] array = {null, null, null};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("空の配列の場合はtrueを返すべき")
    void shouldReturnTrueForEmptyArray() {
      // given
      Integer[] array = {};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("単一の非null要素を持つ配列の場合はtrueを返すべき")
    void shouldReturnTrueForSingleNonNullElement() {
      // given
      Integer[] array = {1};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("単一のnull要素を持つ配列の場合はfalseを返すべき")
    void shouldReturnFalseForSingleNullElement() {
      // given
      Integer[] array = {null};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("異なる型の配列でも動作するべき")
    void shouldWorkWithDifferentTypes() {
      // given
      String[] stringArray = {"a", "b", "c"};

      // when
      boolean result = Arrays2.allElementsNotNull(stringArray);

      // then
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("異なる型の配列でnullが含まれる場合はfalseを返すべき")
    void shouldReturnFalseForDifferentTypesWithNull() {
      // given
      String[] stringArray = {"a", null, "c"};

      // when
      boolean result = Arrays2.allElementsNotNull(stringArray);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("複数のnull要素が連続している場合はfalseを返すべき")
    void shouldReturnFalseForConsecutiveNullElements() {
      // given
      Integer[] array = {1, null, null, 4};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("大きな配列で最後にnullがある場合はfalseを返すべき")
    void shouldReturnFalseForLargeArrayWithNullAtEnd() {
      // given
      Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, null};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("オブジェクト配列の場合も正しく動作するべき")
    void shouldWorkWithObjectArray() {
      // given
      Object[] array = {new Object(), new Object(), new Object()};

      // when
      boolean result = Arrays2.allElementsNotNull(array);

      // then
      assertThat(result).isTrue();
    }
  }

  @DisplayName("equalメソッドのテスト")
  @Nested
  class EqualTest {

    @Test
    @DisplayName("両方の配列が同じ場合はtrueを返す")
    void shouldReturnTrueWhenBothArraysAreIdentical() {
      // given
      Integer[] array1 = {1, 2, 3};
      Integer[] array2 = {1, 2, 3};

      // when
      boolean result = Arrays2.equal(array1, array2);

      // then
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("両方の配列がnullの場合はtrueを返す")
    void shouldReturnTrueWhenBothArraysAreNull() {
      // given
      Integer[] array1 = null;
      Integer[] array2 = null;

      // when
      boolean result = Arrays2.equal(array1, array2);

      // then
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("片方がnull、片方が空の場合はfalseを返す")
    void shouldReturnFalseWhenOneArrayIsNullAndOtherIsEmpty() {
      // given
      Integer[] array1 = null;
      Integer[] array2 = {};

      // when
      boolean result = Arrays2.equal(array1, array2);

      // then
      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("配列の長さが異なる場合はfalseを返す")
    void shouldReturnFalseWhenArraysHaveDifferentLengths() {
      // given
      Integer[] array1 = {1, 2};
      Integer[] array2 = {1, 2, 3};

      // when
      boolean result = Arrays2.equal(array1, array2);

      // then
      assertThat(result).isFalse();
    }
  }
}
