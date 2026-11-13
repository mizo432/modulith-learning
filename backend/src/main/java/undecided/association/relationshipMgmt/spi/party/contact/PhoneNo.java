package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.value.StringValue;

/**
 * 電話番号を表す値オブジェクトです。
 *
 * <p>このクラスは {@link StringValue} インターフェースを実装し、電話番号を不変の値として保持します。
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PhoneNo implements StringValue {

  /** 電話番号の最大文字数。 */
  public static final int MAX_LENGTH = 20;

  private final String value;

  /** null の値を持つインスタンスを作成します。 */
  public PhoneNo() {
    value = null;
  }

  /**
   * 指定された文字列から {@code PhoneNo} インスタンスを作成します。
   *
   * @param phoneNo 電話番号。{@code null} は許容されません。
   * @return 電話番号の {@code PhoneNo} インスタンス
   * @throws IllegalArgumentException 電話番号が {@code null} または空の場合
   */
  public static PhoneNo of(String phoneNo) {
    if (phoneNo == null || phoneNo.isBlank()) {
      throw new IllegalArgumentException("phoneNo must not be null or blank");
    }
    if (phoneNo.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("phoneNo must be " + MAX_LENGTH + " characters or less");
    }
    return new PhoneNo(phoneNo.trim());
  }

  /**
   * 空の {@code PhoneNo} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code PhoneNo} インスタンス
   */
  public static PhoneNo empty() {
    return new PhoneNo();
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
   * @return 電話番号。{@code null} の可能性があります。
   */
  @Override
  public String value() {
    return value;
  }

  @Override
  public String toString() {
    return value != null ? value : "null";
  }

  /** PhoneNoとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class PhoneNoConverter implements AttributeConverter<PhoneNo, String> {

    @Override
    public String convertToDatabaseColumn(PhoneNo attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public PhoneNo convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return PhoneNo.empty();
      }
      return new PhoneNo(dbData);
    }
  }
}
