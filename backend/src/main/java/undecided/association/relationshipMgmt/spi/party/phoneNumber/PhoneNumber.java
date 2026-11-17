package undecided.association.relationshipMgmt.spi.party.phoneNumber;

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
public class PhoneNumber implements StringValue {

  /** 電話番号の最大文字数。 */
  public static final int MAX_LENGTH = 20;

  private final String value;

  /** null の値を持つインスタンスを作成します。 */
  public PhoneNumber() {
    value = null;
  }

  /**
   * 指定された文字列から {@code PhoneNumber} インスタンスを作成します。
   *
   * @param phoneNumber 電話番号。{@code null} は許容されません。
   * @return 電話番号の {@code PhoneNumber} インスタンス
   * @throws IllegalArgumentException 電話番号が {@code null} または空の場合
   */
  public static PhoneNumber of(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.isBlank()) {
      throw new IllegalArgumentException("phoneNumber must not be null or blank");
    }
    if (phoneNumber.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "phoneNumber must be " + MAX_LENGTH + " characters or less");
    }
    return new PhoneNumber(phoneNumber.trim());
  }

  /**
   * 空の {@code PhoneNumber} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code PhoneNumber} インスタンス
   */
  public static PhoneNumber empty() {
    return new PhoneNumber();
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

  /**
   * PhoneNumberとデータベースの文字列を相互に変換するためのJPAコンバーターです。
   *
   * <p>データベースから読み込んだ不正なデータに対しては、{@code empty()}を返して安全にフォールバックします。
   */
  @Converter(autoApply = true)
  public static class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code PhoneNumber} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(PhoneNumber attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code PhoneNumber} インスタンス。データベースデータが {@code null} または不正な場合は空のインスタンス
     */
    @Override
    public PhoneNumber convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return PhoneNumber.empty();
      }
      try {
        return PhoneNumber.of(dbData);
      } catch (IllegalArgumentException e) {
        return PhoneNumber.empty();
      }
    }
  }
}
