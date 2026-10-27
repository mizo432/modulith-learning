package undecided.shared.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("small")
@DisplayName("EncryptedStringConverterの単体テスト")
@ExtendWith(MockitoExtension.class)
class EncryptedStringConverterTest {

  private StringEncryptor mockEncryptor;
  private EncryptedStringConverter converter;

  @BeforeEach
  void setUp() {
    mockEncryptor = mock(StringEncryptor.class);
    converter = new EncryptedStringConverter(mockEncryptor);
  }

  @AfterEach
  void tearDown() {
    EncryptedStringConverter.resetEncryptor();
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

    @Test
    @DisplayName("暗号化器がnullの場合、平文をそのまま返す")
    void shouldReturnPlainTextWhenEncryptorIsNull() {
      // Arrange
      EncryptedStringConverter.resetEncryptor();
      String plainText = "Taro";

      // Act & Assert
      assertThat(converter.convertToDatabaseColumn(plainText)).isEqualTo(plainText);
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

    @Test
    @DisplayName("暗号化器がnullの場合、データベースデータをそのまま返す")
    void shouldReturnDbDataWhenEncryptorIsNull() {
      // Arrange
      EncryptedStringConverter.resetEncryptor();
      String dbData = "someDbData";

      // Act & Assert
      assertThat(converter.convertToEntityAttribute(dbData)).isEqualTo(dbData);
    }
  }

  /** 静的メソッドencrypt()のテスト */
  @Nested
  @DisplayName("静的メソッドencrypt()のテスト")
  class StaticEncryptTest {

    @Test
    @DisplayName("null入力はnullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(EncryptedStringConverter.encrypt(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力は空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(EncryptedStringConverter.encrypt("")).isEmpty();
    }

    @Test
    @DisplayName("平文を暗号化して返す")
    void shouldEncryptPlainText() {
      String plainText = "Taro";
      String encrypted = "encrypted_Taro_base64";
      when(mockEncryptor.encrypt(plainText)).thenReturn(encrypted);

      assertThat(EncryptedStringConverter.encrypt(plainText)).isEqualTo(encrypted);
    }

    @Test
    @DisplayName("暗号化器がnullの場合、平文をそのまま返す")
    void shouldReturnPlainTextWhenEncryptorIsNull() {
      // Arrange
      EncryptedStringConverter.resetEncryptor();
      String plainText = "Taro";

      // Act & Assert
      assertThat(EncryptedStringConverter.encrypt(plainText)).isEqualTo(plainText);
    }
  }

  /** 静的メソッドdecrypt()のテスト */
  @Nested
  @DisplayName("静的メソッドdecrypt()のテスト")
  class StaticDecryptTest {

    @Test
    @DisplayName("null入力はnullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(EncryptedStringConverter.decrypt(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力は空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(EncryptedStringConverter.decrypt("")).isEmpty();
    }

    @Test
    @DisplayName("暗号化文字列を復号化して返す")
    void shouldDecryptEncryptedText() {
      String encrypted = "encrypted_Taro_base64";
      String plainText = "Taro";
      when(mockEncryptor.decrypt(encrypted)).thenReturn(plainText);

      assertThat(EncryptedStringConverter.decrypt(encrypted)).isEqualTo(plainText);
    }

    @Test
    @DisplayName("復号化失敗の場合元の文字列を返す")
    void shouldReturnOriginalOnDecryptFailure() {
      String plainText = "plainText";
      when(mockEncryptor.decrypt(plainText)).thenThrow(new RuntimeException("Decrypt failed"));

      assertThat(EncryptedStringConverter.decrypt(plainText)).isEqualTo(plainText);
    }

    @Test
    @DisplayName("暗号化器がnullの場合、データベースデータをそのまま返す")
    void shouldReturnDbDataWhenEncryptorIsNull() {
      // Arrange
      EncryptedStringConverter.resetEncryptor();
      String dbData = "someDbData";

      // Act & Assert
      assertThat(EncryptedStringConverter.decrypt(dbData)).isEqualTo(dbData);
    }
  }
}
