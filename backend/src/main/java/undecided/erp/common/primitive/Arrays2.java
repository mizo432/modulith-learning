package undecided.erp.common.primitive;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Arrays2 {

  public static <T> boolean isEmpty(@NonNull T[] array) {
    return array.length == 0;
  }

  public static <T> boolean isEmptySilently(T[] array) {
    return (array == null || array.length == 0);
  }

  /**
   * This method verifies if all elements of the given array are non-null.
   *
   * @param array the array to check
   * @return true if all elements are non-null; false otherwise
   */
  public static <E> boolean allElementsNotNull(@NonNull E[] array) {
    for (E e : array) {
      if (e == null) {
        return false;
      }

    }
    return true;
  }

  /**
   * This method takes an array as input and returns a stream of non-null elements.
   *
   * @param array the array from which the stream is to be created
   * @param <T> the type of elements in the array
   * @return a stream of non-null elements from the input array
   */
  public static <T> Stream<@NonNull T> stream(@NonNull T[] array) {
    return Arrays.stream(array);
  }
}
