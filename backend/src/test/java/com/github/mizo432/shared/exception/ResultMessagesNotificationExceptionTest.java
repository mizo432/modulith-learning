package com.github.mizo432.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.github.mizo432.shared.message.ResultMessages;

/**
 * {@code ResultMessagesNotificationException} のユニットテストクラスです。
 * 例外のコンストラクタおよびメソッドの動作を検証します。
 */
@Tag("small")
@DisplayName("ResultMessagesNotificationExceptionのテスト")
class ResultMessagesNotificationExceptionTest {

  @Test
  @DisplayName("単一引数コンストラクタでResultMessagesが設定される")
  void shouldSetResultMessagesWithSingleArgumentConstructor() {
    ResultMessages messages = ResultMessages.success();
    ResultMessagesNotificationException exception =
        new ResultMessagesNotificationExceptionTestImpl(messages);
    assertThat(exception.getResultMessages()).isSameAs(messages);
  }

  @Test
  @DisplayName("2引数コンストラクタでCauseとResultMessagesが設定される")
  void shouldSetCauseAndResultMessagesWithTwoArgumentConstructor() {
    ResultMessages messages = ResultMessages.success();
    Throwable cause = new Throwable();
    ResultMessagesNotificationException exception =
        new ResultMessagesNotificationExceptionTestImpl(messages, cause);
    assertThat(exception.getCause()).isSameAs(cause);
    assertThat(exception.getResultMessages()).isSameAs(messages);
  }

  @Test
  @DisplayName("getMessageはResultMessagesのtoStringを返す")
  void shouldReturnResultMessagesToStringAsMessage() {
    ResultMessages messages = ResultMessages.success();
    ResultMessagesNotificationException exception =
        new ResultMessagesNotificationExceptionTestImpl(messages);
    assertThat(exception.getMessage()).isEqualTo(messages.toString());
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
