package undecided.erp.common.exception;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

public class ExceptionLogger implements InitializingBean {

  private static final String MONITORING_LOG_LOGGER_SUFFIX = ".Monitoring";
  private final Logger applicationLogger;
  private final Logger monitoringLogger;
  private final Map<ExceptionLevel, ExceptionLogger.LogLevelWrappingLogger> exceptionLevelLoggers;
  private final ExceptionLogger.InfoLogger infoLogger;
  private final ExceptionLogger.WarnLogger warnLogger;
  private final ExceptionLogger.ErrorLogger errorLogger;
  @Setter
  private ExceptionCodeResolver exceptionCodeResolver;
  @Setter
  private ExceptionLevelResolver exceptionLevelResolver;
  private String PLACEHOLDER_OF_EXCEPTION_CODE;
  private String PLACEHOLDER_OF_EXCEPTION_MESSAGE;
  @Setter
  private String logMessageFormat;
  @Setter
  private String defaultCode;
  @Setter
  private String defaultMessage;
  @Setter
  private boolean trimLogMessage;

  public ExceptionLogger() {
    this(ExceptionLogger.class.getName());
  }

  public ExceptionLogger(String name) {
    this.exceptionLevelLoggers = new ConcurrentHashMap();
    this.exceptionCodeResolver = new SimpleMappingExceptionCodeResolver();
    this.PLACEHOLDER_OF_EXCEPTION_CODE = "{0}";
    this.PLACEHOLDER_OF_EXCEPTION_MESSAGE = "{1}";
    this.logMessageFormat = String.format("[%s] %s", this.PLACEHOLDER_OF_EXCEPTION_CODE,
        this.PLACEHOLDER_OF_EXCEPTION_MESSAGE);
    this.defaultCode = "UNDEFINED-CODE";
    this.defaultMessage = "UNDEFINED-MESSAGE";
    this.trimLogMessage = true;
    this.applicationLogger = LoggerFactory.getLogger(name);
    this.monitoringLogger = LoggerFactory.getLogger(name + ".Monitoring");
    this.infoLogger = new ExceptionLogger.InfoLogger();
    this.warnLogger = new ExceptionLogger.WarnLogger();
    this.errorLogger = new ExceptionLogger.ErrorLogger();
  }

  public void afterPropertiesSet() {
    this.validateLogMessageFormat(this.logMessageFormat);
    if (this.exceptionLevelResolver == null) {
      this.exceptionLevelResolver = new DefaultExceptionLevelResolver(this.exceptionCodeResolver);
    }

    this.registerExceptionLevelLoggers(ExceptionLevel.INFO, this.infoLogger);
    this.registerExceptionLevelLoggers(ExceptionLevel.WARN, this.warnLogger);
    this.registerExceptionLevelLoggers(ExceptionLevel.ERROR, this.errorLogger);
  }

  public void log(Exception ex) {
    ExceptionLevel level = this.exceptionLevelResolver.resolveExceptionLevel(ex);
    ExceptionLogger.LogLevelWrappingLogger logger = null;
    if (level != null) {
      logger = this.exceptionLevelLoggers.get(level);
    }

    if (logger == null) {
      logger = this.errorLogger;
    }

    this.log(ex, logger);
  }

  public void info(Exception ex) {
    this.log(ex, this.infoLogger);
  }

  public void warn(Exception ex) {
    this.log(ex, this.warnLogger);
  }

  public void error(Exception ex) {
    this.log(ex, this.errorLogger);
  }

  protected void validateLogMessageFormat(String logMessageFormat) {
    if (logMessageFormat == null || !logMessageFormat.contains(this.PLACEHOLDER_OF_EXCEPTION_CODE)
        || !logMessageFormat.contains(this.PLACEHOLDER_OF_EXCEPTION_MESSAGE)) {
      String message =
          "logMessageFormat must have placeholder({0} and {1}). {0} is replaced with exception code. {1} is replaced with exception message. current logMessageFormat is \""
              + logMessageFormat + "\".";
      throw new IllegalArgumentException(message);
    }
  }

