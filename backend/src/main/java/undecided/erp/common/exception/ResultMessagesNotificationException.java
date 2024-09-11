package undecided.erp.common.exception;

import lombok.Getter;
import undecided.erp.common.message.ResultMessages;

/**
 * The {@code ResultMessagesNotificationException} class is an abstract subclass of the
 * {@code RuntimeException} class.
 * <p>
 * It represents an exception that can be thrown when there are result messages to be notified. It
 * provides a way to encapsulate result messages and pass them to the caller.
 *
 * <p>This class has two constructors: a single argument constructor and a two argument
 * constructor.
 * The single argument constructor takes an instance of {@link ResultMessages} as a parameter. The
 * two argument constructor takes an instance of {@link ResultMessages} and a {@link Throwable} as
 * parameters.
 *
 * <p>Instances of this class have a {@code resultMessages} property of type
 * {@link ResultMessages}.
 * This property holds the encapsulated result messages.
 *
 * <p>Instances of this class can be used to retrieve the encapsulated result messages in a string
 * format
 * by calling the {@link #getMessage()} method.
 */
@Getter
public abstract class ResultMessagesNotificationException extends RuntimeException {

  /**
   * Instance of -- GETTER -- Returns the instance
   * <p>
   * {@link ResultMessages}
   */
  private final ResultMessages resultMessages;

  /**
   * Single argument constructor
   *
   * @param messages instance of {@link ResultMessages}
   */
  protected ResultMessagesNotificationException(ResultMessages messages) {
    this(messages, null);
  }

  /**
   * Two argument constructor
   *
   * @param messages instance of {@link ResultMessages}
   * @param cause {@link Throwable} instance
   */
  public ResultMessagesNotificationException(ResultMessages messages,
      Throwable cause) {
    super(cause);
    if (messages == null) {
      throw new IllegalArgumentException("messages must not be null");
    }
    this.resultMessages = messages;
  }

  /**
   * Returns the messages in String format
   *
   * @return String messages
   */
  @Override
  public String getMessage() {
    return resultMessages.toString();
  }

}
