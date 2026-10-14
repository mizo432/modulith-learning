package undecided.association.relationshipMgmt.spi.party.bankAccount;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import undecided.shared.primitive.Strings2;
import undecided.shared.value.StringValue;

/**
 * 銀行口座の口座名義人を表す値オブジェクトクラス。
 *
 * <p>このクラスは銀行口座の所有者の名前を管理します。
 *
 * <p>個人名（漢字、ひらがな、カタカナ）または組織名を格納し、振込・振替などの取引において口座名義人を識別するために使用されます。
 * 口座名義人はnullではなく、空でも空白のみの文字列でもなく、最大100文字である必要があります。
 *
 * <p>用途:
 *
 * <p>銀行口座の所有者を特定し、取引の正当性を確認するために使用されます。
 */
@EqualsAndHashCode
public class HoldersName implements StringValue<HoldersName> {

  /** 口座名義人の最大文字数。 */
  public static final int MAX_LENGTH = 100;

  /** 日本語（漢字、ひらがな、カタカナ）、半角英数字、空白を許容する正規表現。 */
  private static final String REGEX =
      "^[\u4e00-\u9fff\u3040-\u309f\u30a0-\u30ff\u3005a-zA-Z0-9\s]+$";

  private final String value;

  protected HoldersName() {
    value = null;
  }

  private HoldersName(String value) {
    this.value = value;
  }

  /**
   * 文字列から口座名義人を作成します。
   *
   * @param holdersName 口座名義人の名前（null不可、空不可、最大100文字）
   * @return 口座名義人の値オブジェクト
   * @throws IllegalArgumentException 名前がnull、空、空白のみ、最大文字数を超えている、 または許可されていない文字を含む場合
   */
  public static HoldersName of(String holdersName) {
    if (holdersName == null) {
      throw new IllegalArgumentException("holdersName must not be null");
    }
    String trimmed = holdersName.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("holdersName must not be empty");
    }
    if (trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "holdersName must not exceed "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed.length());
    }
    if (!trimmed.matches(REGEX)) {
      throw new IllegalArgumentException(
          "holdersName contains invalid characters, but was: " + trimmed);
    }
    return new HoldersName(trimmed);
  }

  /**
   * 空の口座名義人を表すインスタンスを返します。
   *
   * @return 空の {@link HoldersName} インスタンス
   */
  public static HoldersName empty() {
    return new HoldersName();
  }

  /**
   * 口座名義人の名前を返します。
   *
   * @return 口座名義人の名前
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 口座名義人の名前が空かどうかを判定します。
   *
   * @return 名前が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 口座名義人の文字列表現を返します。
   *
   * @return 口座名義人の名前
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** HoldersNameとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class HoldersNameConverter implements AttributeConverter<HoldersName, String> {

    @Override
    public String convertToDatabaseColumn(HoldersName attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public HoldersName convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return HoldersName.empty();
      }
      return new HoldersName(dbData);
    }
  }
}
