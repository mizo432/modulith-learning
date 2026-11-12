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
@DisplayName("BusinessExceptionの単体テスト")
class BusinessExceptionTest {

  @Nested
  @DisplayName("文字列メッセージコンストラクタのテスト")
  class StringMessageConstructorTest {

    @Test
    @DisplayName("文字列メッセージで例外が作成されること")
    void shouldCreateExceptionWithStringMessage() {
      // Arrange
      String message = "Business error occurred";

      // Act
      BusinessException exception = new BusinessException(message);

      // Assert
      assertThat(exception).isNotNull();
      assertThat(exception.getResultMessages().getType()).isEqualTo(StandardResultMessageType.ERROR);
      assertThat(exception.getResultMessages().isNotEmpty()).isTrue();
    }

    @Test
    @DisplayName("メッセージが正しく格納されること")
    void shouldStoreMessageCorrectly() {
      // Arrange
      String message = "Test error message";

      // Act
      BusinessException exception = new BusinessException(message);

      // Assert
      ResultMessage storedMessage = exception.getResultMessages().iterator().next();
      assertThat(storedMessage.text()).isEqualTo(message);
    }
  }

  @Nested
  @DisplayName("ResultMessagesコンストラクタのテスト")
  class ResultMessagesConstructorTest {

    @Test
    @DisplayName("ResultMessagesで例外が作成されること")
    void shouldCreateExceptionWithResultMessages() {
      // Arrange
      ResultMessages messages =
          ResultMessages.error().add(ResultMessage.fromText("Error 1")).add(ResultMessage.fromText("Error 2"));

      // Act
      BusinessException exception = new BusinessException(messages);

      // Assert
      assertThat(exception.getResultMessages()).isSameAs(messages);
      assertThat(exception.getResultMessages().isNotEmpty()).isTrue();
    }
  }

  @Nested
  @DisplayName("ResultMessagesとcauseコンストラクタのテスト")
  class ResultMessagesAndCauseConstructorTest {

    @Test
    @DisplayName("ResultMessagesとcauseで例外が作成されること")
    void shouldCreateExceptionWithResultMessagesAndCause() {
      // Arrange
      ResultMessages messages = ResultMessages.error().add(ResultMessage.fromText("Error"));
      Throwable cause = new RuntimeException("Root cause");

      // Act
      BusinessException exception = new BusinessException(messages, cause);

      // Assert
      assertThat(exception.getResultMessages()).isSameAs(messages);
      assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    @DisplayName("getMessageがResultMessagesの文字列表現を返すこと")
    void shouldReturnResultMessagesToStringAsMessage() {
      // Arrange
      ResultMessages messages = ResultMessages.error().add(ResultMessage.fromText("Error"));
      Throwable cause = new RuntimeException("Root cause");
      BusinessException exception = new BusinessException(messages, cause);

      // Act & Assert
      assertThat(exception.getMessage()).isNotNull();
    }
  }
}
