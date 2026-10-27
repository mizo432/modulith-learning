package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import undecided.shared.converter.EncryptedStringConverter;
import undecided.shared.primitive.Strings2;
import undecided.shared.sensitive.SensitiveDataMasker;
import undecided.shared.value.StringCode;

/**
 * 姓（ラストネーム）を表す値オブジェクトです。
 *
 * <p>このクラスは {@link StringCode} インターフェースを実装し、個人の姓（ラストネーム）を不変の値として保持します。
 * 姓は全角・半角文字を許容し、1文字以上50文字以下である必要があります。
 */
@EqualsAndHashCode
public class LastName implements StringCode<LastName> {

  /** 姓の最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 姓の最大文字数。 */
  public static final int MAX_LENGTH = 50;

  private final String value;

  protected LastName() {
    value = null;
  }

  protected LastName(String value) {
    this.value = value;
  }

  /**
   * 文字列から姓を作成します。
   *
   * @param lastName 姓（1文字以上50文字以下）
   * @return 姓の値オブジェクト
   * @throws IllegalArgumentException 姓がnull、空、または文字数制限を超えている場合
   */
  public static LastName of(String lastName) {
    if (lastName == null) {
      throw new IllegalArgumentException("lastName must not be null");
    }
    String trimmed = lastName.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("lastName must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "lastName must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed.length());
    }
    return new LastName(trimmed);
  }

  /**
   * 空の姓を表すインスタンスを返します。
   *
   * @return 空の {@link LastName} インスタンス
   */
  public static LastName empty() {
    return new LastName();
  }

  /**
   * 姓の値を返します。
   *
   * @return 姓の文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 姓が空かどうかを判定します。
   *
   * @return 姓が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 姓の文字列表現を返します。
   *
   * @return 姓の文字列
   */
  @Override
  public String toString() {
    return SensitiveDataMasker.maskName(value);
  }

  /** LastNameとデータベースの文字列を相互に変換するためのJPAコンバーターです。暗号化を適用します。 */
  @Converter(autoApply = true)
  public static class LastNameConverter implements AttributeConverter<LastName, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。暗号化して保存します。
     *
     * @param attribute 変換対象の {@code LastName} 属性
     * @return 暗号化された {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(LastName attribute) {
      if (attribute == null) {
        return null;
      }
      String value = attribute.value();
      if (value == null) {
        return null;
      }
      return EncryptedStringConverter.encrypt(value);
    }

    /**
     * データベースカラムをエンティティ属性に変換します。復号化して読み込みます。
     *
     * @param dbData データベースから読み込んだ暗号化文字列。{@code null} の可能性があります。
     * @return 変換された {@code LastName} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public LastName convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return LastName.empty();
      }
      String decrypted = EncryptedStringConverter.decrypt(dbData);
      if (decrypted == null) {
        return LastName.empty();
      }
      return new LastName(decrypted);
    }
  }
}
