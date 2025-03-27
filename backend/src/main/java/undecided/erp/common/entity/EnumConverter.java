package undecided.erp.common.entity;

/**
 * {@link PersistableEnum} インターフェースを実装する列挙型を変換するための 基盤を提供する抽象クラスです。このクラスは、PersistableEnum インスタンスを
 * 関連するデータベース表現に変換するユーティリティメソッドを提供し、 データベース値から列挙型インスタンスを作成するための抽象メソッドを定義します。
 *
 * @param <E> {@link PersistableEnum} を拡張する列挙型のタイプ
 */
public abstract class EnumConverter<E extends PersistableEnum> {

  /**
   * 指定された {@link PersistableEnum} 実装の列挙型インスタンスから、そのデータベースでの表現値を取得して返します。
   *
   * @param value データベース値に変換する対象の列挙型インスタンス
   * @return 対応するデータベース表現値
   */
  protected String toDatabaseValue(E value) {
    return value.getDatabaseValue();
  }

  /**
   * 指定されたデータベース値を基に、指定された列挙型クラスのインスタンスを返します。
   *
   * @param enumClass 変換先の列挙型クラス。{@link PersistableEnum} を実装している必要があります。
   * @param value データベースから取得した値。この値に対応する列挙型のインスタンスを生成します。
   * @return 指定されたデータベース値に対応する列挙型のインスタンス
   */
  protected abstract E toEntityAttribute(Class<E> enumClass, String value);

}
