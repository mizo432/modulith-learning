package undecided.erp.common.exception;

import undecided.erp.common.message.ResultMessage;
import undecided.erp.common.message.ResultMessages;

/**
 * The BusinessException class is a custom exception used for handling business-related errors. It
 * extends the ResultMessagesNotificationException class, enabling the encapsulation and
 * notification of result messages related to business logic issues.
 * <p>
 * This class provides multiple constructors for creating instances with specific messages or causes
 * of exceptions.
 * <p>
 * - The first constructor allows creating an instance by specifying a single message. It generates
 * a ResultMessages instance of error type and adds the provided message.
 * <p>
 * - The second constructor allows creating an instance by specifying multiple messages through a
 * ResultMessages object.
 * <p>
 * - The third constructor allows creating an instance by specifying both multiple messages and the
 * cause of the exception, facilitating a more detailed exception handling approach.
 */
public class BusinessException extends ResultMessagesNotificationException {

  /**
   * Constructor for specify a message.
   * <p>
   * generate a {@link ResultMessages} instance of error type and add a message.
   * </p>
   *
   * @param message result message
   */
  public BusinessException(String message) {
    super(ResultMessages.error().add(ResultMessage.fromText(message)));
  }

  /**
   * Constructor for specify messages.
   * <p>
   * Takes multiple {@code String} messages as argument.
   * </p>
   *
   * @param messages {@link ResultMessages} instance
   */
  public BusinessException(ResultMessages messages) {
    super(messages);
  }

  /**
   * Constructor for specify messages and exception.
   * <p>
   * Takes multiple {@code String} messages and cause of exception as argument.
   * </p>
   *
   * @param messages {@link ResultMessages} instance
   * @param cause {@link Throwable} instance
   */
  public BusinessException(ResultMessages messages, Throwable cause) {
    super(messages, cause);
  }

}
