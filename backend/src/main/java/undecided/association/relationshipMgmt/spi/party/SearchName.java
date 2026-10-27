package undecided.association.relationshipMgmt.spi.party;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import undecided.shared.primitive.Strings2;
import undecided.shared.value.StringCode;

/**
 * 検索用名称を表す値オブジェクトクラス。
 *
 * <p>このクラスは、パーティ（個人や法人など）を検索するために使用される名称を管理します。
 *
 * <p>検索用名称は文字列として格納され、システム内での検索や表示において対象者を特定するために使用されます。 検索用名称は全角文字で構成され、文字数は1文字以上50文字以下である必要があります。
 *
 * <p>用途:
 *
 * <p>パーティを検索や表示で特定するために使用され、姓や名称の一部などで検索を行う際に利用されます。
 */
@EqualsAndHashCode
public class SearchName implements StringCode<SearchName> {

  /** 検索用名称の最小文字数。 */
  public static final int MIN_LENGTH = 1;

  /** 検索用名称の最大文字数。 */
  public static final int MAX_LENGTH = 50;

  /** 検索用名称の正規表現（全角文字のみ）。 */
  public static final String REGEX =
      "^[\u3000-\u303F\u3040-\u309F\u30A0-\u30FF\u4E00-\u9FFF\uFF01-\uFF5E]+$";

  private final String value;

  protected SearchName() {
    value = null;
  }

  private SearchName(String value) {
    this.value = value;
  }

  /**
   * 文字列から検索用名称を作成します。
   *
   * @param searchName 検索用名称（全角文字のみ、1文字以上50文字以下）
   * @return 検索用名称の値オブジェクト
   * @throws IllegalArgumentException 検索用名称がnull、空、文字数制限を超えている、または全角文字のみで構成されていない場合
   */
  public static SearchName of(String searchName) {
    if (searchName == null) {
      throw new IllegalArgumentException("searchName must not be null");
    }
    String trimmed = searchName.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("searchName must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "searchName must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed);
    }
    if (!trimmed.matches(REGEX)) {
      throw new IllegalArgumentException(
          "searchName must contain only full-width characters, but was: " + trimmed);
    }
    return new SearchName(trimmed);
  }

  /**
   * 空の検索用名称を表すインスタンスを返します。
   *
   * @return 空の {@link SearchName} インスタンス
   */
  public static SearchName empty() {
    return new SearchName();
  }

  /**
   * 検索用名称の値を返します。
   *
   * @return 検索用名称の文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 検索用名称が空かどうかを判定します。
   *
   * @return 検索用名称が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 検索用名称の文字列表現を返します。
   *
   * @return 検索用名称の文字列
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /** SearchNameとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class SearchNameConverter implements AttributeConverter<SearchName, String> {

    @Override
    public String convertToDatabaseColumn(SearchName attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public SearchName convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return SearchName.empty();
      }
      return new SearchName(dbData);
    }
  }
}
