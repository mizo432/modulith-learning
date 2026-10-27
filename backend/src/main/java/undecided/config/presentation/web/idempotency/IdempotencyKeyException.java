package undecided.config.presentation.web.idempotency;

/**
 * 冪等性キーが既に存在するリクエストが検出された場合にスローされる例外です。
 *
 * <p>同じ冪等性キーを持つリクエストが重複して送信された場合、この例外をスローすることで、 クライアントに409 Conflictを返却します。
 */
public class IdempotencyKeyException extends RuntimeException {

  /**
   * 指定されたメッセージで例外を構築します。
   *
   * @param message 例外メッセージ
   */
  public IdempotencyKeyException(String message) {
    super(message);
  }
}
