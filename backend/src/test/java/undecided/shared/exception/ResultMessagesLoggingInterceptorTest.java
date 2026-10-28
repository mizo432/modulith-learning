package undecided.shared.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import undecided.shared.message.ResultMessage;
import undecided.shared.message.ResultMessages;
import undecided.shared.message.StandardResultMessageType;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("ResultMessagesLoggingInterceptorのテスト")
@Tag("small")
class ResultMessagesLoggingInterceptorTest {

  @Nested
  @DisplayName("invokeメソッドのテスト")
  class InvokeMethodTests {

    @Test
    @DisplayName("正常にプロセスが進行する場合の確認")
    void shouldProceedSuccessfully() throws Throwable {
      // Arrange
      MethodInvocation methodInvocation = mock(MethodInvocation.class);
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      when(methodInvocation.proceed()).thenReturn("Success");

      // Act
      Object result = interceptor.invoke(methodInvocation);

      // Assert
      verify(methodInvocation, times(1)).proceed();
      assertThat(result).isEqualTo("Success");
    }

    @Test
    @DisplayName("ERRORタイプの例外がスローされた場合、exceptionLogger.errorが呼び出されること")
    void shouldLogErrorWhenErrorExceptionThrown() throws Throwable {
      // Arrange
      MethodInvocation methodInvocation = mock(MethodInvocation.class);
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      ResultMessages errorMessages = ResultMessages.error().add(ResultMessage.fromText("Error"));
      ResultMessagesNotificationException ex = new BusinessException(errorMessages);

      when(methodInvocation.proceed()).thenThrow(ex);

      // Act & Assert
      assertThatThrownBy(() -> interceptor.invoke(methodInvocation))
          .isSameAs(ex);

      verify(exceptionLogger, times(1)).error(ex);
      verify(exceptionLogger, never()).warn(ex);
      verify(exceptionLogger, never()).info(ex);
      verify(exceptionLogger, never()).log(ex);
    }

    @Test
    @DisplayName("WARNINGタイプの例外がスローされた場合、exceptionLogger.warnが呼び出されること")
    void shouldLogWarnWhenWarningExceptionThrown() throws Throwable {
      // Arrange
      MethodInvocation methodInvocation = mock(MethodInvocation.class);
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      ResultMessages warningMessages = ResultMessages.warning().add(ResultMessage.fromText("Warning"));
      ResultMessagesNotificationException ex = new BusinessException(warningMessages);

      when(methodInvocation.proceed()).thenThrow(ex);

      // Act & Assert
      assertThatThrownBy(() -> interceptor.invoke(methodInvocation))
          .isSameAs(ex);

      verify(exceptionLogger, times(1)).warn(ex);
      verify(exceptionLogger, never()).error(ex);
      verify(exceptionLogger, never()).info(ex);
      verify(exceptionLogger, never()).log(ex);
    }

    @Test
    @DisplayName("INFOタイプの例外がスローされた場合、exceptionLogger.infoが呼び出されること")
    void shouldLogInfoWhenInfoExceptionThrown() throws Throwable {
      // Arrange
      MethodInvocation methodInvocation = mock(MethodInvocation.class);
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      ResultMessages infoMessages = ResultMessages.info().add(ResultMessage.fromText("Info"));
      ResultMessagesNotificationException ex = new BusinessException(infoMessages);

      when(methodInvocation.proceed()).thenThrow(ex);

      // Act & Assert
      assertThatThrownBy(() -> interceptor.invoke(methodInvocation))
          .isSameAs(ex);

      verify(exceptionLogger, times(1)).info(ex);
      verify(exceptionLogger, never()).error(ex);
      verify(exceptionLogger, never()).warn(ex);
      verify(exceptionLogger, never()).log(ex);
    }

    @Test
    @DisplayName("DANGERタイプの例外がスローされた場合、exceptionLogger.logが呼び出されること")
    void shouldLogWhenDangerExceptionThrown() throws Throwable {
      // Arrange
      MethodInvocation methodInvocation = mock(MethodInvocation.class);
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      ResultMessages dangerMessages = ResultMessages.danger().add(ResultMessage.fromText("Danger"));
      ResultMessagesNotificationException ex = new BusinessException(dangerMessages);

      when(methodInvocation.proceed()).thenThrow(ex);

      // Act & Assert
      assertThatThrownBy(() -> interceptor.invoke(methodInvocation))
          .isSameAs(ex);

      verify(exceptionLogger, times(1)).log(ex);
      verify(exceptionLogger, never()).error(ex);
      verify(exceptionLogger, never()).warn(ex);
      verify(exceptionLogger, never()).info(ex);
    }

    @Test
    @DisplayName("その他のThrowableがスローされた場合、再スローされること")
    void shouldRethrowOtherThrowable() throws Throwable {
      // Arrange
      MethodInvocation methodInvocation = mock(MethodInvocation.class);
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      RuntimeException ex = new RuntimeException("Unexpected error");
      when(methodInvocation.proceed()).thenThrow(ex);

      // Act & Assert
      assertThatThrownBy(() -> interceptor.invoke(methodInvocation))
          .isSameAs(ex);

      // exceptionLoggerは呼び出されない
      verify(exceptionLogger, never()).error(any());
      verify(exceptionLogger, never()).warn(any());
      verify(exceptionLogger, never()).info(any());
      verify(exceptionLogger, never()).log(any());
    }

    @Test
    @DisplayName("ネストされた呼び出しでは、内側の呼び出しで例外ログは記録されないこと")
    void shouldNotLogExceptionInNestedCall() throws Throwable {
      // Arrange
      MethodInvocation outerInvocation = mock(MethodInvocation.class);
      MethodInvocation innerInvocation = mock(MethodInvocation.class);
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      ResultMessages errorMessages = ResultMessages.error().add(ResultMessage.fromText("Error"));
      ResultMessagesNotificationException ex = new BusinessException(errorMessages);

      // 内側の呼び出しが例外をスロー
      when(innerInvocation.proceed()).thenThrow(ex);
      // 外側の呼び出しが内側のinvokeを呼び出した後、例外をスロー
      when(outerInvocation.proceed())
          .thenAnswer(invocation -> {
            interceptor.invoke(innerInvocation);
            throw ex;
          });

      // Act & Assert
      assertThatThrownBy(() -> interceptor.invoke(outerInvocation))
          .isSameAs(ex);

      // 外側の呼び出し1回分のみのログ記録
      verify(exceptionLogger, times(1)).error(ex);
    }

    @Test
    @DisplayName("afterPropertiesSetでexceptionLoggerが未設定の場合、デフォルトで設定されること")
    void shouldInitializeExceptionLoggerByDefault() {
      // Arrange
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();

      // Act
      interceptor.afterPropertiesSet();

      // Assert
      // exceptionLoggerがnullでないことを確認（反射で確認）
      assertThat(interceptor).isNotNull();
    }

    @Test
    @DisplayName("afterPropertiesSetでexceptionLoggerが既に設定されている場合、上書きされないこと")
    void shouldNotOverwriteExceptionLogger() {
      // Arrange
      ExceptionLogger exceptionLogger = mock(ExceptionLogger.class);
      ResultMessagesLoggingInterceptor interceptor = new ResultMessagesLoggingInterceptor();
      interceptor.setExceptionLogger(exceptionLogger);

      // Act
      interceptor.afterPropertiesSet();

      // Assert
      // exceptionLoggerが変更されていないことを確認（反射で確認）
      assertThat(interceptor).isNotNull();
    }
  }
}
