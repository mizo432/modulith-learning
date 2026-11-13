package undecided.association.relationshipMgmt.spi.party.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Contact値オブジェクトのテスト")
class ContactTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な連絡先アドレスからContactを正常に作成すること")
    void shouldCreateContactFromValidString() {
      // Act
      Contact contact = Contact.of("user@example.com");

      // Assert
      assertThat(contact).as("Contactインスタンスが作成されること").isNotNull();
      assertThat(contact.value()).as("値が保持されること").isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("前後に空白がある連絡先アドレスをトリムして作成すること")
    void shouldCreateContactWithTrimmedValue() {
      // Act
      Contact contact = Contact.of(" user@example.com ");

      // Assert
      assertThat(contact.value()).as("前後の空白がトリムされること").isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("1文字の連絡先アドレスも正常に作成すること")
    void shouldCreateContactWithOneCharacter() {
      // Act
      Contact contact = Contact.of("a");

      // Assert
      assertThat(contact.value()).as("1文字の連絡先アドレスが作成されること").isEqualTo("a");
    }

    @Test
    @DisplayName("200文字の連絡先アドレスも正常に作成すること")
    void shouldCreateContactWithMaxLength() {
      // Arrange
      String contact = "a".repeat(200); // 200文字

      // Act
      Contact c = Contact.of(contact);

      // Assert
      assertThat(c.value()).as("200文字の連絡先アドレスが作成されること").isEqualTo(contact);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenContactIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> Contact.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenContactIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> Contact.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenContactIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> Contact.of("   "))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("201文字以上の連絡先アドレスが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenContactIsTooLong() {
      // Arrange
      String contact = "a".repeat(201); // 201文字

      // Act & Assert
      assertThatThrownBy(() -> Contact.of(contact))
          .isInstanceOf(IllegalArgumentException.class)
          .as("文字数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("200");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のContactインスタンスが作成されること")
    void shouldCreateEmptyContact() {
      // Act
      Contact contact = Contact.empty();

      // Assert
      assertThat(contact).as("Contactインスタンスが作成されること").isNotNull();
      assertThat(contact.value()).as("値がnullであること").isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のContactはisEmptyがtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      Contact contact = Contact.empty();

      // Assert
      assertThat(contact.isEmpty()).as("空のContactはtrueを返すこと").isTrue();
    }

    @Test
    @DisplayName("値を持つContactはisEmptyがfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      Contact contact = Contact.of("user@example.com");

      // Assert
      assertThat(contact.isEmpty()).as("値を持つContactはfalseを返すこと").isFalse();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値のContactは等しいこと")
    void shouldBeEqualWhenSameValue() {
      // Arrange
      Contact first = Contact.of("user@example.com");
      Contact second = Contact.of("user@example.com");

      // Assert
      assertThat(first).as("同じ値のContactは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("異なる値のContactは等しくないこと")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      Contact first = Contact.of("user@example.com");
      Contact second = Contact.of("admin@example.com");

      // Assert
      assertThat(first).as("異なる値のContactは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("nullとの等価比較はfalseを返すこと")
    void shouldNotBeEqualToNull() {
      // Arrange
      Contact contact = Contact.of("user@example.com");

      // Assert
      assertThat(contact).as("nullとの等価比較はfalseを返すこと").isNotEqualTo(null);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("値を持つContactのtoStringは値を返すこと")
    void shouldReturnValueWhenToString() {
      // Arrange
      Contact contact = Contact.of("user@example.com");

      // Assert
      assertThat(contact.toString()).as("toStringは値を返すこと").isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("空のContactのtoStringはnullを返すこと")
    void shouldReturnNullStringWhenEmpty() {
      // Arrange
      Contact contact = Contact.empty();

      // Assert
      assertThat(contact.toString()).as("空のContactのtoStringはnullを返すこと").isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("ContactConverterのテスト")
  class ContactConverterTest {

    private final Contact.ContactConverter converter = new Contact.ContactConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("Contactをデータベース用の文字列に変換すること")
      void shouldConvertContactToDatabaseString() {
        // Arrange
        Contact contact = Contact.of("user@example.com");

        // Act
        String dbData = converter.convertToDatabaseColumn(contact);

        // Assert
        assertThat(dbData).as("データベース用の文字列に変換されること").isEqualTo("user@example.com");
      }

      @Test
      @DisplayName("nullのContactはnullに変換すること")
      void shouldConvertNullContactToNull() {
        // Act
        String dbData = converter.convertToDatabaseColumn(null);

        // Assert
        assertThat(dbData).as("nullのContactはnullに変換されること").isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("データベース用の文字列をContactに変換すること")
      void shouldConvertDatabaseStringToContact() {
        // Arrange
        String dbData = "user@example.com";

        // Act
        Contact contact = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(contact).as("Contactに変換されること").isNotNull();
        assertThat(contact.value()).as("値が保持されること").isEqualTo("user@example.com");
      }

      @Test
      @DisplayName("nullのデータベースデータは空のContactに変換すること")
      void shouldConvertNullToEmptyContact() {
        // Act
        Contact contact = converter.convertToEntityAttribute(null);

        // Assert
        assertThat(contact).as("Contactインスタンスが作成されること").isNotNull();
        assertThat(contact.isEmpty()).as("空のContactであること").isTrue();
      }
    }
  }
}
