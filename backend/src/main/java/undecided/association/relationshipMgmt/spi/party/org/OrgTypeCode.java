package undecided.association.relationshipMgmt.spi.party.org;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.value.StringCode;

/**
 * 組織種類コードを表す値オブジェクトクラス。
 *
 * <p>このクラスは、組織の種類（COMPANY, STORE, DEPARTMENTなど）を識別するためのコードを管理します。
 *
 * <p>組織種類コードは文字列として格納され、組織の種類を特定するために使用されます。
 * 組織種類コードは英大文字とアンダースコアで構成され、文字数は1文字以上20文字以下である必要があります。
 *
 * <p>用途:
 *
 * <p>組織の種類を識別するために使用され、クエリやフィルタリングにおいて利用されます。
 */
@EqualsAndHashCode
public class OrgTypeCode implements StringCode<OrgTypeCode> {

  /** 組織種類コードの最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 組織種類コードの最大文字数。 */
  public static final int MAX_LENGTH = 20;

  /** 組織種類コードの正規表現（英大文字とアンダースコアのみ）。 */
  public static final String REGEX = "^[A-Z_]+$";

  /** 会社種類コード。 */
  public static final OrgTypeCode COMPANY = new OrgTypeCode("COMPANY");

  /** 店舗種類コード。 */
  public static final OrgTypeCode STORE = new OrgTypeCode("STORE");

  /** 部署種類コード。 */
  public static final OrgTypeCode DEPARTMENT = new OrgTypeCode("DEPARTMENT");

  private final String value;

  protected OrgTypeCode() {
    value = null;
  }

  private OrgTypeCode(String value) {
    this.value = value;
  }

  /**
   * 文字列から組織種類コードを作成します。
   *
   * @param orgTypeCode 組織種類コード（英大文字とアンダースコアのみ、1文字以上20文字以下）
   * @return 組織種類コードの値オブジェクト
   * @throws IllegalArgumentException 組織種類コードがnull、空、文字数制限を超えている、または英大文字とアンダースコアのみで構成されていない場合
   */
  public static OrgTypeCode of(String orgTypeCode) {
    if (orgTypeCode == null) {
      throw new IllegalArgumentException("orgTypeCode must not be null");
    }
    String trimmed = orgTypeCode.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("orgTypeCode must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "orgTypeCode must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed);
    }
    if (!trimmed.matches(REGEX)) {
      throw new IllegalArgumentException(
          "orgTypeCode must contain only uppercase letters and underscores, but was: " + trimmed);
    }
    return new OrgTypeCode(trimmed);
  }

  /**
   * 空の組織種類コードを表すインスタンスを返します。
   *
   * @return 空の {@link OrgTypeCode} インスタンス
   */
  public static OrgTypeCode empty() {
    return new OrgTypeCode();
  }

  /**
   * 組織種類コードの値を返します。
   *
   * @return 組織種類コードの文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 組織種類コードが空かどうかを判定します。
   *
   * @return 組織種類コードが空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 組織種類コードの文字列表現を返します。
   *
   * @return 組織種類コードの文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** OrgTypeCodeとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class OrgTypeCodeConverter implements AttributeConverter<OrgTypeCode, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code OrgTypeCode} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(OrgTypeCode attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code OrgTypeCode} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public OrgTypeCode convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return OrgTypeCode.empty();
      }
      return new OrgTypeCode(dbData);
    }
  }
}
