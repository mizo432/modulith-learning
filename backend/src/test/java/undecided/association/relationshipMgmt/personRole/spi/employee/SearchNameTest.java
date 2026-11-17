package undecided.association.relationshipMgmt.personRole.spi.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SearchNameの単体テスト")
class SearchNameTest {

  @Nested
  @DisplayName("of メソッドのテスト")
  class OfTest {

    @Test
    @DisplayName("有効な全角文字が渡された場合、SearchNameインスタンスを返すこと")
    void shouldReturnSearchNameWhenValidValue() {
      // Arrange
      String value = "山田太郎";

      // Act
      SearchName searchName = SearchName.of(value);

      // Assert
      assertThat(searchName).isNotNull();
      assertThat(searchName.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> SearchName.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("SearchName cannot be null");
    }

    @Test
    @DisplayName("50文字を超える値が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueExceedsMaxLength() {
      // Arrange
      String value = "あ".repeat(51); // 51文字

      // Act & Assert
      assertThatThrownBy(() -> SearchName.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("SearchName cannot exceed 50 characters");
    }

    @Test
    @DisplayName("半角文字が含まれる値が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueContainsHalfWidthCharacters() {
      // Arrange
      String value = "山田Taro"; // 半角英字を含む

      // Act & Assert
      assertThatThrownBy(() -> SearchName.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("SearchName must match the pattern");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenValueIsEmpty() {
      // Arrange
      String value = "";

      // Act & Assert
      assertThatThrownBy(() -> SearchName.of(value))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("SearchName must match the pattern");
    }

    @Test
    @DisplayName("50文字の全角文字が渡された場合、SearchNameインスタンスを返すこと")
    void shouldReturnSearchNameWhenMaxLengthValue() {
      // Arrange
      String value = "あ".repeat(50); // 50文字

      // Act
      SearchName searchName = SearchName.of(value);

      // Assert
      assertThat(searchName).isNotNull();
      assertThat(searchName.getValue()).isEqualTo(value);
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
      SearchName searchName = SearchName.of(value);

      // Act
      String actual = searchName.getValue();

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
      SearchName searchName = SearchName.of(value);

      // Act
      String actual = searchName.toString();

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
      SearchName searchName = SearchName.of("山田太郎");

      // Act & Assert
      assertThat(searchName.equals(searchName)).isTrue();
    }

    @Test
    @DisplayName("nullと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenComparedWithNull() {
      // Arrange
      SearchName searchName = SearchName.of("山田太郎");

      // Act & Assert
      assertThat(searchName.equals(null)).isFalse();
    }

    @Test
    @DisplayName("異なるクラスと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenComparedWithDifferentClass() {
      // Arrange
      SearchName searchName = SearchName.of("山田太郎");

      // Act & Assert
      assertThat(searchName.equals("山田太郎")).isFalse();
    }

    @Test
    @DisplayName("同じ値を持つインスタンスと比較した場合、trueを返すこと")
    void shouldReturnTrueWhenSameValue() {
      // Arrange
      SearchName searchName1 = SearchName.of("山田太郎");
      SearchName searchName2 = SearchName.of("山田太郎");

      // Act & Assert
      assertThat(searchName1.equals(searchName2)).isTrue();
    }

    @Test
    @DisplayName("異なる値を持つインスタンスと比較した場合、falseを返すこと")
    void shouldReturnFalseWhenDifferentValue() {
      // Arrange
      SearchName searchName1 = SearchName.of("山田太郎");
      SearchName searchName2 = SearchName.of("鈴木花子");

      // Act & Assert
      assertThat(searchName1.equals(searchName2)).isFalse();
    }
  }

  @Nested
  @DisplayName("hashCode メソッドのテスト")
  class HashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは同じhashCodeを返すこと")
    void shouldReturnSameHashCodeWhenSameValue() {
      // Arrange
      SearchName searchName1 = SearchName.of("山田太郎");
      SearchName searchName2 = SearchName.of("山田太郎");

      // Act & Assert
      assertThat(searchName1.hashCode()).isEqualTo(searchName2.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは異なるhashCodeを返すこと")
    void shouldReturnDifferentHashCodeWhenDifferentValue() {
      // Arrange
      SearchName searchName1 = SearchName.of("山田太郎");
      SearchName searchName2 = SearchName.of("鈴木花子");

      // Act & Assert
      assertThat(searchName1.hashCode()).isNotEqualTo(searchName2.hashCode());
    }
  }
}
