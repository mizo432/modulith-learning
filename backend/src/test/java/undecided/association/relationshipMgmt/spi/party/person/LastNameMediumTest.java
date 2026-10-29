package undecided.association.relationshipMgmt.spi.party.person;

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
import com.github.mizo432.shared.converter.EncryptedStringConverter;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("LastNameConverterの統合テスト")
class LastNameMediumTest {

  @Autowired private StringEncryptor stringEncryptor;

  private LastName.LastNameConverter converter;

  @BeforeEach
  void setUp() {
    // 静的なencryptorを設定するために、EncryptedStringConverterのコンストラクタを呼び出す
    new EncryptedStringConverter(stringEncryptor);
    converter = new LastName.LastNameConverter();
  }

  @Nested
  @DisplayName("実際の暗号化器を使用したconvertToDatabaseColumnのテスト")
  class ConvertToDatabaseColumnWithRealEncryptorTest {

    @Test
    @DisplayName("LastNameを暗号化してデータベース用文字列を返すこと")
    void shouldEncryptLastNameWithRealEncryptor() {
      // Arrange
      LastName lastName = LastName.of("田中");

      // Act
      String encrypted = converter.convertToDatabaseColumn(lastName);

      // Assert
      assertThat(encrypted).isNotNull();
      assertThat(encrypted).isNotEqualTo("田中");
      assertThat(encrypted).isNotEmpty();
    }

    @Test
    @DisplayName("半角文字のLastNameを暗号化できること")
    void shouldEncryptHalfWidthLastName() {
      // Arrange
      LastName lastName = LastName.of("Tanaka");

      // Act
      String encrypted = converter.convertToDatabaseColumn(lastName);

      // Assert
      assertThat(encrypted).isNotNull();
      assertThat(encrypted).isNotEqualTo("Tanaka");
    }

    @Test
    @DisplayName("null属性はnullを返すこと")
    void shouldReturnNullForNullAttribute() {
      // Act & Assert
      assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("空のLastNameはnullを返すこと")
    void shouldReturnNullForEmptyLastName() {
      // Arrange
      LastName lastName = LastName.empty();

      // Act & Assert
      assertThat(converter.convertToDatabaseColumn(lastName)).isNull();
    }
  }

  @Nested
  @DisplayName("実際の暗号化器を使用したconvertToEntityAttributeのテスト")
  class ConvertToEntityAttributeWithRealEncryptorTest {

    @Test
    @DisplayName("暗号化された文字列を復号化してLastNameを返すこと")
    void shouldDecryptEncryptedTextToLastName() {
      // Arrange
      String plainText = "田中";
      String encrypted = stringEncryptor.encrypt(plainText);

      // Act
      LastName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(plainText);
    }

    @Test
    @DisplayName("半角文字の暗号化文字列を復号化できること")
    void shouldDecryptHalfWidthLastName() {
      // Arrange
      String plainText = "Tanaka";
      String encrypted = stringEncryptor.encrypt(plainText);

      // Act
      LastName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(plainText);
    }

    @Test
    @DisplayName("null入力は空のインスタンスを返すこと")
    void shouldReturnEmptyForNullInput() {
      // Act
      LastName result = converter.convertToEntityAttribute(null);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("暗号化と復号化のラウンドトリップが正常に動作すること")
    void shouldRoundTripEncryptAndDecrypt() {
      // Arrange
      LastName original = LastName.of("佐藤");

      // Act
      String encrypted = converter.convertToDatabaseColumn(original);
      LastName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(original.value());
    }

    @Test
    @DisplayName("半角文字のラウンドトリップが正常に動作すること")
    void shouldRoundTripHalfWidthText() {
      // Arrange
      LastName original = LastName.of("Sato");

      // Act
      String encrypted = converter.convertToDatabaseColumn(original);
      LastName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(original.value());
    }

    @Test
    @DisplayName("暗号化されていない文字列はそのままLastNameとして返すこと")
    void shouldReturnPlaintextAsLastNameWhenNotEncrypted() {
      // Arrange
      String plainText = "田中";

      // Act
      LastName result = converter.convertToEntityAttribute(plainText);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(plainText);
    }
  }
}
