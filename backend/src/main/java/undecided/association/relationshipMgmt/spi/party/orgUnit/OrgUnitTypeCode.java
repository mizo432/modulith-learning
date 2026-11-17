package undecided.association.relationshipMgmt.spi.party.orgUnit;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.value.StringCode;

/**
 * 組織単位種類コードを表す値オブジェクトクラス。
 *
 * <p>このクラスは、組織単位の種類（DEPARTMENT, TEAM, SECTIONなど）を識別するためのコードを管理します。
 *
 * <p>組織単位種類コードは文字列として格納され、組織単位の種類を特定するために使用されます。
 * 組織単位種類コードは英大文字とアンダースコアで構成され、文字数は1文字以上20文字以下である必要があります。
 *
 * <p>用途:
 *
 * <p>組織単位の種類を識別するために使用され、クエリやフィルタリングにおいて利用されます。
 */
@EqualsAndHashCode
public class OrgUnitTypeCode implements StringCode<OrgUnitTypeCode> {

  /** 組織単位種類コードの最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 組織単位種類コードの最大文字数。 */
  public static final int MAX_LENGTH = 20;

  /** 組織単位種類コードの正規表現（英大文字とアンダースコアのみ）。 */
  public static final String REGEX = "^[A-Z_]+$";

  /** 部署種類コード。 */
  public static final OrgUnitTypeCode DEPARTMENT = new OrgUnitTypeCode("DEPARTMENT");

  /** チーム種類コード。 */
  public static final OrgUnitTypeCode TEAM = new OrgUnitTypeCode("TEAM");

  /** 課種類コード。 */
  public static final OrgUnitTypeCode SECTION = new OrgUnitTypeCode("SECTION");

  private final String value;

  protected OrgUnitTypeCode() {
    value = null;
  }

  private OrgUnitTypeCode(String value) {
    this.value = value;
  }

  /**
   * 文字列から組織単位種類コードを作成します。
   *
   * @param orgUnitTypeCode 組織単位種類コード（英大文字とアンダースコアのみ、1文字以上20文字以下）
   * @return 組織単位種類コードの値オブジェクト
   * @throws IllegalArgumentException 組織単位種類コードがnull、空、文字数制限を超えている、または英大文字とアンダースコアのみで構成されていない場合
   */
  public static OrgUnitTypeCode of(String orgUnitTypeCode) {
    if (orgUnitTypeCode == null) {
      throw new IllegalArgumentException("orgUnitTypeCode must not be null");
    }
    String trimmed = orgUnitTypeCode.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("orgUnitTypeCode must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "orgUnitTypeCode must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed);
    }
    if (!trimmed.matches(REGEX)) {
      throw new IllegalArgumentException(
          "orgUnitTypeCode must contain only uppercase letters and underscores, but was: "
              + trimmed);
    }
    return new OrgUnitTypeCode(trimmed);
  }

  /**
   * 空の組織単位種類コードを表すインスタンスを返します。
   *
   * @return 空の {@link OrgUnitTypeCode} インスタンス
   */
  public static OrgUnitTypeCode empty() {
    return new OrgUnitTypeCode();
  }

  /**
   * 組織単位種類コードの値を返します。
   *
   * @return 組織単位種類コードの文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 組織単位種類コードが空かどうかを判定します。
   *
   * @return 組織単位種類コードが空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 組織単位種類コードの文字列表現を返します。
   *
   * @return 組織単位種類コードの文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** OrgUnitTypeCodeとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class OrgUnitTypeCodeConverter
      implements AttributeConverter<OrgUnitTypeCode, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code OrgUnitTypeCode} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(OrgUnitTypeCode attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code OrgUnitTypeCode} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public OrgUnitTypeCode convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return OrgUnitTypeCode.empty();
      }
      return new OrgUnitTypeCode(dbData);
    }
  }
}
