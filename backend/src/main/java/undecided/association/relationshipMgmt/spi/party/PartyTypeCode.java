package undecided.association.relationshipMgmt.spi.party;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.value.StringCode;

/**
 * パーティー種類コードを表す値オブジェクトクラス。
 *
 * <p>このクラスは、パーティーの種類（MASTER, PERSON, ORG）を識別するためのコードを管理します。
 *
 * <p>パーティー種類コードは文字列として格納され、パーティーの種類を特定するために使用されます。
 * パーティー種類コードは英大文字とアンダースコアで構成され、文字数は1文字以上20文字以下である必要があります。
 *
 * <p>用途:
 *
 * <p>パーティーの種類を識別するために使用され、クエリやフィルタリングにおいて利用されます。
 */
@EqualsAndHashCode
public class PartyTypeCode implements StringCode<PartyTypeCode> {

  /** パーティー種類コードの最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** パーティー種類コードの最大文字数。 */
  public static final int MAX_LENGTH = 20;

  /** パーティー種類コードの正規表現（英大文字とアンダースコアのみ）。 */
  public static final String REGEX = "^[A-Z_]+$";

  /** MASTER種類コード。 */
  public static final PartyTypeCode MASTER = new PartyTypeCode("MASTER");

  /** PERSON種類コード。 */
  public static final PartyTypeCode PERSON = new PartyTypeCode("PERSON");

  /** ORG種類コード。 */
  public static final PartyTypeCode ORG = new PartyTypeCode("ORG");

  private final String value;

  protected PartyTypeCode() {
    value = null;
  }

  private PartyTypeCode(String value) {
    this.value = value;
  }

  /**
   * 文字列からパーティー種類コードを作成します。
   *
   * @param partyTypeCode パーティー種類コード（英大文字とアンダースコアのみ、1文字以上20文字以下）
   * @return パーティー種類コードの値オブジェクト
   * @throws IllegalArgumentException パーティー種類コードがnull、空、文字数制限を超えている、または英大文字とアンダースコアのみで構成されていない場合
   */
  public static PartyTypeCode of(String partyTypeCode) {
    if (partyTypeCode == null) {
      throw new IllegalArgumentException("partyTypeCode must not be null");
    }
    String trimmed = partyTypeCode.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("partyTypeCode must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "partyTypeCode must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed);
    }
    if (!trimmed.matches(REGEX)) {
      throw new IllegalArgumentException(
          "partyTypeCode must contain only uppercase letters and underscores, but was: " + trimmed);
    }
    return new PartyTypeCode(trimmed);
  }

  /**
   * 空のパーティー種類コードを表すインスタンスを返します。
   *
   * @return 空の {@link PartyTypeCode} インスタンス
   */
  public static PartyTypeCode empty() {
    return new PartyTypeCode();
  }

  /**
   * パーティー種類コードの値を返します。
   *
   * @return パーティー種類コードの文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * パーティー種類コードが空かどうかを判定します。
   *
   * @return パーティー種類コードが空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * パーティー種類コードの文字列表現を返します。
   *
   * @return パーティー種類コードの文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** PartyTypeCodeとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class PartyTypeCodeConverter implements AttributeConverter<PartyTypeCode, String> {

    @Override
    public String convertToDatabaseColumn(PartyTypeCode attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public PartyTypeCode convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return PartyTypeCode.empty();
      }
      return new PartyTypeCode(dbData);
    }
  }
}
