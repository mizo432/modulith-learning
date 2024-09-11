package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.primitives.Ints;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.NonNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Maps2Test {

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
      @NonNull Map<String, String> map = Collections.emptyMap();

      // act
      boolean result = Maps2.isEmpty(map);

      // assert
      assertThat(result)
          .as("isEmpty should return true for an empty Map.")
          .isTrue();
    }

    @Test
    void withNonEmptyMapShouldReturnFalse() {
      // arrange
      var list = new HashMap<>();
      list.put("A", "B");

      // act
      boolean result = Maps2.isEmpty(list);

      // assert
      assertThat(result)
          .as("isEmpty should return false for a non-empty Map.")
          .isFalse();
    }

    @Test
    void withNullValueShouldThrowException() {
      // arrange
      Map<String, String> map = null;

      // act and assert
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> Maps2.isEmpty(map));

    }
  }

  @Nested
  class IsEmptySilentlyTest {

    @Test
    void withNullValueShouldReturnTrue() {
      // arrange
      Map<String, String> map = null;

      // act
      boolean result = Maps2.isEmptySilently(map);

      // assert
      assertThat(result)
          .as("isEmptySilently should return true for a null Map.")
          .isTrue();
    }

    @Test
    void withEmptyMapShouldReturnTrue() {
      // arrange
      @NonNull Map<String, String> map = Collections.emptyMap();

      // act
      boolean result = Maps2.isEmptySilently(map);

      // assert
      assertThat(result)
          .as("isEmptySilently should return true for an empty Map.")
          .isTrue();
    }

    @Test
    void withNonEmptyMapShouldReturnFalse() {
      // arrange
      var list = new HashMap<>();
      list.put("A", "B");

      // act
      boolean result = Maps2.isEmptySilently(list);

      // assert
      assertThat(result)
          .as("isEmptySilently should return false for a non-empty Map.")
          .isFalse();
    }
  }

  @Nested
  class IsAllElementsNotNullTest {

    @Test
    void withAllNotNullElementsShouldReturnTrue() {
      // arrange
      var map = new HashMap<String, String>();
      map.put("A", "B");

      // act
      boolean result = Maps2.isAllElementsNotNull(map);

      // assert
      assertThat(result)
          .as("isAllElementsNotNull should return true for a Map with all elements not null.")
          .isTrue();
    }

    @Test
    void withOneNullElementShouldReturnFalse() {
      // arrange
      var map = new HashMap<String, String>();
      map.put("A", null);

      // act
      boolean result = Maps2.isAllElementsNotNull(map);

      // assert
      assertThat(result)
          .as("isAllElementsNotNull should return false for a Map with at least one null element.")
          .isFalse();
    }
  }

  @Nested
  class NewHashMapWithExpectedSizeTest {

    @Test
    void withPositiveValueShouldReturnCorrectSize() {
      // arrange
      int expectedSize = 10;

      // act
      var result = Maps2.newHashMapWithExpectedSize(expectedSize);

      // assert
      assertThat(result)
          .as("newHashMapWithExpectedSize should return a HashMap with correct size.")
          .hasSize(0);
    }

    @Test
    void withZeroValueShouldReturnEmptyMap() {
      // arrange
      int expectedSize = 0;

      // act
      var result = Maps2.newHashMapWithExpectedSize(expectedSize);

      // assert
      assertThat(result)
          .as("newHashMapWithExpectedSize should return an empty HashMap for zero expected size.")
          .isEmpty();
    }

    @Test
    void withNegativeValueShouldThrowException() {
      // arrange
      int expectedSize = -1;

      // act and assert
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
              () -> Maps2.newHashMapWithExpectedSize(expectedSize))
          .withMessage("expectedSize must positive or zero.");
    }
  }

  @Nested
  class NewHashMapTest {

    @Test
    void shouldReturnNewEmptyHashMap() {
      // act
      var result = Maps2.newHashMap();

      // assert
      assertThat(result)
          .as("newHashMap should return a new empty HashMap.")
          .isEmpty();
    }

    @Test
    void withNonNullMapShouldReturnHashMapWithSameContents() {
      // arrange
      var map = new HashMap<String, String>();
      map.put("A", "B");

      // act
      var result = Maps2.newHashMap(map);

      // assert
      assertThat(result)
          .as("newHashMap should return a new HashMap with the same contents as the input map.")
          .isEqualTo(map);
    }

    @Test
    void withNullMapShouldThrowException() {
      // arrange
      Map<String, String> map = null;

      // act and assert
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> Maps2.newHashMap(map));
    }
  }

  @Nested
  class CapacityTest {

    @Test
    void withPositiveValueLessThanThreeShouldReturnCorrectValue() {
      // arrange
      int expectedSize = 2;

      // act
      int result = Maps2.capacity(expectedSize);

      // assert
      assertThat(result)
          .as("Capacity should return correct value for input less than three.")
          .isEqualTo(expectedSize + 1);
    }

    @Test
    void withPositiveValueGreaterThanTwoAndLessThanMaxPowerOfTwoShouldReturnCorrectValue() {
      // arrange
      int expectedSize = 5;

      // act
      int result = Maps2.capacity(expectedSize);

      // assert
      assertThat(result)
          .as("Capacity should return correct value for input greater than two and less than " +
              "max power of two.")
          .isEqualTo((int) Math.ceil(expectedSize / 0.75));
    }

    @Test
    void withPositiveValueEqualToOrGreaterThanMaxPowerOfTwoShouldReturnMaxIntegerValue() {
      // arrange
      int expectedSize = Ints.MAX_POWER_OF_TWO;

      // act
      int result = Maps2.capacity(expectedSize);

      // assert
      assertThat(result)
          .as("Capacity should return Integer.MAX_VALUE for input equal to or greater than max power of two.")
          .isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void withZeroValueShouldReturnOne() {
      // arrange
      int expectedSize = 0;

      // act
      int result = Maps2.capacity(expectedSize);

      // assert
      assertThat(result)
          .as("Capacity should return one for zero input.")
          .isEqualTo(1);
    }

    @Test
    void withNegativeValueShouldThrowException() {
      // arrange
      int expectedSize = -1;

      // act and assert
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
              () -> Maps2.capacity(expectedSize))
          .withMessage("expectedSize must positive or zero.");
    }
  }

  @Nested
  class UnmodifiableMapTest {

    @Test
    void withNonNullMapShouldReturnUnmodifiableMap() {
      // arrange
      var map = new HashMap<String, String>();
      map.put("A", "B");

      // act
      var result = Maps2.unmodifiableMap(map);

      // assert
      assertThrows(UnsupportedOperationException.class, () -> result.put("B", "A"));
      assertThat(result)
          .as("unmodifiableMap should return a unmodifiable Map identical to the input map.")
          .isEqualTo(map);
    }

    @Test
    void withSortedMapShouldReturnUnmodifiableSortedMap() {
      // arrange
      SortedMap<String, String> map = new TreeMap<>();
      map.put("B", "A");
      map.put("A", "B");

      // act
      var result = Maps2.unmodifiableMap(map);

      // assert
      assertThrows(UnsupportedOperationException.class, () -> result.put("C", "A"));
      assertThat(result)
          .as("unmodifiableMap should return a unmodifiable SortedMap identical to the input SortedMap.")
          .isEqualTo(map);
    }

    @Test
    void withNullMapShouldThrowException() {
      // arrange
      Map<String, String> map = null;

      // act and assert
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> Maps2.unmodifiableMap(map));
    }

  }

  @Nested
  class NewLinkedHashMapTest {

    @Test
    void shouldReturnNewEmptyLinkedHashMap() {
      // act
      var result = Maps2.newLinkedHashMap();

      // assert
      assertThat(result)
          .as("newLinkedHashMap should return a new empty LinkedHashMap.")
          .isEmpty();
    }

    @Test
    void withNonNullMapShouldReturnLinkedHashMapWithSameContents() {
      // arrange
      var map = new LinkedHashMap<String, String>();
      map.put("A", "B");

      // act
      var result = Maps2.newLinkedHashMap(map);

      // assert
      assertThat(result)
          .as("newLinkedHashMap should return a new LinkedHashMap with the same contents as the input map.")
          .isEqualTo(map);
    }

    @Test
    void withNullMapShouldThrowException() {
      // arrange
      Map<String, String> map = null;

      // act and assert
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> Maps2.newLinkedHashMap(map));
    }
  }

  @Nested
  class NewLinkedHashMapWithExpectedSizeTest {

    @Test
    void withPositiveValueShouldReturnCorrectSize() {
      // arrange
      int expectedSize = 10;

      // act
      var result = Maps2.newLinkedHashMapWithExpectedSize(expectedSize);

      // assert
      assertThat(result)
          .as("newLinkedHashMapWithExpectedSize should return a LinkedHashMap with correct size.")
          .hasSize(0);
    }

    @Test
    void withZeroValueShouldReturnEmptyMap() {
      // arrange
      int expectedSize = 0;

      // act
      var result = Maps2.newLinkedHashMapWithExpectedSize(expectedSize);

      // assert
      assertThat(result)
          .as("newLinkedHashMapWithExpectedSize should return an empty LinkedHashMap for zero expected size.")
          .isEmpty();
    }

    @Test
    void withNegativeValueShouldThrowException() {
      // arrange
      int expectedSize = -1;

      // act and assert
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
              () -> Maps2.newLinkedHashMapWithExpectedSize(expectedSize))
          .withMessage("expectedSize must positive or zero.");
    }
  }
}

