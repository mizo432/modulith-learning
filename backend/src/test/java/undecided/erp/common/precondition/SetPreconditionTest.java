package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class SetPreconditionTest {

  @Test
  public void checkNotEmpty_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPrecondition.checkNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    assertThat(returnedSet).isNull();
  }

  @Test
  public void checkNotEmpty_setEmpty_throwsException() {
    Set<String> set = Collections.emptySet();
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetPrecondition.checkNotEmpty(set,
          () -> new IllegalArgumentException("Set must not be empty"));
    });
  }

  @Test
  public void checkNotEmpty_setNonEmpty_returnsSet() {
    Set<String> set = new HashSet<>(Collections.singletonList("test"));
    Set<String> returnedSet = SetPrecondition.checkNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void checkAllElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPrecondition.checkAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void checkAllElementNotNull_setContainsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetPrecondition.checkAllElementNotNull(set,
          () -> new IllegalArgumentException("Set must not have null elements"));
    });
  }

  @Test
  public void checkAllElementNotNull_setDoesNotContainNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", "not null"));
    Set<String> returnedSet = SetPrecondition.checkAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void checkAnyElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPrecondition.checkAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void checkAnyElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetPrecondition.checkAnyElementNotNull(set,
          () -> new IllegalArgumentException("Set must have at least one non-null element"));
    });
  }

  @Test
  public void checkAnyElementNotNull_setContainsNonNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetPrecondition.checkAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void checkOneElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetPrecondition.checkOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void checkOneElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetPrecondition.checkOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }

  @Test
  public void checkOneElementNotNull_setOneNonNullElement_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetPrecondition.checkOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void checkOneElementNotNull_setMoreThanOneNonNullElement_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("first", "second"));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetPrecondition.checkOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }
}
