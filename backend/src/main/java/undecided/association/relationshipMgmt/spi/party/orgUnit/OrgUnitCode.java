package undecided.association.relationshipMgmt.spi.party.orgUnit;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.value.StringCode;

/**
 * 組織単位コードを表す値オブジェクトクラス。
 *
 * <p>このクラスは、組織単位（部署、チームなど）を一意に識別するためのコードを管理します。
 *
 * <p>組織単位コードは文字列として格納され、システム内での組織単位の一意な識別において使用されます。
 * 組織単位コードは英数字とアンダースコアで構成され、文字数は1文字以上30文字以下である必要があります。
 *
 * <p>用途:
 *
 * <p>組織単位の一意な識別子として使用され、外部システムとの連携やAPI通信などで利用されます。
 */
@EqualsAndHashCode
public class OrgUnitCode implements StringCode<OrgUnitCode> {

  /** 組織単位コードの最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 組織単位コードの最大文字数。 */
  public static final int MAX_LENGTH = 30;

  /** 組織単位コードの正規表現（英数字とアンダースコアのみ）。 */
  public static final String REGEX = "^[A-Za-z0-9_]+$";

  private final String value;

  protected OrgUnitCode() {
    value = null;
  }

  private OrgUnitCode(String value) {
    this.value = value;
  }

  /**
   * 文字列から組織単位コードを作成します。
   *
   * @param code 組織単位コード（英数字とアンダースコアのみ、1文字以上30文字以下）
   * @return 組織単位コードの値オブジェクト
   * @throws IllegalArgumentException 組織単位コードがnull、空、文字数制限を超えている、または英数字とアンダースコアのみで構成されていない場合
   */
  public static OrgUnitCode of(String code) {
    if (code == null) {
      throw new IllegalArgumentException("code must not be null");
    }
    String trimmed = code.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("code must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "code must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed);
    }
    if (!trimmed.matches(REGEX)) {
      throw new IllegalArgumentException(
          "code must contain only alphanumeric characters and underscores, but was: " + trimmed);
    }
    return new OrgUnitCode(trimmed);
  }

  /**
   * 空の組織単位コードを表すインスタンスを返します。
   *
   * @return 空の {@link OrgUnitCode} インスタンス
   */
  public static OrgUnitCode empty() {
    return new OrgUnitCode();
  }

  /**
   * 組織単位コードの値を返します。
   *
   * @return 組織単位コードの文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 組織単位コードが空かどうかを判定します。
   *
   * @return 組織単位コードが空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 組織単位コードの文字列表現を返します。
   *
   * @return 組織単位コードの文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** OrgUnitCodeとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class OrgUnitCodeConverter implements AttributeConverter<OrgUnitCode, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code OrgUnitCode} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(OrgUnitCode attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code OrgUnitCode} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public OrgUnitCode convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return OrgUnitCode.empty();
      }
      return new OrgUnitCode(dbData);
    }
  }
}
