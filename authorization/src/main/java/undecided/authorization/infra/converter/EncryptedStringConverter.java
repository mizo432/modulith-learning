package undecided.authorization.infra.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jasypt.encryption.StringEncryptor;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 暗号化された文字列と平文文字列を相互に変換するためのJPAコンバーターです。
 *
 * <p>このコンバーターは、データベースに格納される機微な情報を暗号化し、 アプリケーション内で平文として扱うことを可能にします。 {@link StringEncryptor}
 * を使用して暗号化/復号化を行います。
 *
 * <p>使用例:
 *
 * <pre>{@code
 * @Column(name = "first_name")
 * @Convert(converter = EncryptedStringConverter.class)
 * private String firstName;
 * }</pre>
 */
@Component
@Converter(autoApply = false)
public class EncryptedStringConverter implements AttributeConverter<String, String> {

  private static StringEncryptor stringEncryptor;

  /**
   * 暗号化器を注入します。
   *
   * @param encryptor 使用する {@link StringEncryptor}
   */
  @Autowired
  public EncryptedStringConverter(@Qualifier("stringEncryptor") StringEncryptor encryptor) {
    stringEncryptor = encryptor;
  }

  /**
   * エンティティ属性（平文）をデータベースカラム（暗号化文字列）に変換します。
   *
   * @param attribute 変換対象の平文字列。{@code null} の可能性があります。
   * @return 暗号化された {@code String}。属性が {@code null} または空の場合は {@code null}
   */
  @Override
  public String convertToDatabaseColumn(@Nullable String attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return attribute;
    }
    if (stringEncryptor == null) {
      return attribute;
    }
    return stringEncryptor.encrypt(attribute);
  }

  /**
   * データベースカラム（暗号化文字列）をエンティティ属性（平文）に変換します。
   *
   * @param dbData データベースから読み込んだ暗号化文字列。{@code null} の可能性があります。
   * @return 復号化された {@code String}。データベースデータが {@code null} または空の場合は {@code null}
   */
  @Override
  public String convertToEntityAttribute(@Nullable String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return dbData;
    }
    if (stringEncryptor == null) {
      return dbData;
    }
    try {
      return stringEncryptor.decrypt(dbData);
    } catch (Exception e) {
      // 暗号化されていないデータの場合はそのまま返す（既存データ対応）
      return dbData;
    }
  }
}
