package undecided.erp.common.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import undecided.erp.common.exception.ExceptionLogger;

@Setter
@Component
@RequiredArgsConstructor
public class HandlerExceptionResolverLoggingInterceptor implements MethodInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(
      HandlerExceptionResolverLoggingInterceptor.class);
  private final ExceptionLogger exceptionLogger = null;
  private Set<Class<? extends Exception>> ignoreExceptions = new HashSet<>();

  public Object invoke(MethodInvocation invocation) throws Throwable {
    Object returnObj = invocation.proceed();
    if (returnObj == null) {
      return null;
    } else {
      Object targetObject = invocation.getThis();
      if (!(targetObject instanceof HandlerExceptionResolver)) {
        if (logger.isWarnEnabled()) {
          logger.warn(
              "target object does not implement the HandlerExceptionResolver interface. target object is '{}'.",
              targetObject.getClass().getName());
        }

      } else {
        Exception exception = (Exception) invocation.getArguments()[3];
        if (this.isTargetException(exception)) {
          HttpServletRequest request = (HttpServletRequest) invocation.getArguments()[0];
          HttpServletResponse response = (HttpServletResponse) invocation.getArguments()[1];
          Object handler = invocation.getArguments()[2];
          this.log(exception, request, response, handler);
        }

      }
      return returnObj;
    }
  }


  protected boolean isTargetException(Exception ex) {
    if (this.ignoreExceptions != null) {
      for (Class<? extends Exception> ignoreClass : this.ignoreExceptions) {
        if (ignoreClass.isInstance(ex)) {
          return false;
        }
      }

    }
    return true;
  }

  protected void log(Exception ex, HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    int statusCode = response.getStatus();
    if (500 <= statusCode) {
      this.logServerError(ex, request, response, handler);
    } else if (400 <= statusCode) {
      this.logClientError(ex, request, response, handler);
    } else if (300 <= statusCode) {
      this.logRedirection(ex, request, response, handler);
    } else if (200 <= statusCode) {
      this.logSuccess(ex, request, response, handler);
    } else if (100 <= statusCode) {
      this.logInformational(ex, request, response, handler);
    }
  }

  protected void logInformational(Exception ex, HttpServletRequest request,
      HttpServletResponse response, Object handler) {
    this.exceptionLogger.info(ex);
  }

  protected void logSuccess(Exception ex, HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    this.exceptionLogger.info(ex);
  }

  protected void logRedirection(Exception ex, HttpServletRequest request,
      HttpServletResponse response, Object handler) {
    this.exceptionLogger.info(ex);
  }

  protected void logClientError(Exception ex, HttpServletRequest request,
      HttpServletResponse response, Object handler) {
    this.exceptionLogger.warn(ex);
  }

  protected void logServerError(Exception ex, HttpServletRequest request,
      HttpServletResponse response, Object handler) {
    this.exceptionLogger.error(ex);
  }

  protected ExceptionLogger getExceptionLogger() {
    return this.exceptionLogger;
  }

}
