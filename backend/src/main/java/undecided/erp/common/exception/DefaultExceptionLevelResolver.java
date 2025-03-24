package undecided.erp.common.exception;

import static org.springframework.util.StringUtils.hasText;

public class DefaultExceptionLevelResolver implements ExceptionLevelResolver {

  private ExceptionCodeResolver exceptionCodeResolver;

  public DefaultExceptionLevelResolver() {
  }

  public DefaultExceptionLevelResolver(ExceptionCodeResolver exceptionCodeResolver) {
    this.exceptionCodeResolver = exceptionCodeResolver;
  }

  public ExceptionLevel resolveExceptionLevel(Exception ex) {
    String exceptionCode = this.resolveExceptionCode(ex);
    if (!hasText(exceptionCode)) {
      return ExceptionLevel.ERROR;
    } else {
      String exceptionCodePrefix = exceptionCode.substring(0, 1);
      if ("e".equalsIgnoreCase(exceptionCodePrefix)) {
        return ExceptionLevel.ERROR;
      } else if ("w".equalsIgnoreCase(exceptionCodePrefix)) {
        return ExceptionLevel.WARN;
      } else {
        return "i".equalsIgnoreCase(exceptionCodePrefix) ? ExceptionLevel.INFO
            : ExceptionLevel.ERROR;
      }
    }
  }

  protected String resolveExceptionCode(Exception ex) {
    String exceptionCode = null;
    if (this.exceptionCodeResolver != null) {
      exceptionCode = this.exceptionCodeResolver.resolveExceptionCode(ex);
    }

    return exceptionCode;
  }
}
