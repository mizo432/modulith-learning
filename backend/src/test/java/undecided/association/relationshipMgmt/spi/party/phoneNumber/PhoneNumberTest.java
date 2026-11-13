package undecided.association.relationshipMgmt.spi.party.phoneNumber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("PhoneNumber値オブジェクトのテスト")
class PhoneNumberTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な電話番号からPhoneNumberを正常に作成すること")
    void shouldCreatePhoneNumberFromValidString() {
      // Act
      PhoneNumber phoneNumber = PhoneNumber.of("090-1234-5678");

      // Assert
      assertThat(phoneNumber).as("PhoneNumberインスタンスが作成されること").isNotNull();
      assertThat(phoneNumber.value()).as("値が保持されること").isEqualTo("090-1234-5678");
    }

    @Test
    @DisplayName("前後に空白がある電話番号をトリムして作成すること")
    void shouldCreatePhoneNumberWithTrimmedValue() {
      // Act
      PhoneNumber phoneNumber = PhoneNumber.of(" 090-1234-5678 ");

      // Assert
      assertThat(phoneNumber.value()).as("前後の空白がトリムされること").isEqualTo("090-1234-5678");
    }

    @Test
    @DisplayName("1文字の電話番号も正常に作成すること")
    void shouldCreatePhoneNumberWithOneCharacter() {
      // Act
      PhoneNumber phoneNumber = PhoneNumber.of("0");

      // Assert
      assertThat(phoneNumber.value()).as("1文字の電話番号が作成されること").isEqualTo("0");
    }

    @Test
    @DisplayName("20文字の電話番号も正常に作成すること")
    void shouldCreatePhoneNumberWithMaxLength() {
      // Arrange
      String phoneNumber = "09012345678901234567"; // 20文字

      // Act
      PhoneNumber pn = PhoneNumber.of(phoneNumber);

      // Assert
      assertThat(pn.value()).as("20文字の電話番号が作成されること").isEqualTo(phoneNumber);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNumberIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> PhoneNumber.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNumberIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> PhoneNumber.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNumberIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> PhoneNumber.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("21文字以上の電話番号が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNumberIsTooLong() {
      // Arrange
      String phoneNumber = "090123456789012345678"; // 21文字

      // Act & Assert
      assertThatThrownBy(() -> PhoneNumber.of(phoneNumber))
          .isInstanceOf(IllegalArgumentException.class)
          .as("文字数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のPhoneNumberインスタンスが作成されること")
    void shouldCreateEmptyPhoneNumber() {
      // Act
      PhoneNumber phoneNumber = PhoneNumber.empty();

      // Assert
      assertThat(phoneNumber).as("PhoneNumberインスタンスが作成されること").isNotNull();
      assertThat(phoneNumber.value()).as("値がnullであること").isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のPhoneNumberはisEmptyがtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      PhoneNumber phoneNumber = PhoneNumber.empty();

      // Assert
      assertThat(phoneNumber.isEmpty()).as("空のPhoneNumberはtrueを返すこと").isTrue();
    }

    @Test
    @DisplayName("値を持つPhoneNumberはisEmptyがfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      PhoneNumber phoneNumber = PhoneNumber.of("090-1234-5678");

      // Assert
      assertThat(phoneNumber.isEmpty()).as("値を持つPhoneNumberはfalseを返すこと").isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のPhoneNumberは等しいこと")
    void shouldBeEqualWhenSameValue() {
      // Arrange
      PhoneNumber first = PhoneNumber.of("090-1234-5678");
      PhoneNumber second = PhoneNumber.of("090-1234-5678");

      // Assert
      assertThat(first).as("同じ値のPhoneNumberは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("異なる値のPhoneNumberは等しくないこと")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      PhoneNumber first = PhoneNumber.of("090-1234-5678");
      PhoneNumber second = PhoneNumber.of("080-9876-5432");

      // Assert
      assertThat(first).as("異なる値のPhoneNumberは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("nullとの等価比較はfalseを返すこと")
    void shouldNotBeEqualToNull() {
      // Arrange
      PhoneNumber phoneNumber = PhoneNumber.of("090-1234-5678");

      // Assert
      assertThat(phoneNumber).as("nullとの等価比較はfalseを返すこと").isNotEqualTo(null);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("値を持つPhoneNumberのtoStringは値を返すこと")
    void shouldReturnValueWhenToString() {
      // Arrange
      PhoneNumber phoneNumber = PhoneNumber.of("090-1234-5678");

      // Assert
      assertThat(phoneNumber.toString())
          .as("toStringは値を返すこと")
          .isEqualTo("090-1234-5678");
    }

    @Test
    @DisplayName("空のPhoneNumberのtoStringはnullを返すこと")
    void shouldReturnNullStringWhenEmpty() {
      // Arrange
      PhoneNumber phoneNumber = PhoneNumber.empty();

      // Assert
      assertThat(phoneNumber.toString()).as("空のPhoneNumberのtoStringはnullを返すこと").isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("PhoneNumberConverterのテスト")
  class PhoneNumberConverterTest {

    private final PhoneNumber.PhoneNumberConverter converter = new PhoneNumber.PhoneNumberConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("PhoneNumberをデータベース用の文字列に変換すること")
      void shouldConvertPhoneNumberToDatabaseString() {
        // Arrange
        PhoneNumber phoneNumber = PhoneNumber.of("090-1234-5678");

        // Act
        String dbData = converter.convertToDatabaseColumn(phoneNumber);

        // Assert
        assertThat(dbData).as("データベース用の文字列に変換されること").isEqualTo("090-1234-5678");
      }

      @Test
      @DisplayName("nullのPhoneNumberはnullに変換すること")
      void shouldConvertNullPhoneNumberToNull() {
        // Act
        String dbData = converter.convertToDatabaseColumn(null);

        // Assert
        assertThat(dbData).as("nullのPhoneNumberはnullに変換されること").isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("データベース用の文字列をPhoneNumberに変換すること")
      void shouldConvertDatabaseStringToPhoneNumber() {
        // Arrange
        String dbData = "090-1234-5678";

        // Act
        PhoneNumber phoneNumber = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(phoneNumber).as("PhoneNumberに変換されること").isNotNull();
        assertThat(phoneNumber.value()).as("値が保持されること").isEqualTo("090-1234-5678");
      }

      @Test
      @DisplayName("nullのデータベースデータは空のPhoneNumberに変換すること")
      void shouldConvertNullToEmptyPhoneNumber() {
        // Act
        PhoneNumber phoneNumber = converter.convertToEntityAttribute(null);

        // Assert
        assertThat(phoneNumber).as("PhoneNumberインスタンスが作成されること").isNotNull();
        assertThat(phoneNumber.isEmpty()).as("空のPhoneNumberであること").isTrue();
      }
    }
  }
}
