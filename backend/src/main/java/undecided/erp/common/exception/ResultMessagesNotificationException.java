package undecided.erp.common.exception;

import lombok.Getter;
import undecided.erp.common.message.ResultMessages;

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
