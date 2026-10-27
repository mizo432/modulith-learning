package undecided.shared.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("EncryptedStringConverterの統合テスト")
class EncryptedStringConverterMediumTest {

  @Autowired private StringEncryptor stringEncryptor;

  private EncryptedStringConverter converter;

  @BeforeEach
  void setUp() {
    converter = new EncryptedStringConverter(stringEncryptor);
  }

  @Nested
  @DisplayName("実際の暗号化器を使用したconvertToDatabaseColumnのテスト")
  class ConvertToDatabaseColumnWithRealEncryptorTest {

    @Test
    @DisplayName("平文を暗号化してデータベース用文字列を返すこと")
    void shouldEncryptPlainTextWithRealEncryptor() {
      // Arrange
      String plainText = "Taro";

      // Act
      String encrypted = converter.convertToDatabaseColumn(plainText);

      // Assert
      assertThat(encrypted).isNotNull();
      assertThat(encrypted).isNotEqualTo(plainText);
      assertThat(encrypted).isNotEmpty();
    }

    @Test
    @DisplayName("日本語の氏名を暗号化できること")
    void shouldEncryptJapaneseName() {
      // Arrange
      String plainText = "田中由紀";

      // Act
      String encrypted = converter.convertToDatabaseColumn(plainText);

      // Assert
      assertThat(encrypted).isNotNull();
      assertThat(encrypted).isNotEqualTo(plainText);
    }

    @Test
    @DisplayName("null入力はnullを返すこと")
    void shouldReturnNullForNullInput() {
      // Act & Assert
      assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力は空文字を返すこと")
    void shouldReturnEmptyForEmptyInput() {
      // Act & Assert
      assertThat(converter.convertToDatabaseColumn("")).isEmpty();
    }

    @Test
    @DisplayName("同じ平文を暗号化しても毎回異なる結果になること")
    void shouldProduceDifferentCiphertextForSamePlaintext() {
      // Arrange
      String plainText = "Yuki";

      // Act
      String encrypted1 = converter.convertToDatabaseColumn(plainText);
      String encrypted2 = converter.convertToDatabaseColumn(plainText);

      // Assert
      assertThat(encrypted1).isNotEqualTo(encrypted2);
    }
  }

  @Nested
  @DisplayName("実際の暗号化器を使用したconvertToEntityAttributeのテスト")
  class ConvertToEntityAttributeWithRealEncryptorTest {

    @Test
    @DisplayName("暗号化された文字列を復号化して平文を返すこと")
    void shouldDecryptEncryptedTextWithRealEncryptor() {
      // Arrange
      String plainText = "Taro";
      String encrypted = stringEncryptor.encrypt(plainText);

      // Act
      String decrypted = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(decrypted).isEqualTo(plainText);
    }

    @Test
    @DisplayName("日本語の暗号化文字列を復号化できること")
    void shouldDecryptJapaneseName() {
      // Arrange
      String plainText = "田中由紀";
      String encrypted = stringEncryptor.encrypt(plainText);

      // Act
      String decrypted = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(decrypted).isEqualTo(plainText);
    }

    @Test
    @DisplayName("null入力はnullを返すこと")
    void shouldReturnNullForNullInput() {
      // Act & Assert
      assertThat(converter.convertToEntityAttribute(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力は空文字を返すこと")
    void shouldReturnEmptyForEmptyInput() {
      // Act & Assert
      assertThat(converter.convertToEntityAttribute("")).isEmpty();
    }

    @Test
    @DisplayName("暗号化されていない文字列はそのまま返すこと")
    void shouldReturnPlaintextAsIsWhenNotEncrypted() {
      // Arrange
      String plainText = "This is not encrypted";

      // Act & Assert
      assertThat(converter.convertToEntityAttribute(plainText)).isEqualTo(plainText);
    }

    @Test
    @DisplayName("暗号化と復号化のラウンドトリップが正常に動作すること")
    void shouldRoundTripEncryptAndDecrypt() {
      // Arrange
      String originalText = "Sato Kenjiro";

      // Act
      String encrypted = converter.convertToDatabaseColumn(originalText);
      String decrypted = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(decrypted).isEqualTo(originalText);
    }

    @Test
    @DisplayName("日本語のラウンドトリップが正常に動作すること")
    void shouldRoundTripJapaneseText() {
      // Arrange
      String originalText = "佐藤健二朗";

      // Act
      String encrypted = converter.convertToDatabaseColumn(originalText);
      String decrypted = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(decrypted).isEqualTo(originalText);
    }
  }

  @Nested
  @DisplayName("Employeeエンティティとの連携テスト")
  class EmployeeIntegrationTest {

    @Test
    @DisplayName("Employeeエンティティの氏名フィールドが暗号化されること")
    void shouldEncryptEmployeeNameFields() {
      // Arrange
      String lastName = "田中";
      String firstName = "由紀";

      // Act
      String encryptedLastName = converter.convertToDatabaseColumn(lastName);
      String encryptedFirstName = converter.convertToDatabaseColumn(firstName);

      // Assert
      assertThat(encryptedLastName).isNotNull();
      assertThat(encryptedLastName).isNotEqualTo(lastName);
      assertThat(encryptedFirstName).isNotNull();
      assertThat(encryptedFirstName).isNotEqualTo(firstName);

      // Verify round-trip
      assertThat(converter.convertToEntityAttribute(encryptedLastName)).isEqualTo(lastName);
      assertThat(converter.convertToEntityAttribute(encryptedFirstName)).isEqualTo(firstName);
    }
  }
}
