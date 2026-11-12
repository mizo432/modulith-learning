package undecided.association.relationshipMgmt.spi.party.org;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.value.StringCode;

/**
 * 組織の状態を表す値オブジェクトクラス。
 *
 * <p>このクラスは、組織の有効・無効などの状態を識別するためのコードを管理します。
 *
 * <p>組織の状態は文字列として格納され、組織のライフサイクル管理において使用されます。
 * 状態コードは英大文字とアンダースコアで構成され、文字数は1文字以上20文字以下である必要があります。
 *
 * <p>用途:
 *
 * <p>組織の有効・無効状態を管理するために使用され、アクティブな組織のフィルタリングなどで利用されます。
 */
@EqualsAndHashCode
public class OrgStatus implements StringCode<OrgStatus> {

  /** 状態コードの最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 状態コードの最大文字数。 */
  public static final int MAX_LENGTH = 20;

  /** 状態コードの正規表現（英大文字とアンダースコアのみ）。 */
  public static final String REGEX = "^[A-Z_]+$";

  /** 有効状態。 */
  public static final OrgStatus ACTIVE = new OrgStatus("ACTIVE");

  /** 無効状態。 */
  public static final OrgStatus INACTIVE = new OrgStatus("INACTIVE");

  private final String value;

  protected OrgStatus() {
    value = null;
  }

  private OrgStatus(String value) {
    this.value = value;
  }

  /**
   * 文字列から組織の状態を作成します。
   *
   * @param status 状態コード（英大文字とアンダースコアのみ、1文字以上20文字以下）
   * @return 組織の状態の値オブジェクト
   * @throws IllegalArgumentException 状態コードがnull、空、文字数制限を超えている、または英大文字とアンダースコアのみで構成されていない場合
   */
  public static OrgStatus of(String status) {
    if (status == null) {
      throw new IllegalArgumentException("status must not be null");
    }
    String trimmed = status.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("status must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "status must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed);
    }
    if (!trimmed.matches(REGEX)) {
      throw new IllegalArgumentException(
          "status must contain only uppercase letters and underscores, but was: " + trimmed);
    }
    return new OrgStatus(trimmed);
  }

  /**
   * 空の組織の状態を表すインスタンスを返します。
   *
   * @return 空の {@link OrgStatus} インスタンス
   */
  public static OrgStatus empty() {
    return new OrgStatus();
  }

  /**
   * 組織の状態の値を返します。
   *
   * @return 組織の状態の文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 組織の状態が空かどうかを判定します。
   *
   * @return 組織の状態が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 組織の状態の文字列表現を返します。
   *
   * @return 組織の状態の文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** OrgStatusとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class OrgStatusConverter implements AttributeConverter<OrgStatus, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code OrgStatus} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(OrgStatus attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code OrgStatus} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public OrgStatus convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return OrgStatus.empty();
      }
      return new OrgStatus(dbData);
    }
  }
}
