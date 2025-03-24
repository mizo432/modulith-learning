package undecided.erp.common.exception;

import javax.annotation.Nonnull;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

public class ResultMessagesLoggingInterceptor implements MethodInterceptor, InitializingBean {

  private final ThreadLocal<MethodInvocation> startingPoint = new ThreadLocal<>();
  @Setter
  private ExceptionLogger exceptionLogger = null;

  public ResultMessagesLoggingInterceptor() {
  }

  public Object invoke(@Nonnull MethodInvocation invocation)
      throws Throwable, ResultMessagesNotificationException {
    if (this.startingPoint.get() == null) {
      this.startingPoint.set(invocation);
    }

    Object e;
    try {
      e = invocation.proceed();
    } catch (ResultMessagesNotificationException var6) {
      if (this.isStartingPoint(invocation)) {
        this.logResultMessagesNotificationException(var6);
      }

      throw var6;
    } finally {
      if (this.isStartingPoint(invocation)) {
        this.startingPoint.remove();
      }

    }

    return e;
  }

  public void afterPropertiesSet() throws Exception {
    if (this.exceptionLogger == null) {
      this.exceptionLogger = new ExceptionLogger(this.getClass().getName());
      this.exceptionLogger.afterPropertiesSet();
    }

  }

  protected boolean isStartingPoint(MethodInvocation invocation) {
    return this.startingPoint.get() == invocation;
  }

  protected void logResultMessagesNotificationException(ResultMessagesNotificationException e) {
    this.exceptionLogger.warn(e);
  }

  protected ExceptionLogger getExceptionLogger() {
    return this.exceptionLogger;

  }

}
