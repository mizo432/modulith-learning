package undecided.erp.common.precondition;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SetPreconditionsTest {

  @Test
  public void checkNotEmpty_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPreconditions.checkNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkNotEmpty_setEmpty_throwsException() {
    Set<String> set = Collections.emptySet();
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetPreconditions.checkNotEmpty(set,
          () -> new IllegalArgumentException("Set must not be empty"));
    });
  }

  @Test
  public void checkNotEmpty_setNonEmpty_returnsSet() {
    Set<String> set = new HashSet<>(Collections.singletonList("test"));
    Set<String> returnedSet = SetPreconditions.checkNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkAllElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPreconditions.checkAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkAllElementNotNull_setContainsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetPreconditions.checkAllElementNotNull(set,
          () -> new IllegalArgumentException("Set must not have null elements"));
    });
  }

  @Test
  public void checkAllElementNotNull_setDoesNotContainNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", "not null"));
    Set<String> returnedSet = SetPreconditions.checkAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkAnyElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPreconditions.checkAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkAnyElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetPreconditions.checkAnyElementNotNull(set,
          () -> new IllegalArgumentException("Set must have at least one non-null element"));
    });
  }

  @Test
  public void checkAnyElementNotNull_setContainsNonNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetPreconditions.checkAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkOneElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPreconditions.checkOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkOneElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetPreconditions.checkOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }

  @Test
  public void checkOneElementNotNull_setOneNonNullElement_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetPreconditions.checkOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void checkOneElementNotNull_setMoreThanOneNonNullElement_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("first", "second"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetPreconditions.checkOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }
}
