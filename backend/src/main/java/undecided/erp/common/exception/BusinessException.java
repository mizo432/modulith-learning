package undecided.erp.common.exception;

import undecided.erp.common.message.ResultMessage;
import undecided.erp.common.message.ResultMessages;

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
