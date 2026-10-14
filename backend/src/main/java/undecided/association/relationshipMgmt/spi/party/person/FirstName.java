package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import undecided.shared.primitive.Strings2;
import undecided.shared.value.StringCode;

/**
 * 名（ファーストネーム）を表す値オブジェクトです。
 *
 * <p>このクラスは {@link StringCode} インターフェースを実装し、個人の名（ファーストネーム）を不変の値として保持します。
 * 名は全角・半角文字を許容し、1文字以上50文字以下である必要があります。
 */
@EqualsAndHashCode
public class FirstName implements StringCode<FirstName> {

  /** 名の最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 名の最大文字数。 */
  public static final int MAX_LENGTH = 50;

  private final String value;

  protected FirstName() {
    value = null;
  }

  protected FirstName(String value) {
    this.value = value;
  }

  /**
   * 文字列から名を作成します。
   *
   * @param firstName 名（1文字以上50文字以下）
   * @return 名の値オブジェクト
   * @throws IllegalArgumentException 名がnull、空、または文字数制限を超えている場合
   */
  public static FirstName of(String firstName) {
    if (firstName == null) {
      throw new IllegalArgumentException("firstName must not be null");
    }
    String trimmed = firstName.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("firstName must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "firstName must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed.length());
    }
    return new FirstName(trimmed);
  }

  /**
   * 空の名を表すインスタンスを返します。
   *
   * @return 空の {@link FirstName} インスタンス
   */
  public static FirstName empty() {
    return new FirstName();
  }

  /**
   * 名の値を返します。
   *
   * @return 名の文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 名が空かどうかを判定します。
   *
   * @return 名が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 名の文字列表現を返します。
   *
   * @return 名の文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** FirstNameとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class FirstNameConverter implements AttributeConverter<FirstName, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code FirstName} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(FirstName attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code FirstName} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public FirstName convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return FirstName.empty();
      }
      return new FirstName(dbData);
    }
  }
}
