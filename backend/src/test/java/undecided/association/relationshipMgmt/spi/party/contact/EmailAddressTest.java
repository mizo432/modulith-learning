package undecided.association.relationshipMgmt.spi.party.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("EmailAddress値オブジェクトのテスト")
class EmailAddressTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効なメールアドレスからEmailAddressを正常に作成すること")
    void shouldCreateEmailAddressFromValidString() {
      // Act
      EmailAddress emailAddress = EmailAddress.of("test@example.com");

      // Assert
      assertThat(emailAddress).as("EmailAddressインスタンスが作成されること").isNotNull();
      assertThat(emailAddress.value()).as("値が保持されること").isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("前後に空白があるメールアドレスをトリムして作成すること")
    void shouldCreateEmailAddressWithTrimmedValue() {
      // Act
      EmailAddress emailAddress = EmailAddress.of(" test@example.com ");

      // Assert
      assertThat(emailAddress.value()).as("前後の空白がトリムされること").isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("1文字のメールアドレスも正常に作成すること")
    void shouldCreateEmailAddressWithOneCharacter() {
      // Act
      EmailAddress emailAddress = EmailAddress.of("a");

      // Assert
      assertThat(emailAddress.value()).as("1文字のメールアドレスが作成されること").isEqualTo("a");
    }

    @Test
    @DisplayName("200文字のメールアドレスも正常に作成すること")
    void shouldCreateEmailAddressWithMaxLength() {
      // Arrange
      String email = "a".repeat(198) + "@b"; // 200文字

      // Act
      EmailAddress emailAddress = EmailAddress.of(email);

      // Assert
      assertThat(emailAddress.value()).as("200文字のメールアドレスが作成されること").isEqualTo(email);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenEmailIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> EmailAddress.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenEmailIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> EmailAddress.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenEmailIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> EmailAddress.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("201文字以上のメールアドレスが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenEmailIsTooLong() {
      // Arrange
      String email = "a" + "b".repeat(199) + "@c"; // 201文字

      // Act & Assert
      assertThatThrownBy(() -> EmailAddress.of(email))
          .isInstanceOf(IllegalArgumentException.class)
          .as("文字数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("200");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のEmailAddressインスタンスが作成されること")
    void shouldCreateEmptyEmailAddress() {
      // Act
      EmailAddress emailAddress = EmailAddress.empty();

      // Assert
      assertThat(emailAddress).as("EmailAddressインスタンスが作成されること").isNotNull();
      assertThat(emailAddress.value()).as("値がnullであること").isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のEmailAddressはisEmptyがtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      EmailAddress emailAddress = EmailAddress.empty();

      // Assert
      assertThat(emailAddress.isEmpty()).as("空のEmailAddressはtrueを返すこと").isTrue();
    }

    @Test
    @DisplayName("値を持つEmailAddressはisEmptyがfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      EmailAddress emailAddress = EmailAddress.of("test@example.com");

      // Assert
      assertThat(emailAddress.isEmpty()).as("値を持つEmailAddressはfalseを返すこと").isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のEmailAddressは等しいこと")
    void shouldBeEqualWhenSameValue() {
      // Arrange
      EmailAddress first = EmailAddress.of("test@example.com");
      EmailAddress second = EmailAddress.of("test@example.com");

      // Assert
      assertThat(first).as("同じ値のEmailAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("異なる値のEmailAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      EmailAddress first = EmailAddress.of("test@example.com");
      EmailAddress second = EmailAddress.of("other@example.com");

      // Assert
      assertThat(first).as("異なる値のEmailAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("nullとの等価比較はfalseを返すこと")
    void shouldNotBeEqualToNull() {
      // Arrange
      EmailAddress emailAddress = EmailAddress.of("test@example.com");

      // Assert
      assertThat(emailAddress).as("nullとの等価比較はfalseを返すこと").isNotEqualTo(null);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("値を持つEmailAddressのtoStringは値を返すこと")
    void shouldReturnValueWhenToString() {
      // Arrange
      EmailAddress emailAddress = EmailAddress.of("test@example.com");

      // Assert
      assertThat(emailAddress.toString()).as("toStringは値を返すこと").isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("空のEmailAddressのtoStringはnullを返すこと")
    void shouldReturnNullStringWhenEmpty() {
      // Arrange
      EmailAddress emailAddress = EmailAddress.empty();

      // Assert
      assertThat(emailAddress.toString()).as("空のEmailAddressのtoStringはnullを返すこと").isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("EmailAddressConverterのテスト")
  class EmailAddressConverterTest {

    private final EmailAddress.EmailAddressConverter converter = new EmailAddress.EmailAddressConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("EmailAddressをデータベース用の文字列に変換すること")
      void shouldConvertEmailAddressToDatabaseString() {
        // Arrange
        EmailAddress emailAddress = EmailAddress.of("test@example.com");

        // Act
        String dbData = converter.convertToDatabaseColumn(emailAddress);

        // Assert
        assertThat(dbData).as("データベース用の文字列に変換されること").isEqualTo("test@example.com");
      }

      @Test
      @DisplayName("nullのEmailAddressはnullに変換すること")
      void shouldConvertNullEmailAddressToNull() {
        // Act
        String dbData = converter.convertToDatabaseColumn(null);

        // Assert
        assertThat(dbData).as("nullのEmailAddressはnullに変換されること").isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("データベース用の文字列をEmailAddressに変換すること")
      void shouldConvertDatabaseStringToEmailAddress() {
        // Arrange
        String dbData = "test@example.com";

        // Act
        EmailAddress emailAddress = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(emailAddress).as("EmailAddressに変換されること").isNotNull();
        assertThat(emailAddress.value()).as("値が保持されること").isEqualTo("test@example.com");
      }

      @Test
      @DisplayName("前後に空白があるデータベースデータはトリムされて変換すること")
      void shouldConvertDatabaseStringWithWhitespaceToTrimmedEmailAddress() {
        // Arrange
        String dbData = " test@example.com ";

        // Act
        EmailAddress emailAddress = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(emailAddress).as("EmailAddressに変換されること").isNotNull();
        assertThat(emailAddress.value()).as("空白がトリムされること").isEqualTo("test@example.com");
      }

      @Test
      @DisplayName("nullのデータベースデータは空のEmailAddressに変換すること")
      void shouldConvertNullToEmptyEmailAddress() {
        // Act
        EmailAddress emailAddress = converter.convertToEntityAttribute(null);

        // Assert
        assertThat(emailAddress).as("EmailAddressインスタンスが作成されること").isNotNull();
        assertThat(emailAddress.isEmpty()).as("空のEmailAddressであること").isTrue();
      }

      @Test
      @DisplayName("空白のみのデータベースデータは空のEmailAddressに変換すること")
      void shouldConvertBlankToEmptyEmailAddress() {
        // Act
        EmailAddress emailAddress = converter.convertToEntityAttribute("   ");

        // Assert
        assertThat(emailAddress).as("EmailAddressインスタンスが作成されること").isNotNull();
        assertThat(emailAddress.isEmpty()).as("空のEmailAddressであること").isTrue();
      }

      @Test
      @DisplayName("空文字列のデータベースデータは空のEmailAddressに変換すること")
      void shouldConvertEmptyStringToEmptyEmailAddress() {
        // Act
        EmailAddress emailAddress = converter.convertToEntityAttribute("");

        // Assert
        assertThat(emailAddress).as("EmailAddressインスタンスが作成されること").isNotNull();
        assertThat(emailAddress.isEmpty()).as("空のEmailAddressであること").isTrue();
      }
    }
  }
}
