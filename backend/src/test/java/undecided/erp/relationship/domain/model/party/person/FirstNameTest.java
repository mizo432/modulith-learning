package undecided.erp.relationship.domain.model.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FirstNameTest {

  @Nested
  class ReconstructTest {

    @Test
    void shouldReturnEmptyFirstName_whenReconstructNullInput() {
      final String input = null;
      final FirstName result = FirstName.reconstruct(input);
      assertThat(result).isEqualTo(FirstName.EMPTY);
    }

    @Test
    void shouldReturnEmptyFirstName_whenReconstructEmptyInput() {
      final String input = "";
      final FirstName result = FirstName.reconstruct(input);
      assertThat(result).isEqualTo(FirstName.EMPTY);
    }

    @Test
    void shouldReturnNewFirstNameWithInput_whenReconstructValidInput() {
      final String input = "John";
      final FirstName result = FirstName.reconstruct(input);
      assertThat(result.getValue()).isEqualTo(input);
    }
  }

  @Nested
  class OfTest {

    @Test
    void shouldThrowNullPointerException_whenOfNullInput() {
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> FirstName.of(null));
    }

    @Test
    void shouldThrowIllegalArgumentException_whenOfEmptyInput() {
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> FirstName.of(""));
    }

    @Test
    void shouldReturnNewFirstNameWithInput_whenOfValidInput() {
      final String input = "John";
      final FirstName result = FirstName.of(input);
      assertThat(result.getValue()).isEqualTo(input);
    }
  }

  @Nested
  class ToStringTest {

    @Test
    void shouldReturnNullAsString_whenToStringEmptyFirstName() {
      final FirstName emptyFirstName = FirstName.reconstruct("");
      final String result = emptyFirstName.toString();
      assertThat(result).isEqualTo("null");
    }

    @Test
    void shouldReturnFirstNameAsString_whenToStringValidFirstName() {
      final String input = "John";
      final FirstName validFirstName = FirstName.reconstruct(input);
      final String result = validFirstName.toString();
      assertThat(result).isEqualTo(input);
    }
  }
}
