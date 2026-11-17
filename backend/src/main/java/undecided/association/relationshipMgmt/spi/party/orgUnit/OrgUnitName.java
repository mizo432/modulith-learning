package undecided.association.relationshipMgmt.spi.party.orgUnit;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.value.StringCode;

/**
 * 組織単位名を表す値オブジェクトクラス。
 *
 * <p>このクラスは、組織単位（部署、チームなど）の名称を管理します。
 *
 * <p>組織単位名は文字列として格納され、システム内での表示や識別において組織単位を特定するために使用されます。
 * 組織単位名は全角・半角文字を許容し、文字数は1文字以上100文字以下である必要があります。
 *
 * <p>用途:
 *
 * <p>組織単位の名称として使用され、内部システムでの表示、検索などで利用されます。
 */
@EqualsAndHashCode
public class OrgUnitName implements StringCode<OrgUnitName> {

  /** 組織単位名の最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 組織単位名の最大文字数。 */
  public static final int MAX_LENGTH = 100;

  private final String value;

  protected OrgUnitName() {
    value = null;
  }

  private OrgUnitName(String value) {
    this.value = value;
  }

  /**
   * 文字列から組織単位名を作成します。
   *
   * @param name 組織単位名（1文字以上100文字以下）
   * @return 組織単位名の値オブジェクト
   * @throws IllegalArgumentException 組織単位名がnull、空、または文字数制限を超えている場合
   */
  public static OrgUnitName of(String name) {
    if (name == null) {
      throw new IllegalArgumentException("name must not be null");
    }
    String trimmed = name.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("name must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "name must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed.length());
    }
    return new OrgUnitName(trimmed);
  }

  /**
   * 空の組織単位名を表すインスタンスを返します。
   *
   * @return 空の {@link OrgUnitName} インスタンス
   */
  public static OrgUnitName empty() {
    return new OrgUnitName();
  }

  /**
   * 組織単位名の値を返します。
   *
   * @return 組織単位名の文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 組織単位名が空かどうかを判定します。
   *
   * @return 組織単位名が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 組織単位名の文字列表現を返します。
   *
   * @return 組織単位名の文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** OrgUnitNameとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class OrgUnitNameConverter implements AttributeConverter<OrgUnitName, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code OrgUnitName} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(OrgUnitName attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code OrgUnitName} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public OrgUnitName convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return OrgUnitName.empty();
      }
      return new OrgUnitName(dbData);
    }
  }
}
