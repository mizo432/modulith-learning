package undecided.association.relationshipMgmt.spi.party.bankAccount;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.value.StringCode;

/**
 * 銀行口座の口座番号を表す値オブジェクトクラス。
 *
 * <p>このクラスは銀行口座を一意に識別するための口座番号を管理します。
 *
 * <p>口座番号は文字列として格納され、振込・振替などの取引において対象口座を特定するために使用されます。 口座番号は半角数字のみで構成され、桁数は1桁以上7桁以下である必要があります。
 *
 * <p>用途:
 *
 * <p>銀行口座を一意に識別し、取引の相手先口座を正確に指定するために使用されます。
 */
@EqualsAndHashCode
public class AccountNumber implements StringCode<AccountNumber> {

  /** 口座番号の最小桁数。 */
  public static final int MIN_LENGTH = 1;

  /** 口座番号の最大桁数（日本の銀行口座番号は通常7桁）。 */
  public static final int MAX_LENGTH = 7;

  private final String value;

  protected AccountNumber() {
    value = null;
  }

  private AccountNumber(String value) {
    this.value = value;
  }

  /**
   * 文字列から口座番号を作成します。
   *
   * @param accountNumber 口座番号（半角数字のみ、1桁以上7桁以下）
   * @return 口座番号の値オブジェクト
   * @throws IllegalArgumentException 口座番号がnull、空、桁数制限を超えている、または半角数字のみで構成されていない場合
   */
  public static AccountNumber of(String accountNumber) {
    if (accountNumber == null) {
      throw new IllegalArgumentException("accountNumber must not be null");
    }
    String trimmed = accountNumber.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("accountNumber must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "accountNumber must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " digits, but was: "
              + trimmed);
    }
    if (!trimmed.matches("\\d+")) {
      throw new IllegalArgumentException(
          "accountNumber must contain only digits, but was: " + trimmed);
    }
    return new AccountNumber(trimmed);
  }

  /**
   * 空の口座番号を表すインスタンスを返します。
   *
   * @return 空の {@link AccountNumber} インスタンス
   */
  public static AccountNumber empty() {
    return new AccountNumber();
  }

  /**
   * 口座番号の値を返します。
   *
   * @return 口座番号の文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 口座番号が空かどうかを判定します。
   *
   * @return 口座番号が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 口座番号の文字列表現を返します。
   *
   * @return 口座番号の文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** AccountNumberとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class AccountNumberConverter implements AttributeConverter<AccountNumber, String> {

    @Override
    public String convertToDatabaseColumn(AccountNumber attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public AccountNumber convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return AccountNumber.empty();
      }
      return new AccountNumber(dbData);
    }
  }
}
