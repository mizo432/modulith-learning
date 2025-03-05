package undecided.erp.common.primitive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import static undecided.erp.common.precondition.IntegerPrecondition.checkPositiveOrZero;
import undecided.erp.common.precondition.ObjectPrecondition;
import static undecided.erp.common.primitive.Objects2.isNull;

@UtilityClass
public class Lists2 {

  public static <T> boolean isEmpty(@NonNull List<T> list) {
    return list.isEmpty();

  }

  public static <T> boolean isEmptySilently(List<T> list) {
    return (isNull(list) || list.isEmpty());
  }

  public static <E> boolean isAllElementsNotNull(@NonNull List<E> list) {
    for (E e : list) {
      if (isNull(e)) {
        return false;
      }

    }
    return true;
  }

  public static <T> Stream<@NonNull T> stream(@NonNull List<T> list) {
    return list.stream();
  }

  public static <E> ArrayList<E> newArrayList() {
    return new ArrayList<>();

  }

  @SafeVarargs
  public static <E> ArrayList<E> newArrayList(E... elements) {
    ObjectPrecondition.checkNotNull(elements,
        () -> new IllegalArgumentException("elements is null"));
    int capacity = computeArrayListCapacity(elements.length);
    ArrayList<E> list = new ArrayList<>(capacity);
    Collections.addAll(list, elements);
    return list;
  }

  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
    ObjectPrecondition.checkNotNull(elements,
        () -> new IllegalArgumentException("elements is null")); // for GWT
    return (elements instanceof Collection)
        ? new ArrayList<>((Collection<? extends E>) elements)
        : newArrayList(elements.iterator());
  }

  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
    ArrayList<E> list = newArrayList();
    Iterators.addAll(list, elements);
    return list;
  }

  private static int computeArrayListCapacity(int arraySize) {
    checkPositiveOrZero(arraySize,
        () -> new IllegalArgumentException("arraySize must positive or zero."));

    return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
  }

  public static <E> ArrayList<E> newArrayListWithCapacity(
      int initialArraySize) {
    checkPositiveOrZero(initialArraySize,
        () -> new IllegalArgumentException("initialArraySize must positive or zero"));
    return new ArrayList<>(initialArraySize);
  }
}
