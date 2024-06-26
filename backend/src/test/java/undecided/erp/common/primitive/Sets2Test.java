package undecided.erp.common.primitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class Sets2Test {

  /**
   * Sets2クラスのisEmptyメソッドのテストクラスです。
   * <p>
   * このクラスには、目的のメソッドの実装ロジックを確認するテストスイートが含まれています。
   */
  @Nested
  class IsEmptyTest {

    @Test
    public void withEmptySetShouldReturnTrue() {
      // arrange
      @NonNull Set<Object> set = Collections.emptySet();

      // act
      boolean result = Sets2.isEmpty(set);

      // assert
      assertThat(result)
          .as("isEmpty should return true for an empty Set.")
          .isTrue();
    }

    @Test
    public void withNonEmptySetShouldReturnFalse() {
      // arrange
      var set = new HashSet<>();
      set.add(1);

      // act
      boolean result = Sets2.isEmpty(set);

      // assert
      assertThat(result)
          .as("isEmpty should return false for a non-empty Set.")
          .isFalse();
    }

    @Test
    public void withNullValueShouldThrowException() {
      // arrange
      Set<Object> set = null;

      // act and assert
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> Sets2.isEmpty(set));

    }
  }


  @Nested
  class IsEmptySilentlyTest {

    @Test
    public void withEmptySetShouldReturnTrue() {
      // arrange
      @NonNull Set<Object> set = Collections.emptySet();

      // act
      boolean result = Sets2.isEmptySilently(set);

      // assert
      assertThat(result)
          .as("isEmptySilently should return true for an empty Set.")
          .isTrue();
    }

    @Test
    public void withNonEmptySetShouldReturnFalse() {
      // arrange
      var set = new HashSet<>();
      set.add(1);

      // act
      boolean result = Sets2.isEmptySilently(set);

      // assert
      assertThat(result)
          .as("isEmptySilently should return false for a non-empty Set.")
          .isFalse();
    }

    @Test
    public void withNullValueShouldReturnTrue() {
      // arrange
      Set<Object> set = null;

      // act
      boolean result = Sets2.isEmptySilently(set);

      // assert
      assertThat(result)
          .as("isEmptySilently should return true for a null Set.")
          .isTrue();
    }
  }

  @Nested
  class IsAllElementsNotNullTest {

    @Test
    public void withNonEmptySetShouldReturnTrue() {
      // arrange
      var set = new HashSet<>();
      set.add(1);
      set.add(2);

      // act
      boolean result = Sets2.isAllElementsNotNull(set);

      // assert
      assertThat(result)
          .as("isAllElementsNotNull should return true for a non-empty Set with non-null elements.")
          .isTrue();
    }

    @Test
    public void withEmptySetShouldReturnTrue() {
      // arrange
      @NonNull Set<Object> set = Collections.emptySet();

      // act
      boolean result = Sets2.isAllElementsNotNull(set);

      // assert
      assertThat(result)
          .as("isAllElementsNotNull should return true for an empty Set.")
          .isTrue();
    }

    @Test
    public void withSetContainingNullShouldReturnFalse() {
      // arrange
      var set = new HashSet<>();
      set.add(1);
      set.add(null);

      // act
      boolean result = Sets2.isAllElementsNotNull(set);

      // assert
      assertThat(result)
          .as("isAllElementsNotNull should return false for a set containing null.")
          .isFalse();
    }

  }

  @Nested
  class IntersectionTest {

    @Test
    public void withNonOverlappingSetsShouldReturnEmptySet() {
      // arrange
      Set<Integer> set1 = new HashSet<>();
      set1.add(1);
      set1.add(2);

      Set<Integer> set2 = new HashSet<>();
      set2.add(3);
      set2.add(4);

      // act
      Set<Integer> resultSet = Sets2.intersection(set1, set2);

      // assert
      assertThat(resultSet)
          .as("intersection should return an empty set when there is no overlap.")
          .isEmpty();
    }

    @Test
    public void withOverlappingSetsShouldReturnIntersection() {
      // arrange
      Set<Integer> set1 = new HashSet<>();
      set1.add(1);
      set1.add(2);

      Set<Integer> set2 = new HashSet<>();
      set2.add(2);
      set2.add(3);

      // act
      Set<Integer> resultSet = Sets2.intersection(set1, set2);

      // assert
      assertThat(resultSet)
          .as("intersection should return set of overlapping elements when there is overlap.")
          .containsExactlyInAnyOrder(2);
    }

    @Test
    public void withEmptySetsShouldReturnEmptySet() {
      // arrange
      Set<Integer> set1 = Collections.emptySet();
      Set<Integer> set2 = Collections.emptySet();

      // act
      Set<Integer> resultSet = Sets2.intersection(set1, set2);

      // assert
      assertThat(resultSet)
          .as("intersection should return an empty set when both sets are empty.")
          .isEmpty();
    }

    @Test
    public void withOneEmptySetShouldReturnEmptySet() {
      // arrange
      Set<Integer> set1 = new HashSet<>();
      set1.add(1);
      set1.add(2);

      Set<Integer> set2 = Collections.emptySet();

      // act
      Set<Integer> resultSet = Sets2.intersection(set1, set2);

      // assert
      assertThat(resultSet)
          .as("intersection should return an empty set when one set is empty.")
          .isEmpty();
    }

  }

  @Nested
  class DifferenceTest {

    @Test
    public void withNonOverlappingSetsShouldReturnEqualSet() {
      // Arrange
      Set<Integer> set1 = new HashSet<>();
      set1.add(1);
      set1.add(2);

      Set<Integer> set2 = new HashSet<>();
      set2.add(3);
      set2.add(4);

      // Act
      Set<Integer> resultSet = Sets2.difference(set1, set2);

      // Assert
      assertThat(resultSet)
          .as("Difference should return a set equal to set1 when there is no overlap.")
          .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    public void withOverlappingSetsShouldReturnDifference() {
      // Arrange
      Set<Integer> set1 = new HashSet<>();
      set1.add(1);
      set1.add(2);

      Set<Integer> set2 = new HashSet<>();
      set2.add(2);
      set2.add(3);

      // Act
      Set<Integer> resultSet = Sets2.difference(set1, set2);

      // Assert
      assertThat(resultSet)
          .as("Difference should return set of non-overlapping elements from set1.")
          .containsExactlyInAnyOrder(1);
    }

    @Test
    public void withEmptySetsShouldReturnEmptySet() {
      // Arrange
      Set<Integer> set1 = Collections.emptySet();
      Set<Integer> set2 = Collections.emptySet();

      // Act
      Set<Integer> resultSet = Sets2.difference(set1, set2);

      // Assert
      assertThat(resultSet)
          .as("Difference should return an empty set when both sets are empty.")
          .isEmpty();
    }

    @Test
    public void withOneEmptySetShouldReturnSet1() {
      // Arrange
      Set<Integer> set1 = new HashSet<>();
      set1.add(1);
      set1.add(2);

      Set<Integer> set2 = Collections.emptySet();

      // Act
      Set<Integer> resultSet = Sets2.difference(set1, set2);

      // Assert
      assertThat(resultSet)
          .as("Difference should return a set equal to set1 when set2 is empty.")
          .containsExactlyInAnyOrder(1, 2);
    }
  }

  @Nested
  class ComplementOfTest {

    @Test
    public void complementOfShouldReturnSetWithNonexistentValuesInEnum() {
      // arrange
      EnumSet<TestEnum> collection = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2);

      // act
      EnumSet<TestEnum> resultSet = Sets2.complementOf(collection, TestEnum.class);

      // assert
      assertThat(resultSet)
          .as("complementOf should return a set that contains values ​​not found in the collection.")
          .containsExactlyInAnyOrder(TestEnum.VALUE3);
    }

    @Test
    public void complementOfShouldReturnEmptySetIfEnumValuesFullyRepresented() {
      // arrange
      EnumSet<TestEnum> collection = EnumSet.of(TestEnum.VALUE1, TestEnum.VALUE2, TestEnum.VALUE3);

      // act
      EnumSet<TestEnum> resultSet = Sets2.complementOf(collection, TestEnum.class);

      // assert
      assertThat(resultSet)
          .as("complementOf should return an empty set if all Enum values are represented.")
          .isEmpty();
    }

    @Test
    public void complementOfShouldReturnAllEnumValuesIfCollectionIsEmpty() {
      // arrange
      EnumSet<TestEnum> collection = EnumSet.noneOf(TestEnum.class);

      // act
      EnumSet<TestEnum> resultSet = Sets2.complementOf(collection, TestEnum.class);

      // assert
      assertThat(resultSet)
          .as("complementOf should return a set that contains all Enum values if the collection is empty.")
          .containsExactlyInAnyOrder(TestEnum.VALUE1, TestEnum.VALUE2, TestEnum.VALUE3);
    }

  }

  @Nested
  class StreamTest {

    @Test
    public void streamOfNonNullSetShouldReturnStreamOfElements() {
      // arrange
      @NonNull Set<Object> set = new HashSet<>();
      set.add(1);
      set.add(2);

      // act
      Stream<@NonNull Object> resultStream = Sets2.stream(set);
      List<Object> resultList = resultStream.collect(Collectors.toList());

      // assert
      assertThat(resultList)
          .as("stream should return Stream of elements for a non-null Set.")
          .containsExactlyInAnyOrder(1, 2);
    }

    @Test
    public void streamOfEmptySetShouldReturnEmptyStream() {
      // arrange
      @NonNull Set<Object> set = Collections.emptySet();

      // act
      Stream<Object> resultStream = Sets2.stream(set);
      List<Object> resultList = resultStream.collect(Collectors.toList());

      // assert
      assertThat(resultList)
          .as("stream should return empty Stream for an empty Set.")
          .isEmpty();
    }

    @Test
    public void streamWithNullValueShouldThrowException() {
      // arrange
      Set<Object> set = null;

      // act and assert
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(
          () -> Sets2.stream(set));
    }

  }


  enum TestEnum {
    VALUE1,
    VALUE2,
    VALUE3
  }
}
