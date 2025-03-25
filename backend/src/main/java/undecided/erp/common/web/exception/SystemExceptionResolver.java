package undecided.erp.common.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import javax.annotation.Nonnull;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import undecided.erp.common.exception.ExceptionCodeResolver;
import undecided.erp.common.exception.ResultMessagesNotificationException;
import undecided.erp.common.exception.SimpleMappingExceptionCodeResolver;
import undecided.erp.common.message.ResultMessages;

public class SystemExceptionResolver extends SimpleMappingExceptionResolver {

  @Setter
  private String resultMessagesAttribute;
  @Setter
  private String exceptionCodeAttribute;
  @Setter
  private String exceptionCodeHeader;
  @Setter
  private ExceptionCodeResolver exceptionCodeResolver;
  @Nullable
  private Class<?>[] excludedExceptions;
  @Setter
  private boolean checkCause;
  @Setter
  private boolean checkSubClass;

  public SystemExceptionResolver() {
    this.resultMessagesAttribute = ResultMessages.DEFAULT_MESSAGES_ATTRIBUTE_NAME;
    this.exceptionCodeAttribute = "exceptionCode";
    this.exceptionCodeHeader = "X-Exception-Code";
    this.exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
    this.checkCause = false;
    this.checkSubClass = false;
  }

  public void setExcludedExceptions(Class<?>... excludedExceptions) {
    this.excludedExceptions = excludedExceptions;
  }

  @Nullable
  protected ModelAndView doResolveException(@Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response, Object handler, @Nonnull Exception ex) {
    ModelAndView modelAndView = super.doResolveException(request, response, handler, ex);
    if (modelAndView == null) {
      return null;
    } else {
      this.setExceptionInfo(ex, request, response);
      return modelAndView;
    }
  }

  @Nullable
  protected String determineViewName(@Nonnull Exception ex, @Nonnull HttpServletRequest request) {
    if (this.excludedExceptions != null) {
      if (this.checkExcludedExceptions(ex)) {
        return null;
      }

      if (this.checkCause) {
        for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
          if (this.checkExcludedExceptions(cause)) {
            return null;
          }
        }
      }
    }

    return super.determineViewName(ex, request);
  }

  private boolean checkExcludedExceptions(Throwable ex) {
    for (Class<?> excludedException : Objects.requireNonNull(this.excludedExceptions)) {
      if (this.checkSubClass && excludedException.isInstance(ex)
          || !this.checkSubClass && excludedException.equals(ex.getClass())) {
        return true;
      }
    }

    return false;
  }

  protected void setExceptionInfo(Exception ex, HttpServletRequest request,
      HttpServletResponse response) {
    this.setExceptionCode(ex, request, response);
    this.setResultMessages(ex, request);
  }

  protected void setExceptionCode(Exception ex, HttpServletRequest request,
      HttpServletResponse response) {
    String exceptionCode = null;
    if (this.exceptionCodeResolver != null && (StringUtils.hasText(this.exceptionCodeAttribute)
        || StringUtils.hasText(this.exceptionCodeHeader))) {
      exceptionCode = this.exceptionCodeResolver.resolveExceptionCode(ex);
    }

    if (exceptionCode != null) {
      if (StringUtils.hasText(this.exceptionCodeAttribute)) {
        request.setAttribute(this.exceptionCodeAttribute, exceptionCode);
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(this.exceptionCodeAttribute, exceptionCode);
      }

      if (StringUtils.hasText(this.exceptionCodeHeader)) {
        response.setHeader(this.exceptionCodeHeader, exceptionCode);
      }

    }
  }

  protected void setResultMessages(Exception ex, HttpServletRequest request) {
    if (StringUtils.hasText(this.resultMessagesAttribute)) {
      if (ex instanceof ResultMessagesNotificationException) {
        ResultMessages resultMessages = ((ResultMessagesNotificationException) ex).getResultMessages();
        request.setAttribute(this.resultMessagesAttribute, resultMessages);
        FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
        flashMap.put(this.resultMessagesAttribute, resultMessages);

      }
    }
  }

}
