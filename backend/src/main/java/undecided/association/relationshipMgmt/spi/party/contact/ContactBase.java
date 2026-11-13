package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.value.StringValue;

/**
 * 連絡先の基底クラスです。
 *
 * <p>この抽象クラスは {@link StringValue} インターフェースを実装し、連絡先アドレスを不変の値として保持します。
 * {@link EmailAddress}、{@link PhoneNo}、{@link Address} などのサブタイプはこのクラスを継承します。
 */
@EqualsAndHashCode
public abstract class ContactBase implements StringValue {

  /** 連絡先アドレスの最大文字数。 */
  public static final int MAX_LENGTH = 200;

  /** 連絡先アドレスの最小文字数。 */
  public static final int MIN_LENGTH = 1;

  protected final String value;

  /** null の値を持つインスタンスを作成します。 */
  protected ContactBase() {
    this.value = null;
  }

  /** 指定された値を持つインスタンスを作成します。 */
  protected ContactBase(String value) {
    this.value = value;
  }

  /**
   * この連絡先の種類を返します。
   *
   * @return 連絡先の種類
   */
  public abstract ContactType type();

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

  /** ContactBaseとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class ContactBaseConverter implements AttributeConverter<ContactBase, String> {

    @Override
    public String convertToDatabaseColumn(ContactBase attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public ContactBase convertToEntityAttribute(@Nullable String dbData) {
      // サブクラスのコンバーターが適用されるため、ここではnullを返す
      return null;
    }
  }
}
