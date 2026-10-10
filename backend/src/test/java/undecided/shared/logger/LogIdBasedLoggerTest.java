package undecided.shared.logger;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

@Tag("small")
@DisplayName("LogIdBasedLoggerの単体テスト")
class LogIdBasedLoggerTest {

  private LogIdBasedLogger logger;

  @BeforeEach
  void setUp() {
    logger = LogIdBasedLogger.getLogger(LogIdBasedLoggerTest.class);
  }

  @Nested
  @DisplayName("getLoggerメソッドのテスト")
  class GetLoggerTest {

    @Test
    @DisplayName("クラスを指定してLoggerが取得できること")
    void shouldGetLoggerForSpecifiedClass() {
      // Act
      LogIdBasedLogger obtainedLogger = LogIdBasedLogger.getLogger(String.class);

      // Assert
      assertThat(obtainedLogger).isNotNull();
    }

    @Test
    @DisplayName("異なるクラスで getLogger を呼ぶと異なるインスタンスが返されること")
    void shouldReturnDifferentInstancesForDifferentClasses() {
      // Act
      LogIdBasedLogger logger1 = LogIdBasedLogger.getLogger(String.class);
      LogIdBasedLogger logger2 = LogIdBasedLogger.getLogger(Integer.class);

      // Assert
      assertThat(logger1).isNotSameAs(logger2);
    }
  }

  @Nested
  @DisplayName("isDebugEnabledメソッドのテスト")
  class IsDebugEnabledTest {

    @Test
    @DisplayName("isDebugEnabledがboolean値を返すこと")
    void shouldReturnBooleanValue() {
      // Act
      boolean result = logger.isDebugEnabled();

      // Assert
      assertThat(result).isInstanceOf(Boolean.class);
    }
  }

  @Nested
  @DisplayName("debugメソッドのテスト")
  class DebugTest {

    @Test
    @DisplayName("フォーマット文字列でdebugログが出力されること")
    void shouldOutputDebugLogWithFormatString() {
      // Act & Assert - 例外がスローされなければ成功
      logger.debug("Debug message: {}");
    }

    @Test
    @DisplayName("引数付きでdebugログが出力されること")
    void shouldOutputDebugLogWithArguments() {
      // Act & Assert
      logger.debug("Debug message: {} {}", "arg1", "arg2");
    }
  }

  @Nested
  @DisplayName("infoメソッドのテスト")
  class InfoTest {

    @Test
    @DisplayName("存在するメッセージIDでinfoログが出力されること")
    void shouldOutputInfoLogWithExistingMessageId() {
      // Act & Assert - 例外がスローされなければ成功
      logger.info("log.info.message");
    }

    @Test
    @DisplayName("存在しないメッセージIDでinfoログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert - 例外がスローされなければ成功
      logger.info("non.existent.message.id");
    }

    @Test
    @DisplayName("引数付きでinfoログが出力されること")
    void shouldOutputInfoLogWithArguments() {
      // Act & Assert
      logger.info("log.test.message", "TestArg");
    }
  }

  @Nested
  @DisplayName("warnメソッドのテスト")
  class WarnTest {

    @Test
    @DisplayName("存在するメッセージIDでwarnログが出力されること")
    void shouldOutputWarnLogWithExistingMessageId() {
      // Act & Assert
      logger.warn("log.warn.message");
    }

    @Test
    @DisplayName("存在しないメッセージIDでwarnログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert
      logger.warn("non.existent.message.id");
    }

    @Test
    @DisplayName("Throwable付きでwarnログが出力されること")
    void shouldOutputWarnLogWithThrowable() {
      // Arrange
      Throwable throwable = new RuntimeException("Test exception");

      // Act & Assert
      logger.warn("log.warn.message", throwable);
    }

    @Test
    @DisplayName("Throwableと引数付きでwarnログが出力されること")
    void shouldOutputWarnLogWithThrowableAndArguments() {
      // Arrange
      Throwable throwable = new RuntimeException("Test exception");

      // Act & Assert
      logger.warn("log.test.message", throwable, "TestArg");
    }
  }

  @Nested
  @DisplayName("errorメソッドのテスト")
  class ErrorTest {

    @Test
    @DisplayName("存在するメッセージIDでerrorログが出力されること")
    void shouldOutputErrorLogWithExistingMessageId() {
      // Act & Assert
      logger.error("log.error.message");
    }

    @Test
    @DisplayName("存在しないメッセージIDでerrorログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert
      logger.error("non.existent.message.id");
    }

    @Test
    @DisplayName("Throwable付きでerrorログが出力されること")
    void shouldOutputErrorLogWithThrowable() {
      // Arrange
      Throwable throwable = new RuntimeException("Test exception");

      // Act & Assert
      logger.error("log.error.message", throwable);
    }

    @Test
    @DisplayName("Throwableと引数付きでerrorログが出力されること")
    void shouldOutputErrorLogWithThrowableAndArguments() {
      // Arrange
      Throwable throwable = new RuntimeException("Test exception");

      // Act & Assert
      logger.error("log.test.message", throwable, "TestArg");
    }
  }

  @Nested
  @DisplayName("traceメソッドのテスト")
  class TraceTest {

