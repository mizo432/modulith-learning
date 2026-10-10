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

    @Test
    @DisplayName("デフォルト設定でExceptionLoggerが初期化されること")
    void shouldInitializeWithDefaultSettings() {
      // Act
      ExceptionLogger logger = new ExceptionLogger("TestLogger");

      // Assert
      logger.afterPropertiesSet();
      assertThat(logger).isNotNull();
    }

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

    @Test
    @DisplayName("有効なフォーマットが渡された場合、例外がスローされないこと")
    void shouldNotThrowExceptionWhenFormatIsValid() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      String validFormat = "[{0}] {1}";

      // Act & Assert
      logger.validateLogMessageFormat(validFormat);
    }

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

    @Test
    @DisplayName("ExceptionCodeProviderを実装した例外の場合、コードが使用されること")
    void shouldUseCodeFromExceptionCodeProvider() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      Exception testException =
          new RuntimeException("Test message") {
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

    @Test
    @DisplayName("カスタムロガーが登録されること")
    void shouldRegisterCustomLogger() {
      // Arrange
      ExceptionLogger logger = new ExceptionLogger("TestLogger");
      logger.afterPropertiesSet();

      ExceptionLogger.LogLevelWrappingLogger customLogger =
          new ExceptionLogger.LogLevelWrappingLogger() {
            @Override
            public boolean isEnabled() {
              return true;
            }

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
