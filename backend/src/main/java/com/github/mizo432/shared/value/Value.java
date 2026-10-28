package com.github.mizo432.shared.value;

/**
 * 単一の値を保持するシンプルな値オブジェクトを表すインターフェースです。
 *
 * <p>このインターフェースは {@link ValueObject} を継承せず、JPAとの統合を必要としない
 * 軽量な値オブジェクトに使用されます。日付・時刻などのシンプルな値を扱う際に適しています。
 *
 * @param <T> 保持する値の型
 * @see ValueObject
 * @see DateTimeValue
 * @see DateValue
 */
public interface Value<T> {

  /**
   * 保持している値を返します。
   *
   * @return 保持している値
   */
  T value();
}
