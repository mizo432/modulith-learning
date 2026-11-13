package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.value.StringValue;

/**
 * 連絡先アドレス（メールアドレスなど）を表す値オブジェクトです。
 *
 * <p>このクラスは {@link StringValue} インターフェースを実装し、連絡先アドレスを不変の値として保持します。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Contact implements StringValue {

  /** 連絡先アドレスの最大文字数。 */
  public static final int MAX_LENGTH = 200;

  /** 連絡先アドレスの最小文字数。 */
  public static final int MIN_LENGTH = 1;

  private final String value;

  /** null の値を持つインスタンスを作成します。 */
  public Contact() {
    value = null;
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

  /**
   * 値が空かどうかを返します。
   *
   * @return 値が {@code null} または空の場合に {@code true}
   */
  public boolean isEmpty() {
    return value == null || value.isBlank();
  }

  /**
   * 保持している文字列を返します。
   *
   * @return 連絡先アドレス。{@code null} の可能性があります。
   */
  @Override
  public String value() {
    return value;
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
      return new Contact(dbData);
    }
  }
}
