package undecided.association.relationshipMgmt.spi.party.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("PhoneNo値オブジェクトのテスト")
class PhoneNoTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な電話番号からPhoneNoを正常に作成すること")
    void shouldCreatePhoneNoFromValidString() {
      // Act
      PhoneNo phoneNo = PhoneNo.of("090-1234-5678");

      // Assert
      assertThat(phoneNo).as("PhoneNoインスタンスが作成されること").isNotNull();
      assertThat(phoneNo.value()).as("値が保持されること").isEqualTo("090-1234-5678");
    }

    @Test
    @DisplayName("前後に空白がある電話番号をトリムして作成すること")
    void shouldCreatePhoneNoWithTrimmedValue() {
      // Act
      PhoneNo phoneNo = PhoneNo.of(" 090-1234-5678 ");

      // Assert
      assertThat(phoneNo.value()).as("前後の空白がトリムされること").isEqualTo("090-1234-5678");
    }

    @Test
    @DisplayName("1文字の電話番号も正常に作成すること")
    void shouldCreatePhoneNoWithOneCharacter() {
      // Act
      PhoneNo phoneNo = PhoneNo.of("0");

      // Assert
      assertThat(phoneNo.value()).as("1文字の電話番号が作成されること").isEqualTo("0");
    }

    @Test
    @DisplayName("20文字の電話番号も正常に作成すること")
    void shouldCreatePhoneNoWithMaxLength() {
      // Arrange
      String phoneNo = "09012345678901234567"; // 20文字

      // Act
      PhoneNo pn = PhoneNo.of(phoneNo);

      // Assert
      assertThat(pn.value()).as("20文字の電話番号が作成されること").isEqualTo(phoneNo);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> PhoneNo.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNoIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> PhoneNo.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNoIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> PhoneNo.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("21文字以上の電話番号が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPhoneNoIsTooLong() {
      // Arrange
      String phoneNo = "090123456789012345678"; // 21文字

      // Act & Assert
      assertThatThrownBy(() -> PhoneNo.of(phoneNo))
          .isInstanceOf(IllegalArgumentException.class)
          .as("文字数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のPhoneNoインスタンスが作成されること")
    void shouldCreateEmptyPhoneNo() {
      // Act
      PhoneNo phoneNo = PhoneNo.empty();

      // Assert
      assertThat(phoneNo).as("PhoneNoインスタンスが作成されること").isNotNull();
      assertThat(phoneNo.value()).as("値がnullであること").isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のPhoneNoはisEmptyがtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      PhoneNo phoneNo = PhoneNo.empty();

      // Assert
      assertThat(phoneNo.isEmpty()).as("空のPhoneNoはtrueを返すこと").isTrue();
    }

    @Test
    @DisplayName("値を持つPhoneNoはisEmptyがfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      PhoneNo phoneNo = PhoneNo.of("090-1234-5678");

      // Assert
      assertThat(phoneNo.isEmpty()).as("値を持つPhoneNoはfalseを返すこと").isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のPhoneNoは等しいこと")
    void shouldBeEqualWhenSameValue() {
      // Arrange
      PhoneNo first = PhoneNo.of("090-1234-5678");
      PhoneNo second = PhoneNo.of("090-1234-5678");

      // Assert
      assertThat(first).as("同じ値のPhoneNoは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("異なる値のPhoneNoは等しくないこと")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      PhoneNo first = PhoneNo.of("090-1234-5678");
      PhoneNo second = PhoneNo.of("080-9876-5432");

      // Assert
      assertThat(first).as("異なる値のPhoneNoは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("nullとの等価比較はfalseを返すこと")
    void shouldNotBeEqualToNull() {
      // Arrange
      PhoneNo phoneNo = PhoneNo.of("090-1234-5678");

      // Assert
      assertThat(phoneNo).as("nullとの等価比較はfalseを返すこと").isNotEqualTo(null);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("値を持つPhoneNoのtoStringは値を返すこと")
    void shouldReturnValueWhenToString() {
      // Arrange
      PhoneNo phoneNo = PhoneNo.of("090-1234-5678");

      // Assert
      assertThat(phoneNo.toString()).as("toStringは値を返すこと").isEqualTo("090-1234-5678");
    }

    @Test
    @DisplayName("空のPhoneNoのtoStringはnullを返すこと")
    void shouldReturnNullStringWhenEmpty() {
      // Arrange
      PhoneNo phoneNo = PhoneNo.empty();

      // Assert
      assertThat(phoneNo.toString()).as("空のPhoneNoのtoStringはnullを返すこと").isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("PhoneNoConverterのテスト")
  class PhoneNoConverterTest {

    private final PhoneNo.PhoneNoConverter converter = new PhoneNo.PhoneNoConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("PhoneNoをデータベース用の文字列に変換すること")
      void shouldConvertPhoneNoToDatabaseString() {
        // Arrange
        PhoneNo phoneNo = PhoneNo.of("090-1234-5678");

        // Act
        String dbData = converter.convertToDatabaseColumn(phoneNo);

        // Assert
        assertThat(dbData).as("データベース用の文字列に変換されること").isEqualTo("090-1234-5678");
      }

      @Test
      @DisplayName("nullのPhoneNoはnullに変換すること")
      void shouldConvertNullPhoneNoToNull() {
        // Act
        String dbData = converter.convertToDatabaseColumn(null);

        // Assert
        assertThat(dbData).as("nullのPhoneNoはnullに変換されること").isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("データベース用の文字列をPhoneNoに変換すること")
      void shouldConvertDatabaseStringToPhoneNo() {
        // Arrange
        String dbData = "090-1234-5678";

        // Act
        PhoneNo phoneNo = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(phoneNo).as("PhoneNoに変換されること").isNotNull();
        assertThat(phoneNo.value()).as("値が保持されること").isEqualTo("090-1234-5678");
      }

      @Test
      @DisplayName("nullのデータベースデータは空のPhoneNoに変換すること")
      void shouldConvertNullToEmptyPhoneNo() {
        // Act
        PhoneNo phoneNo = converter.convertToEntityAttribute(null);

        // Assert
        assertThat(phoneNo).as("PhoneNoインスタンスが作成されること").isNotNull();
        assertThat(phoneNo.isEmpty()).as("空のPhoneNoであること").isTrue();
      }
    }
  }
}
