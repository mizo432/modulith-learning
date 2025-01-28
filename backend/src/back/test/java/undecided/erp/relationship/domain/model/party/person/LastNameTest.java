package undecided.erp.relationship.domain.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LastNameTest {

  @Test
  void of_givenValidInput_shouldCreateLastNameObject() {
    // Arrange
    String value = "Smith";

    // Act
    LastName result = LastName.of(value);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getValue()).isEqualTo(value);
  }

  @Test
  void of_givenNullInput_shouldThrowException() {
    // Arrange
    String value = null;
    Exception exception = null;

    // Act
    try {
      LastName.of(value);
    } catch (Exception e) {
      exception = e;
    }

    // Assert
    assertThat(exception).isNotNull();
    assertThat(exception instanceof NullPointerException).isTrue();
    assertThat(exception.getMessage()).isEqualTo("value is marked non-null but is null");
  }

  @Test
  void of_givenEmptyInput_shouldThrowException() {
    // Arrange
    String value = "";
    Exception exception = null;

    // Act
    try {
      LastName.of(value);
    } catch (Exception e) {
      exception = e;
    }

    // Assert
    assertThat(exception).isNotNull();
    assertThat(exception instanceof IllegalArgumentException).isTrue();
    assertThat(exception.getMessage()).isEqualTo("value is not empty");
  }

  @Test
  void reconstruct_givenNullInput_shouldReturnEmptyLastNameObject() {
    // Arrange
    String value = null;

    // Act
    LastName result = LastName.reconstruct(value);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isSameAs(LastName.EMPTY);
  }

  @Test
  void reconstruct_givenEmptyInput_shouldReturnEmptyLastNameObject() {
    // Arrange
    String value = "";

    // Act
    LastName result = LastName.reconstruct(value);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result).isSameAs(LastName.EMPTY);
  }

  @Test
  void reconstruct_givenValidInput_shouldReturnLastNameObjectWithGivenValue() {
    // Arrange
    String value = "Doe";

    // Act
    LastName result = LastName.reconstruct(value);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getValue()).isEqualTo(value);
  }

  @Test
  void toString_givenNonEmptyValue_returnsValue() {
    // Arrange
    String value = "Johnson";

    // Act
    LastName lastName = LastName.of(value);

    // Assert
    assertThat(lastName.toString()).isEqualTo(value);
  }

  @Test
  void toString_givenNullValue_returnsNullString() {
    // Arrange
    LastName lastName = LastName.EMPTY;

    // Act
    String returnValue = lastName.toString();

    // Assert
    assertThat(returnValue).isEqualTo("null");
  }
}
