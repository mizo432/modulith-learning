package undecided.erp.shared.entity;

/**
 * SingleValueインターフェースは、型Tの単一の値を保持するオブジェクトを表します。 これはValueObjectインターフェースを拡張しています。
 *
 * @param <T> SingleValueオブジェクトによって保持される値の型
 */
public interface SingleValue<T> extends ValueObject {

  T getValue();
}
