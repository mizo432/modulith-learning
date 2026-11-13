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

  /**
   * ContactBaseとデータベースの文字列を相互に変換するためのJPAコンバーターです。
   *
   * <p>このコンバーターは{@code autoApply = false}であり、サブクラスのコンバーター（{@link EmailAddress.EmailAddressConverter}、
   * {@link PhoneNo.PhoneNoConverter}、{@link Address.AddressConverter}）が優先して適用されます。
   * これにより、各サブタイプごとに適切な変換ロジックが実行されます。
   *
   * @see EmailAddress.EmailAddressConverter
   * @see PhoneNo.PhoneNoConverter
   * @see Address.AddressConverter
   */
  @Converter
  public static class ContactBaseConverter implements AttributeConverter<ContactBase, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code ContactBase} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(ContactBase attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * <p>このメソッドは常に{@code null}を返します。{@code ContactBase}は抽象クラスであり、
     * 実際のインスタンス化はサブクラスのコンバーターが行うためです。
     * Hibernateは{@code autoApply = true}のサブクラスコンバーターを優先して適用するため、
     * このメソッドは呼び出されません。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 常に {@code null} を返します。サブクラスのコンバーターが実際のインスタンスを作成します。
     */
    @Override
    public ContactBase convertToEntityAttribute(@Nullable String dbData) {
      // サブクラスのコンバーターが適用されるため、ここではnullを返す
      return null;
    }
  }
}
