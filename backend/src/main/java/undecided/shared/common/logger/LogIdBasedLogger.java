package undecided.shared.common.logger;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

public class LogIdBasedLogger {

  private static final String UNDEFINED_MESSAGE_FORMAT = "UNDEFINED-MESSAGE id:{0} arg:{1}";   // (1)

  private static ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();// (2)

  static {    // (3)
    messageSource.setDefaultEncoding("UTF-8");          // (4)
    messageSource.setBasenames("i18n/log-messages");    // (5)
  }

  private final Logger logger;

  private LogIdBasedLogger(Class<?> clazz) {
    logger = LoggerFactory.getLogger(clazz);            // (6)
  }

  public static LogIdBasedLogger getLogger(Class<?> clazz) {
    return new LogIdBasedLogger(clazz);
  }

  public boolean isDebugEnabled() {                       // (7)
    return logger.isDebugEnabled();
  }

  public void debug(String format, Object... args) {
    logger.debug(format, args);                         // (8)
  }

  public void info(String id, Object... args) {
    if (logger.isInfoEnabled()) {
      logger.info(createLogMessage(id, args));        // (9)
    }
  }

  public void warn(String id, Object... args) {
    if (logger.isWarnEnabled()) {
      logger.warn(createLogMessage(id, args));        // (9)
    }
  }

  public void error(String id, Object... args) {
    if (logger.isErrorEnabled()) {
      logger.error(createLogMessage(id, args));       // (9)
    }
  }

  public void trace(String id, Object... args) {
    if (logger.isTraceEnabled()) {
      logger.trace(createLogMessage(id, args));       // (9)
    }
  }

  public void warn(String id, Throwable t, Object... args) {
    if (logger.isWarnEnabled()) {
      logger.warn(createLogMessage(id, args), t);     // (9)
    }
  }

  public void error(String id, Throwable t, Object... args) {
    if (logger.isErrorEnabled()) {
      logger.error(createLogMessage(id, args), t);    // (9)
    }
  }

  private String createLogMessage(String id, Object... args) {
    return getMessage(id, args);
  }

  private String getMessage(String id, Object... args) {
    String message;
    try {
      message = messageSource.getMessage(id, args, Locale
          .getDefault());
    } catch (NoSuchMessageException e) {                // (10)
      message = MessageFormat.format(UNDEFINED_MESSAGE_FORMAT, id, Arrays
          .toString(args));
    }
    return message;
  }
}
