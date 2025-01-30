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
