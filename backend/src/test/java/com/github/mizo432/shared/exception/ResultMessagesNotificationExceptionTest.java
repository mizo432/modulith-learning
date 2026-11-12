package com.github.mizo432.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.mizo432.shared.common.exception.ResultMessagesNotificationException;
import com.github.mizo432.shared.common.message.ResultMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * {@code ResultMessagesNotificationExceptionTest} クラスは、{@link ResultMessagesNotificationException}
 * のテストを行うためのユニットテストクラスです。このクラスでは、例外のコンストラクタおよびメソッドの動作について検証を行います。
 *
 * <p>内部クラス {@code ResultMessagesNotificationExceptionTestImpl} を使用して、 {@code
 * ResultMessagesNotificationException} を直接インスタンス化できない場合のテスト実行を容易にしています。
 *
 * <p>主なテストケース: - 単一引数コンストラクタのテスト - 2つの引数を持つコンストラクタのテスト - {@code getMessage()} メソッドの動作確認
 */
@Tag("small")
@DisplayName("ResultMessagesNotificationException のテスト")
class ResultMessagesNotificationExceptionTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("単一引数コンストラクタでResultMessagesを保持すること")
    void shouldCreateWithSingleArgument() {
      ResultMessages messages = ResultMessages.success();
      ResultMessagesNotificationException exception =
          new ResultMessagesNotificationExceptionTestImpl(messages);
      assertThat(exception.getResultMessages()).isSameAs(messages);
    }

    @Test
    @DisplayName("2つの引数を持つコンストラクタでResultMessagesと原因を保持すること")
    void shouldCreateWithTwoArguments() {
      ResultMessages messages = ResultMessages.success();
      Throwable cause = new Throwable();
      ResultMessagesNotificationException exception =
          new ResultMessagesNotificationExceptionTestImpl(messages, cause);
      assertThat(exception.getCause()).isSameAs(cause);
      assertThat(exception.getResultMessages()).isSameAs(messages);
    }
  }

  @Nested
  @DisplayName("getMessage メソッドのテスト")
  class GetMessageTest {

    @Test
    @DisplayName("getMessageがResultMessagesのtoStringと一致すること")
    void shouldReturnResultMessagesToString() {
      ResultMessages messages = ResultMessages.success();
      ResultMessagesNotificationException exception =
          new ResultMessagesNotificationExceptionTestImpl(messages);
      assertThat(exception.getMessage()).isEqualTo(messages.toString());
    }
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
