package undecided.association.relationshipMgmt.personRole.spi.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("FullNameの単体テスト")
class FullNameTest {

  @Nested
  @DisplayName("of メソッドのテスト")
  class OfTest {

    @Test
    @DisplayName("有効な全角文字が渡された場合、FullNameインスタンスを返すこと")
    void shouldReturnFullNameWhenValidValue() {
      // Arrange
      String value = "山田太郎";

      // Act
      FullName fullName = FullName.of(value);

      // Assert
      assertThat(fullName).isNotNull();
      assertThat(fullName.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> FullName.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("FullName cannot be null");
    }

    @Test
    @DisplayName("255文字を超える値が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueExceedsMaxLength() {
      // Arrange
      String value = "あ".repeat(256); // 256文字

      // Act & Assert
      assertThatThrownBy(() -> FullName.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("FullName cannot exceed 255 characters");
    }

    @Test
    @DisplayName("半角文字が含まれる値が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueContainsHalfWidthCharacters() {
      // Arrange
      String value = "山田Taro"; // 半角英字を含む

      // Act & Assert
      assertThatThrownBy(() -> FullName.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("FullName must match the pattern");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueIsEmpty() {
      // Arrange
      String value = "";

      // Act & Assert
      assertThatThrownBy(() -> FullName.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("FullName must match the pattern");
    }

    @Test
    @DisplayName("255文字の全角文字が渡された場合、FullNameインスタンスを返すこと")
    void shouldReturnFullNameWhenMaxLengthValue() {
      // Arrange
      String value = "あ".repeat(255); // 255文字

      // Act
      FullName fullName = FullName.of(value);

      // Assert
      assertThat(fullName).isNotNull();
      assertThat(fullName.getValue()).isEqualTo(value);
    }
  }

  @Nested
  @DisplayName("getValue メソッドのテスト")
  class GetValueTest {

    @Test
    @DisplayName("設定された値を返すこと")
    void shouldReturnSetValue() {
      // Arrange
      String value = "山田太郎";
      FullName fullName = FullName.of(value);

      // Act
      String actual = fullName.getValue();

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
      String value = "山田太郎";
      FullName fullName = FullName.of(value);

      // Act
      String actual = fullName.toString();

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
      FullName fullName = FullName.of("山田太郎");

      // Act & Assert
      assertThat(fullName.equals(fullName)).isTrue();
    }

    @Test
    @DisplayName("nullと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenComparedWithNull() {
      // Arrange
      FullName fullName = FullName.of("山田太郎");

      // Act & Assert
      assertThat(fullName.equals(null)).isFalse();
    }

    @Test
    @DisplayName("異なるクラスと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenComparedWithDifferentClass() {
      // Arrange
      FullName fullName = FullName.of("山田太郎");

      // Act & Assert
      assertThat(fullName.equals("山田太郎")).isFalse();
    }

    @Test
    @DisplayName("同じ値を持つインスタンスと比較した場合、trueを返すこと")
    void shouldReturnTrueWhenSameValue() {
      // Arrange
      FullName fullName1 = FullName.of("山田太郎");
      FullName fullName2 = FullName.of("山田太郎");

      // Act & Assert
      assertThat(fullName1.equals(fullName2)).isTrue();
    }

    @Test
    @DisplayName("異なる値を持つインスタンスと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenDifferentValue() {
      // Arrange
      FullName fullName1 = FullName.of("山田太郎");
      FullName fullName2 = FullName.of("鈴木花子");

      // Act & Assert
      assertThat(fullName1.equals(fullName2)).isFalse();
    }
  }

  @Nested
  @DisplayName("hashCode メソッドのテスト")
  class HashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは同じhashCodeを返すこと")
    void shouldReturnSameHashCodeWhenSameValue() {
      // Arrange
      FullName fullName1 = FullName.of("山田太郎");
      FullName fullName2 = FullName.of("山田太郎");

      // Act & Assert
      assertThat(fullName1.hashCode()).isEqualTo(fullName2.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは異なるhashCodeを返すこと")
    void shouldReturnDifferentHashCodeWhenDifferentValue() {
      // Arrange
      FullName fullName1 = FullName.of("山田太郎");
      FullName fullName2 = FullName.of("鈴木花子");

      // Act & Assert
      assertThat(fullName1.hashCode()).isNotEqualTo(fullName2.hashCode());
    }
  }
}
