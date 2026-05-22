package undecided.shared.common.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
  }
}