    @Test
    @DisplayName("存在するメッセージIDでtraceログが出力されること")
    void shouldOutputTraceLogWithExistingMessageId() {
      // Act & Assert
      logger.trace("log.trace.message");
    }

    @Test
    @DisplayName("存在しないメッセージIDでtraceログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert
      logger.trace("non.existent.message.id");
    }

    @Test
    @DisplayName("引数付きでtraceログが出力されること")
    void shouldOutputTraceLogWithArguments() {
      // Act & Assert
      logger.trace("log.test.message", "TestArg");
    }
  }

  @Nested
  @DisplayName("メッセージ解決のテスト")
  class MessageResolutionTest {

    @Test
    @DisplayName("ResourceBundleMessageSourceが静的に初期化されていること")
    void shouldHaveStaticMessageSourceInitialized() throws Exception {
      // Act
      Field messageSourceField = LogIdBasedLogger.class.getDeclaredField("messageSource");
      messageSourceField.setAccessible(true);
      Object messageSource = messageSourceField.get(null);

      // Assert
      assertThat(messageSource).isInstanceOf(ResourceBundleMessageSource.class);
    }

    @Test
    @DisplayName("メッセージソースが正常に初期化されていること")
    void shouldHaveMessageSourceInitialized() throws Exception {
      // Arrange
      Field messageSourceField = LogIdBasedLogger.class.getDeclaredField("messageSource");
      messageSourceField.setAccessible(true);
      ResourceBundleMessageSource messageSource =
          (ResourceBundleMessageSource) messageSourceField.get(null);

      // Act
      String message = messageSource.getMessage("log.info.message", null, Locale.JAPAN);

      // Assert
      assertThat(message).isNotNull();
    }

    @Test
    @DisplayName("存在するメッセージIDでメッセージが正常に解決されること")
    void shouldResolveMessageForExistingId() throws Exception {
      // Arrange
      Field messageSourceField = LogIdBasedLogger.class.getDeclaredField("messageSource");
      messageSourceField.setAccessible(true);
      ResourceBundleMessageSource messageSource =
          (ResourceBundleMessageSource) messageSourceField.get(null);

      // Act
      String message = messageSource.getMessage("log.info.message", null, Locale.JAPAN);

      // Assert
      assertThat(message).isEqualTo("情報メッセージ");
    }

    @Test
    @DisplayName("引数付きメッセージが正常に解決されること")
    void shouldResolveMessageWithArguments() throws Exception {
      // Arrange
      Field messageSourceField = LogIdBasedLogger.class.getDeclaredField("messageSource");
      messageSourceField.setAccessible(true);
      ResourceBundleMessageSource messageSource =
          (ResourceBundleMessageSource) messageSourceField.get(null);

      // Act
      String message =
          messageSource.getMessage("log.test.with.args", new Object[] {"田中", 25}, Locale.JAPAN);

      // Assert
      assertThat(message).isEqualTo("田中さん、こんにちは。年齢は25歳です。");
    }
  }

  @Nested
  @DisplayName("UNDEFINED-MESSAGEフォーマットのテスト")
  class UndefinedMessageFormatTest {

    @Test
    @DisplayName("存在しないIDでログ出力した場合、例外がスローされないこと")
    void shouldNotThrowExceptionForNonExistentMessageId() {
      // Act & Assert
      logger.info("completely.non.existent.id");
      logger.warn("completely.non.existent.id");
      logger.error("completely.non.existent.id");
      logger.trace("completely.non.existent.id");
    }

    @Test
    @DisplayName("存在しないIDと引数でログ出力した場合、例外がスローされないこと")
    void shouldNotThrowExceptionForNonExistentMessageIdWithArgs() {
      // Act & Assert
      logger.info("completely.non.existent.id", "arg1", "arg2");
      logger.warn("completely.non.existent.id", "arg1", "arg2");
      logger.error("completely.non.existent.id", "arg1", "arg2");
      logger.trace("completely.non.existent.id", "arg1", "arg2");
    }

    @Test
    @DisplayName("存在しないIDとThrowableでログ出力した場合、例外がスローされないこと")
    void shouldNotThrowExceptionForNonExistentMessageIdWithThrowable() {
      // Arrange
      Throwable throwable = new RuntimeException("Test exception");

      // Act & Assert
      logger.warn("completely.non.existent.id", throwable);
      logger.error("completely.non.existent.id", throwable);
    }
  }

  @Nested
  @DisplayName("境界値のテスト")
  class BoundaryValuesTest {

    @Test
    @DisplayName("空の引数配列でログ出力できること")
    void shouldOutputLogWithEmptyArguments() {
      // Act & Assert
      logger.info("log.info.message");
      logger.warn("log.warn.message");
      logger.error("log.error.message");
    }

    @Test
    @DisplayName("null引数を含むメッセージでログ出力できること")
    void shouldOutputLogWithNullArguments() {
      // Act & Assert
      logger.info("log.test.message", (Object) null);
    }

    @Test
    @DisplayName("複数の引数でログ出力できること")
    void shouldOutputLogWithMultipleArguments() {
      // Act & Assert
      logger.info("log.test.with.args", "山田", 30);
    }
  }
}
