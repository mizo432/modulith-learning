package com.github.mizo432.shared.sensitive;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.spi.LoggingEventBuilder;

@Tag("small")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SensitiveLoggerWrapperTest {

  @Mock private Logger mockLogger;

  @Captor private ArgumentCaptor<String> stringCaptor;

  @Captor private ArgumentCaptor<Object> objectCaptor;

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("loggerのみを指定するとマスク有効のインスタンスが作成される")
    void shouldCreateWithMaskingEnabled() {
      when(mockLogger.getName()).thenReturn("test.logger");

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper)
          .as("デフォルトでマスク有効のSensitiveLoggerWrapperが作成されること")
          .isNotNull();
      assertThat(wrapper.isMaskingEnabled()).isTrue();
      assertThat(wrapper.getName()).isEqualTo("test.logger");
    }

    @Test
    @DisplayName("マスク有効でインスタンスが作成される")
    void shouldCreateWithMaskingExplicitlyEnabled() {
      when(mockLogger.getName()).thenReturn("test.logger");

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger, true);

      assertThat(wrapper.isMaskingEnabled()).isTrue();
    }

    @Test
    @DisplayName("マスク無効でインスタンスが作成される")
    void shouldCreateWithMaskingDisabled() {
      when(mockLogger.getName()).thenReturn("test.logger");

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger, false);

      assertThat(wrapper.isMaskingEnabled()).isFalse();
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenLoggerIsNull() {
      assertThatThrownBy(() -> new SensitiveLoggerWrapper(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("logger must not be null");
    }
  }

  @Nested
  @DisplayName("wrapメソッドのテスト")
  class WrapMethodTest {

    @Test
    @DisplayName("wrap(logger)でマスク有効のラッパーが作成される")
    void shouldWrapWithMaskingEnabled() {
      when(mockLogger.getName()).thenReturn("test.logger");

      Logger wrapped = SensitiveLoggerWrapper.wrap(mockLogger);

      assertThat(wrapped)
          .as("wrapメソッドでデフォルトマスク有効のラッパーが作成されること")
          .isInstanceOf(SensitiveLoggerWrapper.class);
      assertThat(((SensitiveLoggerWrapper) wrapped).isMaskingEnabled()).isTrue();
    }

    @Test
    @DisplayName("wrap(logger, true)でマスク有効のラッパーが作成される")
    void shouldWrapWithMaskingExplicitlyEnabled() {
      when(mockLogger.getName()).thenReturn("test.logger");

      Logger wrapped = SensitiveLoggerWrapper.wrap(mockLogger, true);

      assertThat(((SensitiveLoggerWrapper) wrapped).isMaskingEnabled()).isTrue();
    }

    @Test
    @DisplayName("wrap(logger, false)でマスク無効のラッパーが作成される")
    void shouldWrapWithMaskingDisabled() {
      when(mockLogger.getName()).thenReturn("test.logger");

      Logger wrapped = SensitiveLoggerWrapper.wrap(mockLogger, false);

      assertThat(((SensitiveLoggerWrapper) wrapped).isMaskingEnabled()).isFalse();
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenLoggerIsNull() {
      assertThatThrownBy(() -> SensitiveLoggerWrapper.wrap(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("logger must not be null");
    }
  }

  @Nested
  @DisplayName("getNameメソッドのテスト")
  class GetNameTest {

    @Test
    @DisplayName("内部ロガーの名前を返す")
    void shouldReturnLoggerName() {
      when(mockLogger.getName()).thenReturn("my.package.MyClass");

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.getName()).isEqualTo("my.package.MyClass");
    }
  }

  @Nested
  @DisplayName("unwrapメソッドのテスト")
  class UnwrapTest {

    @Test
    @DisplayName("内部ロガーを返す")
    void shouldReturnWrappedLogger() {
      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.unwrap()).isSameAs(mockLogger);
    }
  }

  @Nested
  @DisplayName("isDebugEnabledメソッドのテスト")
  class IsDebugEnabledTest {

    @Test
    @DisplayName("内部ロガーのisDebugEnabledを返す")
    void shouldReturnDebugEnabledStatus() {
      when(mockLogger.isDebugEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.isDebugEnabled()).isTrue();
    }

    @Test
    @DisplayName("内部ロガーがfalseを返す場合はfalseを返す")
    void shouldReturnFalseWhenDisabled() {
      when(mockLogger.isDebugEnabled()).thenReturn(false);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.isDebugEnabled()).isFalse();
    }
  }

  @Nested
  @DisplayName("isInfoEnabledメソッドのテスト")
  class IsInfoEnabledTest {

    @Test
    @DisplayName("内部ロガーのisInfoEnabledを返す")
    void shouldReturnInfoEnabledStatus() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.isInfoEnabled()).isTrue();
    }
  }

  @Nested
  @DisplayName("isWarnEnabledメソッドのテスト")
  class IsWarnEnabledTest {

    @Test
    @DisplayName("内部ロガーのisWarnEnabledを返す")
    void shouldReturnWarnEnabledStatus() {
      when(mockLogger.isWarnEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.isWarnEnabled()).isTrue();
    }
  }

  @Nested
  @DisplayName("isErrorEnabledメソッドのテスト")
  class IsErrorEnabledTest {

    @Test
    @DisplayName("内部ロガーのisErrorEnabledを返す")
    void shouldReturnErrorEnabledStatus() {
      when(mockLogger.isErrorEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.isErrorEnabled()).isTrue();
    }
  }

  @Nested
  @DisplayName("isTraceEnabledメソッドのテスト")
  class IsTraceEnabledTest {

    @Test
    @DisplayName("内部ロガーのisTraceEnabledを返す")
    void shouldReturnTraceEnabledStatus() {
      when(mockLogger.isTraceEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.isTraceEnabled()).isTrue();
    }
  }

  @Nested
  @DisplayName("infoメソッドのテスト（マスク有効時）")
  class InfoMethodWithMaskingEnabledTest {

    @Test
    @DisplayName("メールアドレスがマスクされて出力される")
    void shouldMaskEmailInMessage() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("User email: {}", "taro@example.com");

      verify(mockLogger).info(stringCaptor.capture(), objectCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("User email: {}");
      assertThat(objectCaptor.getValue())
          .as("infoログの引数に含まれるメールアドレスがマスクされること")
          .isEqualTo("t***@example.com");
    }

    @Test
    @DisplayName("電話番号がマスクされて出力される")
    void shouldMaskPhoneInMessage() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Phone: {}", "09012345678");

      verify(mockLogger).info(stringCaptor.capture(), objectCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Phone: {}");
      assertThat(objectCaptor.getValue()).isEqualTo("*******5678");
    }

    @Test
    @DisplayName("機微な情報がない場合はそのまま出力される")
    void shouldNotMaskWhenNoSensitiveData() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Hello {}", "world");

      verify(mockLogger).info(stringCaptor.capture(), objectCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Hello {}");
      assertThat(objectCaptor.getValue()).isEqualTo("world");
    }

    @Test
    @DisplayName("フォーマット文字列自体の機微な情報がマスクされる")
    void shouldMaskSensitiveDataInFormatString() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Contact taro@example.com for details");

      verify(mockLogger).info("Contact t***@example.com for details");
    }

    @Test
    @DisplayName("複数の引数で機微な情報がマスクされる")
    void shouldMaskMultipleArguments() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Email: {}, Phone: {}", "taro@example.com", "09012345678");

      verify(mockLogger).info("Email: {}, Phone: {}", "t***@example.com", "*******5678");
    }

    @Test
    @DisplayName("null引数はそのまま渡される")
    void shouldPassNullArgumentAsIs() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Value: {}", (Object) null);

      verify(mockLogger).info("Value: {}", (Object) null);
    }

    @Test
    @DisplayName("例外付きinfoでメッセージがマスクされる")
    void shouldMaskMessageWithThrowable() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      Throwable exception = new RuntimeException("error");
      wrapper.info("Failed for user taro@example.com", exception);

      verify(mockLogger).info("Failed for user t***@example.com", exception);
    }

    @Test
    @DisplayName("varargsメソッドで機微な情報がマスクされる")
    void shouldMaskInVarargs() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("A: {}, B: {}, C: {}", "a@foo.com", "b@bar.org", "09012345678");

      verify(mockLogger).info(any(String.class), any(Object[].class));
    }
  }

  @Nested
  @DisplayName("infoメソッドのテスト（マスク無効時）")
  class InfoMethodWithMaskingDisabledTest {

    @Test
    @DisplayName("メールアドレスがマスクされずに出力される")
    void shouldNotMaskEmailWhenDisabled() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger, false);
      wrapper.info("User email: {}", "taro@example.com");

      // マスク無効時はメールアドレスがマスクされずに出力されること
      verify(mockLogger).info("User email: {}", "taro@example.com");
    }

    @Test
    @DisplayName("電話番号がマスクされずに出力される")
    void shouldNotMaskPhoneWhenDisabled() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger, false);
      wrapper.info("Phone: {}", "09012345678");

      verify(mockLogger).info("Phone: {}", "09012345678");
    }
  }

  @Nested
  @DisplayName("debugメソッドのテスト")
  class DebugMethodTest {

    @Test
    @DisplayName("機微な情報がマスクされて出力される")
    void shouldMaskSensitiveData() {
      when(mockLogger.isDebugEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.debug("Debug email: {}", "test@example.com");

      verify(mockLogger).debug(stringCaptor.capture(), objectCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Debug email: {}");
      assertThat(objectCaptor.getValue()).isEqualTo("t***@example.com");
    }

    @Test
    @DisplayName("例外付きdebugでメッセージがマスクされる")
    void shouldMaskMessageWithThrowable() {
      when(mockLogger.isDebugEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      Throwable exception = new RuntimeException("debug error");
      wrapper.debug("Debug error for 09012345678", exception);

      verify(mockLogger).debug("Debug error for *******5678", exception);
    }
  }

  @Nested
  @DisplayName("warnメソッドのテスト")
  class WarnMethodTest {

    @Test
    @DisplayName("機微な情報がマスクされて出力される")
    void shouldMaskSensitiveData() {
      when(mockLogger.isWarnEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.warn("Warning: {}", "user@example.com");

      verify(mockLogger).warn(stringCaptor.capture(), objectCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Warning: {}");
      assertThat(objectCaptor.getValue()).isEqualTo("u***@example.com");
    }

    @Test
    @DisplayName("例外付きwarnでメッセージがマスクされる")
    void shouldMaskMessageWithThrowable() {
      when(mockLogger.isWarnEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      Throwable exception = new RuntimeException("warn error");
      wrapper.warn("Warning for 09012345678", exception);

      verify(mockLogger).warn("Warning for *******5678", exception);
    }
  }

  @Nested
  @DisplayName("errorメソッドのテスト")
  class ErrorMethodTest {

    @Test
    @DisplayName("機微な情報がマスクされて出力される")
    void shouldMaskSensitiveData() {
      when(mockLogger.isErrorEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.error("Error: {}", "secret@example.com");

      verify(mockLogger).error(stringCaptor.capture(), objectCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Error: {}");
      assertThat(objectCaptor.getValue()).isEqualTo("s***@example.com");
    }

    @Test
    @DisplayName("例外付きerrorでメッセージがマスクされる")
    void shouldMaskMessageWithThrowable() {
      when(mockLogger.isErrorEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      Throwable exception = new RuntimeException("error");
      wrapper.error("Error for 09012345678", exception);

      verify(mockLogger).error("Error for *******5678", exception);
    }
  }

  @Nested
  @DisplayName("traceメソッドのテスト")
  class TraceMethodTest {

    @Test
    @DisplayName("機微な情報がマスクされて出力される")
    void shouldMaskSensitiveData() {
      when(mockLogger.isTraceEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.trace("Trace: {}", "trace@example.com");

      verify(mockLogger).trace(stringCaptor.capture(), objectCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Trace: {}");
      assertThat(objectCaptor.getValue()).isEqualTo("t***@example.com");
    }
  }

  @Nested
  @DisplayName("Marker付きメソッドのテスト")
  class MarkerMethodsTest {

    @Mock private Marker mockMarker;

    @Test
    @DisplayName("Marker付きinfoで機微な情報がマスクされる")
    void shouldMaskWithMarkerInInfo() {
      when(mockLogger.isInfoEnabled(mockMarker)).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info(mockMarker, "Email: {}", "marker@example.com");

      verify(mockLogger).info(mockMarker, "Email: {}", "m***@example.com");
    }

    @Test
    @DisplayName("Marker付きerrorで機微な情報がマスクされる")
    void shouldMaskWithMarkerInError() {
      when(mockLogger.isErrorEnabled(mockMarker)).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.error(mockMarker, "Error: {}", "error@example.com");

      verify(mockLogger).error(mockMarker, "Error: {}", "e***@example.com");
    }

    @Test
    @DisplayName("Marker付きisInfoEnabledが正しく委譲される")
    void shouldDelegateIsInfoEnabledWithMarker() {
      when(mockLogger.isInfoEnabled(mockMarker)).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);

      assertThat(wrapper.isInfoEnabled(mockMarker)).isTrue();
    }
  }

  @Nested
  @DisplayName("Fluent APIのテスト")
  class FluentApiTest {

    @Mock private LoggingEventBuilder mockBuilder;

    @Test
    @DisplayName("atInfoで返されたbuilderがラップされる")
    void shouldWrapAtInfoBuilder() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().addArgument("taro@example.com").log("test");

      verify(mockBuilder).addArgument("t***@example.com");
    }

    @Test
    @DisplayName("atErrorで返されたbuilderがラップされる")
    void shouldWrapAtErrorBuilder() {
      when(mockLogger.atError()).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atError().addArgument("error@example.com").log("error test");

      verify(mockBuilder).addArgument("e***@example.com");
    }

    @Test
    @DisplayName("atDebugで返されたbuilderがラップされる")
    void shouldWrapAtDebugBuilder() {
      when(mockLogger.atDebug()).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atDebug().addArgument("debug@example.com").log("debug test");

      verify(mockBuilder).addArgument("d***@example.com");
    }

    @Test
    @DisplayName("atWarnで返されたbuilderがラップされる")
    void shouldWrapAtWarnBuilder() {
      when(mockLogger.atWarn()).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atWarn().addArgument("warn@example.com").log("warn test");

      verify(mockBuilder).addArgument("w***@example.com");
    }

    @Test
    @DisplayName("atTraceで返されたbuilderがラップされる")
    void shouldWrapAtTraceBuilder() {
      when(mockLogger.atTrace()).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atTrace().addArgument("trace@example.com").log("trace test");

      verify(mockBuilder).addArgument("t***@example.com");
    }

    @Test
    @DisplayName("makeLoggingEventBuilderで返されたbuilderがラップされる")
    void shouldWrapMakeLoggingEventBuilder() {
      when(mockLogger.makeLoggingEventBuilder(any(Level.class))).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.makeLoggingEventBuilder(Level.INFO).addArgument("builder@example.com").log("test");

      verify(mockBuilder).addArgument("b***@example.com");
    }

    @Test
    @DisplayName("builderのlogメソッドでメッセージがマスクされる")
    void shouldMaskMessageInBuilderLog() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().log("Message with email: taro@example.com");

      verify(mockBuilder).log(stringCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Message with email: t***@example.com");
    }

    @Test
    @DisplayName("builderのaddKeyValueで値がマスクされる")
    void shouldMaskValueInAddKeyValue() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);
      when(mockBuilder.addKeyValue(any(), any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().addKeyValue("email", "key@example.com").log("test");

      verify(mockBuilder).addKeyValue("email", "k***@example.com");
    }

    @Test
    @DisplayName("builderのsetMessageでメッセージがマスクされる")
    void shouldMaskMessageInSetMessage() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);
      when(mockBuilder.setMessage(any(String.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().setMessage("Email: taro@example.com").log();

      verify(mockBuilder).setMessage(stringCaptor.capture());
      assertThat(stringCaptor.getValue()).isEqualTo("Email: t***@example.com");
    }

    @Test
    @DisplayName("builderのSupplier引数がマスクされる")
    void shouldMaskSupplierArgument() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Supplier.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().addArgument((Supplier<Object>) () -> "supplier@example.com").log("test");

      verify(mockBuilder).addArgument(any(Supplier.class));
    }

    @Test
    @DisplayName("builderのlog(message, arg)で機微な情報がマスクされる")
    void shouldMaskInLogWithMessageAndArg() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().log("Email: {}", "log@example.com");

      verify(mockBuilder).log("Email: {}", "l***@example.com");
    }

    @Test
    @DisplayName("builderのlog(message, arg0, arg1)で機微な情報がマスクされる")
    void shouldMaskInLogWithMessageAndTwoArgs() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().log("{} and {}", "a@foo.com", "b@bar.org");

      verify(mockBuilder).log(any(String.class), any(Object.class), any(Object.class));
    }

    @Test
    @DisplayName("builderのSupplierメッセージがマスクされる")
    void shouldMaskSupplierMessage() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);
      when(mockBuilder.setMessage(any(Supplier.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().setMessage(() -> "Supplier msg: supplier@example.com").log();

      verify(mockBuilder).setMessage(any(Supplier.class));
    }

    @Test
    @DisplayName("builderのlog(Supplier)でメッセージがマスクされる")
    void shouldMaskSupplierInLog() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().log(() -> "Supplier log: supplier@example.com");

      verify(mockBuilder).log(any(Supplier.class));
    }
  }

  @Nested
  @DisplayName("Fluent API（マスク無効時）のテスト")
  class FluentApiWithMaskingDisabledTest {

    @Mock private LoggingEventBuilder mockBuilder;

    @Test
    @DisplayName("マスク無効時は機微な情報がマスクされない")
    void shouldNotMaskWhenDisabled() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);
      when(mockBuilder.addArgument(any(Object.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger, false);
      wrapper.atInfo().addArgument("taro@example.com").log("test");

      verify(mockBuilder).addArgument("taro@example.com");
    }
  }

  @Nested
  @DisplayName("非文字列引数のテスト")
  class NonStringArgumentTest {

    @Test
    @DisplayName("整数引数はマスクされない")
    void shouldNotMaskInteger() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Count: {}", 42);

      verify(mockLogger).info("Count: {}", 42);
    }

    @Test
    @DisplayName("long引数はマスクされない")
    void shouldNotMaskLong() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Id: {}", 123456789L);

      verify(mockLogger).info("Id: {}", 123456789L);
    }

    @Test
    @DisplayName("boolean引数はマスクされない")
    void shouldNotMaskBoolean() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("Flag: {}", true);

      verify(mockLogger).info("Flag: {}", true);
    }
  }

  @Nested
  @DisplayName("両方の引数を持つメソッドのテスト")
  class TwoArgumentMethodTest {

    @Test
    @DisplayName("2つの引数で機微な情報がマスクされる")
    void shouldMaskBothArguments() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("{} and {}", "a@foo.com", "b@bar.org");

      verify(mockLogger).info(stringCaptor.capture(), objectCaptor.capture(), any());
      assertThat(stringCaptor.getValue()).isEqualTo("{} and {}");
    }
  }

  @Nested
  @DisplayName("varargsメソッドのテスト")
  class VarargsMethodTest {

    @Test
    @DisplayName("varargsで複数の機微な情報がマスクされる")
    void shouldMaskMultipleSensitiveDataInVarargs() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("{} {} {}", "email@test.com", "09012345678", "normal text");

      verify(mockLogger).info(any(String.class), any(Object[].class));
    }

    @Test
    @DisplayName("null配列はnullとして渡される")
    void shouldHandleNullArguments() {
      when(mockLogger.isInfoEnabled()).thenReturn(true);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.info("test");

      verify(mockLogger).info("test");
    }
  }

  @Nested
  @DisplayName("addMarkerメソッドのテスト")
  class AddMarkerTest {

    @Mock private LoggingEventBuilder mockBuilder;
    @Mock private Marker mockMarker;

    @Test
    @DisplayName("builderのaddMarkerが正しく委譲される")
    void shouldDelegateAddMarker() {
      when(mockLogger.atInfo()).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      wrapper.atInfo().addMarker(mockMarker);

      verify(mockBuilder).addMarker(mockMarker);
    }
  }

  @Nested
  @DisplayName("setCauseメソッドのテスト")
  class SetCauseTest {

    @Mock private LoggingEventBuilder mockBuilder;

    @Test
    @DisplayName("builderのsetCauseが正しく委譲される")
    void shouldDelegateSetCause() {
      when(mockLogger.atError()).thenReturn(mockBuilder);
      when(mockBuilder.setCause(any(Throwable.class))).thenReturn(mockBuilder);

      SensitiveLoggerWrapper wrapper = new SensitiveLoggerWrapper(mockLogger);
      Throwable cause = new RuntimeException("test");
      wrapper.atError().setCause(cause).log("error");

      verify(mockBuilder).setCause(cause);
    }
  }
}
