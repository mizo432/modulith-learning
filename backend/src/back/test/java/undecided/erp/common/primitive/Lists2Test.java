package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Lists2Test {

  /**
   * Sets2クラスのisEmptyメソッドのテストクラスです。
   * <p>
   * このクラスには、目的のメソッドの実装ロジックを確認するテストスイートが含まれています。
   */
  @Nested
  class IsEmptyTest {

    @Test
    void withEmptySetShouldReturnTrue() {
      // arrange
      @NonNull List<Object> list = Collections.emptyList();

      // act
      boolean result = Lists2.isEmpty(list);

      // assert
      assertThat(result)
          .as("isEmpty should return true for an empty List.")
          .isTrue();
    }

    @Test
    void withNonEmptySetShouldReturnFalse() {
      // arrange
      var list = new ArrayList<>();
      list.add(1);

      // act
      boolean result = Lists2.isEmpty(list);

      // assert
      assertThat(result)
          .as("isEmpty should return false for a non-empty List.")
          .isFalse();
    }

    @Test
    void withNullValueShouldThrowException() {
      // arrange
      List<Object> list = null;

      // act and assert
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> Lists2.isEmpty(list));

    }
  }

  @Nested
  class IsEmptySilentlyTest {

    @Test
    void whenNullInputThenReturnsTrue() {
      // arrange
      List<Object> list = null;

      // act
      boolean result = Lists2.isEmptySilently(list);

      // assert
      assertThat(result)
          .as("isEmptySilently should return true when input is null.")
          .isTrue();
    }

    @Test
    void whenEmptyListThenReturnsTrue() {
      // arrange
      @NonNull List<Object> list = Collections.emptyList();

      // act
      boolean result = Lists2.isEmptySilently(list);

      // assert
      assertThat(result)
          .as("isEmptySilently should return true when list is empty.")
          .isTrue();
    }

    @Test
    void whenNotEmptyListThenReturnsFalse() {
      // arrange
      var list = new ArrayList<>();
      list.add(1);

      // act
      boolean result = Lists2.isEmptySilently(list);

      // assert
      assertThat(result)
          .as("isEmptySilently should return false when list is not empty.")
          .isFalse();
    }
  }

  @Nested
  class IsAllElementsNotNullTest {

    @Test
    void withAllNonNullElementsShouldReturnTrue() {
      // arrange
      var list = new ArrayList<>();
      list.add(1);
      list.add(2);

      // act
      boolean result = Lists2.isAllElementsNotNull(list);

      // assert
      assertThat(result)
          .as("isAllElementsNotNull should return true when all elements of list are not null.")
          .isTrue();
    }

    @Test
    void withAnyNullElementShouldReturnFalse() {
      // arrange
      var list = new ArrayList<>();
      list.add(1);
      list.add(null);

      // act
      boolean result = Lists2.isAllElementsNotNull(list);

      // assert
      assertThat(result)
          .as("isAllElementsNotNull should return false when any element in list is null.")
          .isFalse();
    }
  }

  /**
   * Test Suite for Lists2.stream() method.
   */
  @Nested
  class StreamTest {

    @Test
    void whenNonNullListThenReturnStream() {
      // arrange
      var list = new ArrayList<>();
      list.add(1);

      // act
      Stream<Object> result = Lists2.stream(list);

      // assert
      assertThat(result)
          .as("stream should return a non-null Stream.")
          .isNotNull();
    }

    @Test
    void whenStreamCollectToListShouldEqualOriginalList() {
      // arrange
      var list = new ArrayList<>();
      list.add(1);
      list.add(2);
      list.add(3);

      // act
      List<Object> resultList = Lists2.stream(list).collect(Collectors.toList());

      // assert
      assertThat(resultList)
          .as("stream().collect.toList should equal the original list.")
          .isEqualTo(list);
    }
  }

  @Nested
  class NewArrayListTest {

    @Test
    void withNoArgsShouldReturnEmptyArrayList() {
      // act
      ArrayList<Object> result = Lists2.newArrayList();

      // assert
      assertThat(result)
          .as("newArrayList() should return an empty ArrayList.")
          .isEmpty();
      assertThat(result)
          .as("Returned ArrayList should be instance of ArrayList class.")
          .isInstanceOf(ArrayList.class);
    }

    @Test
    void withArgsShouldReturnArrayListOfThoseElements() {
      // arrange
      String el1 = "element 1", el2 = "element 2";

      // act
      ArrayList<String> result = Lists2.newArrayList(el1, el2);

      // assert
      assertThat(result)
          .as("newArrayList(E... elements) should return an ArrayList with the input elements.")
          .containsExactly(el1, el2);
    }

    @Test
    void withIterableShouldReturnArrayListOfThoseElements() {
      // arrange
      List<String> original = Arrays.asList("el1", "el2", "el3");

      // act
      ArrayList<String> result = Lists2.newArrayList(original);

      // assert
      assertThat(result)
          .as("newArrayList(Iterable<? extends E> elements) should return an ArrayList with the elements from the input Iterable.")
          .containsExactlyInAnyOrderElementsOf(original);
    }

    @Test
    void withIteratorShouldReturnArrayListOfThoseElements() {
      // arrange
      List<String> original = Arrays.asList("el1", "el2", "el3");

      // act
      ArrayList<String> result = Lists2.newArrayList(original.iterator());

      // assert
      assertThat(result)
          .as("newArrayList(Iterator<? extends E> elements) should return an ArrayList with the elements from the input Iterator.")
          .containsExactlyInAnyOrderElementsOf(original);
    }
  }

  @Nested
  class NewArrayListWithCapacityTest {

    @Test
    void withPositiveArgShouldReturnArrayListWithSize() {
      // arrange
      int initialSize = 10;

      // act
      ArrayList<Object> result = Lists2.newArrayListWithCapacity(initialSize);

      // assert
      assertThat(result)
          .as("newArrayListWithCapacity() should return an ArrayList with initial capacity equal to the input.")
          .hasSize(0);
    }

    @Test
    void withZeroArgShouldReturnEmptyArrayList() {
      // act
      ArrayList<Object> result = Lists2.newArrayListWithCapacity(0);

      // assert
      assertThat(result)
          .as("newArrayListWithCapacity(0) should return an empty ArrayList.")
          .isEmpty();
    }

    @Test
    void withNegativeArgShouldThrowException() {
      // arrange
      int initialSize = -1;

      // act and assert
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
              () -> Lists2.newArrayListWithCapacity(initialSize))
          .withMessage("initialArraySize must positive or zero");
    }
  }
}
