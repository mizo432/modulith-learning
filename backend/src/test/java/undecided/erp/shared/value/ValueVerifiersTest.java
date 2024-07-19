package undecided.erp.shared.value;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ValueVerifiersのテスト")
class ValueVerifiersTest {

  @Nested
  @DisplayName("VerifyNotEmpty")
  class VerifyNotEmptyTest {

    @Test
    @DisplayName("verifyNotEmpty should throw exception when ref is empty")
    void shouldThrowExceptionWhenRefIsEmpty() {
      MakeableEmpty ref = () -> true;
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> ValueVerifiers.verifyNotEmpty(ref, IllegalArgumentException::new));
    }

    @Test
    @DisplayName("verifyNotEmpty should return ref when ref is not empty")
    void shouldReturnRefWhenRefIsNotEmpty() {
      MakeableEmpty ref = () -> false;
      MakeableEmpty returnedValue = ValueVerifiers.verifyNotEmpty(ref,
          IllegalArgumentException::new);
      assertThat(returnedValue).isSameAs(ref);
    }
  }

}