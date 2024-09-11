package undecided.erp.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.message.ResultMessage;
import undecided.erp.common.message.ResultMessageType;
import undecided.erp.common.message.ResultMessages;
import undecided.erp.common.message.StandardResultMessageType;

class BusinessExceptionTest {


  @Nested
  class ConstructorTest {

    /**
     * Method under test: {@link BusinessException#BusinessException(String)}
     */
    @Test
    void constructor() {
      // Arrange and Act
      BusinessException actualBusinessException = new BusinessException("An error occurred");

      // Assert
      ResultMessages resultMessages = actualBusinessException.getResultMessages();
      ResultMessageType type = resultMessages.getType();
      assertThat(type instanceof StandardResultMessageType).isTrue();
      List<ResultMessage> list = resultMessages.getList();
      assertThat(list.size()).isEqualTo(1);
      ResultMessage getResult = list.get(0);
      assertThat(getResult.getText()).isEqualTo("An error occurred");
      assertThat(actualBusinessException.getLocalizedMessage())
          .isEqualTo(
              "ResultMessages [type=error, list=[ResultMessage [code=null, args=[], text=An error occurred]]]");
      assertThat(actualBusinessException.getMessage())
          .isEqualTo(
              "ResultMessages [type=error, list=[ResultMessage [code=null, args=[], text=An error occurred]]]");
      assertThat(getResult.getCode()).isNull();
      assertThat(actualBusinessException.getCause()).isNull();
      assertThat(actualBusinessException.getSuppressed().length).isEqualTo(0);
      assertThat(getResult.getArgs().length).isEqualTo(0);
      assertThat(type).isEqualTo(StandardResultMessageType.ERROR);
      assertThat(resultMessages.isNotEmpty()).isTrue();
    }

    /**
     * Method under test: {@link BusinessException#BusinessException(ResultMessages)}
     */
    @Test
    void constructor2() {
      // Arrange
      ResultMessages messages = ResultMessages.danger();

      // Act
      BusinessException actualBusinessException = new BusinessException(messages);

      // Assert
      assertThat(actualBusinessException.getLocalizedMessage())
          .isEqualTo("ResultMessages [type=danger, list=[]]");
      assertThat(actualBusinessException.getMessage())
          .isEqualTo("ResultMessages [type=danger, list=[]]");
      assertThat(actualBusinessException.getCause())
          .isNull();
      assertThat(actualBusinessException.getSuppressed().length)
          .isEqualTo(0);
      assertThat(actualBusinessException.getResultMessages())
          .isSameAs(messages);
    }

    /**
     * Method under test: {@link BusinessException#BusinessException(ResultMessages, Throwable)}
     */
    @Test
    void constructor3() {
      // Arrange
      ResultMessages messages = ResultMessages.danger();
      Throwable cause = new Throwable();

      // Act
      BusinessException actualBusinessException = new BusinessException(messages, cause);

      // Assert
      assertThat(actualBusinessException.getLocalizedMessage()).isEqualTo(
          "ResultMessages [type=danger, list=[]]");
      assertThat(actualBusinessException.getMessage()).isEqualTo(
          "ResultMessages [type=danger, list=[]]");
      assertThat(actualBusinessException.getSuppressed().length).isEqualTo(0);
      assertThat(actualBusinessException.getCause()).isSameAs(cause);
      assertThat(actualBusinessException.getResultMessages()).isSameAs(messages);
    }

    @Test
    public void constructorTest() {
      String message = "Business Exception Message";
      BusinessException exception = new BusinessException(message);
      assertThat(exception).isNotNull();
      assertThat(exception.getResultMessages().getList().getFirst().getText()).isEqualTo(message);
    }

    @Test
    public void constructorWithResultMessages() {
      ResultMessages messages = ResultMessages.error()
          .add(ResultMessage.fromText("Business Exception Message"));
      BusinessException exception = new BusinessException(messages);
      assertThat(exception).isNotNull();
      assertThat(exception.getResultMessages()).isEqualTo(messages);
    }

    @Test
    public void constructorWithResultMessagesAndThrowable() {
      ResultMessages messages = ResultMessages.error()
          .add(ResultMessage.fromText("Business Exception Message"));
      Throwable cause = new Throwable("Cause");
      BusinessException exception = new BusinessException(messages, cause);
      assertThat(exception).isNotNull();
      assertThat(exception.getResultMessages()).isEqualTo(messages);
      assertThat(exception.getCause()).isEqualTo(cause);
    }

  }
}
