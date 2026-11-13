package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;

/**
 * 住所を表す値オブジェクトです。
 *
 * <p>このクラスは {@link ContactBase} を継承し、住所を不変の値として保持します。
 */
@EqualsAndHashCode(callSuper = true)
public class Address extends ContactBase {

  /** 住所の最大文字数。 */
  public static final int MAX_LENGTH = 200;

  private final ContactType type;

  /** null の値を持つインスタンスを作成します。 */
  public Address() {
    super();
    this.type = ContactType.ADDRESS;
  }

  private Address(String value) {
    super(value);
    this.type = ContactType.ADDRESS;
  }

  /**
   * 指定された文字列から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address) {
    if (address == null || address.isBlank()) {
      throw new IllegalArgumentException("address must not be null or blank");
    }
    if (address.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("address must be " + MAX_LENGTH + " characters or less");
    }
    return new Address(address.trim());
  }

  /**
   * 空の {@code Address} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code Address} インスタンス
   */
  public static Address empty() {
    return new Address();
  }

  @Override
  public ContactType type() {
    return type;
  }

  @Override
  public String toString() {
    return value != null ? value : "null";
  }

  /** Addressとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class AddressConverter implements AttributeConverter<Address, String> {

    @Override
    public String convertToDatabaseColumn(Address attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public Address convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return Address.empty();
      }
      return new Address(dbData);
    }
  }
}
