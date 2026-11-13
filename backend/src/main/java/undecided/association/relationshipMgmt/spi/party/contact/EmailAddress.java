package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;

/**
 * メールアドレスを表す値オブジェクトです。
 *
 * <p>このクラスは {@link ContactBase} を継承し、メールアドレスを不変の値として保持します。
 */
@EqualsAndHashCode(callSuper = true)
public class EmailAddress extends ContactBase {

  /** メールアドレスの最大文字数。 */
  public static final int MAX_LENGTH = 200;

  private final ContactType type;

  /** null の値を持つインスタンスを作成します。 */
  public EmailAddress() {
    super();
    this.type = ContactType.EMAIL;
  }

  private EmailAddress(String value) {
    super(value);
    this.type = ContactType.EMAIL;
  }

  /**
   * 指定された文字列から {@code EmailAddress} インスタンスを作成します。
   *
   * @param email メールアドレス。{@code null} は許容されません。
   * @return メールアドレスの {@code EmailAddress} インスタンス
   * @throws IllegalArgumentException メールアドレスが {@code null} または空の場合
   */
  public static EmailAddress of(String email) {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("email must not be null or blank");
    }
    if (email.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("email must be " + MAX_LENGTH + " characters or less");
    }
    return new EmailAddress(email.trim());
  }

  /**
   * 空の {@code EmailAddress} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code EmailAddress} インスタンス
   */
  public static EmailAddress empty() {
    return new EmailAddress();
  }

  @Override
  public ContactType type() {
    return type;
  }

  @Override
  public String toString() {
    return value != null ? value : "null";
  }

  /** EmailAddressとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class EmailAddressConverter implements AttributeConverter<EmailAddress, String> {

    @Override
    public String convertToDatabaseColumn(EmailAddress attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public EmailAddress convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return EmailAddress.empty();
      }
      return new EmailAddress(dbData);
    }
  }
}
