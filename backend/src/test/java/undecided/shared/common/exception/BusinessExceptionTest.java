package undecided.shared.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import undecided.shared.common.message.ResultMessage;
import undecided.shared.common.message.ResultMessageType;
import undecided.shared.common.message.ResultMessages;
import undecided.shared.common.message.StandardResultMessageType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("small")
@DisplayName("BusinessException のテスト")
class BusinessExceptionTest {
    Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessExceptionTest.class);

    @Nested
    @DisplayName("コンストラクタのテスト")
    class ConstructorTest {

        /**
         * Method under test: {@link BusinessException#BusinessException(String)}
         */
        @Test
        @DisplayName("文字列からBusinessExceptionを生成できること")
        void shouldCreateBusinessExceptionFromString() {
            // Arrange and Act
            BusinessException actualBusinessException = new BusinessException("An error occurred");

            // Assert
            ResultMessages resultMessages = actualBusinessException.getResultMessages();
            ResultMessageType type = resultMessages.getType();
            assertThat(type instanceof StandardResultMessageType).isTrue();
            List<ResultMessage> list = resultMessages.getList();
            assertThat(list.size()).isEqualTo(1);
            ResultMessage getResult = list.getFirst();

            assertThat(getResult.text()).isEqualTo("An error occurred");
            assertThat(actualBusinessException.getMessage())
                    .isEqualTo(
                            "ResultMessages [type=error, list=[An error occurred]]");
            assertThat(getResult.code()).isNull();
            assertThat(actualBusinessException.getCause()).isNull();
            assertThat(actualBusinessException.getSuppressed().length).isEqualTo(0);
            assertThat(getResult.args().length).isEqualTo(0);
            assertThat(type).isEqualTo(StandardResultMessageType.ERROR);
            assertThat(resultMessages.isNotEmpty()).isTrue();
        }

        /**
         * Method under test: {@link BusinessException#BusinessException(ResultMessages)}
         */
        @Test
        @DisplayName("ResultMessagesからBusinessExceptionを生成できること")
        void shouldCreateBusinessExceptionFromResultMessages() {
            // Arrange
            ResultMessages messages = ResultMessages.danger();

            // Act
            BusinessException actualBusinessException = new BusinessException(messages);

            // Assert
            assertThat(actualBusinessException.getLocalizedMessage())
                    .isEqualTo("ResultMessages [type=danger, list=[]]");
            assertThat(actualBusinessException.getMessage())
                    .isEqualTo("ResultMessages [type=danger, list=[]]");
            assertThat(actualBusinessException.getCause()).isNull();
            assertThat(actualBusinessException.getSuppressed().length).isEqualTo(0);
            assertThat(actualBusinessException.getResultMessages()).isSameAs(messages);
        }

        /**
         * Method under test: {@link BusinessException#BusinessException(ResultMessages, Throwable)}
         */
        @Test
        @DisplayName("ResultMessagesと原因例外からBusinessExceptionを生成できること")
        void shouldCreateBusinessExceptionFromResultMessagesAndCause() {
            // Arrange
            ResultMessages messages = ResultMessages.danger();
            Throwable cause = new Throwable();

            // Act
            BusinessException actualBusinessException = new BusinessException(messages, cause);

            // Assert
            assertThat(actualBusinessException.getLocalizedMessage())
                    .isEqualTo("ResultMessages [type=danger, list=[]]");
            assertThat(actualBusinessException.getMessage())
                    .isEqualTo("ResultMessages [type=danger, list=[]]");
            assertThat(actualBusinessException.getSuppressed().length).isEqualTo(0);
            assertThat(actualBusinessException.getCause()).isSameAs(cause);
            assertThat(actualBusinessException.getResultMessages()).isSameAs(messages);
        }

        @Test
        @DisplayName("メッセージ指定時に例外を生成できること")
        void shouldCreateExceptionWhenMessageIsProvided() {
            String message = "Business Exception Message";
            BusinessException exception = new BusinessException(message);
            assertThat(exception).isNotNull();
            assertThat(exception.getResultMessages().getList().getFirst().text()).isEqualTo(message);
        }

        @Test
        @DisplayName("ResultMessages指定時に例外を生成できること")
        void shouldCreateExceptionWhenResultMessagesAreProvided() {
            ResultMessages messages =
                    ResultMessages.error().add(ResultMessage.fromText("Business Exception Message"));
            BusinessException exception = new BusinessException(messages);
            assertThat(exception).isNotNull();
            assertThat(exception.getResultMessages()).isEqualTo(messages);
        }

        @Test
        @DisplayName("ResultMessagesとThrowable指定時に例外を生成できること")
        void shouldCreateExceptionWhenResultMessagesAndThrowableAreProvided() {
            ResultMessages messages =
                    ResultMessages.error().add(ResultMessage.fromText("Business Exception Message"));
            Throwable cause = new Throwable("Cause");
            BusinessException exception = new BusinessException(messages, cause);
            assertThat(exception).isNotNull();
            assertThat(exception.getResultMessages()).isEqualTo(messages);
            assertThat(exception.getCause()).isEqualTo(cause);
        }
    }

    @Nested
    @DisplayName("toStringメソッドのテスト")
    class ToStringTest {
        @Test
        @DisplayName("期待どおりの文字列表現を返すこと")
        void shouldReturnExpectedToString() {
            String message = "Business Exception Message";
            BusinessException exception = new BusinessException(message);
            assertThat(exception.toString())
                    .isEqualTo("undecided.shared.common.exception.BusinessException: ResultMessages [type=error, list=[Business Exception Message]]");
            logger.warn(exception.toString(), exception);

        }

    }
}
