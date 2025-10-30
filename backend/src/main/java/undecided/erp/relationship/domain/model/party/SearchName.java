package undecided.erp.relationship.domain.model.party;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.erp.common.entity.StringValue;
import undecided.erp.common.primitive.Objects2;
import undecided.erp.common.primitive.Strings2;

/**
 * SearchNameクラスは、文字列をラップし、特定の値操作や条件判定のための機能を提供します。
 *
 * <p>このクラスは不変オブジェクトのように動作し、値がnullまたは空文字列の場合、 デフォルト値として事前定義された値が設定されます。
 */
@Getter
@NoArgsConstructor
public class SearchName implements StringValue<SearchName> {

  /**
   * valueフィールドは、SearchNameクラス内で管理される文字列データを表します。
   *
   * <p>このフィールドは内部で値の保持および操作に使用されます。特定の条件（nullまたは空文字列）に 該当する場合は、デフォルト値として事前定義された値が設定される設計となっています。
   *
   * <p>SearchNameインスタンスの操作や判定ロジックの基盤を支える重要なフィールドであり、 外部インターフェースを通じて直接アクセスされることはありません。
   */
  private String value;

  /**
   * 指定された文字列を使用してSearchNameインスタンスを生成します。 入力値がnullまたは空文字列である場合、デフォルト値が設定されます。
   *
   * @param value 入力する文字列。nullまたは空文字列の場合にはデフォルト値が設定されます。
   */
  public SearchName(String value) {
    this.value = Strings2.defaultIfEmpty(value, (String) Objects2.NULL);
  }

  /**
   * 指定されたデータベース文字列を使用してSearchNameインスタンスを再構築します。 入力された文字列はSearchNameのコンストラクタに渡され、適切なインスタンスが生成されます。
   *
   * @param dbData データベースから取得した再構築対象の文字列。nullまたは空文字列が渡された場合、SearchNameのデフォルト値が設定されます。
   * @return 再構築されたSearchNameインスタンス
   */
  private static SearchName reconstruct(String dbData) {
    return new SearchName(dbData);
  }

  /**
   * このメソッドはオブジェクトの文字列表現を返します。 具体的には、フィールドvalueの内容を文字列として返します。
   *
   * @return オブジェクトの文字列表現となる文字列
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }

  /**
   * 現在のオブジェクトが空であるかどうかを判定します。
   *
   * @return オブジェクトが空の場合はtrue、それ以外の場合はfalse
   */
  @Override
  public boolean isEmpty() {
    return Objects2.isNull(value);
  }

  /**
   * SearchNamePrefixCriteriaクラスは、特定の名前の接頭辞に基づく検索条件を表現するためのクラスです。
   *
   * <p>このクラスのインスタンスは指定された文字列を基に初期化され、 不変オブジェクトとして動作します。内部で管理される値は、 nullまたは空文字列の場合にデフォルト値が設定されます。
   *
   * <p>インスタンスは指定した接頭辞を使用して生成できます。
   */
  public record SearchNamePrefixCriteria(String value) {

    /**
     * SearchNamePrefixCriteriaクラスのコンストラクタ。
     *
     * <p>指定された名前接頭辞を検索条件として設定するオブジェクトを生成します。 入力された値がnullまたは空文字列の場合、デフォルト値が内部的に設定されます。
     *
     * @param value 名前接頭辞を示す文字列。nullまたは空文字列が指定された場合、 デフォルト値が自動的に設定されます。
     */
    public SearchNamePrefixCriteria(String value) {
      this.value = Strings2.defaultIfEmpty(value, (String) Objects2.NULL);
    }

    /**
     * 指定された文字列を基にSearchNamePrefixCriteriaのインスタンスを生成するファクトリメソッド。
     *
     * @param prefix 使用する名前接頭辞を示す文字列
     * @return 指定された接頭辞を基に作成されたSearchNamePrefixCriteriaのインスタンス
     */
    public static SearchNamePrefixCriteria of(String prefix) {
      return new SearchNamePrefixCriteria(prefix);
    }

    /**
     * このメソッドはSearchNamePrefixCriteriaオブジェクトの文字列表現を生成します。
     *
     * @return 内部で保持している値(value)を文字列として表したもの
     */
    @Override
    @NotNull
    public String toString() {
      return String.valueOf(value);
    }
  }

  /**
   * SearchNameConverterクラスは、SearchNameオブジェクトとデータベース上の文字列値との間の 変換処理を提供するクラスです。
   *
   * <p>このクラスは、JPAのAttributeConverterインターフェースを実装し、エンティティ属性と データベース列間のデータ変換を自動化します。
   *
   * <p>convertToDatabaseColumnメソッドは、SearchNameインスタンスをデータベースの文字列値に
   * 変換します。この変換の際に、SearchNameオブジェクトのtoStringメソッドを使用します。
   *
   * <p>convertToEntityAttributeメソッドは、データベースの文字列値を基にSearchNameオブジェクトを
   * 再構築します。この処理には、SearchNameクラスのreconstructメソッドを使用します。
   */
  @Converter
  public static class SearchNameConverter implements AttributeConverter<SearchName, String> {

    /**
     * convertToDatabaseColumnメソッドは、SearchNameオブジェクトをデータベースで使用される
     * 文字列形式に変換します。この変換には、SearchNameクラスのtoStringメソッドが用いられます。
     *
     * @param attribute データベースカラムに変換したいSearchNameオブジェクト
     * @return 引数で渡されたSearchNameオブジェクトの文字列表現
     */
    @Override
    public String convertToDatabaseColumn(SearchName attribute) {
      return attribute.toString();
    }

    /**
     * データベースから取得された文字列データを基に、SearchNameオブジェクトを再構築します。
     *
     * @param dbData データベースから取得された文字列データ
     * @return 再構築されたSearchNameオブジェクト
     */
    @Override
    public SearchName convertToEntityAttribute(String dbData) {
      return SearchName.reconstruct(dbData);
    }
  }
}
