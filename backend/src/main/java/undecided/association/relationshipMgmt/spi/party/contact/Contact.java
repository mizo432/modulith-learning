package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;

/**
 * 連絡先アドレス（メールアドレスなど）を表す値オブジェクトです。
 *
 * <p>このクラスは {@link ContactBase} を継承し、連絡先アドレスを不変の値として保持します。
 * 後方互換性のために残されており、{@link EmailAddress} をラップします。
 *
 * @deprecated {@link EmailAddress} を使用してください。
 */
@Deprecated
@EqualsAndHashCode(callSuper = true)
public class Contact extends ContactBase {

  /** 連絡先アドレスの最大文字数。 */
  public static final int MAX_LENGTH = 200;

  private final ContactType type;

  /** null の値を持つインスタンスを作成します。 */
  public Contact() {
    super();
    this.type = ContactType.EMAIL;
  }

  private Contact(String value) {
    super(value);
    this.type = ContactType.EMAIL;
  }

  /**
   * 指定された文字列から {@code Contact} インスタンスを作成します。
   *
   * @param contact 連絡先アドレス。{@code null} は許容されません。
   * @return 連絡先アドレスの {@code Contact} インスタンス
   * @throws IllegalArgumentException 連絡先アドレスが {@code null} または空の場合
   */
  public static Contact of(String contact) {
    if (contact == null || contact.isBlank()) {
      throw new IllegalArgumentException("contact must not be null or blank");
    }
    if (contact.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("contact must be " + MAX_LENGTH + " characters or less");
    }
    return new Contact(contact.trim());
  }

  /**
   * 空の {@code Contact} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code Contact} インスタンス
   */
  public static Contact empty() {
    return new Contact();
  }

  @Override
  public ContactType type() {
    return type;
  }

  @Override
  public String toString() {
    return value != null ? value : "null";
  }

  /** Contactとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class ContactConverter implements AttributeConverter<Contact, String> {

    @Override
    public String convertToDatabaseColumn(Contact attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public Contact convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return Contact.empty();
      }
      try {
        return Contact.of(dbData);
      } catch (IllegalArgumentException e) {
        return Contact.empty();
      }
    }
  }
}
