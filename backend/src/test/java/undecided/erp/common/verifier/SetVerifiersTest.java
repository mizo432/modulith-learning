package undecided.erp.common.verifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SetVerifiersTest {

  @Test
  public void verifyNotEmpty_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyNotEmpty_setEmpty_throwsException() {
    Set<String> set = Collections.emptySet();
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetVerifiers.verifyNotEmpty(set,
          () -> new IllegalArgumentException("Set must not be empty"));
    });
  }

  @Test
  public void verifyNotEmpty_setNonEmpty_returnsSet() {
    Set<String> set = new HashSet<>(Collections.singletonList("test"));
    Set<String> returnedSet = SetVerifiers.verifyNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyAllElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyAllElementNotNull_setContainsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetVerifiers.verifyAllElementNotNull(set,
          () -> new IllegalArgumentException("Set must not have null elements"));
    });
  }

  @Test
  public void verifyAllElementNotNull_setDoesNotContainNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", "not null"));
    Set<String> returnedSet = SetVerifiers.verifyAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyAnyElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyAnyElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetVerifiers.verifyAnyElementNotNull(set,
          () -> new IllegalArgumentException("Set must have at least one non-null element"));
    });
  }

  @Test
  public void verifyAnyElementNotNull_setContainsNonNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetVerifiers.verifyAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyOneElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyOneElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetVerifiers.verifyOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }

  @Test
  public void verifyOneElementNotNull_setOneNonNullElement_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetVerifiers.verifyOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    Assertions.assertEquals(set, returnedSet);
  }

  @Test
  public void verifyOneElementNotNull_setMoreThanOneNonNullElement_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("first", "second"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      SetVerifiers.verifyOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }
}
