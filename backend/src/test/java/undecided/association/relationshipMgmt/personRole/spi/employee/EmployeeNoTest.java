package undecided.association.relationshipMgmt.personRole.spi.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("EmployeeNoの単体テスト")
class EmployeeNoTest {

  @Nested
  @DisplayName("of メソッドのテスト")
  class OfTest {

    @Test
    @DisplayName("有効な10桁の数字が渡された場合、EmployeeNoインスタンスを返すこと")
    void shouldReturnEmployeeNoWhenValidValue() {
      // Arrange
      String value = "1234567890";

      // Act
      EmployeeNo employeeNo = EmployeeNo.of(value);

      // Assert
      assertThat(employeeNo).isNotNull();
      assertThat(employeeNo.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> EmployeeNo.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("EmployeeNo cannot be null");
    }

    @Test
    @DisplayName("10文字を超える値が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueExceedsMaxLength() {
      // Arrange
      String value = "12345678901"; // 11文字

      // Act & Assert
      assertThatThrownBy(() -> EmployeeNo.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("EmployeeNo cannot exceed 10 characters");
    }

    @Test
    @DisplayName("正規表現にマッチしない値が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueDoesNotMatchRegex() {
      // Arrange
      String value = "123456789a"; // 英字を含む

      // Act & Assert
      assertThatThrownBy(() -> EmployeeNo.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("EmployeeNo must match the pattern ^[0-9]{10}$");
    }

    @Test
    @DisplayName("10文字未満の数字が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueIsShorterThan10Digits() {
      // Arrange
      String value = "12345"; // 5文字

      // Act & Assert
      assertThatThrownBy(() -> EmployeeNo.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("EmployeeNo must match the pattern ^[0-9]{10}$");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueIsEmpty() {
      // Arrange
      String value = "";

      // Act & Assert
      assertThatThrownBy(() -> EmployeeNo.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("EmployeeNo must match the pattern ^[0-9]{10}$");
    }
  }

  @Nested
  @DisplayName("getValue メソッドのテスト")
  class GetValueTest {

    @Test
    @DisplayName("設定された値を返すこと")
    void shouldReturnSetValue() {
      // Arrange
      String value = "1234567890";
      EmployeeNo employeeNo = EmployeeNo.of(value);

      // Act
      String actual = employeeNo.getValue();

      // Assert
      assertThat(actual).isEqualTo(value);
    }
  }

  @Nested
  @DisplayName("toString メソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値の文字列表現を返すこと")
    void shouldReturnStringValue() {
      // Arrange
      String value = "1234567890";
      EmployeeNo employeeNo = EmployeeNo.of(value);

      // Act
      String actual = employeeNo.toString();

      // Assert
      assertThat(actual).isEqualTo(value);
    }
  }

  @Nested
  @DisplayName("equals メソッドのテスト")
  class EqualsTest {

    @Test
    @DisplayName("同じインスタンスの場合、trueを返すこと")
    void shouldReturnTrueWhenSameInstance() {
      // Arrange
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");

      // Act & Assert
      assertThat(employeeNo.equals(employeeNo)).isTrue();
    }

    @Test
    @DisplayName("nullと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenComparedWithNull() {
      // Arrange
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");

      // Act & Assert
      assertThat(employeeNo.equals(null)).isFalse();
    }

    @Test
    @DisplayName("異なるクラスと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenComparedWithDifferentClass() {
      // Arrange
      EmployeeNo employeeNo = EmployeeNo.of("1234567890");

      // Act & Assert
      assertThat(employeeNo.equals("1234567890")).isFalse();
    }

    @Test
    @DisplayName("同じ値を持つインスタンスと比較した場合、trueを返すこと")
    void shouldReturnTrueWhenSameValue() {
      // Arrange
      EmployeeNo employeeNo1 = EmployeeNo.of("1234567890");
      EmployeeNo employeeNo2 = EmployeeNo.of("1234567890");

      // Act & Assert
      assertThat(employeeNo1.equals(employeeNo2)).isTrue();
    }

    @Test
    @DisplayName("異なる値を持つインスタンスと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenDifferentValue() {
      // Arrange
      EmployeeNo employeeNo1 = EmployeeNo.of("1234567890");
      EmployeeNo employeeNo2 = EmployeeNo.of("0987654321");

      // Act & Assert
      assertThat(employeeNo1.equals(employeeNo2)).isFalse();
    }
  }

  @Nested
  @DisplayName("hashCode メソッドのテスト")
  class HashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは同じhashCodeを返すこと")
    void shouldReturnSameHashCodeWhenSameValue() {
      // Arrange
      EmployeeNo employeeNo1 = EmployeeNo.of("1234567890");
      EmployeeNo employeeNo2 = EmployeeNo.of("1234567890");

      // Act & Assert
      assertThat(employeeNo1.hashCode()).isEqualTo(employeeNo2.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは異なるhashCodeを返すこと")
    void shouldReturnDifferentHashCodeWhenDifferentValue() {
      // Arrange
      EmployeeNo employeeNo1 = EmployeeNo.of("1234567890");
      EmployeeNo employeeNo2 = EmployeeNo.of("0987654321");

      // Act & Assert
      assertThat(employeeNo1.hashCode()).isNotEqualTo(employeeNo2.hashCode());
    }
  }
}
