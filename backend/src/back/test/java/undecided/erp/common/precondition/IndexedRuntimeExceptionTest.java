package undecided.erp.common.precondition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IndexedRuntimeExceptionTest {

  /**
   * Constructor.
   * <p>
   * Method under test: {@link IndexedRuntimeException#IndexedRuntimeException(Integer, Throwable)}
   */
  @Test
  @DisplayName("Constructor")
  void constructor() {
    // Arrange
    Throwable cause = new Throwable();

    // Act
    IndexedRuntimeException actualIndexedRuntimeException = new IndexedRuntimeException(1, cause);

    // Assert
    assertThat(actualIndexedRuntimeException.getMessage()).isEqualTo("java.lang.Throwable");
    assertThat(actualIndexedRuntimeException.getSuppressed().length).isEqualTo(0);
    assertThat(actualIndexedRuntimeException.getIndex().intValue()).isEqualTo(1);
    assertThat(actualIndexedRuntimeException.getCause()).isSameAs(cause);
  }

  /**
   * Constructor.
   * <p>
   * Method under test: {@link IndexedRuntimeException#IndexedRuntimeException(String, Integer)}
   */
  @Test
  @DisplayName("Constructor")
  void constructor2() {
    // Arrange and Act
    IndexedRuntimeException actualIndexedRuntimeException = new IndexedRuntimeException(
        "An error occurred", 1);

    // Assert
    assertThat(actualIndexedRuntimeException.getLocalizedMessage()).isEqualTo(
        "Index:1 An error occurred");
    assertThat(actualIndexedRuntimeException.getMessage()).isEqualTo("Index:1 An error occurred");
    assertThat(actualIndexedRuntimeException.getCause()).isNull();
    assertThat(actualIndexedRuntimeException.getSuppressed().length).isEqualTo(0);
    assertThat(actualIndexedRuntimeException.getIndex().intValue()).isEqualTo(1);
  }

  /**
   * Constructor.
   * <p>
   * Method under test:
   * {@link IndexedRuntimeException#IndexedRuntimeException(String, Integer, Throwable)}
   */
  @Test
  @DisplayName("Constructor")
  void constructor3() {
    // Arrange
    Throwable cause = new Throwable();

    // Act
    IndexedRuntimeException actualIndexedRuntimeException = new IndexedRuntimeException(
        "An error occurred", 1, cause);

    // Assert
    assertThat(actualIndexedRuntimeException.getLocalizedMessage()).isEqualTo(
        "Index:1 An error occurred");
    assertThat(actualIndexedRuntimeException.getMessage()).isEqualTo("Index:1 An error occurred");
    assertThat(actualIndexedRuntimeException.getSuppressed().length).isEqualTo(0);
    assertThat(actualIndexedRuntimeException.getIndex().intValue()).isEqualTo(1);
    assertThat(actualIndexedRuntimeException.getCause()).isSameAs(cause);
  }
}
