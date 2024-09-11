package undecided.erp.relationship.domain.model.personRole.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EmployeeNoTest {

  @Nested
  class OfTest {

    @Test
    void shouldThrowNullPointerExceptionForNullValue() {
      assertThatExceptionOfType(NullPointerException.class)
          .isThrownBy(() -> EmployeeNo.of(null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNonDecimalValue() {
      String value = "abc";
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> EmployeeNo.of(value));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForValuesWithLengthLessThan8() {
      String value = "1234567"; // Length is less than 8
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> EmployeeNo.of(value));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForValuesWithLengthMoreThan8() {
      String value = "123456789"; // Length is more than 8
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> EmployeeNo.of(value));
    }

    @Test
    void shouldCreateEmployeeNoWhenValidValue() {
      String value = "12345678";
      EmployeeNo employeeNo = EmployeeNo.of(value);
      assertThat(employeeNo.value()).isEqualTo(value);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForValuesWithNonNumericCharacters() {
      String value = "123@5678";
      assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
          () -> EmployeeNo.of(value));
    }

  }
}

