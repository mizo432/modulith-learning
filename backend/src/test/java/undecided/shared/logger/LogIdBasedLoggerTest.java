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

  /** 各テストの前にテストクラス用のロガーを取得します。 */
  @BeforeEach
  void setUp() {
    logger = LogIdBasedLogger.getLogger(LogIdBasedLoggerTest.class);
  }

  @Nested
  @DisplayName("getLoggerメソッドのテスト")
  class GetLoggerTest {

    /** クラスを指定してLoggerが取得できることを検証します。 */
    @Test
    @DisplayName("クラスを指定してLoggerが取得できること")
    void shouldGetLoggerForSpecifiedClass() {
      // Act
      LogIdBasedLogger obtainedLogger = LogIdBasedLogger.getLogger(String.class);

      // Assert
      assertThat(obtainedLogger).isNotNull();
    }

    /** 異なるクラスで getLogger を呼ぶと異なるインスタンスが返されることを検証します。 */
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

    /** isDebugEnabledがboolean値を返すことを検証します。 */
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

    /** フォーマット文字列でdebugログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("フォーマット文字列でdebugログが出力されること")
    void shouldOutputDebugLogWithFormatString() {
      // Act & Assert - 例外がスローされなければ成功
      logger.debug("Debug message: {}");
    }

    /** 引数付きでdebugログ呼び出しが例外なく完了することを検証します。 */
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

    /** 存在するメッセージIDでinfoログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在するメッセージIDでinfoログが出力されること")
    void shouldOutputInfoLogWithExistingMessageId() {
      // Act & Assert - 例外がスローされなければ成功
      logger.info("log.info.message");
    }

    /** 未定義のメッセージIDでもログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在しないメッセージIDでinfoログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert - 例外がスローされなければ成功
      logger.info("non.existent.message.id");
    }

    /** 引数付きでinfoログ呼び出しが例外なく完了することを検証します。 */
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

    /** 存在するメッセージIDでwarnログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在するメッセージIDでwarnログが出力されること")
    void shouldOutputWarnLogWithExistingMessageId() {
      // Act & Assert
      logger.warn("log.warn.message");
    }

    /** 未定義のメッセージIDでもログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在しないメッセージIDでwarnログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert
      logger.warn("non.existent.message.id");
    }

    /** Throwable付きでwarnログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("Throwable付きでwarnログが出力されること")
    void shouldOutputWarnLogWithThrowable() {
      // Arrange
      Throwable throwable = new RuntimeException("Test exception");

      // Act & Assert
      logger.warn("log.warn.message", throwable);
    }

    /** Throwableと引数付きでwarnログ呼び出しが例外なく完了することを検証します。 */
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

    /** 存在するメッセージIDでerrorログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在するメッセージIDでerrorログが出力されること")
    void shouldOutputErrorLogWithExistingMessageId() {
      // Act & Assert
      logger.error("log.error.message");
    }

    /** 未定義のメッセージIDでもログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在しないメッセージIDでerrorログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert
      logger.error("non.existent.message.id");
    }

    /** Throwable付きでerrorログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("Throwable付きでerrorログが出力されること")
    void shouldOutputErrorLogWithThrowable() {
      // Arrange
      Throwable throwable = new RuntimeException("Test exception");

      // Act & Assert
      logger.error("log.error.message", throwable);
    }

    /** Throwableと引数付きでerrorログ呼び出しが例外なく完了することを検証します。 */
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

    /** 存在するメッセージIDでtraceログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在するメッセージIDでtraceログが出力されること")
    void shouldOutputTraceLogWithExistingMessageId() {
      // Act & Assert
      logger.trace("log.trace.message");
    }

    /** 未定義のメッセージIDでもログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("存在しないメッセージIDでtraceログが出力される場合、UNDEFINED-MESSAGEが使用されること")
    void shouldOutputUndefinedMessageWhenMessageIdNotFound() {
      // Act & Assert
      logger.trace("non.existent.message.id");
    }

    /** 引数付きでtraceログ呼び出しが例外なく完了することを検証します。 */
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

    /**
     * 静的なメッセージソースがResourceBundleMessageSourceであることを検証します。
     *
     * @throws Exception メッセージソースへのリフレクションによるアクセスに失敗した場合
     */
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

    /**
     * 初期化済みのメッセージソースから情報メッセージを取得できることを検証します。
     *
     * @throws Exception メッセージソースへのリフレクションによるアクセスに失敗した場合
     */
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

    /**
     * 存在するメッセージIDでメッセージが正常に解決されることを検証します。
     *
     * @throws Exception メッセージソースへのリフレクションによるアクセスに失敗した場合
     */
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

    /**
     * 引数付きメッセージが正常に解決されることを検証します。
     *
     * @throws Exception メッセージソースへのリフレクションによるアクセスに失敗した場合
     */
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

    /** 存在しないIDでログ出力した場合、例外がスローされないことを検証します。 */
    @Test
    @DisplayName("存在しないIDでログ出力した場合、例外がスローされないこと")
    void shouldNotThrowExceptionForNonExistentMessageId() {
      // Act & Assert
      logger.info("completely.non.existent.id");
      logger.warn("completely.non.existent.id");
      logger.error("completely.non.existent.id");
      logger.trace("completely.non.existent.id");
    }

    /** 存在しないIDと引数でログ出力した場合、例外がスローされないことを検証します。 */
    @Test
    @DisplayName("存在しないIDと引数でログ出力した場合、例外がスローされないこと")
    void shouldNotThrowExceptionForNonExistentMessageIdWithArgs() {
      // Act & Assert
      logger.info("completely.non.existent.id", "arg1", "arg2");
      logger.warn("completely.non.existent.id", "arg1", "arg2");
      logger.error("completely.non.existent.id", "arg1", "arg2");
      logger.trace("completely.non.existent.id", "arg1", "arg2");
    }

    /** 存在しないIDとThrowableでログ出力した場合、例外がスローされないことを検証します。 */
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

    /** 空の引数配列でログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("空の引数配列でログ出力できること")
    void shouldOutputLogWithEmptyArguments() {
      // Act & Assert
      logger.info("log.info.message");
      logger.warn("log.warn.message");
      logger.error("log.error.message");
    }

    /** null引数を含むメッセージでログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("null引数を含むメッセージでログ出力できること")
    void shouldOutputLogWithNullArguments() {
      // Act & Assert
      logger.info("log.test.message", (Object) null);
    }

    /** 複数の引数でログ呼び出しが例外なく完了することを検証します。 */
    @Test
    @DisplayName("複数の引数でログ出力できること")
    void shouldOutputLogWithMultipleArguments() {
      // Act & Assert
      logger.info("log.test.with.args", "山田", 30);
    }
  }
}
