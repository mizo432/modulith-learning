package com.github.mizo432.shared.message;

/**
 * 結果メッセージのタイプを表すインターフェースです。
 *
 * <p>このインターフェースを実装することで、結果メッセージの種類（例: 成功、情報、警告、エラーなど）を
 * 識別するための契約を提供します。{@link ResultMessages} と組み合わせて使用され、
 * メッセージの重要度や用途に応じた分類を行います。
 *
 * @see ResultMessages
 * @see StandardResultMessageType
 */
public interface ResultMessageType {

  /**
   * メッセージタイプの文字列表現を返します。
   *
   * @return メッセージタイプの文字列
   */
  String getType();
}
