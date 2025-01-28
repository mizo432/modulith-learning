package undecided.erp.common.primitive;

import java.util.Collection;
import java.util.Iterator;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import undecided.erp.common.annotation.CanIgnoreReturnValue;

@UtilityClass
public class Iterators {

  @CanIgnoreReturnValue
  public static <T> boolean addAll(
      @NonNull Collection<T> addTo, @NonNull Iterator<? extends T> iterator) {
    boolean wasModified = false;
    while (iterator.hasNext()) {
      wasModified |= addTo.add(iterator.next());
    }
    return wasModified;
  }
}
