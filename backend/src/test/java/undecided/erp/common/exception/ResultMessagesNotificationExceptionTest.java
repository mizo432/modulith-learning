package undecided.erp.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import undecided.shared.common.message.ResultMessages;

/**
 * {@code ResultMessagesNotificationExceptionTest} クラスは、{@link ResultMessagesNotificationException}
 * のテストを行うためのユニットテストクラスです。このクラスでは、例外のコンストラクタおよびメソッドの動作について検証を行います。
 *
 * <p>内部クラス {@code ResultMessagesNotificationExceptionTestImpl} を使用して、 {@code
 * ResultMessagesNotificationException} を直接インスタンス化できない場合のテスト実行を容易にしています。
 *
 * <p>主なテストケース: - 単一引数コンストラクタのテスト - 2つの引数を持つコンストラクタのテスト - {@code getMessage()} メソッドの動作確認
 */
class ResultMessagesNotificationExceptionTest {

  @Test
  void testSingleArgumentConstructor() {
    ResultMessages messages = ResultMessages.success();
    ResultMessagesNotificationException exception =
        new ResultMessagesNotificationExceptionTestImpl(messages);
    assertSame(messages, exception.getResultMessages());
  }

  @Test
  void testTwoArgumentConstructor() {
    ResultMessages messages = ResultMessages.success();
    Throwable cause = new Throwable();
    ResultMessagesNotificationException exception =
        new ResultMessagesNotificationExceptionTestImpl(messages, cause);
    assertSame(cause, exception.getCause());
    assertSame(messages, exception.getResultMessages());
  }

  @Test
  void testGetMessage() {
    ResultMessages messages = ResultMessages.success();
    ResultMessagesNotificationException exception =
        new ResultMessagesNotificationExceptionTestImpl(messages);
    assertEquals(messages.toString(), exception.getMessage());
  }

  static class ResultMessagesNotificationExceptionTestImpl
      extends ResultMessagesNotificationException {

    ResultMessagesNotificationExceptionTestImpl(ResultMessages messages) {
      super(messages);
    }

    ResultMessagesNotificationExceptionTestImpl(ResultMessages messages, Throwable cause) {
      super(messages, cause);
    }
  }
}
