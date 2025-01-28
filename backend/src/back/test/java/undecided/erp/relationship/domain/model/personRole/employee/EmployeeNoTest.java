package undecided.erp.relationship.domain.model.personRole.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EmployeeNoTest {

  @DisplayName("EmployeeNoのテスト")
  @Nested
  class OfTest {

    @DisplayName("値がnullの場合")
    @Test
    void shouldThrowExceptionWhenValueIsNull() {
      assertThatThrownBy(() -> EmployeeNo.of(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessage("value is marked non-null but is null");
    }

    @DisplayName("値が数字でない場合")
    @Test
    void shouldThrowExceptionWhenValueIsNotDecimal() {
      assertThatThrownBy(() -> EmployeeNo.of("ABCDEFGH"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("value must be decimal.");
    }

    @DisplayName("値の長さが8でない場合")
    @Test
    void shouldThrowExceptionWhenValueLengthIsNotEight() {
      assertThatThrownBy(() -> EmployeeNo.of("123456789"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("value length");
    }

    @DisplayName("値が正常な場合")
    @Test
    void shouldReturnEmployeeNoWhenValueIsValid() {
      EmployeeNo employeeNo = EmployeeNo.of("12345678");
      assertThat(employeeNo.value()).isEqualTo("12345678");
    }
  }

  @DisplayName("Reconstructメソッドのテスト")
  @Nested
  class ReconstructTest {

    @DisplayName("引数がnullの場合、EMPTYが返される")
    @Test
    void shouldReturnEmptyWhenArgumentIsNull() {
      assertThat(EmployeeNo.reconstruct(null)).isEqualTo(EmployeeNo.EMPTY);
    }

    @DisplayName("引数が空文字列の場合、EMPTYが返される")
    @Test
    void shouldReturnEmptyWhenArgumentIsEmpty() {
      assertThat(EmployeeNo.reconstruct("")).isEqualTo(EmployeeNo.EMPTY);
    }

    @DisplayName("引数が数字以外の文字列の場合、IllegalArgumentExceptionが送出される")
    @Test
    void shouldThrowExceptionWhenArgumentIsNonNumeric() {
      assertThatThrownBy(() -> EmployeeNo.reconstruct("ABCDEFGH"))
          .isInstanceOf(NumberFormatException.class);
    }

    @DisplayName("引数が数字の文字列の場合、その文字列をvalueとしたEmployeeNoが返される")
    @Test
    void shouldReturnEmployeeNoWhenArgumentIsNumeric() {
      EmployeeNo employeeNo = EmployeeNo.reconstruct("12345678");
      assertThat(employeeNo.value()).isEqualTo("12345678");
    }
  }
}
