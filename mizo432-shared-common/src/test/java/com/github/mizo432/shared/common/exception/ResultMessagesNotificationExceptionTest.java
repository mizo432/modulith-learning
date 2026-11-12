package com.github.mizo432.shared.common.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.message.ResultMessage;
import com.github.mizo432.shared.common.message.ResultMessages;
import com.github.mizo432.shared.common.message.StandardResultMessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ResultMessagesNotificationExceptionの単体テスト")
class ResultMessagesNotificationExceptionTest {

  private static class TestException extends ResultMessagesNotificationException {
    protected TestException(ResultMessages messages) {
      super(messages);
    }

    public TestException(ResultMessages messages, Throwable cause) {
      super(messages, cause);
    }

    public TestException(StandardResultMessageType type, ResultMessage message) {
      super(type, message);
    }

    public TestException(StandardResultMessageType type, ResultMessage message, Throwable cause) {
      super(type, message, cause);
    }

    public TestException(StandardResultMessageType type, String code, Object... args) {
      super(type, code, args);
    }

    public TestException(StandardResultMessageType type, String code, Object[] args, Throwable cause) {
      super(type, code, args, cause);
    }
  }

  @Nested
  @DisplayName("ResultMessagesコンストラクタのテスト")
  class ResultMessagesConstructorTest {

    @Test
    @DisplayName("ResultMessagesで例外が作成されること")
    void shouldCreateExceptionWithResultMessages() {
      // Arrange
      ResultMessages messages = ResultMessages.error().add(ResultMessage.fromText("Error"));

      // Act
      TestException exception = new TestException(messages);

      // Assert
      assertThat(exception.getResultMessages()).isSameAs(messages);
    }

    @Test
    @DisplayName("nullのResultMessagesで例外が作成されること")
    void shouldCreateExceptionWithNullMessages() {
      // Act & Assert
      assertThatThrownBy(() -> new TestException(null))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("ResultMessagesとcauseで例外が作成されること")
    void shouldCreateExceptionWithResultMessagesAndCause() {
      // Arrange
      ResultMessages messages = ResultMessages.error().add(ResultMessage.fromText("Error"));
      Throwable cause = new RuntimeException("Root cause");

      // Act
      TestException exception = new TestException(messages, cause);

      // Assert
      assertThat(exception.getResultMessages()).isSameAs(messages);
      assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    @DisplayName("2引数コンストラクタでnullのmessagesが渡された場合例外がスローされること")
    void shouldThrowExceptionWhenMessagesIsNullWithTwoArgs() {
      // Act & Assert
      assertThatThrownBy(() -> new TestException((ResultMessages) null, new RuntimeException()))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("messages must not be null");
    }
  }

  @Nested
  @DisplayName("ResultMessageTypeとResultMessageコンストラクタのテスト")
  class TypeAndMessageConstructorTest {

    @Test
    @DisplayName("タイプとメッセージで例外が作成されること")
    void shouldCreateExceptionWithTypeAndMessage() {
      // Arrange
      ResultMessage message = ResultMessage.fromText("Error message");

      // Act
      TestException exception = new TestException(StandardResultMessageType.ERROR, message);

      // Assert
      assertThat(exception.getResultMessages().getType()).isEqualTo(StandardResultMessageType.ERROR);
      assertThat(exception.getResultMessages().isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("タイプ、メッセージ、causeで例外が作成されること")
    void shouldCreateExceptionWithTypeMessageAndCause() {
      // Arrange
      ResultMessage message = ResultMessage.fromText("Error message");
      Throwable cause = new RuntimeException("Root cause");

      // Act
      TestException exception = new TestException(StandardResultMessageType.ERROR, message, cause);

      // Assert
      assertThat(exception.getResultMessages().getType()).isEqualTo(StandardResultMessageType.ERROR);
      assertThat(exception.getCause()).isSameAs(cause);
    }
  }

  @Nested
  @DisplayName("エラーコードコンストラクタのテスト")
  class ErrorCodeConstructorTest {

    @Test
    @DisplayName("タイプとエラーコードで例外が作成されること")
    void shouldCreateExceptionWithTypeAndErrorCode() {
      // Act
      TestException exception = new TestException(StandardResultMessageType.ERROR, "ERROR.001");

      // Assert
      assertThat(exception.getResultMessages().getType()).isEqualTo(StandardResultMessageType.ERROR);
      assertThat(exception.getResultMessages().isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("タイプ、エラーコードと引数で例外が作成されること")
    void shouldCreateExceptionWithTypeErrorCodeAndArgs() {
      // Act
      TestException exception =
          new TestException(StandardResultMessageType.ERROR, "ERROR.001", "arg1", "arg2");

      // Assert
      assertThat(exception.getResultMessages().getType()).isEqualTo(StandardResultMessageType.ERROR);
      ResultMessage message = exception.getResultMessages().iterator().next();
      assertThat(message.args()).containsExactly("arg1", "arg2");
    }

    @Test
    @DisplayName("タイプ、エラーコード、引数配列とcauseで例外が作成されること")
    void shouldCreateExceptionWithTypeErrorCodeArgsArrayAndCause() {
      // Arrange
      Object[] args = new Object[] {"arg1"};
      Throwable cause = new RuntimeException("Root cause");

      // Act
      TestException exception =
          new TestException(StandardResultMessageType.ERROR, "ERROR.001", args, cause);

      // Assert
      assertThat(exception.getResultMessages().getType()).isEqualTo(StandardResultMessageType.ERROR);
      assertThat(exception.getCause()).isSameAs(cause);
    }
  }

  @Nested
  @DisplayName("getMessage メソッドのテスト")
  class GetMessageMethodTest {

    @Test
    @DisplayName("ResultMessagesの文字列表現が返されること")
    void shouldReturnResultMessagesToString() {
      // Arrange
      ResultMessages messages = ResultMessages.error().add(ResultMessage.fromText("Error"));
      TestException exception = new TestException(messages);

      // Act & Assert
      assertThat(exception.getMessage()).isNotNull();
    }
  }

  @Nested
  @DisplayName("RuntimeExceptionとしての動作のテスト")
  class RuntimeExceptionBehaviorTest {

    @Test
    @DisplayName("RuntimeExceptionとしてスローできること")
    void shouldBeThrowAsRuntimeException() {
      // Arrange
      ResultMessages messages = ResultMessages.error().add(ResultMessage.fromText("Error"));

      // Act & Assert
      assertThatThrownBy(() -> {
        throw new TestException(messages);
      })
          .isInstanceOf(TestException.class)
          .isInstanceOf(RuntimeException.class);
    }
  }
}
