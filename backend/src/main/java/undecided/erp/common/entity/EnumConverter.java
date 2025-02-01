package undecided.erp.common.entity;

/**
 * {@link PersistableEnum} インターフェースを実装する列挙型を変換するための 基盤を提供する抽象クラスです。このクラスは、PersistableEnum インスタンスを
 * 関連するデータベース表現に変換するユーティリティメソッドを提供し、 データベース値から列挙型インスタンスを作成するための抽象メソッドを定義します。
 *
 * @param <E> {@link PersistableEnum} を拡張する列挙型のタイプ
 */
public abstract class EnumConverter<E extends PersistableEnum> {

  protected String toDatabaseValue(E value) {
    return value.getDatabaseValue();
  }

  protected abstract E toEntityAttribute(Class<E> enumClass, String value);

}
