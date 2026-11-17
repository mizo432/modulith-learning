package com.github.mizo432.shared.entity;

/**
 * データベースに永続化可能な列挙型を表すインターフェースです。
 *
 * <p>このインターフェースを実装する列挙型は、データベースとの変換を行う際に
 * {@link EnumConverter} と組み合わせて使用されます。各列挙型はデータベースに
 * 格納される文字列値を {@code getDatabaseValue()} メソッドで提供します。
 *
 * @see EnumConverter
 */
public interface PersistableEnum {

  /**
   * データベースに格納される値を返します。
   *
   * @return データベースに格納される文字列値
   */
  String getDatabaseValue();
}
