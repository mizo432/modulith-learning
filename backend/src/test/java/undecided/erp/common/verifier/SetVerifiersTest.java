package undecided.erp.common.verifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class SetVerifiersTest {

  @Test
  public void verifyNotEmpty_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    assertThat(returnedSet).isNull();
  }

  @Test
  public void verifyNotEmpty_setEmpty_throwsException() {
    Set<String> set = Collections.emptySet();
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetVerifiers.verifyNotEmpty(set,
          () -> new IllegalArgumentException("Set must not be empty"));
    });
  }

  @Test
  public void verifyNotEmpty_setNonEmpty_returnsSet() {
    Set<String> set = new HashSet<>(Collections.singletonList("test"));
    Set<String> returnedSet = SetVerifiers.verifyNotEmpty(set,
        () -> new IllegalArgumentException("Set must not be empty"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void verifyAllElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void verifyAllElementNotNull_setContainsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetVerifiers.verifyAllElementNotNull(set,
          () -> new IllegalArgumentException("Set must not have null elements"));
    });
  }

  @Test
  public void verifyAllElementNotNull_setDoesNotContainNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", "not null"));
    Set<String> returnedSet = SetVerifiers.verifyAllElementNotNull(set,
        () -> new IllegalArgumentException("Set must not have null elements"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void verifyAnyElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void verifyAnyElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetVerifiers.verifyAnyElementNotNull(set,
          () -> new IllegalArgumentException("Set must have at least one non-null element"));
    });
  }

  @Test
  public void verifyAnyElementNotNull_setContainsNonNull_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetVerifiers.verifyAnyElementNotNull(set,
        () -> new IllegalArgumentException("Set must have at least one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void verifyOneElementNotNull_setNull_returnsSet() {
    Set<String> set = null;
    Set<String> returnedSet = SetVerifiers.verifyOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void verifyOneElementNotNull_setAllElementsNull_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList(null, null));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetVerifiers.verifyOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }

  @Test
  public void verifyOneElementNotNull_setOneNonNullElement_returnsSet() {
    Set<String> set = new HashSet<>(Arrays.asList("test", null));
    Set<String> returnedSet = SetVerifiers.verifyOneElementNotNull(set,
        () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    assertThat(returnedSet).isEqualTo(set);
  }

  @Test
  public void verifyOneElementNotNull_setMoreThanOneNonNullElement_throwsException() {
    Set<String> set = new HashSet<>(Arrays.asList("first", "second"));
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
      SetVerifiers.verifyOneElementNotNull(set,
          () -> new IllegalArgumentException("Set must have exactly one non-null element"));
    });
  }
}
