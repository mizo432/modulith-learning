package undecided.erp.common.web.exception;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import undecided.erp.common.exception.ExceptionLogger;

@Component
@Setter
@RequiredArgsConstructor
public class ExceptionLoggingFilter extends GenericFilterBean {

  private final ExceptionLogger exceptionLogger;


  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    try {
      filterChain.doFilter(servletRequest, servletResponse);
    } catch (IOException e) {
      this.logIOException(e, servletRequest, servletResponse);
      throw e;
    } catch (ServletException e) {
      this.logServletException(e, servletRequest, servletResponse);
      throw e;
    } catch (RuntimeException e) {
      this.logRuntimeException(e, servletRequest, servletResponse);
      throw e;
    }
  }


  protected void logIOException(IOException ex, ServletRequest request, ServletResponse response) {
    this.exceptionLogger.error(ex);
  }

  protected void logServletException(ServletException ex, ServletRequest request,
      ServletResponse response) {
    this.exceptionLogger.error(ex);
  }

  protected void logRuntimeException(RuntimeException ex, ServletRequest request,
      ServletResponse response) {
    this.exceptionLogger.error(ex);
  }

  protected ExceptionLogger getExceptionLogger() {
    return this.exceptionLogger;
  }

}
