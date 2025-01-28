package undecided.erp.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import undecided.erp.common.message.ResultMessages;

class ResultMessagesNotificationExceptionTest {


  @Test
  void testSingleArgumentConstructor() {
    ResultMessages messages = ResultMessages.success();
    ResultMessagesNotificationException exception = new ResultMessagesNotificationExceptionTestImpl(
        messages);
    assertSame(messages, exception.getResultMessages());
  }

  @Test
  void testTwoArgumentConstructor() {
    ResultMessages messages = ResultMessages.success();
    Throwable cause = new Throwable();
    ResultMessagesNotificationException exception = new ResultMessagesNotificationExceptionTestImpl(
        messages, cause);
    assertSame(cause, exception.getCause());
    assertSame(messages, exception.getResultMessages());
  }

  @Test
  void testGetMessage() {
    ResultMessages messages = ResultMessages.success();
    ResultMessagesNotificationException exception = new ResultMessagesNotificationExceptionTestImpl(
        messages);
    assertEquals(messages.toString(), exception.getMessage());
  }

  static class ResultMessagesNotificationExceptionTestImpl extends
      ResultMessagesNotificationException {

    ResultMessagesNotificationExceptionTestImpl(ResultMessages messages) {
      super(messages);
    }

    ResultMessagesNotificationExceptionTestImpl(ResultMessages messages, Throwable cause) {
      super(messages, cause);
    }
  }
}
