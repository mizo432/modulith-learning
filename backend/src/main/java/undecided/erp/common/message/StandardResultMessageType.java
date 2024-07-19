package undecided.erp.common.message;

public enum StandardResultMessageType implements ResultMessageType {
  /**
   * message type is <code>success</code>.
   */
  SUCCESS("success"),
  /**
   * message type is <code>info</code>.
   */
  INFO("info"),
  /**
   * message type is <code>warning</code>.
   *
   * @since 5.0.0
   */
  WARNING("warning"),
  /**
   * message type is <code>error</code>.
   */
  ERROR("error"),
  /**
   * message type is <code>danger</code>.
   */
  DANGER("danger"),
  /**
   * message type is <code>primary</code>.
   *
   * @since 5.7.0
   */
  PRIMARY("primary"),
  /**
   * message type is <code>secondary</code>.
   *
   * @since 5.7.0
   */
  SECONDARY("secondary"),
  /**
   * message type is <code>light</code>.
   *
   * @since 5.7.0
   */
  LIGHT("light"),
  /**
   * message type is <code>dark</code>.
   *
   * @since 5.7.0
   */
  DARK("dark");

  /**
   * message type
   */
  private final String type;

  /**
   * Create ResultMessageType instance<br>
   *
   * @param type message type
   */
  private StandardResultMessageType(String type) {
    this.type = type;
  }

  @Override
  public String getType() {
    return this.type;
  }

  /**
   * <p>
   * returns message type
   * </p>
   *
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return this.type;
  }
}
