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
@DisplayName("FirstNameConverterの統合テスト")
class FirstNameMediumTest {

  @Autowired private StringEncryptor stringEncryptor;

  private FirstName.FirstNameConverter converter;

  @BeforeEach
  void setUp() {
    // 静的なencryptorを設定するために、EncryptedStringConverterのコンストラクタを呼び出す
    new EncryptedStringConverter(stringEncryptor);
    converter = new FirstName.FirstNameConverter();
  }

  @Nested
  @DisplayName("実際の暗号化器を使用したconvertToDatabaseColumnのテスト")
  class ConvertToDatabaseColumnWithRealEncryptorTest {

    @Test
    @DisplayName("FirstNameを暗号化してデータベース用文字列を返すこと")
    void shouldEncryptFirstNameWithRealEncryptor() {
      // Arrange
      FirstName firstName = FirstName.of("太郎");

      // Act
      String encrypted = converter.convertToDatabaseColumn(firstName);

      // Assert
      assertThat(encrypted).isNotNull();
      assertThat(encrypted).isNotEqualTo("太郎");
      assertThat(encrypted).isNotEmpty();
    }

    @Test
    @DisplayName("半角文字のFirstNameを暗号化できること")
    void shouldEncryptHalfWidthFirstName() {
      // Arrange
      FirstName firstName = FirstName.of("Taro");

      // Act
      String encrypted = converter.convertToDatabaseColumn(firstName);

      // Assert
      assertThat(encrypted).isNotNull();
      assertThat(encrypted).isNotEqualTo("Taro");
    }

    @Test
    @DisplayName("null属性はnullを返すこと")
    void shouldReturnNullForNullAttribute() {
      // Act & Assert
      assertThat(converter.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    @DisplayName("空のFirstNameはnullを返すこと")
    void shouldReturnNullForEmptyFirstName() {
      // Arrange
      FirstName firstName = FirstName.empty();

      // Act & Assert
      assertThat(converter.convertToDatabaseColumn(firstName)).isNull();
    }
  }

  @Nested
  @DisplayName("実際の暗号化器を使用したconvertToEntityAttributeのテスト")
  class ConvertToEntityAttributeWithRealEncryptorTest {

    @Test
    @DisplayName("暗号化された文字列を復号化してFirstNameを返すこと")
    void shouldDecryptEncryptedTextToFirstName() {
      // Arrange
      String plainText = "太郎";
      String encrypted = stringEncryptor.encrypt(plainText);

      // Act
      FirstName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(plainText);
    }

    @Test
    @DisplayName("半角文字の暗号化文字列を復号化できること")
    void shouldDecryptHalfWidthFirstName() {
      // Arrange
      String plainText = "Taro";
      String encrypted = stringEncryptor.encrypt(plainText);

      // Act
      FirstName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(plainText);
    }

    @Test
    @DisplayName("null入力は空のインスタンスを返すこと")
    void shouldReturnEmptyForNullInput() {
      // Act
      FirstName result = converter.convertToEntityAttribute(null);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("暗号化と復号化のラウンドトリップが正常に動作すること")
    void shouldRoundTripEncryptAndDecrypt() {
      // Arrange
      FirstName original = FirstName.of("花子");

      // Act
      String encrypted = converter.convertToDatabaseColumn(original);
      FirstName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(original.value());
    }

    @Test
    @DisplayName("半角文字のラウンドトリップが正常に動作すること")
    void shouldRoundTripHalfWidthText() {
      // Arrange
      FirstName original = FirstName.of("Hanako");

      // Act
      String encrypted = converter.convertToDatabaseColumn(original);
      FirstName result = converter.convertToEntityAttribute(encrypted);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(original.value());
    }

    @Test
    @DisplayName("暗号化されていない文字列はそのままFirstNameとして返すこと")
    void shouldReturnPlaintextAsFirstNameWhenNotEncrypted() {
      // Arrange
      String plainText = "太郎";

      // Act
      FirstName result = converter.convertToEntityAttribute(plainText);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.value()).isEqualTo(plainText);
    }
  }
}
