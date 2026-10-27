package undecided.shared.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
class EncryptedStringConverterTest {

  private StringEncryptor mockEncryptor;
  private EncryptedStringConverter converter;

  @BeforeEach
  void setUp() {
    mockEncryptor = mock(StringEncryptor.class);
    converter = new EncryptedStringConverter(mockEncryptor);
  }

  /** Method under test: {@link EncryptedStringConverter#convertToDatabaseColumn(String)} */
  @Nested
  @DisplayName("convertToDatabaseColumnメソッドのテスト")
  class ConvertToDatabaseColumnTest {

    @Test
    @DisplayName("null入力の場合nullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力の場合空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(converter.convertToDatabaseColumn("")).isEmpty();
    }

    @Test
    @DisplayName("平文を暗号化して返す")
    void shouldEncryptPlainText() {
      String plainText = "Taro";
      String encrypted = "encrypted_Taro_base64";
      when(mockEncryptor.encrypt(plainText)).thenReturn(encrypted);

      assertThat(converter.convertToDatabaseColumn(plainText)).isEqualTo(encrypted);
    }
  }

  /** Method under test: {@link EncryptedStringConverter#convertToEntityAttribute(String)} */
  @Nested
  @DisplayName("convertToEntityAttributeメソッドのテスト")
  class ConvertToEntityAttributeTest {

    @Test
    @DisplayName("null入力の場合nullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力の場合空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(converter.convertToEntityAttribute("")).isEmpty();
    }

    @Test
    @DisplayName("暗号化文字列を復号化して返す")
    void shouldDecryptEncryptedText() {
      String encrypted = "encrypted_Taro_base64";
      String plainText = "Taro";
      when(mockEncryptor.decrypt(encrypted)).thenReturn(plainText);

      assertThat(converter.convertToEntityAttribute(encrypted)).isEqualTo(plainText);
    }

    @Test
    @DisplayName("復号化失敗の場合元の文字列を返す")
    void shouldReturnOriginalOnDecryptFailure() {
      String plainText = "plainText";
      when(mockEncryptor.decrypt(plainText)).thenThrow(new RuntimeException("Decrypt failed"));

      assertThat(converter.convertToEntityAttribute(plainText)).isEqualTo(plainText);
    }
  }
}
