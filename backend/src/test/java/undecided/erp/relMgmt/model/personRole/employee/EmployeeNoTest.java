package undecided.erp.relMgmt.model.personRole.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class EmployeeNoTest {

  @Test
  void testOf_method_with_Null_Value() {
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> EmployeeNo.of(null));
  }

  @Test
  void testOf_method_with_Non_Decimal_Value() {
    String value = "abc";
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> EmployeeNo.of(value));
  }

  @Test
  void testOf_method_with_Length_Less_Than_8_Value() {
    String value = "1234567"; // Length is less than 8
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> EmployeeNo.of(value));
  }

  @Test
  void testOf_method_with_Length_More_Than_8_Value() {
    String value = "123456789"; // Length is more than 8
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> EmployeeNo.of(value));
  }

  @Test
  void testOf_method_with_Valid_Value() {
    String value = "12345678";
    EmployeeNo employeeNo = EmployeeNo.of(value);
    assertThat(employeeNo.value()).isEqualTo(value);
  }

  @Test
  void testOf_method_with_Non_Numeric_Character() {
    String value = "123@5678";
    assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
        () -> EmployeeNo.of(value));
  }
}