  protected String resolveExceptionCode(Exception ex) {
    String exceptionCode = null;
    if (this.exceptionCodeResolver != null) {
      exceptionCode = this.exceptionCodeResolver.resolveExceptionCode(ex);
    }

    return exceptionCode;
  }

  protected String makeLogMessage(Exception ex) {
    String exceptionCode = this.resolveExceptionCode(ex);
    return this.formatLogMessage(exceptionCode, ex.getMessage());
  }

  protected String formatLogMessage(String exceptionCode, String exceptionMessage) {
    String bindingExceptionCode = exceptionCode;
    String bindingExceptionMessage = exceptionMessage;
    if (!StringUtils.hasText(exceptionCode)) {
      bindingExceptionCode = this.defaultCode;
    }

    if (!StringUtils.hasText(exceptionMessage)) {
      bindingExceptionMessage = this.defaultMessage;
    }

    String message = MessageFormat.format(this.logMessageFormat, bindingExceptionCode,
        bindingExceptionMessage);
    if (this.trimLogMessage) {
      message = message.trim();
    }

    return message;
  }

  protected void registerExceptionLevelLoggers(
      ExceptionLevel level, ExceptionLogger.LogLevelWrappingLogger logger) {
    this.exceptionLevelLoggers.put(level, logger);
  }

  protected Logger getApplicationLogger() {
    return this.applicationLogger;
  }

  protected Logger getMonitoringLogger() {
    return this.monitoringLogger;
  }

  private void log(Exception ex, ExceptionLogger.LogLevelWrappingLogger logger) {
    if (logger.isEnabled()) {
      String logMessage = this.makeLogMessage(ex);
      logger.log(logMessage, ex);
    }
  }

  protected interface LogLevelWrappingLogger {

    boolean isEnabled();

    void log(String var1, Exception var2);
  }

  private final class InfoLogger implements
      ExceptionLogger.LogLevelWrappingLogger {

    private InfoLogger() {
    }

    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isInfoEnabled()
          || ExceptionLogger.this.applicationLogger.isInfoEnabled();
    }

    public void log(String logMessage, Exception ex) {
      if (ExceptionLogger.this.monitoringLogger.isInfoEnabled()) {
        ExceptionLogger.this.monitoringLogger.info(logMessage);
      }

      if (ExceptionLogger.this.applicationLogger.isInfoEnabled()) {
        ExceptionLogger.this.applicationLogger.info(logMessage, ex);
      }

    }
  }

  private final class WarnLogger implements
      ExceptionLogger.LogLevelWrappingLogger {

    private WarnLogger() {
    }

    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isWarnEnabled()
          || ExceptionLogger.this.applicationLogger.isWarnEnabled();
    }

    public void log(String logMessage, Exception ex) {
      if (ExceptionLogger.this.monitoringLogger.isWarnEnabled()) {
        ExceptionLogger.this.monitoringLogger.warn(logMessage);
      }

      if (ExceptionLogger.this.applicationLogger.isWarnEnabled()) {
        ExceptionLogger.this.applicationLogger.warn(logMessage, ex);
      }

    }
  }

  private final class ErrorLogger implements
      ExceptionLogger.LogLevelWrappingLogger {

    private ErrorLogger() {
    }

    public boolean isEnabled() {
      return ExceptionLogger.this.monitoringLogger.isErrorEnabled() ||
          ExceptionLogger.this.applicationLogger.isErrorEnabled();
    }

    public void log(String logMessage, Exception ex) {
      if (ExceptionLogger.this.monitoringLogger.isErrorEnabled()) {
        ExceptionLogger.this.monitoringLogger.error(logMessage);
      }

      if (ExceptionLogger.this.applicationLogger.isErrorEnabled()) {
        ExceptionLogger.this.applicationLogger.error(logMessage, ex);
      }

    }
  }
}
