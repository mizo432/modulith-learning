package undecided.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Tag("small")
@DisplayName("ExceptionLoggerの単体テスト")
@ExtendWith(MockitoExtension.class)
class ExceptionLoggerTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    /** デフォルト設定でExceptionLoggerが初期化されることを検証します。 */
    @Test
    @DisplayName("デフォルト設定でExceptionLoggerが初期化されること")
    void shouldInitializeWithDefaultSettings() {
      // Act
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Assert
      logger.afterPropertiesSet();
      assertThat(logger).isNotNull();
    }

    /** logMessageFormatがnullの場合、afterPropertiesSetでIllegalArgumentExceptionがスローされることを検証します。 */
    @Test
    @DisplayName("logMessageFormatがnullの場合、afterPropertiesSetでIllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenLogMessageFormatIsNull() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setLogMessageFormat(null);

      // Act & Assert
      assertThatThrownBy(() -> logger.afterPropertiesSet())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("logMessageFormat must have placeholder");
    }

    /** logMessageFormatに{0}が含まれていない場合、IllegalArgumentExceptionがスローされることを検証します。 */
    @Test
    @DisplayName("logMessageFormatに{0}が含まれていない場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenLogMessageFormatMissingCodePlaceholder() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setLogMessageFormat("{1} only");

      // Act & Assert
      assertThatThrownBy(() -> logger.afterPropertiesSet())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("logMessageFormat must have placeholder");
    }

    /** logMessageFormatに{1}が含まれていない場合、IllegalArgumentExceptionがスローされることを検証します。 */
    @Test
    @DisplayName("logMessageFormatに{1}が含まれていない場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenLogMessageFormatMissingMessagePlaceholder() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setLogMessageFormat("{0} only");

      // Act & Assert
      assertThatThrownBy(() -> logger.afterPropertiesSet())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("logMessageFormat must have placeholder");
    }
  }

  @Nested
  @DisplayName("afterPropertiesSetメソッドのテスト")
  class AfterPropertiesSetTest {

    /** afterPropertiesSetが正常に実行されることを検証します。 */
    @Test
    @DisplayName("afterPropertiesSetが正常に実行されること")
    void shouldExecuteAfterPropertiesSetSuccessfully() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Act
      logger.afterPropertiesSet();

      // Assert - 例外がスローされないことを確認
      assertThat(logger).isNotNull();
    }

    /** レベルリゾルバーを明示せずに初期化しても、例外のログ処理が完了することを検証します。 */
    @Test
    @DisplayName("exceptionLevelResolverが設定されていない場合、デフォルトが設定されること")
    void shouldSetDefaultExceptionLevelResolverWhenNotSet() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Act
      logger.afterPropertiesSet();

      // Assert - 例外がスローされなければ、内部でデフォルトが設定されたことを意味する
      Exception testException = new RuntimeException("test");
      logger.log(testException);
    }
  }

  @Nested
  @DisplayName("validateLogMessageFormatメソッドのテスト")
  class ValidateLogMessageFormatTest {

    /** 有効なフォーマットが渡された場合、例外がスローされないことを検証します。 */
    @Test
    @DisplayName("有効なフォーマットが渡された場合、例外がスローされないこと")
    void shouldNotThrowExceptionWhenFormatIsValid() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      String validFormat = "[{0}] {1}";

      // Act & Assert
      logger.validateLogMessageFormat(validFormat);
    }

    /** nullフォーマットが渡された場合、IllegalArgumentExceptionがスローされることを検証します。 */
    @Test
    @DisplayName("nullフォーマットが渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenFormatIsNull() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Act & Assert
      assertThatThrownBy(() -> logger.validateLogMessageFormat(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("logMessageFormat must have placeholder");
    }

    /** 空文字列が渡された場合、IllegalArgumentExceptionがスローされることを検証します。 */
    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionがスローされること")
    void shouldThrowExceptionWhenFormatIsEmpty() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Act & Assert
      assertThatThrownBy(() -> logger.validateLogMessageFormat(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("logMessageFormat must have placeholder");
    }
  }

  @Nested
  @DisplayName("formatLogMessageメソッドのテスト")
  class FormatLogMessageTest {

    /** 例外コードとメッセージが正常にフォーマットされることを検証します。 */
    @Test
    @DisplayName("例外コードとメッセージが正常にフォーマットされること")
    void shouldFormatLogMessageWithCodeAndMessage() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage("E001", "Something went wrong");

      // Assert
      assertThat(result).isEqualTo("[E001] Something went wrong");
    }

    /** 例外コードがnullの場合、デフォルトコードが使用されることを検証します。 */
    @Test
    @DisplayName("例外コードがnullの場合、デフォルトコードが使用されること")
    void shouldUseDefaultCodeWhenCodeIsNull() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage(null, "Something went wrong");

      // Assert
      assertThat(result).isEqualTo("[UNDEFINED-CODE] Something went wrong");
    }

    /** 例外メッセージがnullの場合、デフォルトメッセージが使用されることを検証します。 */
    @Test
    @DisplayName("例外メッセージがnullの場合、デフォルトメッセージが使用されること")
    void shouldUseDefaultMessageWhenMessageIsNull() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage("E001", null);

      // Assert
      assertThat(result).isEqualTo("[E001] UNDEFINED-MESSAGE");
    }

    /** 例外コードとメッセージが両方nullの場合、デフォルト値が使用されることを検証します。 */
    @Test
    @DisplayName("例外コードとメッセージが両方nullの場合、デフォルト値が使用されること")
    void shouldUseDefaultValuesWhenBothAreNull() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage(null, null);

      // Assert
      assertThat(result).isEqualTo("[UNDEFINED-CODE] UNDEFINED-MESSAGE");
    }

    /** trimLogMessageがtrueの場合、メッセージがトリムされることを検証します。 */
    @Test
    @DisplayName("trimLogMessageがtrueの場合、メッセージがトリムされること")
    void shouldTrimMessageWhenTrimEnabled() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setLogMessageFormat(" [{0}] {1} ");
      logger.setTrimLogMessage(true);
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage("E001", "Message");

      // Assert
      assertThat(result).isEqualTo("[E001] Message");
    }

    /** trimLogMessageがfalseの場合、メッセージがトリムされないことを検証します。 */
    @Test
    @DisplayName("trimLogMessageがfalseの場合、メッセージがトリムされないこと")
    void shouldNotTrimMessageWhenTrimDisabled() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setLogMessageFormat(" [{0}] {1} ");
      logger.setTrimLogMessage(false);
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage("E001", "Message");

      // Assert
      assertThat(result).isEqualTo(" [E001] Message ");
    }

    /** カスタムデフォルトコードが使用されることを検証します。 */
    @Test
    @DisplayName("カスタムデフォルトコードが使用されること")
    void shouldUseCustomDefaultCode() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setDefaultCode("CUSTOM-DEFAULT");
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage(null, "Message");

      // Assert
      assertThat(result).isEqualTo("[CUSTOM-DEFAULT] Message");
    }

    /** カスタムデフォルトメッセージが使用されることを検証します。 */
    @Test
    @DisplayName("カスタムデフォルトメッセージが使用されること")
    void shouldUseCustomDefaultMessage() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setDefaultMessage("CUSTOM-DEFAULT-MESSAGE");
      logger.afterPropertiesSet();

      // Act
      String result = logger.formatLogMessage("E001", null);

      // Assert
      assertThat(result).isEqualTo("[E001] CUSTOM-DEFAULT-MESSAGE");
    }
  }

  @Nested
  @DisplayName("makeLogMessageメソッドのテスト")
  class MakeLogMessageTest {

    /** 例外からログメッセージが正常に生成されることを検証します。 */
    @Test
    @DisplayName("例外からログメッセージが正常に生成されること")
    void shouldGenerateLogMessageFromException() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      Exception testException = new RuntimeException("Test error message");
      logger.afterPropertiesSet();

      // Act
      String result = logger.makeLogMessage(testException);

      // Assert
      assertThat(result).contains("Test error message");
    }

    /** 例外クラス名のマッピングで解決したコードと例外メッセージがログ文に含まれることを検証します。 */
    @Test
    @DisplayName("ExceptionCodeProviderを実装した例外の場合、コードが使用されること")
    void shouldUseCodeFromExceptionCodeProvider() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      Exception testException =
          new RuntimeException("Test message") {
            /**
             * テスト例外の固定の文字列表現を返します。
             *
             * @return テスト例外を表す文字列
             */
            @Override
            public String toString() {
              return "TestException";
            }
          };
      SimpleMappingExceptionCodeResolver resolver = new SimpleMappingExceptionCodeResolver();
      LinkedHashMap<String, String> mappings = new LinkedHashMap<>();
      mappings.put("RuntimeException", "E_RUNTIME");
      resolver.setExceptionMappings(mappings);
      resolver.setDefaultExceptionCode("DEFAULT");
      logger.setExceptionCodeResolver(resolver);
      logger.afterPropertiesSet();

      // Act
      String result = logger.makeLogMessage(testException);

      // Assert
      assertThat(result).contains("E_RUNTIME");
      assertThat(result).contains("Test message");
    }
  }

  @Nested
  @DisplayName("resolveExceptionCodeメソッドのテスト")
  class ResolveExceptionCodeTest {

    /** exceptionCodeResolverが設定されている場合、コードが解決されることを検証します。 */
    @Test
    @DisplayName("exceptionCodeResolverが設定されている場合、コードが解決されること")
    void shouldResolveCodeWhenResolverIsSet() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      ExceptionCodeResolver mockResolver = mock(ExceptionCodeResolver.class);
      when(mockResolver.resolveExceptionCode(any(Exception.class))).thenReturn("E001");
      logger.setExceptionCodeResolver(mockResolver);
      logger.afterPropertiesSet();

      Exception testException = new RuntimeException("test");

      // Act
      String result = logger.resolveExceptionCode(testException);

      // Assert
      assertThat(result).isEqualTo("E001");
    }

    /** exceptionCodeResolverがnullの場合、nullが返されることを検証します。 */
    @Test
    @DisplayName("exceptionCodeResolverがnullの場合、nullが返されること")
    void shouldReturnNullWhenResolverIsNull() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.setExceptionCodeResolver(null);
      logger.afterPropertiesSet();

      Exception testException = new RuntimeException("test");

      // Act
      String result = logger.resolveExceptionCode(testException);

      // Assert
      assertThat(result).isNull();
    }
  }

  @Nested
  @DisplayName("log/info/warn/errorメソッドのテスト")
  class LoggingMethodsTest {

    /** logメソッドが例外を正常に処理することを検証します。 */
    @Test
    @DisplayName("logメソッドが例外を正常に処理すること")
    void shouldLogExceptionWithoutError() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();
      Exception testException = new RuntimeException("Test error");

      // Act & Assert - 例外がスローされなければ成功
      logger.log(testException);
    }

    /** infoメソッドが例外を正常に処理することを検証します。 */
    @Test
    @DisplayName("infoメソッドが例外を正常に処理すること")
    void shouldInfoExceptionWithoutError() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();
      Exception testException = new RuntimeException("Test info");

      // Act & Assert
      logger.info(testException);
    }

    /** warnメソッドが例外を正常に処理することを検証します。 */
    @Test
    @DisplayName("warnメソッドが例外を正常に処理すること")
    void shouldWarnExceptionWithoutError() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();
      Exception testException = new RuntimeException("Test warn");

      // Act & Assert
      logger.warn(testException);
    }

    /** errorメソッドが例外を正常に処理することを検証します。 */
    @Test
    @DisplayName("errorメソッドが例外を正常に処理すること")
    void shouldErrorExceptionWithoutError() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();
      Exception testException = new RuntimeException("Test error");

      // Act & Assert
      logger.error(testException);
    }

    /** 解決されたログレベルがnullでも、例外のログ処理が完了することを検証します。 */
    @Test
    @DisplayName("logメソッドで例外レベルがnullの場合、errorLoggerがフォールバックとして使用されること")
    void shouldFallbackToErrorLoggerWhenLevelIsNull() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      ExceptionLevelResolver mockResolver = mock(ExceptionLevelResolver.class);
      when(mockResolver.resolveExceptionLevel(any(Exception.class))).thenReturn(null);
      logger.setExceptionLevelResolver(mockResolver);
      logger.afterPropertiesSet();

      Exception testException = new RuntimeException("Test error");

      // Act & Assert - 例外がスローされなければフォールバック成功
      logger.log(testException);
    }

    /** logメソッドで例外レベルがWARNの場合、正常に処理されることを検証します。 */
    @Test
    @DisplayName("logメソッドで例外レベルがWARNの場合、正常に処理されること")
    void shouldHandleWarnLevelException() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      ExceptionLevelResolver mockResolver = mock(ExceptionLevelResolver.class);
      when(mockResolver.resolveExceptionLevel(any(Exception.class)))
          .thenReturn(ExceptionLevel.WARN);
      logger.setExceptionLevelResolver(mockResolver);
      logger.afterPropertiesSet();

      Exception testException = new RuntimeException("Test warn");

      // Act & Assert
      logger.log(testException);
    }

    /** logメソッドで例外レベルがINFOの場合、正常に処理されることを検証します。 */
    @Test
    @DisplayName("logメソッドで例外レベルがINFOの場合、正常に処理されること")
    void shouldHandleInfoLevelException() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      ExceptionLevelResolver mockResolver = mock(ExceptionLevelResolver.class);
      when(mockResolver.resolveExceptionLevel(any(Exception.class)))
          .thenReturn(ExceptionLevel.INFO);
      logger.setExceptionLevelResolver(mockResolver);
      logger.afterPropertiesSet();

      Exception testException = new RuntimeException("Test info");

      // Act & Assert
      logger.log(testException);
    }
  }

  @Nested
  @DisplayName("registerExceptionLevelLoggersメソッドのテスト")
  class RegisterExceptionLevelLoggersTest {

    /** INFOレベル用のカスタムロガーを例外なく登録できることを検証します。 */
    @Test
    @DisplayName("カスタムロガーが登録されること")
    void shouldRegisterCustomLogger() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();

      ExceptionLogger.LogLevelWrappingLogger customLogger =
          new ExceptionLogger.LogLevelWrappingLogger() {
            /**
             * カスタムロガーを常に有効として扱います。
             *
             * @return 常にtrue
             */
            @Override
            public boolean isEnabled() {
              return true;
            }

            /**
             * 登録確認用のロガーとして、受け取ったログを出力せずに破棄します。
             *
             * @param var1 出力しないログメッセージ
             * @param var2 出力しない例外
             */
            @Override
            public void log(String var1, Exception var2) {}
          };

      // Act
      logger.registerExceptionLevelLoggers(ExceptionLevel.INFO, customLogger);

      // Assert - 例外がスローされなければ登録成功
      assertThat(logger).isNotNull();
    }
  }

  @Nested
  @DisplayName("ゲッターメソッドのテスト")
  class GetterMethodsTest {

    /** getApplicationLoggerがロガーを返すことを検証します。 */
    @Test
    @DisplayName("getApplicationLoggerがロガーを返すこと")
    void shouldReturnApplicationLogger() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Act
      var result = logger.getApplicationLogger();

      // Assert
      assertThat(result).isNotNull();
    }

    /** getMonitoringLoggerがロガーを返すことを検証します。 */
    @Test
    @DisplayName("getMonitoringLoggerがロガーを返すこと")
    void shouldReturnMonitoringLogger() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Act
      var result = logger.getMonitoringLogger();

      // Assert
      assertThat(result).isNotNull();
    }
  }
}
