package undecided.authorization.infra.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@Tag("small")
@SpringBootTest(classes = EncryptionConfig.class)
@TestPropertySource(properties = "jasypt.encryptor.password=test-password")
class EncryptionConfigTest {

  @Autowired
  @Qualifier("stringEncryptor")
  private StringEncryptor stringEncryptor;

  @Test
  @DisplayName("StringEncryptor Beanが正常に作成される")
  void shouldCreateStringEncryptorBean() {
    assertThat(stringEncryptor).isNotNull();
  }

  @Test
  @DisplayName("暗号化と復号化が正常に動作する")
  void shouldEncryptAndDecrypt() {
    String plainText = "Taro";
    String encrypted = stringEncryptor.encrypt(plainText);
    String decrypted = stringEncryptor.decrypt(encrypted);

    assertThat(encrypted).isNotNull().isNotEqualTo(plainText);
    assertThat(decrypted).isEqualTo(plainText);
  }
}
