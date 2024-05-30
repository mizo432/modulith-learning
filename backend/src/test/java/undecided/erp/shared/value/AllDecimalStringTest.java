package undecided.erp.shared.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class AllDecimalStringTest {

  @Test
  void shouldThrowExceptionForLetterCharacter() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> AllDecimalString.of("abc"));
  }

  @Test
  void shouldThrowExceptionForZero() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> AllDecimalString.of("0"));
  }

  @Test
  void shouldCreateAllDecimalCodeForBigNumber() {
    assertThat("99999999999999999").isEqualTo(AllDecimalString.of("99999999999999999").value());
  }


  @Test
  void shouldThrowExceptionForNegativeNumber() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> AllDecimalString.of("-1"));
  }

  @Test
  void shouldThrowExceptionForNonNumericString() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> AllDecimalString.of("Hello"));
  }

  @Test
  void shouldCreateAllDecimalCodeForSmallNumber() {
    assertThat("1").isEqualTo(AllDecimalString.of("1").value());
  }
}
